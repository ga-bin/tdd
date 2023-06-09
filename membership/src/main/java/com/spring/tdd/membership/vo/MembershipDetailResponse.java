package com.spring.tdd.membership.vo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembershipDetailResponse {

	private Long id;
	private MembershipType membershipType;
	private String userId;
	private Integer point;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
