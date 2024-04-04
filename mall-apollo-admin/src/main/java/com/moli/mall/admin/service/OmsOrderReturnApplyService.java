package com.moli.mall.admin.service;

import com.moli.mall.admin.dto.OmsReturnApplyQueryParams;
import com.moli.mall.admin.dto.OmsUpdateStatusParams;
import com.moli.mall.admin.vo.OmsOrderReturnApplyVo;
import com.moli.mall.mbg.model.OmsOrderReturnApply;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 16:47:51
 * @description 订单退货申请管理
 */
public interface OmsOrderReturnApplyService {
    /**
     * 分页查询退货申请
     */
    List<OmsOrderReturnApply> list(OmsReturnApplyQueryParams queryParam, Integer pageNum, Integer pageSize);

    /**
     * 批量删除
     */
    int delete(List<Long> ids);

    /**
     * 获取退货申请信息
     */
    OmsOrderReturnApplyVo info(Long id);

    /**
     * 更新状态
     */
    int updateStatus(Long id, OmsUpdateStatusParams statusParam);
}
