package com.moli.mall.portal.service.impl;

import com.moli.mall.mbg.model.UmsMember;
import com.moli.mall.portal.domain.MemberProductCollection;
import com.moli.mall.portal.repository.MemberProductCollectionRepository;
import com.moli.mall.portal.service.MemberProductCollectionService;
import com.moli.mall.portal.service.UmsMemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/**
 * @author moli
 * @time 2024-04-06 22:51:13
 * @description 会员收藏管理
 */
@Service
public class MemberProductCollectionServiceImpl implements MemberProductCollectionService {

    @Resource
    private MemberProductCollectionRepository memberProductCollectionRepository;

    @Resource
    private UmsMemberService umsMemberService;

    @Override
    public int add(MemberProductCollection productCollection) {
        UmsMember current = umsMemberService.info();
        productCollection.setMemberId(current.getId());
        productCollection.setMemberNickname(current.getNickname());
        productCollection.setMemberIcon(current.getIcon());
        productCollection.setCreateTime(new Date());
        productCollection.setId(null);

        MemberProductCollection contains = memberProductCollectionRepository.findByMemberIdAndProductId(current.getId(), productCollection.getProductId());
        if (Objects.isNull(contains)) {
            memberProductCollectionRepository.insert(productCollection);
            return 1;
        }
        return 0;
    }

    @Override
    public int delete(Long productId) {
        UmsMember current = umsMemberService.info();
        return memberProductCollectionRepository.deleteByMemberIdAndProductId(current.getId(), productId);
    }

    @Override
    public Page<MemberProductCollection> list(Integer pageNum, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
        UmsMember current = umsMemberService.info();
        return memberProductCollectionRepository.findByMemberId(current.getId(), pageRequest);
    }

    @Override
    public MemberProductCollection detail(Long productId) {
        UmsMember current = umsMemberService.info();
        return memberProductCollectionRepository.findByMemberIdAndProductId(current.getId(), productId);
    }

    @Override
    public void clear() {
        UmsMember current = umsMemberService.info();
        memberProductCollectionRepository.deleteAllByMemberId(current.getId());
    }
}
