package com.moli.mall.admin.service;

import com.moli.mall.admin.dto.PmsBrandParams;
import com.moli.mall.mbg.model.PmsBrand;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 15:33:41
 * @description 品牌服务层
 */
public interface PmsBrandService {

    /**
     * 分页模糊查询品牌信息
     */
    List<PmsBrand> listBrand(String keyword, Integer pageNum, Integer pageSize);

    /**
     * 根据id获取品牌信息
     * @param id id
     */
    PmsBrand info(Long id);

    /**
     * 创建新品牌
     */
    int createBrand(PmsBrandParams pmsBrand);

    /**
     * 更新品牌信息
     */
    int updateBrand(Long id, PmsBrandParams pmsBrandParam);

    /**
     * 删除品牌
     */
    int deleteBrand(Long id);

    /**
     * 更新显示状态
     * @param ids 需要更新的id集合
     * @param showStatus 状态
     */
    int updateShowStatus(List<Long> ids, Integer showStatus);

    /**
     * 修改是否为品牌制造商
     */
    int updateFactoryStatus(List<Long> ids, Integer factoryStatus);
}
