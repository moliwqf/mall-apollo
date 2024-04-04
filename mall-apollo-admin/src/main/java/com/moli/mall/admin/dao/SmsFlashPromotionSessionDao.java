package com.moli.mall.admin.dao;

import com.moli.mall.admin.vo.SmsFlashPromotionSessionVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-05 01:05:45
 * @description 限时购场次管理dao
 */
@Repository
public interface SmsFlashPromotionSessionDao {
    /**
     * 获取全部可选场次及其数量
     */
    List<SmsFlashPromotionSessionVo> seleList(@Param("flashPromotionId") Long flashPromotionId);
}
