package com.spring.tdd.membership.vo;

import com.spring.tdd.membership.validation.ValidationGroups.MembershipAccumulateMarker;
import com.spring.tdd.membership.validation.ValidationGroups.MembershipAddMarker;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class MembershipRequest {
	
	@NotNull(groups = {MembershipAddMarker.class, MembershipAccumulateMarker.class})
	@Min(value = 0, groups = {MembershipAddMarker.class, MembershipAccumulateMarker.class})
	private final Integer point;
	
	@NotNull(groups = {MembershipAddMarker.class})
	private final MembershipType membershipType;
}
