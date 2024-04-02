package com.moli.mall.admin.service;

import com.moli.mall.admin.dto.PmsProductParams;
import com.moli.mall.admin.dto.PmsProductQueryParams;
import com.moli.mall.admin.vo.PmsProductResultVo;
import com.moli.mall.mbg.model.PmsProduct;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 15:38:34
 * @description 产品服务层
 */
public interface PmsProductService {
    /**
     * 分页模糊查询产品信息
     */
    List<PmsProduct> list(PmsProductQueryParams productQueryParam, Integer pageNum, Integer pageSize);

    /**
     * 根据商品id获取商品编辑信息
     * @param id 商品id
     */
    PmsProductResultVo getUpdateInfo(Long id);

    /**
     * 更新商品信息
     * @param id 商品id
     * @param productParam 商品的信息
     * @return 是否更新
     */
    int update(Long id, PmsProductParams productParam);

    /**
     * 创建新的商品
     * @param productParam 商品信息
     */
    int create(PmsProductParams productParam);

    /**
     * 更新删除状态
     * @param ids 更新的id
     * @param deleteStatus 状态信息
     * @return 更新的行数
     */
    int updateDeleteStatus(List<Long> ids, Integer deleteStatus);

    /**
     * 批量更新推荐状态
     */
    int updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 批量更新审核状态
     * @param ids id集合
     * @param verifyStatus 状态
     * @param detail 详情
     */
    int updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail);

    /**
     * 批量更新发布状态
     */
    int updatePublishStatus(List<Long> ids, Integer publishStatus);

    /**
     * 批量设置为新品
     */
    int updateNewStatus(List<Long> ids, Integer newStatus);

    /**
     * 根据商品名称或货号模糊查询
     * @param keyword 商品名称或货号
     */
    List<PmsProduct> list(String keyword);
}
