interface RankBadgeProps {
  rank: "first" | "second" | "third" | "others";
  children: React.ReactNode;
}

const RankBadge: React.FC<RankBadgeProps> = ({ rank, children }) => {
  const rankClasss = {
    first:
      "flex justify-center bg-yellow400 h-7 w-7 p-10 text-white text-14 font-600 leading-19 items-center rounded-round box-border",
    second:
      "flex justify-center bg-coolGray400 h-7 w-7 p-10 text-white text-14 font-600 leading-19 items-center rounded-round box-border",
    third:
      "flex justify-center bg-yellow700 h-7 w-7 p-10 text-white text-14 font-6`00 leading-19 items-center rounded-round box-border",
    others:
      "flex justify-center bg-baseWhite h-7 w-7 p-10 color-baseWhite text-14 font-600 leading-19 items-center rounded-round box-border",
  };

  return <div className={rankClasss[rank]}>{children}</div>;
};

export default RankBadge;
