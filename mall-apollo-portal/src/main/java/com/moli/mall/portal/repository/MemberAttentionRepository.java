package com.moli.mall.portal.repository;

import com.moli.mall.portal.domain.MemberBrandAttention;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author moli
 * @time 2024-04-06 22:39:47
 * @description 会员关注品牌管理
 */
public interface MemberAttentionRepository extends MongoRepository<MemberBrandAttention, String> {

    /**
     * 根据用户id和品牌id删除
     */
    int deleteByMemberIdAndBrandId(Long memberId, Long brandId);

    /**
     * 根据用户id和品牌id查询
     */
    MemberBrandAttention findByMemberIdAndBrandId(Long memberId, Long brandId);

    /**
     * 根据用户id分页查询
     */
    Page<MemberBrandAttention> findByMemberId(Long memberId, Pageable pageable);

    /**
     * 根据用户id删除所有关注信息
     */
    void deleteAllByMemberId(Long memberId);
}
