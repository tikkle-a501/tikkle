interface ExchangeListProps {
  time: number;
  tikkle: number;
  mode: "toTikkle" | "toTime";
}

const formatDate = (date: Date): string => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");

  return `${year}.${month}.${day} ${hours}:${minutes}`;
};

const ExchangeList: React.FC<ExchangeListProps> = ({ time, tikkle, mode }) => {
  const currentDate = new Date();

  return (
    <div className="flex w-full flex-row items-center justify-between p-10">
      <div className="flex items-center gap-10">
        {mode === "toTikkle" ? (
          <>
            <span className="text-20 font-600 leading-25">{time}</span>
            <span className="text-20 leading-25">시간</span>
            <span className="material-symbols-outlined text-warmGray200">
              arrow_forward
            </span>
            <span className="text-20 font-600 leading-25">{tikkle}</span>
            <span className="text-20 leading-25">티끌</span>
          </>
        ) : (
          <>
            <span className="text-20 font-600 leading-25">{tikkle}</span>
            <span className="text-20 leading-25">티끌</span>
            <span className="material-symbols-outlined text-warmGray200">
              arrow_forward
            </span>
            <span className="text-20 font-600 leading-25">{time}</span>
            <span className="text-20 leading-25">시간</span>
          </>
        )}
      </div>
      <span className="text-17 leading-22 text-warmGray400">
        {formatDate(currentDate)}
      </span>
    </div>
  );
};

export default ExchangeList;
