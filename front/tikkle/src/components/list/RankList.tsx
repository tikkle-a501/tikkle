import RankBadge from "@/components/badge/rank/RankBadge";

interface RankListProps {
  size: "s" | "m";
  rank: "first" | "second" | "third" | "others";
  rankNumber: number;
  name: string;
  tikkle: number;
  count: number;
  success: number;
}

const RankList: React.FC<RankListProps> = ({
  size,
  rank,
  rankNumber,
  name,
  tikkle,
  count,
  success,
}) => {
  if (size === "s") {
    return (
      <div className="flex flex-row items-center gap-10 p-10">
        <RankBadge rank={rank}>{rankNumber}</RankBadge>
        <span className="text-14 font-600 leading-19">{name}</span>
        <span className="text-14leading-19">{tikkle}</span>
      </div>
    );
  } else if (size === "m") {
    return (
      <div className="flex flex-row items-center justify-between p-10">
        <div className="flex flex-row items-center justify-center gap-20">
          <RankBadge rank={rank}>{rankNumber}</RankBadge>
          <span className="text-17 font-600 leading-22">{name}</span>
        </div>
        <div className="flex flex-row justify-between gap-64">
          <span className="text-18 leading-23">{tikkle}</span>
          <span className="text-18 leading-23">{count}</span>
          <span className="text-18 leading-23">{success}</span>
        </div>
      </div>
    );
  }
};

export default RankList;
