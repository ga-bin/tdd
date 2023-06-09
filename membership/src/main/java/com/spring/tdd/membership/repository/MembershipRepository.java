package com.spring.tdd.membership.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.tdd.membership.entity.Membership;
import com.spring.tdd.membership.vo.MembershipType;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

	Membership findByUserIdAndMembershipType(final String userId, final MembershipType membershipType);

	List<Membership> findAllByUserId(final String userId);




}
