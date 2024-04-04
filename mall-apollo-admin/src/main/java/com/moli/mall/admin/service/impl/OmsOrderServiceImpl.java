package com.moli.mall.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.moli.mall.admin.constant.enums.OrderStatusEnum;
import com.moli.mall.admin.dao.OmsOrderDao;
import com.moli.mall.admin.dao.OmsOrderOperateHistoryDao;
import com.moli.mall.admin.dto.OmsOrderDeliveryParams;
import com.moli.mall.admin.dto.OmsOrderQueryParams;
import com.moli.mall.admin.service.OmsOrderService;
import com.moli.mall.admin.vo.OmsOrderDetailVo;
import com.moli.mall.common.constant.CommonStatus;
import com.moli.mall.common.utils.AssetsUtil;
import com.moli.mall.common.utils.BeanCopyUtil;
import com.moli.mall.mbg.mapper.OmsOrderItemMapper;
import com.moli.mall.mbg.mapper.OmsOrderMapper;
import com.moli.mall.mbg.mapper.OmsOrderOperateHistoryMapper;
import com.moli.mall.mbg.model.*;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author moli
 * @time 2024-04-04 07:55:36
 * @description 订单服务层
 */
@Service
public class OmsOrderServiceImpl implements OmsOrderService {

    @Resource
    private OmsOrderMapper omsOrderMapper;

    @Resource
    private OmsOrderItemMapper omsOrderItemMapper;

    @Resource
    private OmsOrderOperateHistoryMapper omsOrderOperateHistoryMapper;

    @Resource
    private OmsOrderOperateHistoryDao omsOrderOperateHistoryDao;

    @Resource
    private OmsOrderDao omsOrderDao;

    @Override
    public List<OmsOrder> list(Integer pageNum, Integer pageSize, OmsOrderQueryParams queryParam) {
        PageHelper.startPage(pageNum, pageSize);

        OmsOrderExample omsOrderExample = checkAndGetExample(queryParam);

        return omsOrderMapper.selectByExample(omsOrderExample);
    }

    @Override
    public OmsOrderDetailVo detail(Long orderId) {
        // 根据id获取订单信息
        OmsOrder rawOrder = omsOrderMapper.selectByPrimaryKey(orderId);
        if (Objects.isNull(rawOrder)) {
            return null;
        }
        OmsOrderDetailVo detail = BeanCopyUtil.copyBean(rawOrder, OmsOrderDetailVo.class);
        assert detail != null;

        // 设置订单项
        CompletableFuture<List<OmsOrderItem>> itemFuture = CompletableFuture.supplyAsync(() -> {
            OmsOrderItemExample omsOrderItemExample = new OmsOrderItemExample();
            omsOrderItemExample.createCriteria().andOrderIdEqualTo(orderId);
            return omsOrderItemMapper.selectByExample(omsOrderItemExample);
        }).whenComplete((omsOrderItems, e) -> detail.setOrderItemList(omsOrderItems));

        // 设置操作历史记录
        OmsOrderOperateHistoryExample omsOrderOperateHistoryExample = new OmsOrderOperateHistoryExample();
        omsOrderOperateHistoryExample.createCriteria().andOrderIdEqualTo(orderId);
        omsOrderOperateHistoryExample.setOrderByClause("create_time desc");
        List<OmsOrderOperateHistory> omsOrderOperateHistories = omsOrderOperateHistoryMapper.selectByExample(omsOrderOperateHistoryExample);
        detail.setHistoryList(omsOrderOperateHistories);

        try {
            CompletableFuture.allOf(itemFuture).get();
        } catch (InterruptedException | ExecutionException e) {
            AssetsUtil.fail("获取订单信息失败");
        }
        return detail;
    }

    @Override
    @Transactional
    public int delivery(List<OmsOrderDeliveryParams> deliveryParamList) {
        if (CollectionUtils.isEmpty(deliveryParamList)) return 0;
        // 修改订单发货状态
        // 更新发货时间
        int count = omsOrderDao.updateDeliveryList(deliveryParamList);
        // 订单操作记录
        List<OmsOrderOperateHistory> historyList = deliveryParamList.stream().map((del) -> {
            OmsOrderOperateHistory history = new OmsOrderOperateHistory();
            history.setOrderId(del.getOrderId());
            history.setOperateMan("后台管理员");
            history.setOrderStatus(OrderStatusEnum.SHIPPING.getStatus());
            history.setCreateTime(new Date());
            history.setNote("完成发货");
            return history;
        }).collect(Collectors.toList());
        omsOrderOperateHistoryDao.insertList(historyList);
        return count;
    }

