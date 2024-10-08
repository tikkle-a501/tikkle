"use client";

import Button from "@/components/button/Button";
import Chart from "@/components/chart/Chart";
import {
  useFetchRecentRate,
  useCreateRate,
  useFetchAccount,
  useCreateExchange,
} from "@/hooks";
import Loading from "@/components/loading/Loading";
import { useState } from "react";

export default function Exchange() {
  const {
    mutate: createRateMutation,
    isPending: isCreating,
    error: createError,
  } = useCreateRate();

  const {
    mutate: createExchangeMutation,
    isPending: isExchanging,
    error: exchangeError,
  } = useCreateExchange();

  const [timeToConvertToTikkle, setTimeToConvertToTikkle] = useState(0); // 시간 -> 티끌
  const [tikkleToConvertToTime, setTikkleToConvertToTime] = useState(0); // 티끌 -> 시간

  const {
    data: latestRate,
    isPending,
    error: fetchError,
    refetch: refetchRate,
  } = useFetchRecentRate();
  const {
    data: accountData,
    isPending: isAccountPending,
    error: fetchAccountError,
    refetch: refetchAccount,
  } = useFetchAccount();

  const handleCreateRate = () => {
    createRateMutation(); // 테스트용 환율 생성 요청
  };

  const handleExchange = (exchangeType: "TTOR" | "RTOT") => {
    if (!latestRate || !latestRate.id) {
      console.error("환율 정보가 없습니다.");
      return;
    }

    const confirmationMessage =
      exchangeType === "TTOR"
        ? `${timeToConvertToTikkle}시간으로 ${latestRate.timeToRank * timeToConvertToTikkle}티끌 구매하시겠습니까?`
        : `${tikkleToConvertToTime * latestRate.timeToRank}티끌로 ${tikkleToConvertToTime}시간 구매하시겠습니까?`;

    if (!window.confirm(confirmationMessage)) {
      return;
    }

    const exchangeData = {
      rateId: latestRate?.id,
      timeToRank: latestRate?.timeToRank,
      quantity:
        exchangeType === "TTOR" ? timeToConvertToTikkle : tikkleToConvertToTime,
      exchangeType,
    };

    createExchangeMutation(exchangeData, {
      onSuccess: () => {
        alert("환전이 완료되었습니다.");
        refetchAccount(); // 계정 정보 새로고침
        refetchRate(); // 최신 환율 새로고침
      },
      onError: (error) => {
        alert(error);
        console.error("Error during exchange:", error);
      },
    });
  };

  const maxExchangeableTimeFromPoints =
    latestRate && accountData
      ? Math.floor(accountData.rankingPoint / latestRate.timeToRank)
      : 0;

  const maxExchangeableTime = accountData ? accountData.timeQnt : 0;

  const getExchangeText = (exchangeType: "TTOR" | "RTOT") => {
    if (!latestRate) {
      return exchangeType === "TTOR" ? `티끌 구매` : `시간 구매`;
    }

    if (exchangeType === "TTOR") {
      const tikkleValue = timeToConvertToTikkle * latestRate.timeToRank;
      return `${tikkleValue} 티끌 구매`;
    }

    const timeValue = Math.floor(tikkleToConvertToTime / latestRate.timeToRank);
    return `${timeValue} 시간 구매`;
  };

  return (
    <>
      {/* title: 환전 */}
      <div className="text-40 font-bold text-teal900">환전</div>

      <div className="flex flex-col gap-10 px-40 py-20">
        {/* 보유 재화 */}
        <div className="flex items-start justify-between gap-10 self-stretch px-12">
          {/* 현재 환율 */}
          <div>
            <div className="flex items-center gap-10">
              <div className="text-20 text-teal900">현재 환율: </div>
              <div className="text-34 text-teal900">1시간 = </div>
              <div className="text-34 font-bold text-teal500">
                {isPending ? (
                  <Loading />
                ) : fetchError ? (
                  <div className="text-red-500">
                    Error: {fetchError.message}
                  </div>
                ) : latestRate ? (
                  `${latestRate.timeToRank}`
                ) : (
                  "데이터 없음"
                )}
              </div>
              <div className="text-34 text-teal900">티끌</div>
            </div>
            <div className="text-end text-warmGray500">
              {latestRate?.createdAt} 기준
            </div>
          </div>

          {/* account 정보 */}
          <div className="flex">
            <div className="flex items-center justify-center gap-10 px-10">
              <div className="text-teal900">나의 보유 시간</div>
              <div className="text-34 font-bold text-teal500">
                {isAccountPending ? (
                  <Loading />
                ) : fetchAccountError ? (
                  <div className="text-red-500">
                    Error: {fetchAccountError.message}
                  </div>
                ) : (
                  accountData?.timeQnt
                )}
              </div>
              <div className="text-34 text-teal900">시간</div>
            </div>
            <div className="flex items-center justify-center gap-10 px-10">
              <div className="text-teal900">나의 보유 티끌</div>
              <div className="text-34 font-bold text-teal500">
                {isAccountPending ? (
                  <Loading />
                ) : fetchAccountError ? (
                  <div className="text-red-500">
                    Error: {fetchAccountError.message}
                  </div>
                ) : (
                  accountData?.rankingPoint
                )}
              </div>
              <div className="text-34 text-teal900">티끌</div>
            </div>
          </div>
        </div>

        {/* 환전 인풋 */}
        <div className="flex gap-10 p-10">
          {/* 시간 -> 티끌 */}
          <div className="flex flex-1 flex-col gap-10 rounded-12 border border-warmGray200 px-40 py-[30px]">
            <div className="flex items-center justify-between self-stretch pb-10">
              <div className="flex items-end font-semibold text-teal900">
                시간을
                <span className="text-24 font-bold">&nbsp;티끌로 바꾸기</span>
              </div>
              <Button
                size="l"
                variant="primary"
                design="fill"
                main={getExchangeText("TTOR")}
                onClick={() => handleExchange("TTOR")}
                disabled={
                  isExchanging ||
                  timeToConvertToTikkle === 0 ||
                  timeToConvertToTikkle === 0
                }
              >
                <span className="material-symbols-outlined">bubble_chart</span>
              </Button>
            </div>

            <div className="flex flex-1 items-center justify-center gap-10">
              <div className="flex flex-1 flex-row items-end justify-end rounded-12 bg-warmGray200 px-20 py-14">
                <input
                  className="max-w-[174px] flex-grow bg-warmGray200 text-right text-34 focus:outline-none"
                  type="number"
                  placeholder="0"
                  value={timeToConvertToTikkle || ""}
                  onChange={(e) =>
                    setTimeToConvertToTikkle(
                      Math.min(Number(e.target.value), maxExchangeableTime),
                    )
                  }
                />
                <div className="ml-4 whitespace-nowrap">시간</div>
              </div>
              <div>=</div>
              <div className="flex flex-1 flex-row items-end justify-end rounded-12 bg-warmGray200 px-20 py-14">
                <input
                  className="max-w-[174px] flex-grow bg-warmGray200 text-right text-34 focus:outline-none"
                  type="number"
                  value={
                    latestRate
                      ? timeToConvertToTikkle * latestRate.timeToRank
                      : 0
                  }
                  disabled
                />
                <div className="ml-4 whitespace-nowrap">티끌</div>
              </div>
            </div>

            <div className="text-sm text-warmGray500">
              최대 {accountData?.timeQnt || 0}시간까지 환전 가능
            </div>
          </div>

          {/* 티끌 -> 시간 */}

          <div className="flex flex-1 flex-col gap-10 rounded-12 border border-warmGray200 px-40 py-[30px]">
            <div className="flex items-center justify-between self-stretch pb-10">
              <div className="flex items-end font-semibold text-teal900">
                티끌을
                <span className="text-24 font-bold">&nbsp;시간으로 바꾸기</span>
              </div>
              <Button
                size="l"
                variant="primary"
                design="fill"
                main={`${tikkleToConvertToTime} 시간 구매`}
                onClick={() => handleExchange("RTOT")}
                disabled={
                  isExchanging ||
                  tikkleToConvertToTime === 0 ||
                  tikkleToConvertToTime === 0
                }
              >
                <span className="material-symbols-outlined">access_time</span>
              </Button>
            </div>

            <div className="flex flex-1 items-center justify-center gap-10">
              <div className="flex flex-1 flex-row items-end justify-end rounded-12 bg-warmGray200 px-20 py-14">
                <input
                  className="max-w-[174px] flex-grow bg-warmGray200 text-right text-34 focus:outline-none"
                  type="number"
                  placeholder="0"
                  value={tikkleToConvertToTime || ""}
                  onChange={(e) =>
                    setTikkleToConvertToTime(
                      Math.min(
                        Number(e.target.value),
                        maxExchangeableTimeFromPoints,
                      ),
                    )
                  }
                />
                <div className="ml-4 whitespace-nowrap">시간</div>
              </div>
              <div>=</div>
              <div className="flex flex-1 flex-row items-end justify-end rounded-12 bg-warmGray200 px-20 py-14">
                <input
                  className="max-w-[174px] flex-grow bg-warmGray200 text-right text-34 focus:outline-none"
                  type="number"
                  value={
                    latestRate
                      ? tikkleToConvertToTime * latestRate.timeToRank
                      : 0
                  }
                  disabled
                />
                <div className="ml-4 whitespace-nowrap">티끌</div>
              </div>
            </div>

            <div className="text-sm text-warmGray500">
              최대 {maxExchangeableTimeFromPoints}시간까지 환전 가능
            </div>
          </div>
        </div>

        {/* 환율 그래프 */}
        <div className="flex flex-1 flex-col items-center justify-center gap-10 rounded-10 border border-warmGray200 p-28">
          <Chart />
        </div>
      </div>
    </>
  );
}
