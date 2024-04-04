package com.moli.mall.admin.vo;

import com.moli.mall.mbg.model.SmsFlashPromotionSession;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author moli
 * @time 2024-04-05 01:03:19
 * @description 包含商品数量的场次信息
 */
public class SmsFlashPromotionSessionVo extends SmsFlashPromotionSession {

    @Setter
    @Getter
    @ApiModelProperty("商品数量")
    private Long productCount;
}
