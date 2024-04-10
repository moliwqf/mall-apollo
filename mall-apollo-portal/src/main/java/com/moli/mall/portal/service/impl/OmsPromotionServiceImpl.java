package com.moli.mall.portal.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.moli.mall.common.utils.BeanCopyUtil;
import com.moli.mall.mbg.model.OmsCartItem;
import com.moli.mall.mbg.model.PmsProductFullReduction;
import com.moli.mall.mbg.model.PmsProductLadder;
import com.moli.mall.mbg.model.PmsSkuStock;
import com.moli.mall.portal.dao.PortalProductDao;
import com.moli.mall.portal.domain.PromotionProduct;
import com.moli.mall.portal.service.OmsPromotionService;
import com.moli.mall.portal.vo.CartPromotionItemVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author moli
 * @time 2024-04-08 10:17:06
 * @description 促销管理Service实现类
 */
@Service
public class OmsPromotionServiceImpl implements OmsPromotionService {

    @Resource
    private PortalProductDao productDao;

    @Override
    public List<CartPromotionItemVo> calcCartPromotion(List<OmsCartItem> cartItemList) {
        // 根据productId对购物车项进行分组，spu
        Map<Long, List<OmsCartItem>> productCartMap = groupCartItemBySpu(cartItemList);

        // 查询商品的优惠信息
        List<PromotionProduct> promotionProductList = getPromotionProductList(cartItemList);
        Map<Long, PromotionProduct> promotionProductMap = promotionProductList.stream().collect(Collectors.toMap(PromotionProduct::getId, Function.identity()));

        // 计算商品促销优惠价格
        List<CartPromotionItemVo> ret = new ArrayList<>();
        for (Map.Entry<Long, List<OmsCartItem>> entry : productCartMap.entrySet()) {
            Long productId = entry.getKey();
            PromotionProduct promotionProduct = promotionProductMap.get(productId);
            List<OmsCartItem> itemList = entry.getValue();
            Integer type = promotionProduct.getPromotionType();
            if (type == 1) {
                // 单品促销
                for (OmsCartItem item : itemList) {
                    CartPromotionItemVo current = BeanCopyUtil.copyBean(item, CartPromotionItemVo.class);
                    assert current != null;
                    current.setPromotionMessage("单品促销");
                    // 获取原价信息
                    PmsSkuStock skuStock = getOriginalPrice(promotionProduct, item.getProductSkuId());
                    BigDecimal originalPrice = skuStock.getPrice();

                    // 设置原价
                    current.setPrice(originalPrice);
                    // 设置促销价 原价 -促销价
                    current.setReduceAmount(originalPrice.subtract(skuStock.getPromotionPrice()));
                    // 设置剩余库存
                    current.setRealStock(skuStock.getStock() - skuStock.getLockStock());
                    // 设置增长点数
                    current.setIntegration(promotionProduct.getGiftPoint());
                    current.setGrowth(promotionProduct.getGiftGrowth());

                    ret.add(current);
                }
            } else if (type == 3) {
                // 使用阶梯价格
                int count = getCartItemCount(itemList);
                PmsProductLadder productLadder = getProductLadder(count, promotionProduct.getProductLadderList());
                if (Objects.isNull(productLadder)) {
                    handleNoReduce(ret, itemList, promotionProduct);
                } else {
                    for (OmsCartItem item : itemList) {
                        CartPromotionItemVo current = BeanCopyUtil.copyBean(item, CartPromotionItemVo.class);
                        assert current != null;
                        String message = getLadderPromotionMsg(productLadder);
                        current.setPromotionMessage(message);

                        PmsSkuStock skuStock = getOriginalPrice(promotionProduct, item.getProductSkuId());
                        BigDecimal originalPrice = skuStock.getPrice();

                        // 设置原价
                        current.setPrice(originalPrice);
                        // 设置促销价 原价 - 打折价
                        current.setReduceAmount(originalPrice.subtract(productLadder.getDiscount().multiply(originalPrice)));
                        // 设置剩余库存
                        current.setRealStock(skuStock.getStock() - skuStock.getLockStock());
                        // 设置增长点数
                        current.setIntegration(promotionProduct.getGiftPoint());
                        current.setGrowth(promotionProduct.getGiftGrowth());

                        ret.add(current);
                    }
                }
            } else if (type == 4) {
                // 使用满减价格
                // 计算当前商品总价格
                BigDecimal totalAmount = getCartItemAmount(itemList, promotionProductMap);
                PmsProductFullReduction fullReduction = getFullReduction(totalAmount, promotionProduct.getProductFullReductionList());
                if (Objects.nonNull(fullReduction)) {
                    for (OmsCartItem item : itemList) {
                        CartPromotionItemVo current = BeanCopyUtil.copyBean(item, CartPromotionItemVo.class);
                        assert current != null;
                        String message = getFullReductionMessage(fullReduction);
                        current.setPromotionMessage(message);

                        PmsSkuStock skuStock = getOriginalPrice(promotionProduct, item.getProductSkuId());
                        BigDecimal originalPrice = skuStock.getPrice();

                        // 设置原价
                        current.setPrice(originalPrice);
                        // 设置减免价
                        current.setReduceAmount(originalPrice.divide(totalAmount, RoundingMode.HALF_EVEN).multiply(fullReduction.getFullPrice()));
                        // 设置剩余库存
                        current.setRealStock(skuStock.getStock() - skuStock.getLockStock());
                        // 设置增长点数
                        current.setIntegration(promotionProduct.getGiftPoint());
                        current.setGrowth(promotionProduct.getGiftGrowth());

                        ret.add(current);
                    }
                } else {
                    handleNoReduce(ret, itemList, promotionProduct);
                }
            } else {
                // 无优惠
                handleNoReduce(ret, itemList, promotionProduct);
            }
        }
        return  ret;
    }

