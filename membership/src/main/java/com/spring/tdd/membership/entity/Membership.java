package com.spring.tdd.membership.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.spring.tdd.membership.vo.MembershipType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Membership {

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private MembershipType membershipType;
	
	@Column(nullable = false)
	private String userId;
	
	@Column(nullable = false)
	@ColumnDefault("0")
	private Integer point;
	
	@CreationTimestamp
	@Column(nullable = false, length = 20, updatable = false)
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(length = 20)
	private LocalDateTime updatedAt;
	
}
