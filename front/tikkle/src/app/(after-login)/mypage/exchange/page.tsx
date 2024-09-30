import ExchangeList from "@/components/list/ExchangeList";

export default function Exchange() {
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
          <ExchangeList mode="toTikkle" time={0} tikkle={0} />
          <ExchangeList mode="toTikkle" time={110} tikkle={10} />
        </div>
        <div className="scrollbar-custom flex w-full flex-col overflow-y-auto rounded-10 border border-warmGray200 p-20">
          <div className="flex items-center space-x-6 py-14">
            <span className="text-24 leading-30">티끌을</span>
            <span className="text-28 font-700 leading-34 text-teal500">
              시간으로 바꾼 내역
            </span>
          </div>
          <ExchangeList mode="toTime" time={0} tikkle={0} />
          <ExchangeList mode="toTime" time={110} tikkle={10} />
        </div>
      </div>
    </div>
  );
}
