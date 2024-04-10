package com.moli.mall.portal.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.common.service.RedisService;
import com.moli.mall.common.utils.AssetsUtil;
import com.moli.mall.common.utils.BeanCopyUtil;
import com.moli.mall.mbg.mapper.*;
import com.moli.mall.mbg.model.*;
import com.moli.mall.portal.components.OrderCancelSender;
import com.moli.mall.portal.dao.OmsOrderItemDao;
import com.moli.mall.portal.dao.PortalOrderDao;
import com.moli.mall.portal.dto.OmsOrderQueryParams;
import com.moli.mall.portal.dto.OrderParams;
import com.moli.mall.portal.service.*;
import com.moli.mall.portal.vo.CartPromotionItemVo;
import com.moli.mall.portal.vo.ConfirmOrderVo;
import com.moli.mall.portal.vo.OmsOrderDetailVo;
import com.moli.mall.portal.vo.SmsCouponHistoryDetailVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author moli
 * @time 2024-04-06 16:45:03
 * @description 订单管理
 */
@Service
public class OmsPortalOrderServiceImpl implements OmsPortalOrderService {

    @Resource
    private OmsOrderService omsOrderService;

    @Resource
    private UmsMemberService umsMemberService;

    @Resource
    private OmsOrderMapper omsOrderMapper;

    @Resource
    private OmsCartItemMapper omsCartItemMapper;

    @Resource
    private OmsCartItemService omsCartItemService;

    @Resource
    private UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Resource
    private UmsMemberReceiveAddressService umsMemberReceiveAddressService;

    @Resource
    private UmsMemberCouponService umsMemberCouponService;

    @Resource
    private UmsIntegrationConsumeSettingMapper umsIntegrationConsumeSettingMapper;

    @Resource
    private PmsSkuStockMapper skuStockMapper;

    @Value("%{redis.database:'mall'}")
    private String REDIS_DATABASE;

    @Value("${redis.key.prefix.order:'oms:order'}")
    private String ORDER_PREFIX_KEY;

    @Resource
    private RedisService redisService;

    @Resource
    private OmsOrderSettingMapper omsOrderSettingMapper;

    @Resource
    private OmsOrderItemDao omsOrderItemDao;

    @Resource
    private SmsCouponHistoryMapper smsCouponHistoryMapper;

    @Resource
    private OrderCancelSender orderCancelSender;

    @Resource
    private OmsOrderItemMapper omsOrderItemMapper;

    @Resource
    private PortalOrderDao portalOrderDao;


    @Override
    public CommonPage<OmsOrderDetailVo> list(Integer status, Integer pageNum, Integer pageSize) {
        // 设置请求参数
        UmsMember current = umsMemberService.info();
        OmsOrderQueryParams queryParams = new OmsOrderQueryParams();
        if (Objects.nonNull(status) && !status.equals(-1)) {
            queryParams.setStatus(status);
        }
        queryParams.setMemberId(current.getId());
        // rpc调用
        CommonResult<CommonPage<OmsOrderDetailVo>> page = omsOrderService.detailList(queryParams, pageNum, pageSize);
        return page.getData();
    }

    @Override
    public ConfirmOrderVo generateConfirmOrder(List<Long> cartIds) {
        if (CollectionUtils.isEmpty(cartIds)) return null;

        UmsMember current = umsMemberService.info();
        ConfirmOrderVo orderInfo = new ConfirmOrderVo();

        // 查询优惠信息的购物车信息
        List<CartPromotionItemVo> cartPromotionItemList = omsCartItemService.listPromotion(current.getId(), cartIds);
        orderInfo.setCartPromotionItemList(cartPromotionItemList);

        // 查询用户收货地址列表
        CompletableFuture<List<UmsMemberReceiveAddress>> memberAddressFuture = CompletableFuture.supplyAsync(() -> {
            UmsMemberReceiveAddressExample umsMemberReceiveAddressExample = new UmsMemberReceiveAddressExample();
            umsMemberReceiveAddressExample.createCriteria().andMemberIdEqualTo(current.getId());
            return umsMemberReceiveAddressMapper.selectByExample(umsMemberReceiveAddressExample);
        }).whenComplete((memberReceiveAddressList, e) -> orderInfo.setMemberReceiveAddressList(memberReceiveAddressList));

        // 获取用户积分
        orderInfo.setMemberIntegration(current.getIntegration());

        // 查询用户可用优惠券列表
        List<SmsCouponHistoryDetailVo> couponHistoryDetailList = umsMemberCouponService.listCoupons(cartPromotionItemList, 1);
        orderInfo.setCouponHistoryDetailList(couponHistoryDetailList);
        // 计算总金额、活动优惠、应付金额
        ConfirmOrderVo.CalcAmount calcAmount = calcCartAmount(cartPromotionItemList);
        orderInfo.setCalcAmount(calcAmount);

        // 获取积分使用规则
        UmsIntegrationConsumeSetting consumeSetting = umsIntegrationConsumeSettingMapper.selectByPrimaryKey(1L);
        orderInfo.setIntegrationConsumeSetting(consumeSetting);

        try {
            CompletableFuture.allOf(memberAddressFuture).get();
        } catch (InterruptedException | ExecutionException e) {
            AssetsUtil.fail("获取确认购物车信息失败");
        }

        return orderInfo;
    }

