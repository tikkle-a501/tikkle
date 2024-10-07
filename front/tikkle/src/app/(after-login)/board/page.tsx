"use client";
import { SetStateAction, useState, KeyboardEvent } from "react";
import Button from "@/components/button/Button";
import BoardCard from "@/components/card/BoardCard";
import SearchInput from "@/components/input/SearchInput";
import { useRouter } from "next/navigation";
import { useFetchBoardByKeyword, useFetchBoardList } from "@/hooks/board/index";

export default function BoardPage() {
  const [currentPage, setCurrentPage] = useState(1); // 현재 페이지 상태
  const cardsPerPage = 8; // 한 페이지에 8개의 카드(두 줄)
  const router = useRouter();

  // 검색어 상태 관리
  const [searchKeyword, setSearchKeyword] = useState(""); // 검색어 상태
  const [submittedKeyword, setSubmittedKeyword] = useState(""); // 제출된 검색어 상태

  console.log(searchKeyword);

  // useFetchBoardList에서 가져온 데이터를 명시적으로 BoardListResponses로 처리
  const { data, isLoading, error } = useFetchBoardList();

  // 검색어에 맞는 게시글 목록 불러오기
  const {
    data: searchData,
    isLoading: isSearchLoading,
    error: searchError,
  } = useFetchBoardByKeyword(submittedKeyword); // 제출된 검색어에 맞춰 API 호출

  // 검색어가 있을 경우 검색 결과, 없으면 전체 리스트
  const boardList = submittedKeyword ? searchData : data || [];

  // 현재 페이지에서 보여줄 카드들 계산
  const indexOfLastCard = currentPage * cardsPerPage;
  const indexOfFirstCard = indexOfLastCard - cardsPerPage;
  const currentCards = boardList?.slice(indexOfFirstCard, indexOfLastCard);

  // 페이지 변경 핸들러
  const paginate = (pageNumber: SetStateAction<number>) =>
    setCurrentPage(pageNumber);

  // 총 페이지 수 계산
  const totalPages = Math.ceil((boardList?.length || 0) / cardsPerPage);

  // 검색창에서 엔터키를 감지하여 submit 처리
  const handleSearchSubmit = (e: KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      setSubmittedKeyword(searchKeyword); // 제출된 검색어로 API 호출
      setCurrentPage(1); // 검색 후 첫 페이지로 이동
    }
  };

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
          onSearch={(value: string) => setSearchKeyword(value)} // 검색어 상태 업데이트
          handleKeyDown={(e: React.KeyboardEvent<HTMLInputElement>) =>
            handleSearchSubmit(e)
          } // 엔터 키 이벤트 핸들러
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
        {currentCards?.map((card, index) => (
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
