package com.moli.mall.portal.service.impl;

import com.moli.mall.mbg.model.UmsMember;
import com.moli.mall.portal.domain.MemberBrandAttention;
import com.moli.mall.portal.repository.MemberAttentionRepository;
import com.moli.mall.portal.service.MemberAttentionService;
import com.moli.mall.portal.service.UmsMemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/**
 * @author moli
 * @time 2024-04-06 20:46:33
 * @description 会员关注品牌管理
 */
@Service
public class MemberAttentionServiceImpl implements MemberAttentionService {

    @Resource
    private MemberAttentionRepository memberAttentionRepository;

    @Resource
    private UmsMemberService umsMemberService;

    @Override
    public int add(MemberBrandAttention memberBrandAttention) {
        UmsMember current = umsMemberService.info();
        memberBrandAttention.setMemberId(current.getId());
        memberBrandAttention.setMemberNickname(current.getNickname());
        memberBrandAttention.setMemberIcon(current.getIcon());
        memberBrandAttention.setCreateTime(new Date());
        memberBrandAttention.setId(null);

        // 查询是否存在该关注信息
        MemberBrandAttention contains = memberAttentionRepository.findByMemberIdAndBrandId(current.getId(), memberBrandAttention.getBrandId());
        if (Objects.isNull(contains)) {
            memberAttentionRepository.insert(memberBrandAttention);
            return 1;
        }

        return 0;
    }

    @Override
    public int delete(Long brandId) {
        UmsMember current = umsMemberService.info();
        return memberAttentionRepository.deleteByMemberIdAndBrandId(current.getId(), brandId);
    }

    @Override
    public Page<MemberBrandAttention> list(Integer pageNum, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
        UmsMember current = umsMemberService.info();
        return memberAttentionRepository.findByMemberId(current.getId(), pageRequest);
    }

    @Override
    public MemberBrandAttention detail(Long brandId) {
        UmsMember current = umsMemberService.info();
        return memberAttentionRepository.findByMemberIdAndBrandId(current.getId(), brandId);
    }

    @Override
    public void clear() {
        UmsMember current = umsMemberService.info();
        memberAttentionRepository.deleteAllByMemberId(current.getId());
    }
}
