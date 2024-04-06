package com.moli.mall.portal.repository;

import com.moli.mall.portal.domain.MemberProductCollection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author moli
 * @time 2024-04-06 22:54:35
 * @description 会员收藏管理
 */
public interface MemberProductCollectionRepository extends MongoRepository<MemberProductCollection, String> {
    /**
     * 根据用户id和产品id获取收藏信息
     */
    MemberProductCollection findByMemberIdAndProductId(Long memberId, Long productId);

    /**
     * 根据用户id和产品id删除信息
     */
    int deleteByMemberIdAndProductId(Long id, Long productId);

    /**
     * 分页查询收藏信息
     */
    Page<MemberProductCollection> findByMemberId(Long memberId, Pageable pageable);

    /**
     * 根据用户id删除所有收藏信息
     */
    void deleteAllByMemberId(Long memberId);
}