    @Override
    @Transactional
    public Map<String, Object> generateOrder(OrderParams orderParam) {
        // TODO
        if (Objects.isNull(orderParam.getMemberReceiveAddressId())) {
            AssetsUtil.fail("请选择收货地址!!");
        }
        List<OmsOrderItem> orderItemList = new ArrayList<>();
        UmsMember current = umsMemberService.info();

        // 获取用户的购物车信息
        List<CartPromotionItemVo> cartPromotionItemList = omsCartItemService.listPromotion(current.getId(), orderParam.getCartIds());

        // 生成订单信息
        for (CartPromotionItemVo item : cartPromotionItemList) {
            OmsOrderItem orderItem = new OmsOrderItem();
            orderItem.setProductAttr(item.getProductAttr());
            orderItem.setProductBrand(item.getProductBrand());
            orderItem.setProductCategoryId(item.getProductCategoryId());
            orderItem.setProductId(item.getProductId());
            orderItem.setProductPic(item.getProductPic());
            orderItem.setProductName(item.getProductName());
            orderItem.setProductPrice(item.getPrice());
            orderItem.setProductQuantity(item.getQuantity());
            orderItem.setProductSkuId(item.getProductSkuId());
            orderItem.setProductSkuCode(item.getProductSkuCode());
            orderItem.setProductSn(item.getProductSn());
            orderItem.setPromotionAmount(item.getReduceAmount());
            orderItem.setPromotionName(item.getPromotionMessage());
            orderItem.setGiftGrowth(item.getGrowth());
            orderItem.setGiftIntegration(item.getIntegration());

            orderItemList.add(orderItem);
        }

        // 判断购物车商品中是否有库存
        if (!hasStock(cartPromotionItemList)) {
            AssetsUtil.fail("库存不足，无法下单");
        }

        if (Objects.isNull(orderParam.getCouponId())) {
            // 没有使用优惠券
            for (OmsOrderItem orderItem : orderItemList) {
                orderItem.setCouponAmount(new BigDecimal(0));
            }
        } else {
            // 使用了优惠券
            SmsCouponHistoryDetailVo couponHistoryDetail = getUseCoupon(orderParam.getCouponId(), cartPromotionItemList);
            if (Objects.isNull(couponHistoryDetail)) AssetsUtil.fail("该优惠券不可用!!");

            // 对优惠券进行处理
            handleCouponAmount(orderItemList, couponHistoryDetail);
        }

        if (Objects.isNull(orderParam.getUseIntegration()) || orderParam.getUseIntegration() == 0) {
            // 不使用积分
            for (OmsOrderItem item : orderItemList) {
                item.setIntegrationAmount(new BigDecimal(0));
            }
        } else {
            // 计算总金额
            BigDecimal totalAmount = calcTotalAmount(orderItemList);
            BigDecimal useIntegrationAmount = getUseIntegrationAmount(
                    orderParam.getUseIntegration(),
                    totalAmount,
                    current,
                    Objects.nonNull(orderParam.getCouponId())
            );

            if (useIntegrationAmount.compareTo(new BigDecimal(0)) == 0) {
                AssetsUtil.fail("积分不可用");
            } else {
                // 积分可用，金额分摊
                for (OmsOrderItem item : orderItemList) {
                    BigDecimal integrationAmount = item.getProductPrice().divide(totalAmount, 3, RoundingMode.HALF_EVEN).multiply(useIntegrationAmount);
                    item.setIntegrationAmount(integrationAmount);
                }
            }
        }

        // 计算订单项真实金额
        calcOrderItemRealAmount(orderItemList);

        // 进行库存锁定
        handleLockStock(cartPromotionItemList);

        // 生成订单信息
        OmsOrder order = new OmsOrder();
        order.setDiscountAmount(new BigDecimal(0));
        order.setTotalAmount(calcTotalAmount(orderItemList));
        order.setFreightAmount(new BigDecimal(0));
        order.setPromotionAmount(calcPromotionAmount(orderItemList));
        order.setPromotionInfo(getPromotionInfo(orderItemList));

        // 计算优惠券减免价格
        if (Objects.isNull(orderParam.getCouponId())) {
            order.setCouponAmount(new BigDecimal(0));
        } else {
            order.setCouponId(orderParam.getCouponId());
            order.setCouponAmount(calcCouponAmount(orderItemList));
        }

        // 计算积分减免价格
        if (Objects.isNull(orderParam.getUseIntegration())) {
            order.setIntegration(0);
            order.setIntegrationAmount(new BigDecimal(0));
        } else {
            order.setIntegration(orderParam.getUseIntegration());
            order.setIntegrationAmount(calcIntegrationAmount(orderItemList));
        }

        // 计算支付价格
        order.setPayAmount(calcPayAmount(order));

        order.setMemberId(current.getId());
        order.setCreateTime(new Date());
        order.setMemberUsername(current.getUsername());

        //支付方式：0->未支付；1->支付宝；2->微信
        order.setPayType(orderParam.getPayType());
        //订单来源：0->PC订单；1->app订单
        order.setSourceType(1);
        //订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单
        order.setStatus(0);
        //订单类型：0->正常订单；1->秒杀订单
        order.setOrderType(0);

        // 收货人信息
        UmsMemberReceiveAddress address = umsMemberReceiveAddressService.getItem(orderParam.getMemberReceiveAddressId());
        order.setReceiverCity(address.getCity());
        order.setReceiverName(address.getName());
        order.setReceiverDetailAddress(address.getDetailAddress());
        order.setReceiverPhone(address.getPhoneNumber());
        order.setReceiverProvince(address.getProvince());
        order.setReceiverPostCode(address.getPostCode());
        order.setReceiverRegion(address.getRegion());

        // 确认状态和删除状态
        order.setConfirmStatus(0);
        order.setDeleteStatus(0);

        // 计算赠送积分
        order.setIntegration(calcIntegration(orderItemList));

        // 计算赠送成长值
        order.setGrowth(calcGrowth(orderItemList));

        // 生成订单号
        order.setOrderSn(generateOrderSn(order));

        // 设置自动售货天数
        List<OmsOrderSetting> settings = omsOrderSettingMapper.selectByExample(new OmsOrderSettingExample());
        if (CollUtil.isNotEmpty(settings)) {
            order.setAutoConfirmDay(settings.get(0).getConfirmOvertime());
        }

        // 插入数据库
        omsOrderMapper.insert(order);

        for (OmsOrderItem item : orderItemList) {
            item.setOrderId(order.getId());
            item.setOrderSn(order.getOrderSn());
        }
        omsOrderItemDao.insertList(orderItemList);

        // 如果使用了优惠券，更新优惠券状态
        if (Objects.nonNull(orderParam.getCouponId())) {
            updateCouponStatus(orderParam.getCouponId(), current.getId(), 1);
        }

        // 如果使用了积分，扣除对应的积分
        if (Objects.nonNull(orderParam.getUseIntegration())) {
            order.setUseIntegration(orderParam.getUseIntegration());
            if (Objects.isNull(current.getIntegration())) {
                current.setIntegration(0);
            }
            umsMemberService.updateIntegration(current.getId(), current.getIntegration());
        }

        // 删除购物车中的下单商品
        deleteCartItemList(cartPromotionItemList, current.getId());

        // 发送延迟消息取消订单
        sendDelayMsgToCancelOrder(order.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("orderItemList", orderItemList);
        return result;
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        // 查询未付款的订单
        OmsOrderExample omsOrderExample = new OmsOrderExample();
        omsOrderExample.createCriteria().andIdEqualTo(orderId).andStatusEqualTo(0).andDeleteStatusEqualTo(0);
        List<OmsOrder> omsOrders = omsOrderMapper.selectByExample(omsOrderExample);
        if (CollUtil.isEmpty(omsOrders)) {
            return;
        }
        OmsOrder cancelOrder = omsOrders.get(0);
        if (Objects.isNull(cancelOrder)) {
            return;
        }

        // 修改订单状态为取消
        cancelOrder.setStatus(4);
        omsOrderMapper.updateByPrimaryKey(cancelOrder);

        // 查询订单项
        OmsOrderItemExample omsOrderItemExample = new OmsOrderItemExample();
        omsOrderItemExample.createCriteria().andOrderIdEqualTo(orderId);
        List<OmsOrderItem> itemList = omsOrderItemMapper.selectByExample(omsOrderItemExample);

        // 解除订单锁定库存
        if (CollUtil.isNotEmpty(itemList)) {
            omsOrderItemDao.releaseSkuStockLock(itemList);
        }

        // 修改优惠券使用状态
        updateCouponStatus(cancelOrder.getCouponId(), cancelOrder.getMemberId(), 0);

        // 修改用户积分
        if (Objects.nonNull(cancelOrder.getUseIntegration())) {
            UmsMember member = umsMemberService.infoById(cancelOrder.getMemberId());
            umsMemberService.updateIntegration(member.getId(), member.getIntegration() + cancelOrder.getUseIntegration());
        }
    }

    @Override
    public OmsOrderDetailVo detail(Long orderId) {
        // 查询基础订单信息
        OmsOrder order = omsOrderMapper.selectByPrimaryKey(orderId);
        if (Objects.isNull(order)) {
            return null;
        }
        // 查询订单项
        OmsOrderItemExample omsOrderItemExample = new OmsOrderItemExample();
        omsOrderItemExample.createCriteria().andOrderIdEqualTo(orderId);
        List<OmsOrderItem> omsOrderItems = omsOrderItemMapper.selectByExample(omsOrderItemExample);
        List<OmsOrderItem> itemList = Optional.ofNullable(omsOrderItems).orElse(new ArrayList<>());

        OmsOrderDetailVo result = BeanCopyUtil.copyBean(order, OmsOrderDetailVo.class);
        assert result != null;
        result.setOrderItemList(itemList);

        return result;
    }

    @Override
    @Transactional
    public Integer paySuccess(Long orderId, Integer payType) {
        // 修改订单状态
        OmsOrder order = new OmsOrder();
        order.setStatus(1); // 已支付
        order.setId(orderId);
        order.setPayType(payType);
        order.setPaymentTime(new Date());
        omsOrderMapper.updateByPrimaryKeySelective(order);

        // 恢复所有库存的锁定状态，扣减库存
        OmsOrderDetailVo detailVo = portalOrderDao.getDetail(orderId);
        return omsOrderItemDao.updateSkuStock(detailVo.getOrderItemList());
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        UmsMember current = umsMemberService.info();
        OmsOrder order = omsOrderMapper.selectByPrimaryKey(orderId);
        if (!order.getMemberId().equals(current.getId())) {
            AssetsUtil.fail("不能删除他人订单");
        }

        Integer orderStatus = order.getStatus();
        if (Objects.nonNull(orderStatus) && (orderStatus == 3 || orderStatus == 4)) {
            // 删除订单
            order.setDeleteStatus(1);
            omsOrderMapper.updateByPrimaryKeySelective(order);
        } else {
            AssetsUtil.fail("只能删除已完成或者已关闭的订单!");
        }
    }

    @Override
    @Transactional
    public void confirmReceiveOrder(Long orderId) {
        UmsMember current = umsMemberService.info();
        OmsOrder order = omsOrderMapper.selectByPrimaryKey(orderId);
        if (!order.getMemberId().equals(current.getId())) {
            AssetsUtil.fail("不能确认他们订单");
        }

        Integer orderStatus = order.getStatus();
        if (Objects.nonNull(orderStatus) && orderStatus == 2) {
            // 确认订单 - 已完成
            order.setStatus(3);
            order.setConfirmStatus(1);
            order.setReceiveTime(new Date());
            omsOrderMapper.updateByPrimaryKeySelective(order);
        } else {
            AssetsUtil.fail("订单状态异常，无法确认收货!");
        }
    }

    /**
     * 发送延迟消息取消订单
     *
     * @param orderId 订单id
     */
    private void sendDelayMsgToCancelOrder(Long orderId) {
        // 获取订单超时时间
        OmsOrderSetting orderSetting = omsOrderSettingMapper.selectByPrimaryKey(1L);
        long delayTimes = orderSetting.getNormalOrderOvertime() * 60 * 1000;
        orderCancelSender.sendMessage(orderId, delayTimes);
    }

    /**
     * 删除购物车中的下单商品
     */
    private void deleteCartItemList(List<CartPromotionItemVo> cartPromotionItemList, Long memberId) {
        List<Long> ids = cartPromotionItemList.stream().map(CartPromotionItemVo::getId).collect(Collectors.toList());
        omsCartItemService.delete(ids, memberId);
    }

    /**
     * 更新优惠券使用状态
     *
     * @param couponId 优惠券id
     * @param memberId 用户id
     * @param status   状态
     */
    private void updateCouponStatus(Long couponId, Long memberId, int status) {
        if (Objects.isNull(couponId)) return;

        SmsCouponHistoryExample smsCouponHistoryExample = new SmsCouponHistoryExample();
        smsCouponHistoryExample.createCriteria()
                .andUseStatusEqualTo(status == 0 ? 1 : 0)
                .andCouponIdEqualTo(couponId)
                .andMemberIdEqualTo(memberId);
        List<SmsCouponHistory> smsCouponHistories = smsCouponHistoryMapper.selectByExample(smsCouponHistoryExample);

        if (CollUtil.isNotEmpty(smsCouponHistories)) {
            SmsCouponHistory couponHistory = smsCouponHistories.get(0);
            couponHistory.setUseStatus(status);
            couponHistory.setUseTime(new Date());
            smsCouponHistoryMapper.updateByPrimaryKeySelective(couponHistory);
        }
    }

    /**
     * 生成18位订单编号:8位日期+2位平台号码+2位支付方式+6位以上自增id
     */
    private String generateOrderSn(OmsOrder order) {
        String date = DateUtil.format(new Date(), "yyyyMMdd");
        String key = REDIS_DATABASE + ":" + ORDER_PREFIX_KEY + date;
        Long incr = redisService.incr(key, 1);

        StringBuilder result = new StringBuilder(date);
        result.append(String.format("%02d", order.getSourceType()));
        result.append(String.format("%02d", order.getPayType()));

        String incrStr = incr.toString();
        if (incrStr.length() <= 6) {
            result.append(String.format("%06d", incr));
        } else {
            result.append(incrStr);
        }
        return result.toString();
    }

    /**
     * 根据订单项计算赠送成长值
     */
    private Integer calcGrowth(List<OmsOrderItem> orderItemList) {
        int sum = 0;
        for (OmsOrderItem item : orderItemList) {
            sum += (item.getGiftGrowth() * item.getProductQuantity());
        }
        return sum;
    }

    /**
     * 根据订单项计算赠送的积分
     */
    private Integer calcIntegration(List<OmsOrderItem> orderItemList) {
        int sum = 0;
        for (OmsOrderItem item : orderItemList) {
            sum += (item.getGiftIntegration() * item.getProductQuantity());
        }
        return sum;
    }

    /**
     * 计算订单支付价格
     */
    private BigDecimal calcPayAmount(OmsOrder order) {
        return order.getTotalAmount()
                .add(order.getFreightAmount())
                .subtract(order.getCouponAmount())
                .subtract(order.getIntegrationAmount())
                .subtract(order.getPromotionAmount());
    }

    /**
     * 计算订单项使用积分减免价格
     */
    private BigDecimal calcIntegrationAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal amount = new BigDecimal(0);
        for (OmsOrderItem item : orderItemList) {
            if (Objects.isNull(item.getIntegrationAmount())) continue;
            amount = amount.add(item.getIntegrationAmount());
        }
        return amount;
    }

