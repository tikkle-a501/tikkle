"use client";

import { useState, useEffect } from "react";
import { useFetchTrade } from "@/hooks";
import Chips from "@/components/chips/Chips";
import SearchInput from "@/components/input/SearchInput";
import HistoryList from "@/components/list/HistoryList";
import { TradeGetResponses } from "@/types";

export default function Trade() {
  const { data, isLoading, error } = useFetchTrade();
  const [filter, setFilter] = useState("전체");
  const [filteredTrade, setFilteredTrade] = useState<TradeGetResponses[]>([]);
  const [activeIndex, setActiveIndex] = useState<number | null>(null);

  const trade: TradeGetResponses[] = Array.isArray(data) ? data : [];

  useEffect(() => {
    if (filter === "전체") {
      setFilteredTrade(trade);
    } else {
      const filtered = trade.filter((item) => item.status === filter);
      setFilteredTrade(filtered);
    }
  }, [filter, data]);

  const handleButtonClick = (index: number) => {
    setActiveIndex((prevIndex) => (prevIndex === index ? null : index));
  };

  return (
    <div className="relative flex w-full flex-col gap-10">
      <span className="py-6 text-28 font-bold leading-34 text-teal-900">
        나의 거래 내역
      </span>

      <div className="flex flex-row justify-between pt-24">
        <div className="flex items-center gap-10 px-10">
          <span className="text-18 font-600 leading-23 text-teal-900">
            +0시간
          </span>
          <span className="text-18 font-600 leading-23 text-teal-900">
            -0시간
          </span>
        </div>
        <div className="flex items-center gap-10 px-10">
          <span className="text-18 leading-23 text-teal-900">
            나의 보유 시간
          </span>
          <span className="text-34 font-700 leading-41 text-teal-500">0</span>
          <span className="text-34 leading-41 text-teal-900">시간</span>
        </div>
      </div>
      <div className="flex items-center justify-between py-10">
        <div className="flex gap-10">
          <Chips
            size="l"
            variant="primary"
            design={filter === "전체" ? "fill" : "outline"}
            onClick={() => setFilter("전체")}
          >
            전체
          </Chips>
          <Chips
            size="l"
            variant="primary"
            design={filter === "진행전" ? "fill" : "outline"}
            onClick={() => setFilter("진행전")}
          >
            진행전
          </Chips>
          <Chips
            size="l"
            variant="primary"
            design={filter === "진행중" ? "fill" : "outline"}
            onClick={() => setFilter("진행중")}
          >
            진행중
          </Chips>
          <Chips
            size="l"
            variant="primary"
            design={filter === "완료됨" ? "fill" : "outline"}
            onClick={() => setFilter("완료됨")}
          >
            완료됨
          </Chips>
        </div>
        <SearchInput
          placeholder="검색어를 입력하세요."
          leftIcon={true}
          width="23.25rem"
        />
      </div>
      <div className="scrollbar-custom flex h-[41rem] flex-col overflow-y-auto border-t border-t-warmGray200 py-10">
        {filteredTrade.length > 0 ? (
          filteredTrade.map((item, index) => (
            <HistoryList
              key={index}
              profileImg={"/profile.png"}
              nickname={item.member.name}
              status={item.status}
              title={item.title}
              appointmentTime={item.createdAt}
              boardId={item.member.id}
              time={item.time}
            />
          ))
        ) : (
          <span className="p-[30px] text-18 text-warmGray300">
            거래 내역이 없습니다.
          </span>
        )}
      </div>
    </div>
  );
}
