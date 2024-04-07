package com.moli.mall.portal.vo;

import com.moli.mall.mbg.model.CmsSubject;
import com.moli.mall.mbg.model.PmsBrand;
import com.moli.mall.mbg.model.PmsProduct;
import com.moli.mall.mbg.model.SmsHomeAdvertise;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-05 20:16:04
 * @description 首页内容返回信息封装
 */
@Getter
@Setter
public class HomeContentVo {

    @ApiModelProperty("轮播广告")
    private List<SmsHomeAdvertise> advertiseList;

    @ApiModelProperty("首页推荐品牌")
    private List<PmsBrand> brandList;

    @ApiModelProperty("当前秒杀场次")
    private HomeFlashPromotionVo homeFlashPromotion;

    @ApiModelProperty("新品推荐")
    private List<PmsProduct> newProductList;

    @ApiModelProperty("人气推荐")
    private List<PmsProduct> hotProductList;

    @ApiModelProperty("推荐专题")
    private List<CmsSubject> subjectList;
}
