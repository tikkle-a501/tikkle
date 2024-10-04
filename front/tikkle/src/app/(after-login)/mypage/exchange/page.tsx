"use client";

import ExchangeList from "@/components/list/ExchangeList";
import { useFetchExchange } from "@/hooks";
import { ExchangeGetResponse } from "@/types";

export default function Exchange() {
  const { data, isLoading, error } = useFetchExchange();
  const exchange: ExchangeGetResponse[] = Array.isArray(data) ? data : [];
  const toTikkleList = exchange.filter((item) => item.exchangeType === "TTOR");
  const toTimeList = exchange.filter((item) => item.exchangeType !== "TTOR");

  return (
    <div className="flex w-full flex-col gap-10">
      <span className="py-6 text-28 font-bold leading-34 text-teal900">
        나의 환전 내역
      </span>
      <div className="flex flex-row gap-10">
        <div className="scrollbar-custom flex h-[737px] w-full flex-col overflow-y-auto rounded-10 border border-warmGray200 p-20">
          <div className="flex items-center space-x-6 py-14">
            <span className="text-24 leading-30">시간을</span>
            <span className="text-28 font-700 leading-34 text-teal500">
              티끌로 바꾼 내역
            </span>
          </div>
          {isLoading ? (
            <span className="text-warmGray300">
              환전 내역을 불러오는 중입니다.
            </span>
          ) : toTikkleList.length > 0 ? (
            toTikkleList.map((item, index) => (
              <ExchangeList
                key={index}
                mode="toTikkle"
                time={item.time}
                tikkle={item.rankingPoint}
              />
            ))
          ) : (
            <span className="text-warmGray300">환전 내역이 없습니다.</span>
          )}
        </div>
        <div className="scrollbar-custom flex w-full flex-col overflow-y-auto rounded-10 border border-warmGray200 p-20">
          <div className="flex items-center space-x-6 py-14">
            <span className="text-24 leading-30">티끌을</span>
            <span className="text-28 font-700 leading-34 text-teal500">
              시간으로 바꾼 내역
            </span>
          </div>
          {isLoading ? (
            <span className="text-warmGray300">
              환전 내역을 불러오는 중입니다.
            </span>
          ) : toTimeList.length > 0 ? (
            toTimeList.map((item, index) => (
              <ExchangeList
                key={index}
                mode="toTime"
                time={item.time}
                tikkle={item.rankingPoint}
              />
            ))
          ) : (
            <span className="text-warmGray300">환전 내역이 없습니다.</span>
          )}
        </div>
      </div>
    </div>
  );
}
