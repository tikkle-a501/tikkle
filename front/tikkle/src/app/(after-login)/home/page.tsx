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
import { useMypageStore } from "@/store/mypageStore";
import { useFetchBoardList } from "@/hooks/board";
import {
  useFetchRank,
  useFetchAccount,
  useFetchTodoAppointment,
} from "@/hooks"; // useFetchTodoAppointment 추가
import Loading from "@/components/loading/Loading"; // 로딩 컴포넌트 추가

export default function Home() {
  const member = useMypageStore((state) => state.member); // zustand에서 현재 member 상태 가져오기
  const setMember = useMypageStore((state) => state.setMember); // zustand에서 setMember 가져오기
  const [fetchData, setFetchData] = useState(false); // API 호출 여부를 제어하기 위한 로컬 상태

  useEffect(() => {
    if (!member) {
      setFetchData(true); // member가 없으면 데이터를 받아오도록 설정
    }
  }, [member]);

  const { data: memberData } = useFetchMypageMember(); // 인자 없이 호출

  useEffect(() => {
    if (memberData && !member && fetchData) {
      setMember(memberData); // member 상태가 없을 때만 zustand에 저장
      setFetchData(false); // 데이터를 받아온 후 다시 API 호출을 막기 위해 설정
    }
  }, [memberData, member, setMember, fetchData]);

  console.log("zustand member state:", member);

  const {
    data: rankData,
    isLoading: isRankLoading,
    isError: isRankError,
  } = useFetchRank();

  const { data: boardList } = useFetchBoardList();

  const {
    data: accountData,
    isPending: isAccountPending,
    error: fetchAccountError,
    refetch: refetchAccount,
  } = useFetchAccount(); // 나의 시간 & 티끌

  // 내가 맡은 일을 가져오는 훅
  const {
    data: todoAppointments,
    isLoading: isTodoLoading,
    isError: isTodoError,
  } = useFetchTodoAppointment();

  console.log(todoAppointments);

  if (isRankLoading || isTodoLoading) return <Loading />; // 로딩 중일 때 로딩 컴포넌트 표시
  if (isRankError || isTodoError || !rankData || !todoAppointments)
    return <div>Error loading data</div>; // 에러 처리

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
          <div className="text-24 font-700 text-teal900">랭킹</div>
          <div className="scrollbar-hidden flex h-[465px] flex-col items-start gap-10 overflow-y-auto rounded-[12px] border border-warmGray200 px-56 py-28">
            {rankData?.rankList?.length > 0 ? (
              rankData.rankList.map((data) => (
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
              {isAccountPending ? (
                <Loading />
              ) : fetchAccountError ? (
                <div className="text-red-500">
                  Error: {fetchAccountError.message}
                </div>
              ) : (
                <>
                  <div className="flex items-center gap-[10px]">
                    <span className="text-[48px] text-teal500">
                      {accountData?.timeQnt}
                    </span>
                    <span className="text-[24px]">시간</span>
                  </div>
                  <div className="flex items-center gap-[10px]">
                    <span className="text-[48px] text-teal500">
                      {accountData?.rankingPoint}
                    </span>
                    <span className="text-[24px]">티끌</span>
                  </div>
                </>
              )}
            </div>
          </div>
          <div className="h-2/3 px-16 text-24 font-700 text-teal900">
            내가 맡은 일
            <div className="flex h-[290px] flex-col items-center gap-[10px] rounded-[10px] border border-warmGray200 p-[20px]">
              {todoAppointments?.data?.length > 0 ? (
                todoAppointments.data.map((appointment) => (
                  <TodoList
                    key={appointment.appointmentId}
                    status={appointment.status}
                    appointmentTime={appointment.startTime}
                    title={appointment.title}
                    nickname={appointment.partner}
                    chatId={appointment.chatroomId}
                  />
                ))
              ) : (
                <span className="font-400 text-warmGray300">
                  할일이 없습니다.
                </span>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
