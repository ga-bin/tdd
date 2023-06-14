package com.spring.tdd.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.spring.tdd.membership.service.RatePointService;

@ExtendWith(MockitoExtension.class)
public class RatePointServiceTest {

	@InjectMocks
	private RatePointService ratePointService;
	
	@Test
	public void _10000원의적립은100원() {
		// given
		final int price = 10000;
		
		// when
		final int result = ratePointService.calculateAmount(price);
		
		// then
		assertThat(result).isEqualTo(100);
	}
	
	@Test
	public void _30000만원의적립은300원() {
		// given
		final int price = 30000;
		
		// when
		final int result = ratePointService.calculateAmount(price);
		
		// then
		assertThat(result).isEqualTo(300);
	}
}
