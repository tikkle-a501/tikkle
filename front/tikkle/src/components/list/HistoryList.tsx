import Link from "next/link";
import Badge from "../badge/Badge";
import Image from "next/image";
import Button from "../button/Button";

interface HistoryListProps {
  profileImg: string; // 프로필 이미지
  nickname: string; // 닉네임
  status: string; // 상태
  title: string; // 제목
  appointmentTime: string; // 약속 시간
  boardId: string; // 게시글 ID
  time: number; // 시간(재화)
  buttonText: string; // 버튼 내용
}

const HistoryList: React.FC<HistoryListProps> = ({
  profileImg,
  nickname,
  status,
  title,
  appointmentTime,
  time,
  boardId,
  buttonText,
}) => {
  return (
    <Link href={`/board/${boardId}`} passHref>
      <div className="inline-flex w-[1010px] h-[61px] justify-between items-center px-12 text-18 cursor-pointer">
        <div className="flex p-10 items-center gap-10">
          <Image
            src={profileImg}
            alt={`${nickname} profile`}
            width={36}
            height={36}
            className="rounded-round"
          />
          <div>{nickname}</div>
        </div>
        <Badge size="l" color="red">
          {/* 추후 뱃지 컬러를 동적으로 받는 로직 작성 필요 */}
          {status}
        </Badge>
        <div className="truncate">{title}</div>
        <div>{appointmentTime}</div>
        <div>{time}시간</div>
        <Button
          size="m"
          variant="primary"
          design="outline"
          main={`${buttonText}`}
        ></Button>
      </div>
    </Link>
  );
};

export default HistoryList;
