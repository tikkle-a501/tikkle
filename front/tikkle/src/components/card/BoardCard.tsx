"use client";
import Link from "next/link";
import Badge from "../badge/Badge";

interface BoardCardProps {
  boardId: string;
  title: string;
  status: string;
  createdAt: string;
  time: number;
  content: string;
}

const BoardCard: React.FC<BoardCardProps> = ({
  boardId,
  title,
  status,
  createdAt,
  time,
  content,
}) => {
  const getBadgeColor = (status: string) => {
    switch (status) {
      case "POSTED":
        return "red";
      case "ACTIVE":
        return "yellow";
      case "DONE":
        return "gray";
      default:
        return "red";
    }
  };

  return (
    <Link href={`/board/${boardId}`} passHref className="hover:scale-110">
      <div className="flex h-[219px] w-[307px] flex-col items-start gap-10 rounded-12 bg-warmGray100 p-28">
        <div className="flex w-full items-center justify-between">
          <div className="truncate text-20 font-semibold text-teal600">
            {title}
          </div>
          <Badge size="l" color={getBadgeColor(status)}>
            {status}
          </Badge>
        </div>
        <div className="flex w-full items-center justify-between text-12">
          <div className="text-warmGray400">{createdAt}</div>
        </div>
        <div className="flex items-center justify-end gap-10 self-stretch text-13 text-warmGray700">
          {time} 시간
        </div>
        <div className="self-stretch overflow-hidden text-ellipsis text-15 text-warmGray900 [-webkit-box-orient:vertical] [-webkit-line-clamp:3] [display:-webkit-box]">
          {content}
        </div>
      </div>
    </Link>
  );
};

export default BoardCard;
