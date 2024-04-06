package com.moli.mall.portal.repository;

import com.moli.mall.portal.domain.MemberReadHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author moli
 * @time 2024-04-06 20:11:56
 * @description 浏览记录dao
 */
public interface MemberReadHistoryRepository extends MongoRepository<MemberReadHistory, String> {
    /**
     * 分页查询浏览记录
     */
    Page<MemberReadHistory> findByMemberIdOrderByCreateTimeDesc(Long memberId, Pageable pageable);

    /**
     * 根据会员id删除记录
     */
    void deleteAllByMemberId(Long memberId);
}
