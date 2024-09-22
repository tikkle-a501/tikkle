"use client";

import { useState } from "react";
import SearchInput from "@/components/input/SearchInput";
import RankList from "@/components/list/RankList";

export default function Ranking() {
  const rankData = Array.from({ length: 40 }, (_, i) => {
    const rankNumber = i + 1;
    let rank: "first" | "second" | "third" | "others";
    const size: "m" = "m";

    if (rankNumber === 1) {
      rank = "first";
    } else if (rankNumber === 2) {
      rank = "second";
    } else if (rankNumber === 3) {
      rank = "third";
    } else {
      rank = "others";
    }

    return {
      size,
      rank,
      rankNumber,
      name: `User ${rankNumber}`,
      tikkle: Math.floor(Math.random() * 1000),
      count: Math.floor(Math.random() * 500),
      success: Math.floor(Math.random() * 300),
    };
  });

  // 페이지 당 20개의 데이터를 보여주기 위해 상태 관리
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 20;
  const totalPages = Math.ceil(rankData.length / itemsPerPage);

  // 현재 페이지에 해당하는 데이터 슬라이스
  const currentData = rankData.slice(
    (currentPage - 1) * itemsPerPage,
    currentPage * itemsPerPage,
  );

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
      <div className="flex flex-col gap-12">
        <div className="flex h-[63px] items-center justify-between rounded-t-12 bg-teal500 px-[64px] py-12 text-18 font-semibold text-white">
          <div className="flex-start flex gap-16">
            <div>순위</div>
            <div>이름</div>
          </div>
          <div className="flex-start flex gap-64 px-10">
            <div>tikkle</div>
            <div>count</div>
            <div>success</div>
          </div>
        </div>
        {currentData.map((data) => (
          <RankList
            key={data.rankNumber}
            size={data.size}
            rank={data.rank}
            rankNumber={data.rankNumber}
            name={data.name}
            tikkle={data.tikkle}
            count={data.count}
            success={data.success}
          />
        ))}
      </div>
      {/* 페이지네이션 버튼 */}
      <div className="mt-8 flex justify-center">
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
    </>
  );
}
