package com.spring.tdd.membership.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.spring.tdd.membership.service.MembershipService;
import com.spring.tdd.membership.vo.MembershipAddResponse;
import com.spring.tdd.membership.vo.MembershipDetailResponse;
import com.spring.tdd.membership.vo.MembershipRequest;

import jakarta.validation.Valid;
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
		 @RequestBody @Valid final MembershipRequest membershipRequest) {
		
		final MembershipAddResponse membershipResponse = membershipService.addMembership(userId, membershipRequest.getMembershipType(), membershipRequest.getPoint());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(membershipResponse);
		
	}
	
	@GetMapping("/api/v1/memberships")
	public ResponseEntity<List<MembershipDetailResponse>> getMembershipList(
		@RequestHeader(MembershipConstants.USER_ID_HEADER) final String userId ) {
		return ResponseEntity.ok(membershipService.getMembershipList(userId));
	}
}
