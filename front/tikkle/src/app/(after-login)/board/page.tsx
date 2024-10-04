"use client";
import { SetStateAction, useState } from "react";
import Button from "@/components/button/Button";
import BoardCard from "@/components/card/BoardCard";
import SearchInput from "@/components/input/SearchInput";
import { useRouter } from "next/navigation";
import { useFetchBoardList } from "@/hooks/board/index";

export default function BoardPage() {
  const [currentPage, setCurrentPage] = useState(1); // 현재 페이지 상태
  const cardsPerPage = 8; // 한 페이지에 8개의 카드(두 줄)
  const router = useRouter();

  // useFetchBoardList에서 가져온 데이터를 명시적으로 BoardListResponses로 처리
  const { data, isLoading, error } = useFetchBoardList();

  // data가 BoardListResponses일 경우만 접근. 없으면 빈 배열로 처리.
  const boardList = data || [];
  console.log(boardList);
  // 현재 페이지에서 보여줄 카드들 계산
  const indexOfLastCard = currentPage * cardsPerPage;
  const indexOfFirstCard = indexOfLastCard - cardsPerPage;
  const currentCards = boardList.slice(indexOfFirstCard, indexOfLastCard);

  // 페이지 변경 핸들러
  const paginate = (pageNumber: SetStateAction<number>) =>
    setCurrentPage(pageNumber);

  // 총 페이지 수 계산
  const totalPages = Math.ceil(boardList.length / cardsPerPage);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: 게시글을 불러오는 중 문제가 발생했습니다.</div>;
  }

  return (
    <div className="bg- flex flex-shrink-0 flex-col items-start gap-10">
      <div className="text-40 font-bold text-teal900">SSAFY의 티끌</div>
      {/* 헤더 */}
      <div className="flex w-full items-center justify-between self-stretch px-10">
        <select name="category" id="category">
          <option value="업무">업무</option>
          <option value="비업무">비업무</option>
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
      {/* 게시글 목록 */}
      <div className="grid w-full grid-cols-1 gap-32 pt-16 sm:grid-cols-2 lg:grid-cols-4">
        {currentCards.map((card, index) => (
          <BoardCard
            key={index}
            boardId={card.boardId}
            title={card.title}
            status={card.status}
            writer={card.nickname}
            createdAt={card.createdAt}
            time={card.time}
            content={card.content}
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
