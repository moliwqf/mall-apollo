package com.moli.mall.admin.service.impl;

import com.moli.mall.admin.dao.SmsFlashPromotionSessionDao;
import com.moli.mall.admin.service.SmsFlashPromotionSessionService;
import com.moli.mall.admin.vo.SmsFlashPromotionSessionVo;
import com.moli.mall.mbg.mapper.SmsFlashPromotionSessionMapper;
import com.moli.mall.mbg.model.SmsFlashPromotionSession;
import com.moli.mall.mbg.model.SmsFlashPromotionSessionExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
    private SmsFlashPromotionSessionDao smsFlashPromotionSessionDao;

    @Override
    public List<SmsFlashPromotionSession> list() {
        return smsFlashPromotionSessionMapper.selectByExample(new SmsFlashPromotionSessionExample());
    }

    @Override
    public List<SmsFlashPromotionSessionVo> selectList(Long flashPromotionId) {
        return smsFlashPromotionSessionDao.seleList(flashPromotionId);
    }
}
