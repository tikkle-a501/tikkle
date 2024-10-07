"use client";
import { useEffect, useState } from "react";
import BoardCard from "@/components/card/BoardCard";
import { Swiper, SwiperSlide } from "swiper/react";
import { Autoplay, Scrollbar } from "swiper/modules";
import "swiper/css";
import "swiper/css/scrollbar";
import "./custom-swiper.css";
import Chart from "@/components/chart/Chart";
import RankList from "@/components/list/RankList";
import { useFetchMypageMember } from "@/hooks";
import { useMypageStore } from "@/store/mypageStore"; // zustand 스토어 임포트
import { useFetchBoardList } from "@/hooks/board";

export default function Home() {
  const member = useMypageStore((state) => state.member); // zustand에서 현재 member 상태 가져오기
  const setMember = useMypageStore((state) => state.setMember); // zustand에서 setMember 가져오기
  const [fetchData, setFetchData] = useState(false); // API 호출 여부를 제어하기 위한 로컬 상태

  useEffect(() => {
    if (!member) {
      setFetchData(true); // member가 없으면 데이터를 받아오도록 설정
    }
  }, [member]);

  const { data: memberData, isLoading, error } = useFetchMypageMember(); // 인자 없이 호출

  useEffect(() => {
    if (memberData && !member && fetchData) {
      setMember(memberData); // member 상태가 없을 때만 zustand에 저장
      setFetchData(false); // 데이터를 받아온 후 다시 API 호출을 막기 위해 설정
    }
  }, [memberData, member, setMember, fetchData]);

  console.log("zustand member state:", member);

  const rankData = Array.from({ length: 10 }, (_, i) => {
    const rankNumber = i + 1;
    let rank: "first" | "second" | "third" | "others";
    const size: "s" = "s";

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
    };
  });

  const { data: boardList } = useFetchBoardList();
  return (
    <div className="flex w-full flex-col items-start gap-10">
      <div className="w-full text-40 font-bold text-teal900">SSAFY의 티끌</div>
      {/* 공고 슬라이드 */}
      <div className="w-full">
        <Swiper
          spaceBetween={50}
          slidesPerView={4}
          grabCursor={true}
          modules={[Autoplay, Scrollbar]} // Autoplay 모듈 추가
          loop={true}
          autoplay={{
            delay: 0, // 딜레이 없이
            disableOnInteraction: false,
          }}
          speed={10000} // 슬라이드 전환 속도를 느리게 설정
          scrollbar={{ draggable: true, hide: true }} // 드래그 가능한 스크롤바 설정
        >
          {boardList?.map((card, index) => (
            <SwiperSlide key={index}>
              <BoardCard
                boardId={card.boardId}
                title={card.title}
                status={card.status}
                writer={card.nickname}
                createdAt={card.createdAt}
                time={card.time}
                content={card.content}
              />
            </SwiperSlide>
          ))}
        </Swiper>
      </div>
      {/* 랭킹, 환율, 마이페이지가 들어갈 하단 div */}
      <div className="flex h-[544px] w-full items-start gap-28 pt-16">
        {/* 랭킹 */}
        <div className="h-full w-1/5">
          <div className="px-16 text-24 font-700 text-teal900">랭킹</div>
          <div className="flex flex-col items-start justify-center gap-10 px-56 py-28">
            {rankData.map((data) => (
              <RankList
                key={data.rankNumber}
                size={data.size}
                rank={data.rank}
                rankNumber={data.rankNumber}
                name={data.name}
                tikkle={data.tikkle}
                count={data.count}
              />
            ))}
          </div>
        </div>
        {/* 환율 */}
        <div className="h-full w-2/5">
          <div className="px-16 text-24 font-700 text-teal900">환율</div>
          <Chart />
        </div>
        {/* 마이페이지 */}
        <div className="h-full w-2/5">
          <div className="h-1/2 px-16 text-24 font-700 text-teal900">
            나의 타임 & 티끌
          </div>
          <div className="h-1/2 px-16 text-24 font-700 text-teal900">
            내가 맡은 일
          </div>
        </div>
      </div>
    </div>
  );
}
