package com.simple.mall.repository;

import com.simple.mall.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findMemberById(String id);

    int countById(String id);

    int countByEmail(String email);
}
