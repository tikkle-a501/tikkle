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
    <div className="inline-flex h-[61px] w-full items-center px-10 text-18">
      <div className="flex w-[172px] items-center gap-10 p-10">
        <Image
          src={profileImg}
          alt={`${nickname} profile`}
          width={36}
          height={36}
          className="rounded-round"
        />
        <div>{nickname}</div>
      </div>
      <div className="flex w-[119px] items-center justify-center">
        <Badge size="l" color="red">
          {/* 추후 뱃지 컬러를 동적으로 받는 로직 작성 필요 */}
          {status}
        </Badge>
      </div>
      <Link href={`/board/${boardId}`} passHref>
        <div className="flex w-[210px] cursor-pointer items-center justify-center truncate text-warmGray600">
          {title}
        </div>
      </Link>
      <div className="flex w-[255px] items-center justify-center text-warmGray600">
        {appointmentTime}
      </div>
      <div className="flex w-[82px] items-center justify-center text-warmGray600">
        {time}시간
      </div>
      <div className="flex w-[152px] items-center justify-center">
        <Button
          size="m"
          variant="primary"
          design="outline"
          main={`${buttonText}`}
        ></Button>
      </div>
    </div>
  );
};

export default HistoryList;
