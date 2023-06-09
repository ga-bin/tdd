package com.spring.tdd.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.spring.tdd.membership.entity.Membership;
import com.spring.tdd.membership.error.MembershipErrorResult;
import com.spring.tdd.membership.error.MembershipException;
import com.spring.tdd.membership.repository.MembershipRepository;
import com.spring.tdd.membership.service.MembershipService;
import com.spring.tdd.membership.vo.MembershipAddResponse;
import com.spring.tdd.membership.vo.MembershipDetailResponse;
import com.spring.tdd.membership.vo.MembershipType;

@ExtendWith(MockitoExtension.class)
public class MembershipServiceTest {

	private final String userId = "userId";
	private final MembershipType membershipType = MembershipType.NAVER;
	private final Integer point = 10000;
	private final Long membershipId = -1L;
	
	@InjectMocks
	private MembershipService target;
	@Mock
	private MembershipRepository membershipRepository;
	
	@Test
	public void 멤버쉽등록실패_이미존재함() {
		// given
		doReturn(Membership.builder().build()).when(membershipRepository).findByUserIdAndMembershipType(userId, membershipType);
		
		// when
		final MembershipException result = assertThrows(MembershipException.class, () -> target.addMembership(userId, membershipType, point));
		
		// then
		assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
	}
	
	@Test
	public void 멤버쉽등록성공() {
		// given
		doReturn(null).when(membershipRepository).findByUserIdAndMembershipType(userId, membershipType);
		doReturn(membership()).when(membershipRepository).save(any(Membership.class));
		
		// when
		final MembershipAddResponse result = target.addMembership(userId, membershipType, point);
		
		// then
		assertThat(result.getId()).isNotNull();
		assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);
		
		// verify
		verify(membershipRepository, times(1)).findByUserIdAndMembershipType(userId, membershipType);
		verify(membershipRepository, times(1)).save(any(Membership.class));
	}
	
	@Test
	public void 멤버쉽목록조회() {
		// given
		doReturn(Arrays.asList(
			Membership.builder().build(),
			Membership.builder().build(),
			Membership.builder().build()
		)).when(membershipRepository).findAllByUserId(userId);	
		
		// when
		final List<MembershipDetailResponse> result = target.getMembershipList(userId);
		
		// then
		assertThat(result.size()).isEqualTo(3);
	}
	
	private Membership membership() {
		return Membership.builder()
				.id(-1L)
				.userId(userId)
				.point(point)
				.membershipType(membershipType.NAVER)
				.build();
	}
	
	@Test
	public void 멤버십상세조회실패_존재하지않음() {
		// given
		doReturn(Optional.empty()).when(membershipRepository).findById(membershipId);
		
		// when
		final MembershipException result = assertThrows(MembershipException.class, () -> target.getMembership(membershipId, userId));
		
		assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
	}
	
	@Test
	public void 멤버십상세조회실패_본인이아님() {
		// given
		doReturn(Optional.empty()).when(membershipRepository).findById(membershipId);
		
		// when
		final MembershipException result = assertThrows(MembershipException.class, () -> target.getMembership(membershipId, "notowner"));
		
		assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
	}
	
	
	@Test
	public void 멤버십상세조회성공() {
		// given
		doReturn(Optional.of(membership())).when(membershipRepository).findById(membershipId);
		
		// when
		final MembershipDetailResponse result = target.getMembership(membershipId, userId);
		
		// then
		assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);
		assertThat(result.getPoint()).isEqualTo(point);
	}
}
