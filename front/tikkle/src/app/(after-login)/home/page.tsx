"use client";
import BoardCard from "@/components/card/BoardCard";
import { Swiper, SwiperSlide } from "swiper/react";
import { Autoplay, Scrollbar } from "swiper/modules";
import "swiper/css";
import "swiper/css/scrollbar";
import "./custom-swiper.css";
import Chart from "@/components/chart/Chart";
import RankList from "@/components/list/RankList";
export default function Home() {
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
      success: Math.floor(Math.random() * 300),
    };
  });
  const testCards = [
    {
      title: "Announcement",
      status: "Active",
      writer: "John Doe",
      createdAt: "2024.09.10 10:00",
      time: "10",
      context:
        "This is an important announcement regarding the upcoming event. Please read carefully.",
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
        "This is an important announcement regarding the upcoming event. Please read carefully.",
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
        "This is an important announcement regarding the upcoming event. Please read carefully.",
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
        "This is an important announcement regarding the upcoming event. Please read carefully.",
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
  ];

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
          {testCards.map((card, index) => (
            <SwiperSlide key={index}>
              <BoardCard
                boardId="1"
                title={card.title}
                status={card.status}
                writer={card.writer}
                createdAt={card.createdAt}
                time={card.time}
                context={card.context}
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
                success={data.success}
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
