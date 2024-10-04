"use client";

import { useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import SearchInput from "@/components/input/SearchInput";
import RankList from "@/components/list/RankList";
import { useFetchRank, useSearchRank } from "@/hooks";
import Loading from "@/components/loading/Loading";

export default function Ranking() {
  // 페이지네이션
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 20;

  // URL의 쿼리 파라미터에서 검색어를 추출
  const searchParams = useSearchParams();
  const keyword = searchParams.get("keyword") || ""; // 검색어 추출

  // useSearchRank 훅 사용
  const {
    data: searchData,
    isLoading: isSearchLoading,
    isError: isSearchError,
  } = useSearchRank(keyword);

  // 기본 랭킹 데이터를 가져오는 훅 (검색어가 없을 때 사용)
  const { data: rankData, isLoading, isError } = useFetchRank();

  // 검색어가 있으면 검색 결과를 사용, 없으면 기본 랭킹 데이터 사용
  const isSearchActive = !!keyword;
  const displayedData = isSearchActive
    ? searchData?.rankList
    : rankData?.rankList;

  const totalPages = Math.ceil((displayedData?.length || 0) / itemsPerPage);
  const currentData =
    displayedData?.slice(
      (currentPage - 1) * itemsPerPage,
      currentPage * itemsPerPage,
    ) || [];

  // 키 입력 핸들러
  const router = useRouter();
  const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === "Enter") {
      const searchKeyword = (event.target as HTMLInputElement).value;
      router.push(`/ranking?keyword=${searchKeyword}`);
    }
  };

  // 로딩 중인 경우
  if (isLoading || isSearchLoading) {
    return (
      <>
        <div className="text-40 font-bold text-teal900">랭킹</div>
        <Loading />
      </>
    );
  }

  // 에러가 발생한 경우
  if (isError || isSearchError || !displayedData) {
    return <div>Error loading rank data</div>;
  }

  const getRankLabel = (order: number) => {
    switch (order) {
      case 1:
        return "first";
      case 2:
        return "second";
      case 3:
        return "third";
      default:
        return "others";
    }
  };

  return (
    <>
      <div className="text-40 font-bold text-teal900">랭킹</div>
      <div className="flex flex-col items-end justify-center">
        <SearchInput
          width="410px"
          placeholder="사용자명을 입력하세요."
          leftIcon
          handleKeyDown={handleKeyDown} // handleKeyDown을 props로 전달
        />
      </div>
      <div className="relative">
        {isSearchActive && searchData?.rankList?.length === 0 ? (
          <div className="text-center">"{keyword}"의 검색 결과가 없습니다.</div>
        ) : (
          <div className="flex flex-col gap-12">
            <div className="flex h-[63px] items-center justify-between rounded-t-12 bg-teal500 px-[64px] py-12 text-18 font-semibold text-white">
              <div className="flex-start flex gap-16">
                <div>순위</div>
                <div>이름</div>
              </div>
              <div className="flex-start flex gap-64 px-10">
                <div>티끌</div>
                <div>거래횟수</div>
              </div>
            </div>
            {currentData.map((data) => (
              <RankList
                key={data.order}
                size="m"
                rank={getRankLabel(data.order)}
                rankNumber={data.order}
                name={data.nickname}
                tikkle={data.rankingPoint}
                count={data.tradeCount}
              />
            ))}
          </div>
        )}
        {/* 나의 랭킹 및 페이지네이션 (검색어가 없을 때만 나의 랭킹 표시) */}
        {!isSearchActive && (
          <div className="sticky bottom-0 z-10">
            <div className="pt-10 font-semibold text-teal500">나의 랭킹</div>
            <div className="items-center justify-between rounded-8 border-2 border-teal500 bg-warmGray50 p-2">
              {rankData?.myRank ? (
                <RankList
                  size="m"
                  rank={getRankLabel(rankData.myRank.order)}
                  rankNumber={rankData.myRank.order}
                  name={rankData.myRank.nickname}
                  tikkle={rankData.myRank.rankingPoint}
                  count={rankData.myRank.tradeCount}
                />
              ) : (
                <div className="px-[64px] py-12">로그인 정보가 없습니다.</div>
              )}
            </div>
          </div>
        )}
        <div className="flex justify-center bg-warmGray50 py-8">
          <button
            onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
            disabled={currentPage === 1}
            className={`mx-2 px-4 py-2 ${
              currentPage === 1 ? "text-gray-400" : "text-black"
            }`}
          >
            이전
          </button>
          <span className="px-4 py-2">{`${currentPage} / ${totalPages}`}</span>
          <button
            onClick={() =>
              setCurrentPage((prev) => Math.min(prev + 1, totalPages))
            }
            disabled={currentPage === totalPages}
            className={`mx-2 px-4 py-2 ${
              currentPage === totalPages ? "text-gray-400" : "text-black"
            }`}
          >
            다음
          </button>
        </div>
      </div>
    </>
  );
}