    @SneakyThrows
    @Override
    @Transactional
    public int updateNote(Long id, String note, Integer status) {
        // 查询是否存在该订单
        OmsOrderExample omsOrderExample = new OmsOrderExample();
        omsOrderExample.createCriteria().andDeleteStatusEqualTo(0).andIdEqualTo(id);
        List<OmsOrder> omsOrders = omsOrderMapper.selectByExample(omsOrderExample);
        if (CollectionUtils.isEmpty(omsOrders)) {
            AssetsUtil.fail("无效订单！！");
        }
        // 修改订单操作记录
        CompletableFuture<Void> historyFuture = CompletableFuture.runAsync(() -> {
            OmsOrderOperateHistory history = new OmsOrderOperateHistory();
            history.setOrderId(id);
            history.setNote("修改订单备注：" + note);
            history.setOrderStatus(status);
            history.setOperateMan("后台管理员");
            history.setCreateTime(new Date());
            omsOrderOperateHistoryMapper.insert(history);
        });
        // 修改订单
        OmsOrder updateOrder = new OmsOrder();
        updateOrder.setId(id);
        updateOrder.setNote(note);
        updateOrder.setModifyTime(new Date());

        historyFuture.get();

        return omsOrderMapper.updateByPrimaryKeySelective(updateOrder);
    }

    @Override
    @Transactional
    public int delete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        OmsOrder deleteOrder = new OmsOrder();
        deleteOrder.setDeleteStatus(1);
        OmsOrderExample omsOrderExample = new OmsOrderExample();
        omsOrderExample.createCriteria().andIdIn(ids).andDeleteStatusEqualTo(0);
        return omsOrderMapper.updateByExampleSelective(deleteOrder, omsOrderExample);
    }

    @SneakyThrows
    @Override
    public int close(List<Long> ids, String note) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        // 新增操作记录
        CompletableFuture<Void> historyFuture = CompletableFuture.runAsync(() -> {
            List<OmsOrderOperateHistory> historyList = ids.stream().map((id) -> {
                OmsOrderOperateHistory history = new OmsOrderOperateHistory();
                history.setOrderId(id);
                history.setNote("关闭订单备注：" + note);
                history.setOrderStatus(OrderStatusEnum.CLOSED.getStatus());
                history.setOperateMan("后台管理员");
                history.setCreateTime(new Date());
                return history;
            }).collect(Collectors.toList());
            omsOrderOperateHistoryDao.insertList(historyList);
        });

        // 更新订单状态
        OmsOrder closeOrder = new OmsOrder();
        closeOrder.setStatus(OrderStatusEnum.CLOSED.getStatus());
        OmsOrderExample omsOrderExample = new OmsOrderExample();
        omsOrderExample.createCriteria().andIdIn(ids).andDeleteStatusEqualTo(0);

        historyFuture.get();

        return omsOrderMapper.updateByExampleSelective(closeOrder, omsOrderExample);
    }

    /**
     * 根据请求参数获取查询条件
     */
    private OmsOrderExample checkAndGetExample(OmsOrderQueryParams queryParam) {
        OmsOrderExample omsOrderExample = new OmsOrderExample();
        OmsOrderExample.Criteria criteria = omsOrderExample.createCriteria();

        criteria.andDeleteStatusEqualTo(0);

        if (StrUtil.isNotEmpty(queryParam.getOrderSn())) {
            criteria.andOrderSnLike("%" + queryParam.getOrderSn() + "%");
        }

        if (StrUtil.isNotEmpty(queryParam.getReceiverKeyword())) {
            criteria.andReceiverNameLike("%" + queryParam.getReceiverKeyword() + "%");
            omsOrderExample.or().andReceiverPhoneLike("%" + queryParam.getReceiverKeyword() + "%");
        }

        if (StrUtil.isNotEmpty(queryParam.getCreateTime())) {
            Date createTime  = new Date(DateUtil.parse(queryParam.getCreateTime(), "yyyy-MM-dd").getTime());
            criteria.andCreateTimeGreaterThanOrEqualTo(createTime);
        }

        if (Objects.nonNull(queryParam.getStatus())) {
            criteria.andStatusEqualTo(queryParam.getStatus());
        }

        if (Objects.nonNull(queryParam.getOrderType())) {
            criteria.andOrderTypeEqualTo(queryParam.getOrderType());
        }

        if (Objects.nonNull(queryParam.getSourceType())) {
            criteria.andSourceTypeEqualTo(queryParam.getSourceType());
        }
        return omsOrderExample;
    }
}
