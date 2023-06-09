package com.spring.tdd.membership.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.spring.tdd.membership.entity.Membership;
import com.spring.tdd.membership.vo.MembershipType;

@DataJpaTest
public class MembershipRepositoryTest {

	@Autowired
	private MembershipRepository membershipRepository;
	

	@Test
	public void 멤버쉽등록() {
		//given
		final Membership membership = Membership.builder()
										.userId("userId")
										.membershipType(MembershipType.NAVER)
										.point(10000)
										.build();
		
		// when
		final Membership result = membershipRepository.save(membership);
		
		// then
		assertThat(result.getId()).isNotNull();
		assertThat(result.getUserId()).isEqualTo("userId");
		assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);
		assertThat(result.getPoint()).isEqualTo(10000);
	}
	
	@Test
	public void 멤버쉽이존재하는지테스트() {
		// given
		final Membership membership = Membership.builder()
				.userId("userId")
				.membershipType(MembershipType.NAVER)
				.point(10000)
				.build();
		
		// when
		membershipRepository.save(membership);
		final Membership findResult = membershipRepository.findByUserIdAndMembershipType("userId", MembershipType.NAVER);
		
		// then
		assertThat(findResult).isNotNull();
		assertThat(findResult.getId()).isNotNull();
		assertThat(findResult.getUserId()).isEqualTo("userId");
		assertThat(findResult.getMembershipType()).isEqualTo(MembershipType.NAVER);
		assertThat(findResult.getPoint()).isEqualTo(10000);
	}
	
	@Test
	public void 멤버쉽조회_사이즈가0() {
		// given
		
		// when
		List<Membership> result = membershipRepository.findAllByUserId("userId");
		
		// then 
		assertThat(result.size()).isEqualTo(0);
	}
	
	@Test
	public void 멤버쉽조회_사이즈가2() {
		// given
		final Membership naverMembership = Membership.builder()
				.userId("userId")
				.membershipType(MembershipType.NAVER)
				.point(10000)
				.build();
		
		final Membership kakaoMembership = Membership.builder()
				.userId("userId")
				.membershipType(MembershipType.NAVER)
				.point(10000)
				.build();
		
		membershipRepository.save(naverMembership);
		membershipRepository.save(kakaoMembership);
		
		// when
		List<Membership> result = membershipRepository.findAllByUserId("userId");
		
		// then
		assertThat(result.size()).isEqualTo(2);
	}
}

