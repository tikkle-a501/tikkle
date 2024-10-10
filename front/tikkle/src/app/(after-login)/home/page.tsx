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
import TodoList from "@/components/list/TodoList";
import { useFetchMypageMember } from "@/hooks";
import { useMypageStore } from "@/store/mypageStore"; // zustand 스토어 임포트
import { useFetchBoardList } from "@/hooks/board";
import { useFetchRank } from "@/hooks"; // 랭킹 데이터를 가져오는 훅
import { useFetchTodoAppointment } from "@/hooks";
import { TodoAppointmentResponse } from "@/types";
import Loading from "@/components/loading/Loading"; // 로딩 컴포넌트 추가

export default function Home() {
  // zustand로부터 member 관련 상태를 가져오는 부분은 유지
  const member = useMypageStore((state) => state.member);
  const setMember = useMypageStore((state) => state.setMember);
  const [fetchData, setFetchData] = useState(false);

  useEffect(() => {
    if (!member) {
      setFetchData(true);
    }
  }, [member]);

  const { data: memberData } = useFetchMypageMember();

  useEffect(() => {
    if (memberData && !member && fetchData) {
      setMember(memberData);
      setFetchData(false);
    }
  }, [memberData, member, setMember, fetchData]);

  console.log("zustand member state:", member);

  const {
    data: rankData,
    isLoading: isRankLoading,
    isError: isRankError,
  } = useFetchRank();

  const { data: boardList } = useFetchBoardList();

  // 할일 데이터를 가져오는 훅 사용
  const { data: todoAppointments, isError: isTodoError } =
    useFetchTodoAppointment();

  if (isRankError || !rankData || isTodoError || !todoAppointments)
    return <div>Error loading data</div>;

  return (
    <div className="flex w-full flex-col items-start gap-10">
      <div className="w-full text-40 font-bold text-teal900">SSAFY의 티끌</div>
      {/* 공고 슬라이드 */}
      <div className="w-full">
        <Swiper
          spaceBetween={50}
          slidesPerView={4}
          grabCursor={true}
          modules={[Autoplay, Scrollbar]}
          loop={true}
          autoplay={{
            delay: 0,
            disableOnInteraction: false,
          }}
          speed={10000}
          scrollbar={{ draggable: true, hide: true }}
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
      <div className="flex h-[544px] w-full items-start gap-28 pt-16">
        {/* 랭킹 */}
        <div className="h-full w-1/5">
          <div className="text-24 font-700 text-teal900">랭킹</div>
          <div className="scrollbar-hidden flex h-[465px] flex-col items-start gap-10 overflow-y-auto rounded-[12px] border border-warmGray200 px-56 py-28">
            {rankData?.rankList?.slice(0, 10).length > 0 ? (
              rankData.rankList
                .slice(0, 10)
                .map((data) => (
                  <RankList
                    key={data.order}
                    size="s"
                    rank={
                      data.order === 1
                        ? "first"
                        : data.order === 2
                          ? "second"
                          : data.order === 3
                            ? "third"
                            : "others"
                    }
                    rankNumber={data.order}
                    name={data.nickname}
                    tikkle={data.rankingPoint}
                    count={data.tradeCount}
                  />
                ))
            ) : (
              <div>랭킹 데이터를 불러올 수 없습니다.</div>
            )}
          </div>
        </div>
        {/* 환율 */}
        <div className="h-full w-2/5">
          <div className="px-16 text-24 font-700 text-teal900">환율</div>
          <Chart />
        </div>
        {/* 마이페이지 */}
        <div className="h-full w-2/5">
          <div className="h-1/3 px-16 text-24 font-700 text-teal900">
            나의 타임 & 티끌
            <div className="flex justify-center gap-[20px] rounded-[10px] border border-warmGray200 px-[56px] py-[28px]">
              <div className="flex items-center gap-[10px]">
                <span className="text-[48px] text-teal500">0</span>
                <span className="text-[24px]">시간</span>
              </div>
              <div className="flex items-center gap-[10px]">
                <span className="text-[48px] text-teal500">0</span>
                <span className="text-[24px]">티끌</span>
              </div>
            </div>
          </div>
          {/* 내가 맡은 일 */}
          <div className="h-2/3 px-16 text-24 font-700 text-teal900">
            내가 맡은 일
            <div className="flex h-[300px] flex-col items-center gap-[10px] rounded-[10px] border border-warmGray200 p-[20px]">
              {todoAppointments?.data?.length > 0 ? (
                todoAppointments.data.map(
                  (appointment: TodoAppointmentResponse) => (
                    <TodoList
                      key={appointment.appointmendId}
                      status={appointment.status}
                      appointmentTime={appointment.startTime}
                      title={appointment.title}
                      nickname={appointment.partner}
                      chatId={appointment.chatroomId}
                    />
                  ),
                )
              ) : (
                <div>할일이 없습니다.</div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
