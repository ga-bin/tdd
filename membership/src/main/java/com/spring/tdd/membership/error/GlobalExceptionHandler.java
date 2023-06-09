package com.spring.tdd.membership.error;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	// 유효성 검사 통과못한 경우
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			final MethodArgumentNotValidException ex, 
			final HttpHeaders headers, 
			final HttpStatusCode status, 
			final WebRequest request) {

		
		final List<String> errorList = ex.getBindingResult()
					.getAllErrors()
					.stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage)
					.collect(Collectors.toList());
		
		log.warn("Invalid DTO Parameter errors : {}", errorList);
		return this.makeErrorResponseEnity(errorList.toString());
	}

	private ResponseEntity<Object> makeErrorResponseEnity(final String errorDescription) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), errorDescription));
	}
	

	// membership exception이 발생한 경우
	@ExceptionHandler({MembershipException.class})
	public ResponseEntity<ErrorResponse> handleRestApiException(final MembershipException exception) {
		log.warn("MembershipException occur: ", exception);
		return this.makeErrorResponseEnity(exception.getErrorResult());
	}
	
	private ResponseEntity<ErrorResponse> makeErrorResponseEnity(MembershipErrorResult errorResult) {
		return ResponseEntity.status(errorResult.getHttpStatus())
				.body(new ErrorResponse(errorResult.name(), errorResult.getMessage()));
	}
	
	// 그외 일반적인 exception의 경우
	@ExceptionHandler({Exception.class})
	public ResponseEntity<ErrorResponse> handleException(final Exception exception) {
		log.warn("Exception occur :", exception);
		return this.makeErrorResponseEnity(MembershipErrorResult.UNKNOWN_EXCEPTION);
	}
	

	
	@Getter
	@RequiredArgsConstructor
	static class ErrorResponse {
		private final String code;
		private final String message;
	}
}
