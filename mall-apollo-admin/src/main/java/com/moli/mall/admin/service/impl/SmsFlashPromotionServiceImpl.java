package com.moli.mall.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.moli.mall.admin.service.SmsFlashPromotionService;
import com.moli.mall.common.utils.AssetsUtil;
import com.moli.mall.mbg.mapper.SmsFlashPromotionMapper;
import com.moli.mall.mbg.model.SmsFlashPromotion;
import com.moli.mall.mbg.model.SmsFlashPromotionExample;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author moli
 * @time 2024-04-05 00:31:45
 * @description 限时购活动管理
 */
@Service
public class SmsFlashPromotionServiceImpl implements SmsFlashPromotionService {

    @Resource
    private SmsFlashPromotionMapper smsFlashPromotionMapper;

    @Override
    public List<SmsFlashPromotion> list(String keyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        SmsFlashPromotionExample smsFlashPromotionExample = new SmsFlashPromotionExample();
        SmsFlashPromotionExample.Criteria criteria = smsFlashPromotionExample.createCriteria();

        if (StrUtil.isNotEmpty(keyword)) {
            criteria.andTitleLike("%" + keyword + "%");
        }
        return smsFlashPromotionMapper.selectByExample(smsFlashPromotionExample);
    }

    @Override
    public SmsFlashPromotion getItem(Long id) {
        if (Objects.isNull(id) || id <= 0) AssetsUtil.fail("秒杀活动id非法");
        return smsFlashPromotionMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int updateStatus(Long id, Integer status) {
        if (Objects.isNull(id) || id <= 0) AssetsUtil.fail("秒杀活动id非法");
        SmsFlashPromotion update = new SmsFlashPromotion();
        update.setId(id);
        update.setStatus(status);
        return smsFlashPromotionMapper.updateByPrimaryKeySelective(update);
    }

    @Override
    @Transactional
    public int delete(Long id) {
        if (Objects.isNull(id) || id <= 0) AssetsUtil.fail("秒杀活动id非法");
        return smsFlashPromotionMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int update(Long id, SmsFlashPromotion flashPromotion) {
        if (Objects.isNull(id) || id <= 0) AssetsUtil.fail("秒杀活动id非法");
        flashPromotion.setId(id);
        return smsFlashPromotionMapper.updateByPrimaryKeySelective(flashPromotion);
    }

    @Override
    @Transactional
    public int create(SmsFlashPromotion flashPromotion) {
        return smsFlashPromotionMapper.insert(flashPromotion);
    }
}
