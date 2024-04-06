package com.moli.mall.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.moli.mall.admin.dao.SmsFlashPromotionProductRelationDao;
import com.moli.mall.admin.dao.SmsFlashPromotionSessionDao;
import com.moli.mall.admin.service.SmsFlashPromotionProductRelationService;
import com.moli.mall.admin.service.SmsFlashPromotionService;
import com.moli.mall.admin.vo.FlashPromotionProduct;
import com.moli.mall.admin.vo.HomeFlashPromotionVo;
import com.moli.mall.admin.vo.SmsFlashPromotionSessionVo;
import com.moli.mall.common.utils.AssetsUtil;
import com.moli.mall.mbg.mapper.SmsFlashPromotionMapper;
import com.moli.mall.mbg.mapper.SmsFlashPromotionProductRelationMapper;
import com.moli.mall.mbg.mapper.SmsFlashPromotionSessionMapper;
import com.moli.mall.mbg.model.SmsFlashPromotion;
import com.moli.mall.mbg.model.SmsFlashPromotionExample;
import com.moli.mall.mbg.model.SmsFlashPromotionSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
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

    @Resource
    private SmsFlashPromotionSessionDao smsFlashPromotionSessionDao;

    @Resource
    private SmsFlashPromotionProductRelationService smsFlashPromotionProductRelationService;

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
        flashPromotion.setCreateTime(new Date());
        return smsFlashPromotionMapper.insert(flashPromotion);
    }

    @Override
    public HomeFlashPromotionVo getCurrentFlashPromotion() {

        HomeFlashPromotionVo current = new HomeFlashPromotionVo();
        // 获取当前场次信息
        Date now = new Date();
        SmsFlashPromotionExample smsFlashPromotionExample = new SmsFlashPromotionExample();
        smsFlashPromotionExample.createCriteria()
                .andStartDateLessThanOrEqualTo(now)
                .andEndDateGreaterThanOrEqualTo(now)
                .andStatusEqualTo(1);
        List<SmsFlashPromotion> smsFlashPromotions = smsFlashPromotionMapper.selectByExample(smsFlashPromotionExample);

        if (CollectionUtils.isEmpty(smsFlashPromotions)) return null;

        SmsFlashPromotion promotion = smsFlashPromotions.get(0);
        Long promotionId = promotion.getId();
        // 获取当前开始的场次
        List<SmsFlashPromotionSession> currentSessions = smsFlashPromotionSessionDao.selectCurrentSession(now);
        if (!CollectionUtils.isEmpty(currentSessions)) {
            SmsFlashPromotionSession currentSession = currentSessions.get(0);
            current.setStartTime(currentSession.getStartTime());
            current.setEndTime(currentSession.getEndTime());

            // 获取下一场次
            List<SmsFlashPromotionSession> nextSessions = smsFlashPromotionSessionDao.selectNextSession(currentSession.getStartTime());
            if (!CollectionUtils.isEmpty(nextSessions)) {
                SmsFlashPromotionSession nextSession = nextSessions.get(0);
                current.setNextStartTime(nextSession.getStartTime());
                current.setNextEndTime(nextSession.getEndTime());
            }

            Long promotionSessionId = currentSession.getId();

            // 获取当前场次的商品
            List<FlashPromotionProduct> products = smsFlashPromotionProductRelationService.selectCurrentSessionProducts(promotionId, promotionSessionId);
            current.setProductList(products);
        } else {
            // 获取下一场次
            List<SmsFlashPromotionSession> nextSessions = smsFlashPromotionSessionDao.selectNextSession(now);
            if (!CollectionUtils.isEmpty(nextSessions)) {
                SmsFlashPromotionSession nextSession = nextSessions.get(0);
                current.setNextStartTime(nextSession.getStartTime());
                current.setNextEndTime(nextSession.getEndTime());
            }

            current.setProductList(new ArrayList<>());
        }

        return current;
    }
}
