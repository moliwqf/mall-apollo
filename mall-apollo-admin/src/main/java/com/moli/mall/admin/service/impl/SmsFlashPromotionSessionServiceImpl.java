package com.moli.mall.admin.service.impl;

import com.moli.mall.admin.dao.SmsFlashPromotionProductRelationDao;
import com.moli.mall.admin.service.SmsFlashPromotionSessionService;
import com.moli.mall.admin.vo.SmsFlashPromotionSessionVo;
import com.moli.mall.common.utils.AssetsUtil;
import com.moli.mall.common.utils.BeanCopyUtil;
import com.moli.mall.mbg.mapper.SmsFlashPromotionSessionMapper;
import com.moli.mall.mbg.model.SmsFlashPromotionSession;
import com.moli.mall.mbg.model.SmsFlashPromotionSessionExample;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author moli
 * @time 2024-04-05 01:00:58
 * @description 限时购场次管理
 */
@Service
public class SmsFlashPromotionSessionServiceImpl implements SmsFlashPromotionSessionService {

    @Resource
    private SmsFlashPromotionSessionMapper smsFlashPromotionSessionMapper;

    @Resource
    private SmsFlashPromotionProductRelationDao smsFlashPromotionProductRelationDao;

    @Override
    public List<SmsFlashPromotionSession> list() {
        return smsFlashPromotionSessionMapper.selectByExample(new SmsFlashPromotionSessionExample());
    }

    @Override
    public List<SmsFlashPromotionSessionVo> selectList(Long flashPromotionId) {
        // 查询所有启用的场次信息
        SmsFlashPromotionSessionExample smsFlashPromotionSessionExample = new SmsFlashPromotionSessionExample();
        smsFlashPromotionSessionExample.createCriteria().andStatusEqualTo(1);
        List<SmsFlashPromotionSession> smsFlashPromotionSessions = smsFlashPromotionSessionMapper.selectByExample(smsFlashPromotionSessionExample);

        // 查询各个场次下该促销的商品种类数
        List<SmsFlashPromotionSessionVo> sessionVoList = BeanCopyUtil.copyBeanList(smsFlashPromotionSessions, SmsFlashPromotionSessionVo.class);
        for (SmsFlashPromotionSessionVo session : sessionVoList) {
            Long count = smsFlashPromotionProductRelationDao.getCount(session.getId(), flashPromotionId);
            session.setProductCount(count);
        }
        return sessionVoList;
    }

    @Override
    @Transactional
    public int updateStatus(Long id, Integer status) {
        if (Objects.isNull(id) || id <= 0) AssetsUtil.fail("id非法");
        SmsFlashPromotionSession raw = smsFlashPromotionSessionMapper.selectByPrimaryKey(id);
        if (Objects.isNull(raw)) {
            return 0;
        }
        SmsFlashPromotionSession update = new SmsFlashPromotionSessionVo();
        update.setStatus(status);
        update.setId(id);
        return smsFlashPromotionSessionMapper.updateByPrimaryKeySelective(update);
    }

    @Override
    @Transactional
    public int update(Long id, SmsFlashPromotionSession promotionSession) {
        if (Objects.isNull(id) || id <= 0) AssetsUtil.fail("id非法");
        SmsFlashPromotionSession raw = smsFlashPromotionSessionMapper.selectByPrimaryKey(id);
        if (Objects.isNull(raw)) {
            return 0;
        }
        promotionSession.setId(id);
        return smsFlashPromotionSessionMapper.updateByPrimaryKeySelective(promotionSession);
    }

    @Override
    @Transactional
    public int delete(Long id) {
        return smsFlashPromotionSessionMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int create(SmsFlashPromotionSession promotionSession) {
        promotionSession.setCreateTime(new Date());
        return smsFlashPromotionSessionMapper.insert(promotionSession);
    }
}
