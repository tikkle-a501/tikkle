"use client";

import React, { useState } from "react";
import ReviewBadge from "../badge/review/ReviewBadge";
import Button from "../button/Button";
import { useCreateReview } from "@/hooks";
import { ReviewCreateRequest } from "@/types";

interface ReviewCardProps {
  chatroomId: string; // chatroomId를 prop으로 받음
}

const ReviewCard: React.FC<ReviewCardProps> = ({ chatroomId }) => {
  const [selectedBadge, setSelectedBadge] = useState<string | null>(null);

  const {
    mutate: createReviewMutate,
    isSuccess,
    isError,
    isPending,
  } = useCreateReview();

  const handleBadgeClick = (type: string, content: string) => {
    setSelectedBadge(type);
  };

  const handleSubmit = () => {
    if (!selectedBadge) {
      alert("리뷰 선택이 필요합니다.");
      return;
    }

    // 리뷰 작성 요청 전송
    const reviewData: ReviewCreateRequest = {
      chatroomId, // chatroomId를 함께 전송
      type: selectedBadge,
    };

    createReviewMutate(reviewData);
  };

  return (
    <div className="absolute right-[135px] flex flex-col gap-[10px] rounded-bl-[12px] rounded-br-[12px] rounded-tl-[12px] border bg-white p-[20px] text-teal900 shadow-s">
      <h2 className="mb-4 text-lg font-bold">거래는 어땠나요?</h2>
      <div className="flex flex-col gap-[10px] pb-[10px]">
        <ReviewBadge type="time" onClick={handleBadgeClick} />
        <ReviewBadge type="accuracy" onClick={handleBadgeClick} />
        <ReviewBadge type="workload" onClick={handleBadgeClick} />
        <ReviewBadge type="kindness" onClick={handleBadgeClick} />
        <ReviewBadge type="fastReply" onClick={handleBadgeClick} />
      </div>
      {isSuccess && <p>리뷰 작성이 완료되었습니다.</p>} {/* 성공 상태 */}
      {isError && <p>리뷰 작성에 실패했습니다.</p>} {/* 에러 상태 */}
      <Button
        size="s"
        variant="primary"
        design="fill"
        main="리뷰 보내기"
        onClick={handleSubmit} // 리뷰 보내기 버튼 클릭 시 요청 전송
        disabled={isPending} // 로딩 중 disabled 처리
      />
    </div>
  );
};

export default ReviewCard;
