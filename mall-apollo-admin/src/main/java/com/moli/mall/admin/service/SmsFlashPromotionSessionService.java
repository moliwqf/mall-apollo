package com.moli.mall.admin.service;

import com.moli.mall.admin.vo.SmsFlashPromotionSessionVo;
import com.moli.mall.mbg.model.SmsFlashPromotionSession;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-05 01:00:45
 * @description 限时购场次管理
 */
public interface SmsFlashPromotionSessionService {
    /**
     * 获取所有场次信息
     */
    List<SmsFlashPromotionSession> list();

    /**
     * 获取全部可选场次及其数量
     * @param flashPromotionId 活动id
     * @return 场次信息
     */
    List<SmsFlashPromotionSessionVo> selectList(Long flashPromotionId);
}
