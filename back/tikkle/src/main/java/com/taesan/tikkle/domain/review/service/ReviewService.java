package com.taesan.tikkle.domain.review.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taesan.tikkle.domain.appointment.entity.Appointment;
import com.taesan.tikkle.domain.appointment.repository.AppointmentRepository;
import com.taesan.tikkle.domain.member.entity.Member;
import com.taesan.tikkle.domain.member.repository.MemberRepository;
import com.taesan.tikkle.domain.review.dto.request.CreateReviewRequest;
import com.taesan.tikkle.domain.review.entity.Review;
import com.taesan.tikkle.domain.review.repository.ReviewRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
	private final ReviewRepository reviewRepository;
	private final AppointmentRepository appointmentRepository;
	private final MemberRepository memberRepository;

	public UUID createReview(CreateReviewRequest request) {
		Appointment appointment = appointmentRepository.findById(request.getAppointmentId()).orElseThrow(
			EntityNotFoundException::new);
		Member performer = appointment.getRoom().getPerformer();
		Member writer = appointment.getRoom().getWriter();
		// TODO : refresh token
		UUID senderId = UUID.randomUUID();
		Member receiver = (senderId.equals(performer.getId()) ? writer : performer);
		Member sender = memberRepository.findById(senderId).orElseThrow(EntityNotFoundException::new);
		Review review = new Review(sender,receiver,request.getType());
		reviewRepository.save(review);
		return review.getId();
	}
}
