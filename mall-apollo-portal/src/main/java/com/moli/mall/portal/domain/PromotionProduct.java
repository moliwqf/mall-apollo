package com.moli.mall.portal.domain;

import com.moli.mall.mbg.model.PmsProduct;
import com.moli.mall.mbg.model.PmsProductFullReduction;
import com.moli.mall.mbg.model.PmsProductLadder;
import com.moli.mall.mbg.model.PmsSkuStock;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-08 10:25:57
 * @description 商品的促销信息，包括sku、打折优惠、满减优惠
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PromotionProduct extends PmsProduct {
    //商品库存信息
    private List<PmsSkuStock> skuStockList;
    //商品打折信息
    private List<PmsProductLadder> productLadderList;
    //商品满减信息
    private List<PmsProductFullReduction> productFullReductionList;
}