    /**
     * 计算订单项使用优惠券减免价格
     */
    private BigDecimal calcCouponAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal amount = new BigDecimal(0);
        for (OmsOrderItem item : orderItemList) {
            if (Objects.isNull(item.getCouponAmount())) continue;
            amount = amount.add(item.getCouponAmount());
        }
        return amount;
    }

    /**
     * 获取订单项中的总减免信息
     */
    private String getPromotionInfo(List<OmsOrderItem> orderItemList) {
        StringBuilder sb = new StringBuilder();
        for (OmsOrderItem item : orderItemList) {
            if (StrUtil.isNotEmpty(item.getPromotionName())) {
                sb.append(item.getPromotionName())
                        .append(";");
            }
        }
        String res = sb.toString();
        if (res.endsWith(";")) {
            res = res.substring(0, res.length() - 1);
        }

        return res;
    }

    /**
     * 计算订单项中的总减免价格
     */
    private BigDecimal calcPromotionAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal amount = new BigDecimal(0);
        for (OmsOrderItem item : orderItemList) {
            if (Objects.isNull(item.getPromotionAmount())) continue;
            amount = amount.add(item.getPromotionAmount());
        }
        return amount;
    }

    /**
     * 锁定库存
     *
     * @param cartPromotionItemList 购物车项
     */
    private void handleLockStock(List<CartPromotionItemVo> cartPromotionItemList) {
        for (CartPromotionItemVo item : cartPromotionItemList) {
            PmsSkuStock skuStock = skuStockMapper.selectByPrimaryKey(item.getProductSkuId());
            skuStock.setLockStock(Optional.ofNullable(skuStock.getLockStock()).orElse(0) + item.getQuantity());
            skuStockMapper.updateByPrimaryKeySelective(skuStock);
        }
    }

    /**
     * 就算订单项真实金额
     */
    private void calcOrderItemRealAmount(List<OmsOrderItem> orderItemList) {
        for (OmsOrderItem item : orderItemList) {
            BigDecimal realAmount = item.getProductPrice()
                    .subtract(item.getPromotionAmount())
                    .subtract(item.getCouponAmount())
                    .subtract(item.getIntegrationAmount());
            item.setRealAmount(realAmount);
        }
    }

    /**
     * 获取积分兑换金额
     *
     * @param useIntegration 积分
     * @param totalAmount    总金额
     * @param current        当前用户
     * @param hasCoupon      是否使用了优惠券
     */
    private BigDecimal getUseIntegrationAmount(Integer useIntegration, BigDecimal totalAmount, UmsMember current, boolean hasCoupon) {
        BigDecimal amount = new BigDecimal(0);
        if (useIntegration.compareTo(current.getIntegration()) > 0) return amount;

        // 根据积分使用规则判断是否可用
        UmsIntegrationConsumeSetting consumeSetting = umsIntegrationConsumeSettingMapper.selectByPrimaryKey(1L);
        if (hasCoupon && consumeSetting.getCouponStatus().equals(0)) {
            return amount;
        }

        // 是否达到了积分使用门槛
        if (useIntegration.compareTo(consumeSetting.getUseUnit()) < 0) return amount;

        // 是否超过了订单抵用最高百分比
        BigDecimal percent = new BigDecimal(useIntegration).divide(new BigDecimal(consumeSetting.getUseUnit()), 2, RoundingMode.HALF_EVEN);
        BigDecimal maxPercent = new BigDecimal(consumeSetting.getMaxPercentPerOrder()).divide(new BigDecimal(100), 2, RoundingMode.HALF_EVEN);
        if (percent.compareTo(maxPercent) > 0) return amount;

        return percent;
    }

    /**
     * 对优惠券可以使用的商品进行减免操作
     *
     * @param orderItemList       订单项
     * @param couponHistoryDetail 优惠券信息
     */
    private void handleCouponAmount(List<OmsOrderItem> orderItemList, SmsCouponHistoryDetailVo couponHistoryDetail) {
        SmsCoupon coupon = couponHistoryDetail.getCoupon();
        Integer useType = coupon.getUseType();
        if (useType == 0) {
            // 全场通用
            calcPerCouponAmount(orderItemList, coupon);
            return;
        } else if (useType == 1) {
            // 指定分类
            orderItemList = getCouponOrderItemByRelation(couponHistoryDetail, orderItemList, 0);
        } else if (useType == 2) {
            // 指定商品
            orderItemList = getCouponOrderItemByRelation(couponHistoryDetail, orderItemList, 1);
        }
        calcPerCouponAmount(orderItemList, coupon);
    }

    /**
     * 获取置顶的订单项进行减免操作
     *
     * @param detail        优惠券详情
     * @param orderItemList 订单项
     * @param type          类型 0，分类 1，商品
     */
    private List<OmsOrderItem> getCouponOrderItemByRelation(SmsCouponHistoryDetailVo detail, List<OmsOrderItem> orderItemList, int type) {
        List<OmsOrderItem> ret = new ArrayList<>();
        if (type == 0) {
            // 过滤出分类id
            Set<Long> productCategoryIds = detail.getCategoryRelationList().stream().map(SmsCouponProductCategoryRelation::getProductCategoryId).collect(Collectors.toSet());
            for (OmsOrderItem item : orderItemList) {
                if (productCategoryIds.contains(item.getProductCategoryId())) {
                    ret.add(item);
                } else {
                    item.setCouponAmount(new BigDecimal(0));
                }
            }
        } else if (type == 1) {
            // 过滤出商品id
            Set<Long> productIds = detail.getProductRelationList().stream().map(SmsCouponProductRelation::getProductId).collect(Collectors.toSet());
            for (OmsOrderItem item : orderItemList) {
                if (productIds.contains(item.getProductId())) {
                    ret.add(item);
                } else {
                    item.setCouponAmount(new BigDecimal(0));
                }
            }
        }
        return ret;
    }

    /**
     * 对每个订单项进行减免
     */
    private void calcPerCouponAmount(List<OmsOrderItem> orderItemList, SmsCoupon coupon) {
        BigDecimal totalAmount = calcTotalAmount(orderItemList);
        for (OmsOrderItem item : orderItemList) {
            // 商品使用优惠券价格 = 商品价格 / 总价格 * 优惠券面额
            BigDecimal couponAmount = item.getProductPrice().divide(totalAmount, 3, RoundingMode.HALF_EVEN).multiply(coupon.getAmount());
            item.setCouponAmount(couponAmount);
        }
    }

    /**
     * 计算总金额
     */
    private BigDecimal calcTotalAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal total = new BigDecimal(0);
        for (OmsOrderItem item : orderItemList) {
            total = total.add(item.getProductPrice().multiply(new BigDecimal(item.getProductQuantity())));
        }
        return total;
    }

    /**
     * 获取该用户可以使用的优惠券
     *
     * @param couponId              优惠券id
     * @param cartPromotionItemList 购物项
     */
    private SmsCouponHistoryDetailVo getUseCoupon(Long couponId, List<CartPromotionItemVo> cartPromotionItemList) {
        List<SmsCouponHistoryDetailVo> details = umsMemberCouponService.listCoupons(cartPromotionItemList, 1);
        for (SmsCouponHistoryDetailVo detail : details) {
            if (detail.getCoupon().getId().equals(couponId)) {
                return detail;
            }
        }
        return null;
    }

    /**
     * 判断是否还有库存
     *
     * @param promotionItemVoList 购物车想
     */
    private boolean hasStock(List<CartPromotionItemVo> promotionItemVoList) {
        for (CartPromotionItemVo item : promotionItemVoList) {
            if (Objects.isNull(item.getRealStock()) || item.getRealStock() == 0 || item.getRealStock() < item.getQuantity()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 根据购物车项获取购物车总金额等信息
     */
    private ConfirmOrderVo.CalcAmount calcCartAmount(List<CartPromotionItemVo> itemList) {
        ConfirmOrderVo.CalcAmount amount = new ConfirmOrderVo.CalcAmount();
        amount.setFreightAmount(new BigDecimal(0));
        BigDecimal promotion = new BigDecimal(0);
        BigDecimal totalAmount = new BigDecimal(0);

        for (CartPromotionItemVo item : itemList) {
            BigDecimal quantity = new BigDecimal(item.getQuantity());
            totalAmount = totalAmount.add(item.getPrice().multiply(quantity));
            promotion = promotion.add(item.getReduceAmount().multiply(quantity));
        }

        amount.setPromotionAmount(promotion);
        amount.setPayAmount(totalAmount.subtract(promotion));
        amount.setTotalAmount(totalAmount);

        return amount;
    }
}
