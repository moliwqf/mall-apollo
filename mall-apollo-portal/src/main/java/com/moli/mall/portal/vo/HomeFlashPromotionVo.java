package com.moli.mall.portal.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-05 20:17:45
 * @description 首页当前秒杀场次信息
 */
@Getter
@Setter
public class HomeFlashPromotionVo {

    @ApiModelProperty("开始的时间")
    private Date startTime;

    @ApiModelProperty("结束事件")
    private Date endTime;

    @ApiModelProperty("下一次开启时间")
    private Date nextStartTime;

    @ApiModelProperty("下一次结束时间")
    private Date nextEndTime;

    @ApiModelProperty("属于该秒杀活动的商品")
    private List<FlashPromotionProduct> productList;
}
