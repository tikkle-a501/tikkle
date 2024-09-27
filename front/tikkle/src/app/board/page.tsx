"use client";
import { SetStateAction, useEffect, useState } from "react";
import Button from "@/components/button/Button";
import BoardCard from "@/components/card/BoardCard";
import SearchInput from "@/components/input/SearchInput";
import { useRouter } from "next/navigation";
import { useFetchBoardList } from "@/hooks/board/useFetchBoardList";

export default function Board() {
  const [currentPage, setCurrentPage] = useState(1); // 현재 페이지 상태
  const cardsPerPage = 8; // 한 페이지에 8개의 카드(두 줄)
  const router = useRouter();
  const testCards = [
    {
      title: "Announcement",
      status: "Active",
      writer: "John Doe",
      createdAt: "2024.09.10 10:00",
      time: "10",
      context:
        "This is an important announcement regarding the upcoming event. Please read carefully. This is an important announcement regarding the upcoming event. Please read carefully.",
    },
    {
      title: "Maintenance Update",
      status: "Scheduled",
      writer: "Jane Smith",
      createdAt: "2024.09.09 13:00",
      time: "2.5",
      context:
        "There will be a scheduled maintenance on our servers next week. Expect some downtime.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "Announcement",
      status: "Active",
      writer: "John Doe",
      createdAt: "2024.09.10 10:00",
      time: "10",
      context:
        "This is an important announcement regarding the upcoming event. Please read carefully. This is an important announcement regarding the upcoming event. Please read carefully.",
    },
    {
      title: "Maintenance Update",
      status: "Scheduled",
      writer: "Jane Smith",
      createdAt: "2024.09.09 13:00",
      time: "2.5",
      context:
        "There will be a scheduled maintenance on our servers next week. Expect some downtime.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
  ];

  // 현재 페이지에서 보여줄 카드들 계산
  const indexOfLastCard = currentPage * cardsPerPage;
  const indexOfFirstCard = indexOfLastCard - cardsPerPage;
  const currentCards = testCards.slice(indexOfFirstCard, indexOfLastCard);

  // 페이지 변경 핸들러
  const paginate = (pageNumber: SetStateAction<number>) =>
    setCurrentPage(pageNumber);

  // 총 페이지 수
  const totalPages = Math.ceil(testCards.length / cardsPerPage);

  const { data, isLoading, error } = useFetchBoardList();
  console.log(data);
  console.log(error);
  return (
    <div className="bg- flex flex-shrink-0 flex-col items-start gap-10">
      <div className="text-40 font-bold text-teal900">SSAFY의 티끌</div>
      {/* 헤더 */}
      <div className="flex w-full items-center justify-between self-stretch px-10">
        <select name="category" id="category">
          <option value="업무">업무</option>
          <option value="업무">비업무</option>
        </select>
        <SearchInput
          width="609px"
          placeholder="검색어를 입력하세요"
          leftIcon
          warningMessage="그건 아니지"
        />
        <Button
          size="m"
          variant="primary"
          design="fill"
          main="게시글 작성"
          onClick={() => router.push("/board/write")}
        />
      </div>
      {/* 공고 */}
      <div className="grid w-full grid-cols-1 gap-32 pt-16 sm:grid-cols-2 lg:grid-cols-4">
        {currentCards.map((card, index) => (
          <BoardCard
            key={index}
            boardId="1"
            title={card.title}
            status={card.status}
            writer={card.writer}
            createdAt={card.createdAt}
            time={card.time}
            context={card.context}
          />
        ))}
      </div>
      {/* 페이지네이션 */}
      <div className="flex w-full justify-center space-x-8 pt-10">
        {/* 이전 페이지 버튼 */}
        <Button
          onClick={() => paginate(currentPage - 1)}
          disabled={currentPage === 1}
          size="s"
          variant="primary"
          design="fill"
          main="이전"
        ></Button>

        {/* 페이지 숫자 표시 */}
        {Array.from({ length: totalPages }, (_, i) => i + 1).map(
          (pageNumber) => (
            <button
              key={pageNumber}
              onClick={() => paginate(pageNumber)}
              className={`rounded px-12 py-2 ${
                currentPage === pageNumber
                  ? "bg-teal-500 text-white"
                  : "bg-gray-200 text-gray-700"
              }`}
            >
              {pageNumber}
            </button>
          ),
        )}

        {/* 다음 페이지 버튼 */}
        <Button
          onClick={() => paginate(currentPage + 1)}
          disabled={currentPage === totalPages}
          size="s"
          variant="primary"
          design="fill"
          main="다음"
        ></Button>
      </div>
    </div>
  );
}