    /**
     * 获取满减信息
     */
    private String getFullReductionMessage(PmsProductFullReduction fullReduction) {
        return "满减优惠：" +
                "满" +
                fullReduction.getFullPrice() +
                "元，" +
                "减" +
                fullReduction.getReducePrice() +
                "元";
    }

    /**
     * 获取满减优惠信息
     */
    private PmsProductFullReduction getFullReduction(BigDecimal totalAmount, List<PmsProductFullReduction> productFullReductionList) {
        if (CollUtil.isEmpty(productFullReductionList)) return null;

        productFullReductionList.sort(new Comparator<PmsProductFullReduction>() {
            @Override
            public int compare(PmsProductFullReduction pre, PmsProductFullReduction rear) {
                return rear.getFullPrice().subtract(pre.getFullPrice()).intValueExact();
            }
        });
        for (PmsProductFullReduction fullReduction : productFullReductionList) {
            if (totalAmount.compareTo(fullReduction.getFullPrice()) > 0) {
                return fullReduction;
            }
        }
        return null;
    }

    /**
     * 没有减免优惠
     */
    private void handleNoReduce(List<CartPromotionItemVo> ret, List<OmsCartItem> itemList, PromotionProduct promotionProduct) {
        for (OmsCartItem item : itemList) {
            CartPromotionItemVo current = BeanCopyUtil.copyBean(item, CartPromotionItemVo.class);
            assert current != null;

            current.setPromotionMessage("无优惠");
            // 减免价格为0
            current.setReduceAmount(new BigDecimal(0));
            // 设置真实库存
            PmsSkuStock skuStock = getOriginalPrice(promotionProduct, item.getProductSkuId());
            if (Objects.nonNull(skuStock)) {
                current.setRealStock(skuStock.getStock() - Optional.ofNullable(skuStock.getLockStock()).orElse(0));
            }
            // 设置增长点数
            current.setIntegration(promotionProduct.getGiftPoint());
            current.setGrowth(promotionProduct.getGiftGrowth());

            ret.add(current);
        }
    }

    /**
     * 根据阶梯价获取信息
     */
    private String getLadderPromotionMsg(PmsProductLadder productLadder) {
        return "打折优惠：" +
                "满" +
                productLadder.getCount() +
                "件，" +
                "打" +
                productLadder.getDiscount().multiply(new BigDecimal(10)) +
                "折";
    }

    /**
     * 根据数量获取指定阶梯价格
     */
    private PmsProductLadder getProductLadder(int count, List<PmsProductLadder> productLadderList) {
        if (CollUtil.isEmpty(productLadderList)) return null;
        productLadderList.sort(new Comparator<PmsProductLadder>() {
            @Override
            public int compare(PmsProductLadder pre, PmsProductLadder rear) {
                return rear.getCount() - pre.getCount();
            }
        });
        for (PmsProductLadder pmsProductLadder : productLadderList) {
            if (count >= pmsProductLadder.getCount()) return pmsProductLadder;
        }
        return null;
    }

    /**
     * 获取购物车项中的总数量
     */
    private int getCartItemCount(List<OmsCartItem> itemList) {
        int count = 0;
        for (OmsCartItem omsCartItem : itemList) {
            count += omsCartItem.getQuantity();
        }
        return count;
    }

    /**
     * 获取购物车项中的总价格
     */
    private BigDecimal getCartItemAmount(List<OmsCartItem> itemList, Map<Long, PromotionProduct> promotionProductMap) {
        BigDecimal ret = new BigDecimal(0);

        for (OmsCartItem omsCartItem : itemList) {
            Long productId = omsCartItem.getProductId();
            PromotionProduct promotionProduct = promotionProductMap.get(productId);
            PmsSkuStock skuStock = getOriginalPrice(promotionProduct, omsCartItem.getProductSkuId());
            ret = ret.add(skuStock.getPrice().multiply(new BigDecimal(omsCartItem.getQuantity())));
        }
        return ret;
    }

    /**
     * 获取原价
     */
    private PmsSkuStock getOriginalPrice(PromotionProduct promotionProduct, Long productSkuId) {
        for (PmsSkuStock skuStock : promotionProduct.getSkuStockList()) {
            if (skuStock.getId().equals(productSkuId)) return skuStock;
        }
        return null;
    }

    private List<PromotionProduct> getPromotionProductList(List<OmsCartItem> cartItemList) {
        if (CollUtil.isEmpty(cartItemList)) return new ArrayList<>();
        List<Long> productIdList = cartItemList.stream().map(OmsCartItem::getProductId).collect(Collectors.toList());
        return productDao.getPromotionProductList(productIdList);
    }

    /**
     * 以spu为单位对购物车项进行分组
     */
    private Map<Long, List<OmsCartItem>> groupCartItemBySpu(List<OmsCartItem> cartItemList) {
        Map<Long, List<OmsCartItem>> productCartMap = new TreeMap<>();
        for (OmsCartItem omsCartItem : cartItemList) {
            Long productId = omsCartItem.getProductId();
            List<OmsCartItem> targetList = productCartMap.getOrDefault(productId, new ArrayList<>());
            targetList.add(omsCartItem);
            productCartMap.put(productId, targetList);
        }
        return productCartMap;
    }
}
