import Image from "next/image";
import MenuList from "../list/MenuList";

interface InfoBoxProps {
  profileImg: string;
  name: string;
  email: string;
  rate: number;
}

// 별점을 렌더링하는 함수
const renderStars = (rate: number) => {
  const stars = [];
  for (let i = 1; i <= 5; i++) {
    if (i <= rate) {
      stars.push(
        <span key={i} className="material-symbols-outlined">
          star
        </span>,
      ); // 꽉 찬 별 구현 어떻게?
    } else {
      stars.push(
        <span key={i} className="material-symbols-outlined">
          star
        </span>,
      ); // 빈 별
    }
  }
  return stars;
};

const InfoBox: React.FC<InfoBoxProps> = ({ profileImg, name, email, rate }) => {
  return (
    <div className="flex h-[793px] w-[298px] flex-col items-center gap-10 rounded-12 border border-warmGray200 px-20 py-40 text-20">
      <div className="flex-shrink-0">
        <Image
          src={profileImg}
          alt={`${name} profile`}
          width={150}
          height={150}
          className="rounded-round"
        />
      </div>

      <div className="flex flex-col justify-center gap-10 py-10 text-center">
        <div className="font-semibold">{name}</div>
        <div className="text-warmGray500">{email}</div>
      </div>
      <div className="flex items-center justify-center gap-10 p-10">
        {renderStars(rate)}
      </div>

      <div className="border-warmGray-200 flex w-full border-t"></div>

      <div className="flex w-full flex-col items-start gap-14 p-14">
        <MenuList href="activity" icon="edit">
          나의 게시글
        </MenuList>
        <MenuList href="trade" icon="history_toggle_off">
          나의 거래 내역
        </MenuList>
        <MenuList href="exchange" icon="swap_horiz">
          나의 환전 내역
        </MenuList>
      </div>

      <div className="flex flex-[1_0_0] flex-col items-center justify-end self-stretch text-15 text-warmGray300">
        <button className="cursor-pointer">로그아웃</button>
      </div>
    </div>
  );
};

export default InfoBox;
