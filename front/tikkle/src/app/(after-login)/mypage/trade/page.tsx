"use client";

import Chips from "@/components/chips/Chips";
import SearchInput from "@/components/input/SearchInput";
import { useFetchTrade } from "@/hooks";

export default function Trade() {
  const { data, isLoading, error } = useFetchTrade();
  const account = data || [];
  console.log(data);

  return (
    <div className="flex w-full flex-col gap-10">
      <span className="py-6 text-28 font-bold leading-34 text-teal-900">
        나의 거래 내역
      </span>
      <div className="flex flex-row justify-between">
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
          <Chips size="l" variant="primary" design="fill">
            전체
          </Chips>
          <Chips size="l" variant="primary" design="outline">
            진행전
          </Chips>
          <Chips size="l" variant="primary" design="outline">
            진행중
          </Chips>
          <Chips size="l" variant="primary" design="outline">
            완료
          </Chips>
        </div>
        <SearchInput
          placeholder="검색어를 입력하세요."
          leftIcon={true}
          width="23.25rem"
        />
      </div>
      <div className="scrollbar-custom flex h-[41rem] w-full flex-col overflow-y-auto border-t border-t-warmGray200 py-10">
        {/* {historyData.map((item, index) => (
          <HistoryList
            key={index}
            profileImg={item.profileImg}
            nickname={item.nickname}
            status={item.status}
            title={item.title}
            appointmentTime={item.appointmentTime}
            boardId={item.boardId}
            time={item.time}
            buttonText={item.buttonText}
          />
        ))} */}
      </div>
    </div>
  );
}
