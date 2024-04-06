package com.moli.mall.portal.service.impl;

import com.moli.mall.mbg.model.UmsMember;
import com.moli.mall.portal.repository.MemberReadHistoryRepository;
import com.moli.mall.portal.domain.MemberReadHistory;
import com.moli.mall.portal.service.MemberReadHistoryService;
import com.moli.mall.portal.service.UmsMemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author moli
 * @time 2024-04-06 17:23:25
 * @description 会员商品浏览记录管理
 */
@Service
public class MemberReadHistoryServiceImpl implements MemberReadHistoryService {

    @Resource
    private MemberReadHistoryRepository memberReadHistoryRepository;

    @Resource
    private UmsMemberService umsMemberService;

    @Override
    public int create(MemberReadHistory memberReadHistory) {
        UmsMember current = umsMemberService.info();
        memberReadHistory.setMemberId(current.getId());
        memberReadHistory.setMemberNickname(current.getNickname());
        memberReadHistory.setMemberIcon(current.getIcon());
        memberReadHistory.setCreateTime(new Date());
        memberReadHistory.setId(null);

        memberReadHistoryRepository.save(memberReadHistory);
        return 1;
    }

    @Override
    public int delete(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) return 0;
        // 过滤重复的id
        List<String> newIds = ids.stream().distinct().collect(Collectors.toList());
        memberReadHistoryRepository.deleteAllById(newIds);
        return newIds.size();
    }

    @Override
    public void clear() {
        UmsMember current = umsMemberService.info();
        memberReadHistoryRepository.deleteAllByMemberId(current.getId());
    }

    @Override
    public Page<MemberReadHistory> list(Integer pageNum, Integer pageSize) {
        UmsMember current = umsMemberService.info();
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
        return memberReadHistoryRepository.findByMemberIdOrderByCreateTimeDesc(current.getId(), pageRequest);
    }
}
