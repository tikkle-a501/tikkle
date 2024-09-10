import Badge from "../badge/Badge";

interface BoardCardProps {
  title: string;
  status: string;
  writer: string;
  createdAt: string;
  time: string;
  context: string;
}

const BoardCard: React.FC<BoardCardProps> = ({
  title,
  status,
  writer,
  createdAt,
  time,
  context,
}) => {
  return (
    <div className="flex w-[307px] h-[219px] p-28 flex-col items-start gap-10 rounded-12 bg-warmGray100">
      <div className="flex justify-between items-center w-full">
        <div className="text-teal600 text-20 font-semibold truncate">
          {title}
        </div>
        <Badge size="l" color="teal">
          {status}
        </Badge>
      </div>
      <div className="flex justify-between items-center text-12 w-full">
        <div className="text-warmGray600 font-semibold">{writer}</div>
        <div className="text-warmGray400">{createdAt}</div>
      </div>
      <div className="flex justify-end items-center gap-10 self-stretch text-13 text-warmGray700">
        {time} 시간
      </div>
      <div
        className="self-stretch text-warmGray900 text-15 overflow-hidden text-ellipsis 
             [display:-webkit-box] [-webkit-line-clamp:3] [-webkit-box-orient:vertical]"
      >
        {context}
      </div>
    </div>
  );
};

export default BoardCard;
