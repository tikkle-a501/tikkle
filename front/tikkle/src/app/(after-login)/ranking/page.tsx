"use client";

import { useState } from "react";
import SearchInput from "@/components/input/SearchInput";
import RankList from "@/components/list/RankList";
import { useFetchRank } from "@/hooks";
import Loading from "@/components/loading/Loading";

export default function Ranking() {
  const { data: rankData, isLoading, isError } = useFetchRank();

  // 페이지 당 20개의 데이터를 보여주기 위해 상태 관리
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 20;

  // 데이터가 로딩 중일 때 보여줄 UI
  if (isLoading) {
    return (
      <>
        <div className="text-40 font-bold text-teal900">랭킹</div>
        <Loading />
      </>
    );
  }

  // 에러가 발생했을 때 보여줄 UI
  if (isError || !rankData) {
    return <div>Error loading rank data</div>;
  }

  const totalPages = Math.ceil(rankData.rankList.length / itemsPerPage);

  // 현재 페이지에 해당하는 데이터 슬라이스
  const currentData = rankData.rankList.slice(
    (currentPage - 1) * itemsPerPage,
    currentPage * itemsPerPage,
  );

  // order에 따른 rank 설정 로직
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
        />
      </div>
      <div className="relative">
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
              rank={getRankLabel(data.order)} // order에 따라 rank 결정
              rankNumber={data.order}
              name={data.nickname}
              tikkle={data.rankingPoint}
              count={data.tradeCount}
            />
          ))}
        </div>
        {/* 고정될 나의 랭킹 및 페이지네이션 */}
        <div className="sticky bottom-0 z-10">
          {/* 나의 랭킹 */}
          <div className="pt-10 font-semibold text-teal500">나의 랭킹</div>
          <div className="items-center justify-between rounded-8 border-2 border-teal500 bg-warmGray50 p-2">
            {rankData.myRank ? (
              <RankList
                size="m"
                rank={getRankLabel(rankData.myRank.order)} // order에 따라 rank 결정
                rankNumber={rankData.myRank.order}
                name={rankData.myRank.nickname}
                tikkle={rankData.myRank.rankingPoint}
                count={rankData.myRank.tradeCount}
              />
            ) : (
              <div className="px-[64px] py-12">로그인 정보가 없습니다.</div>
            )}
          </div>

          {/* 페이지네이션 버튼 */}
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
      </div>
    </>
  );
}
