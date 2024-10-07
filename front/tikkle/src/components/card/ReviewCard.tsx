// "use client";

// import React, { useState } from "react";
// import ReviewBadge from "../badge/review/ReviewBadge";
// import Button from "../button/Button";
// import { useCreateReview } from "@/hooks";
// import { ReviewCreateRequest } from "@/types";

// const ReviewCard: React.FC = () => {
//   const [selectedBadge, setSelectedBadge] = useState<string | null>(null);
//   const [reviewContent, setReviewContent] = useState<string>("");

//   const {
//     mutate: createReviewMutate,
//     isSuccess,
//     isError,
//   } = useCreateReview(type);

//   const handleBadgeClick = (type: string, content: string) => {
//     setSelectedBadge(type);
//     setReviewContent(content);
//   };

//   const handleSubmit = () => {
//     if (!selectedBadge || !reviewContent) {
//       alert("리뷰 타입과 내용을 모두 선택하세요.");
//       return;
//     }

//     // 리뷰 작성 요청 전송
//     const reviewData: ReviewCreateRequest = {
//       type: selectedBadge,
//       content: reviewContent,
//     };

//     createReviewMutate(reviewData);
//   };

//   return (
//     <div className="flex flex-col gap-[10px] rounded-bl-[12px] rounded-br-[12px] rounded-tl-[12px] border bg-white p-[20px] text-teal900 shadow-s">
//       <h2 className="mb-4 text-lg font-bold">거래는 어땠나요?</h2>
//       <div className="flex flex-col gap-[10px] pb-[10px]">
//         <ReviewBadge type="time" onClick={handleBadgeClick} />
//         <ReviewBadge type="accuracy" onClick={handleBadgeClick} />
//         <ReviewBadge type="workload" onClick={handleBadgeClick} />
//         <ReviewBadge type="kindness" onClick={handleBadgeClick} />
//         <ReviewBadge type="fastReply" onClick={handleBadgeClick} />
//       </div>
//       {isSuccess && <p>리뷰 작성이 완료되었습니다.</p>} {/* 성공 상태 */}
//       {isError && <p>리뷰 작성에 실패했습니다.</p>} {/* 에러 상태 */}
//       <Button
//         size="s"
//         variant="primary"
//         design="fill"
//         main="리뷰 보내기"
//         onClick={handleSubmit} // 리뷰 보내기 버튼 클릭 시 요청 전송
//       />
//     </div>
//   );
// };

// export default ReviewCard;
