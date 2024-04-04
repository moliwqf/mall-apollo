package com.moli.mall.admin.dao;

import com.moli.mall.mbg.model.SmsHomeRecommendSubject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 22:24:23
 * @description 首页专题推荐管理dao层
 */
@Repository
public interface SmsHomeRecommendSubjectDao {
    /**
     * 批量添加
     */
    int insertList(@Param("homeBrandList") List<SmsHomeRecommendSubject> homeBrandList);
}
