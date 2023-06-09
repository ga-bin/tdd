package com.spring.tdd.membership.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.google.gson.Gson;
import com.spring.tdd.membership.controller.MembershipController.MembershipConstants;
import com.spring.tdd.membership.entity.Membership;
import com.spring.tdd.membership.error.GlobalExceptionHandler;
import com.spring.tdd.membership.error.MembershipErrorResult;
import com.spring.tdd.membership.error.MembershipException;
import com.spring.tdd.membership.service.MembershipService;
import com.spring.tdd.membership.vo.MembershipRequest;
import com.spring.tdd.membership.vo.MembershipAddResponse;
import com.spring.tdd.membership.vo.MembershipDetailResponse;
import com.spring.tdd.membership.vo.MembershipType;

@ExtendWith(MockitoExtension.class)
public class MembershipControllerTest {

	@InjectMocks
	private MembershipController target;
	@Mock
	private MembershipService membershipService;
	
	private MockMvc mockMvc;
	private Gson gson;
	
	@BeforeEach
	public void init() {
		gson = new Gson();
		mockMvc = MockMvcBuilders.standaloneSetup(target)
					.setControllerAdvice(new GlobalExceptionHandler())
					.build();
	}


	@Test
	public void 멤버쉽등록실패_사용자식별값이헤더에없음() throws Exception {
		// given
		final String url = "/api/v1/memberships";
		
		// when
		final ResultActions resultActions = mockMvc.perform(
				MockMvcRequestBuilders.post(url)
					.content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
					.contentType(MediaType.APPLICATION_JSON)
				);
		
		// then
		resultActions.andExpect(status().isBadRequest());
		
	}
	
	@ParameterizedTest
	@MethodSource("invalidMembershipAddParameter")
	public void 멤버쉽등록실패_잘못된_파라미터(final Integer point, final MembershipType membershipType) throws Exception {
	
		// given
		final String url = "/api/v1/memberships";
		
		// when
		final ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.post(url)
				.header(MembershipConstants.USER_ID_HEADER, "12345")
				.content(gson.toJson(membershipRequest(point, membershipType)))
				.contentType(MediaType.APPLICATION_JSON)
		);
		
		// then
		resultActions.andExpect(status().isBadRequest());
	}
	
	
	private static Stream<Arguments> invalidMembershipAddParameter() {
		return Stream.of(
			Arguments.of(null, MembershipType.NAVER),
			Arguments.of(-1, MembershipType.NAVER),
			Arguments.of(10000, null)
		);
	}
	
//	@Test
//	public void 멤버쉽등록실패_포인트가null() throws Exception {
//	
//		// given
//		final String url = "/api/v1/memberships";
//		
//		// when
//		final ResultActions resultActions = mockMvc.perform(
//			MockMvcRequestBuilders.post(url)
//				.header(MembershipConstants.USER_ID_HEADER, "12345")
//				.content(gson.toJson(membershipRequest(null, MembershipType.NAVER)))
//				.contentType(MediaType.APPLICATION_JSON)
//		);
//		
//		// then
//		resultActions.andExpect(status().isBadRequest());
//	}
//	
//	
//	@Test
//	public void 멤버쉽등록실패_포인트가음수() throws Exception {
//		// given
//		final String url = "/api/v1/memberships";
//		
//		// when
//		final ResultActions resultActions = mockMvc.perform(
//			MockMvcRequestBuilders.post(url)
//				.header(MembershipConstants.USER_ID_HEADER, "12345")
//				.content(gson.toJson(membershipRequest(-1, MembershipType.NAVER)))
//				.contentType(MediaType.APPLICATION_JSON)
//		);
//		
//		// then
//		resultActions.andExpect(status().isBadRequest());
//	}
//	
//	
//	@Test
//	public void 멤버쉽등록실패_멥버쉽종류가Null() throws Exception {
//		// given
//		final String url = "/api/v1/memberships";
//		
//		// when
//		final ResultActions resultActions = mockMvc.perform(
//				MockMvcRequestBuilders.post(url)
//					.header(MembershipConstants.USER_ID_HEADER, "12345")
//					.content(gson.toJson(membershipRequest(10000, null)))
//					.contentType(MediaType.APPLICATION_JSON)
//		);
//		
//		// then
//		resultActions.andExpect(status().isBadRequest());
//	}
	
	
	@Test
	public void 멤버쉽등록실패_MemberService에서에러Throw() throws Exception {
		// given
		final String url = "/api/v1/memberships";
		doThrow(new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER))
			.when(membershipService)
			.addMembership("12345", MembershipType.NAVER, 10000);
		
		// when
		final ResultActions resultActions = mockMvc.perform(
				MockMvcRequestBuilders.post(url)
					.header(MembershipConstants.USER_ID_HEADER, "12345")
					.content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
					.contentType(MediaType.APPLICATION_JSON)
		);
		
		// then
		resultActions.andExpect(status().isBadRequest());
	}
	
	@Test
	public void 멤버십등록성공() throws Exception {
		// given
		final String url = "/api/v1/memberships";
		final MembershipAddResponse membershipResponse = MembershipAddResponse.builder()
				.id(-1L)
				.membershipType(MembershipType.NAVER).build();
		
		doReturn(membershipResponse).when(membershipService).addMembership("12345", MembershipType.NAVER, 10000);
		
		// when
		final ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.post(url)
				.header(MembershipConstants.USER_ID_HEADER, "12345")
				.content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
				.contentType(MediaType.APPLICATION_JSON)
		);
		
		// then
		resultActions.andExpect(status().isCreated());
		
		final MembershipAddResponse response = gson.fromJson(resultActions.andReturn()
				.getResponse()
				.getContentAsString(StandardCharsets.UTF_8), MembershipAddResponse.class);
		
		assertThat(response.getMembershipType()).isEqualTo(MembershipType.NAVER);
		assertThat(response.getId()).isNotNull();
	}
	

	@Test
	public void 멤버십목록조회실패_사용자식별값이헤더에없음() throws Exception {
		// given
		final String url = "/api/v1/memberships";
		
		// when
		final ResultActions resultActions = mockMvc.perform(
				MockMvcRequestBuilders.get(url)
		);
		
		// then
		resultActions.andExpect(status().isBadRequest());
	}
	
	
	@Test
	public void 멤버십목록조회성공() throws Exception {
		// given
		final String url = "/api/v1/memberships";
		
		List<MembershipDetailResponse> list = new ArrayList<>();
		
		list.add(MembershipDetailResponse.builder().build());
		list.add(MembershipDetailResponse.builder().build());
		list.add(MembershipDetailResponse.builder().build());
	
		doReturn(list).when(membershipService).getMembershipList("12345");
		
		// when
		final ResultActions resultActions = mockMvc.perform(
				MockMvcRequestBuilders.get(url)
					.header(MembershipConstants.USER_ID_HEADER, "12345")
		);
		
		// then
		resultActions.andExpect(status().isOk());
				
	}
	
 	private MembershipRequest membershipRequest(final Integer point, final MembershipType membershipType) {
		return MembershipRequest.builder()
				.point(point)
				.membershipType(membershipType)
				.build();
		
	}
	
 	
	
}
