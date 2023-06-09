package com.spring.tdd.membership.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.spring.tdd.membership.entity.Membership;
import com.spring.tdd.membership.error.MembershipErrorResult;
import com.spring.tdd.membership.error.MembershipException;
import com.spring.tdd.membership.repository.MembershipRepository;
import com.spring.tdd.membership.vo.MembershipAddResponse;
import com.spring.tdd.membership.vo.MembershipDetailResponse;
import com.spring.tdd.membership.vo.MembershipType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MembershipService {

	private final MembershipRepository membershipRepository;
	
	
	public MembershipAddResponse addMembership(final String userId, final MembershipType membershipType, final Integer point) {
		final Membership result = membershipRepository.findByUserIdAndMembershipType(userId, membershipType);
		if(result != null) {
			throw new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
		}
		
		final Membership membership = Membership.builder()
					.userId(userId)
					.point(point)
					.membershipType(membershipType)
					.build();
	
		final Membership savedMembership = membershipRepository.save(membership);
		
		return MembershipAddResponse.builder()
					.id(savedMembership.getId())
					.membershipType(savedMembership.getMembershipType())
					.build();
	}
	
	public List<MembershipDetailResponse> getMembershipList(final String userId) {
		final List<Membership> membershipList = membershipRepository.findAllByUserId(userId);
		
		return membershipList.stream()
				.map(v -> MembershipDetailResponse.builder()
						.id(v.getId())
						.membershipType(v.getMembershipType())
						.point(v.getPoint())
						.createdAt(v.getCreatedAt())
						.build())
				.collect(Collectors.toList());
	}

	public MembershipDetailResponse getMembership(final Long membershipId, final String userId) {
		final Optional<Membership> optionalMembership = membershipRepository.findById(membershipId);
		final Membership membership = optionalMembership.orElseThrow(() -> new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND));
		
		if(!membership.getUserId().equals(userId)) {
			throw new MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
		}
		
		return MembershipDetailResponse.builder()
				.id(membership.getId())
				.membershipType(membership.getMembershipType())
				.point(membership.getPoint())
				.createdAt(membership.getCreatedAt())
				.build();
		
	}

}
