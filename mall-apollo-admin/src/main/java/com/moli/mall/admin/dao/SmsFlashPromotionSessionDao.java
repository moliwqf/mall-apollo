package com.moli.mall.admin.dao;

import com.moli.mall.mbg.model.SmsFlashPromotionSession;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


/**
 * @author moli
 * @time 2024-04-05 01:05:45
 * @description 限时购场次管理dao
 */
@Repository
public interface SmsFlashPromotionSessionDao {
    /**
     * 查询目标时间的场次
     * @param date 目标时间
     */
    List<SmsFlashPromotionSession> selectCurrentSession(@Param("date") Date date);

    /**
     * 查询大于开始时间date时间的第一个场次
     * @param date 目标时间
     */
    List<SmsFlashPromotionSession> selectNextSession(@Param("date") Date date);
}
