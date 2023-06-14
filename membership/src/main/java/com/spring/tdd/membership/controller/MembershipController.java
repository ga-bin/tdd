package com.spring.tdd.membership.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.spring.tdd.membership.service.MembershipService;
import com.spring.tdd.membership.validation.ValidationGroups.MembershipAccumulateMarker;
import com.spring.tdd.membership.validation.ValidationGroups.MembershipAddMarker;
import com.spring.tdd.membership.vo.MembershipAddResponse;
import com.spring.tdd.membership.vo.MembershipDetailResponse;
import com.spring.tdd.membership.vo.MembershipRequest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class MembershipController {

	private final MembershipService membershipService;

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public final class MembershipConstants {
		public final static String USER_ID_HEADER = "X-USER-ID";
	}
	
	
	@PostMapping("/api/v1/memberships")
	public ResponseEntity<MembershipAddResponse> addMembership(
		 @RequestHeader(MembershipConstants.USER_ID_HEADER) final String userId,
		 @RequestBody @Validated(MembershipAddMarker.class) final MembershipRequest membershipRequest) {
		
		final MembershipAddResponse membershipResponse = membershipService.addMembership(userId, membershipRequest.getMembershipType(), membershipRequest.getPoint());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(membershipResponse);
		
	}
	
	@GetMapping("/api/v1/memberships")
	public ResponseEntity<List<MembershipDetailResponse>> getMembershipList(
		@RequestHeader(MembershipConstants.USER_ID_HEADER) final String userId ) {
		return ResponseEntity.ok(membershipService.getMembershipList(userId));
	}
	
	@GetMapping("/api/v1/memberships/{id}")
	public ResponseEntity<MembershipDetailResponse> getMembership(
		@RequestHeader(MembershipConstants.USER_ID_HEADER) final String userId,
		@PathVariable final Long id) {
		return ResponseEntity.ok(membershipService.getMembership(id, userId));
	}
	
	@DeleteMapping("/api/v1/memberships/{id}")
	public ResponseEntity<Void> removeMembership(
		@RequestHeader(MembershipConstants.USER_ID_HEADER) final String userId,
		@PathVariable final Long id
		) {
		membershipService.removeMembership(id, userId);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/api/v1/memberships/{id}/accumulate")
	public ResponseEntity<Void> accumulateMembershipPoint(
		@RequestHeader(MembershipConstants.USER_ID_HEADER) final String userId,
		@PathVariable final Long id,
		@RequestBody @Validated(MembershipAccumulateMarker.class) final MembershipRequest membershipRequest
	) {
		membershipService.accumulateMembershipPoint(id, userId, membershipRequest.getPoint());
		return ResponseEntity.noContent().build();
	}
} 
