package com.moli.mall.admin.dao;

import com.moli.mall.mbg.model.PmsProductLadder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 21:26:10
 * @description 商品价格区间服务dao层
 */
@Repository
public interface PmsProductLadderDao {
    /**
     * 批量添加商品价格区间
     * @param productLadderList 批量添加的对象
     */
    int insertList(@Param("productLadderList") List<PmsProductLadder> productLadderList);

}
