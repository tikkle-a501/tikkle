package com.taesan.tikkle.domain.review.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taesan.tikkle.domain.appointment.entity.Appointment;
import com.taesan.tikkle.domain.appointment.repository.AppointmentRepository;
import com.taesan.tikkle.domain.member.entity.Member;
import com.taesan.tikkle.domain.member.repository.MemberRepository;
import com.taesan.tikkle.domain.review.dto.request.CreateReviewRequest;
import com.taesan.tikkle.domain.review.dto.response.ReviewListResponse;
import com.taesan.tikkle.domain.review.entity.Review;
import com.taesan.tikkle.domain.review.repository.ReviewRepository;
import com.taesan.tikkle.global.errors.ErrorCode;
import com.taesan.tikkle.global.exceptions.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
	private final ReviewRepository reviewRepository;
	private final AppointmentRepository appointmentRepository;
	private final MemberRepository memberRepository;

	public UUID createReview(CreateReviewRequest request, UUID senderId) {
		Appointment appointment = appointmentRepository.findById(request.getAppointmentId()).orElseThrow(
			() -> new CustomException(ErrorCode.APPOINTMENT_NOT_FOUND));
		Member performer = appointment.getRoom().getPerformer();
		Member writer = appointment.getRoom().getWriter();
		Member receiver = (senderId.equals(performer.getId()) ? writer : performer);
		Member sender = memberRepository.findById(senderId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
		Review review = new Review(sender, receiver, request.getType());
		reviewRepository.save(review);
		return review.getId();
	}

	public List<ReviewListResponse> getReviews(UUID memberId) {
		Member receiver = memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		List<Review> reviews = reviewRepository.findByReceiver(receiver);

		// reviews를 List<ReviewListResponse>로 변환
		return reviews.stream()
			.map(review -> new ReviewListResponse(
				review.getId(),
				review.getType()
			))
			.collect(Collectors.toList());
	}

}