package com.moli.mall.admin.dao;

import com.moli.mall.mbg.model.PmsMemberPrice;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 21:38:39
 * @description 会员价格dao层
 */
@Repository
public interface PmsMemberPriceDao {
    /**
     * 批量添加会员价格设置
     * @param memberPriceList 添加的对象集合
     * @return 添加的行数
     */
    int insertList(@Param("memberPriceList") List<PmsMemberPrice> memberPriceList);
}
