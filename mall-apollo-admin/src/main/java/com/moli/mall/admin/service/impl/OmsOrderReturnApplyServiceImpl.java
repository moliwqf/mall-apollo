package com.moli.mall.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.moli.mall.admin.constant.enums.OrderReturnStatus;
import com.moli.mall.admin.dto.OmsReturnApplyQueryParams;
import com.moli.mall.admin.dto.OmsUpdateStatusParams;
import com.moli.mall.admin.service.OmsOrderReturnApplyService;
import com.moli.mall.admin.vo.OmsOrderReturnApplyVo;
import com.moli.mall.common.utils.AssetsUtil;
import com.moli.mall.common.utils.BeanCopyUtil;
import com.moli.mall.mbg.mapper.OmsCompanyAddressMapper;
import com.moli.mall.mbg.mapper.OmsOrderReturnApplyMapper;
import com.moli.mall.mbg.model.OmsCompanyAddress;
import com.moli.mall.mbg.model.OmsOrderReturnApply;
import com.moli.mall.mbg.model.OmsOrderReturnApplyExample;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author moli
 * @time 2024-04-04 16:48:05
 * @description 订单退货申请管理
 */
@Service
public class OmsOrderReturnApplyServiceImpl implements OmsOrderReturnApplyService {

    @Resource
    private OmsOrderReturnApplyMapper omsOrderReturnApplyMapper;

    @Resource
    private OmsCompanyAddressMapper omsCompanyAddressMapper;

    @Override
    public List<OmsOrderReturnApply> list(OmsReturnApplyQueryParams queryParam, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        OmsOrderReturnApplyExample omsOrderReturnApplyExample = checkAndGetExample(queryParam);

        return omsOrderReturnApplyMapper.selectByExample(omsOrderReturnApplyExample);
    }

    @Override
    @Transactional
    public int delete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) return 0;
        OmsOrderReturnApplyExample omsOrderReturnApplyExample = new OmsOrderReturnApplyExample();
        omsOrderReturnApplyExample.createCriteria().andIdIn(ids);
        return omsOrderReturnApplyMapper.deleteByExample(omsOrderReturnApplyExample);
    }

    @Override
    public OmsOrderReturnApplyVo info(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            AssetsUtil.fail("获取申请信息失败");
        }

        // 获取基础信息
        OmsOrderReturnApply omsOrderReturnApply = omsOrderReturnApplyMapper.selectByPrimaryKey(id);
        if (Objects.isNull(omsOrderReturnApply)) {
            AssetsUtil.fail("获取申请信息失败");
        }

        OmsOrderReturnApplyVo result = BeanCopyUtil.copyBean(omsOrderReturnApply, OmsOrderReturnApplyVo.class);
        assert result != null;

        // 获取公司收获地址
        Long companyAddressId = omsOrderReturnApply.getCompanyAddressId();
        if (Objects.isNull(companyAddressId)) {
            return result;
        }
        OmsCompanyAddress omsCompanyAddress = omsCompanyAddressMapper.selectByPrimaryKey(companyAddressId);
        result.setCompanyAddress(omsCompanyAddress);

        return result;
    }

    @Override
    @Transactional
    public int updateStatus(Long id, OmsUpdateStatusParams statusParam) {
        // 查询退货申请信息
        OmsOrderReturnApply rawInfo = omsOrderReturnApplyMapper.selectByPrimaryKey(id);
        if (Objects.isNull(rawInfo)) {
            AssetsUtil.fail("不存在该申请信息!!");
        }

        OmsOrderReturnApply update = new OmsOrderReturnApply();
        update.setId(id);

        Integer status = statusParam.getStatus();
        OrderReturnStatus orderReturnStatus = OrderReturnStatus.castStatus(status);

        switch (orderReturnStatus) {
            case RETURNING:
                // 确认退货
                update.setStatus(OrderReturnStatus.RETURNING.getStatus());
                update.setHandleTime(new Date());
                update.setHandleMan(statusParam.getHandleMan());
                update.setHandleNote(statusParam.getHandleNote());
                update.setCompanyAddressId(statusParam.getCompanyAddressId());
                update.setReturnAmount(statusParam.getReturnAmount());
            case REJECTED:
                update.setStatus(OrderReturnStatus.REJECTED.getStatus());
                update.setHandleTime(new Date());
                update.setHandleMan(statusParam.getHandleMan());
                update.setHandleNote(statusParam.getHandleNote());
            case FINISHED:
                update.setStatus(OrderReturnStatus.FINISHED.getStatus());
                update.setReceiveTime(new Date());
                update.setReceiveMan(statusParam.getReceiveMan());
                update.setReceiveNote(statusParam.getReceiveNote());
                break;
            case UN_HANDLE:
            default:
                return 0;
        }

        return omsOrderReturnApplyMapper.updateByPrimaryKeySelective(update);
    }

    /**
     * 根据请求参数获取查询语句
     */
    private OmsOrderReturnApplyExample checkAndGetExample(OmsReturnApplyQueryParams queryParam) {
        OmsOrderReturnApplyExample omsOrderReturnApplyExample = new OmsOrderReturnApplyExample();
        OmsOrderReturnApplyExample.Criteria criteria = omsOrderReturnApplyExample.createCriteria();
        if (Objects.nonNull(queryParam.getId())) {
            criteria.andIdEqualTo(queryParam.getId());
        }
        if (StrUtil.isNotEmpty(queryParam.getReceiverKeyword())) {
            String keyword = queryParam.getReceiverKeyword();
            criteria.andReceiveManLike("%" + keyword + "%");
            omsOrderReturnApplyExample.or().andReturnPhoneLike("%" + keyword + "%");
        }
        if (StrUtil.isNotEmpty(queryParam.getHandleMan())) {
            criteria.andHandleManLike("%" + queryParam.getHandleMan() + "%");
        }
        if (Objects.nonNull(queryParam.getStatus())) {
            criteria.andStatusEqualTo(queryParam.getStatus());
        }
        if (StrUtil.isNotEmpty(queryParam.getCreateTime())) {
            Date createTime = new Date(DateUtil.parse(queryParam.getCreateTime(), "yyyy-MM-dd").getTime());
            criteria.andCreateTimeGreaterThanOrEqualTo(createTime);
        }
        if (StrUtil.isNotEmpty(queryParam.getHandleTime())) {
            Date handleTime = new Date(DateUtil.parse(queryParam.getHandleTime(), "yyyy-MM-dd").getTime());
            criteria.andHandleTimeGreaterThanOrEqualTo(handleTime);
        }
        return omsOrderReturnApplyExample;
    }
}
