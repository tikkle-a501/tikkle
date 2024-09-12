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
        </span>
      ); // 꽉 찬 별 구현 어떻게?
    } else {
      stars.push(
        <span key={i} className="material-symbols-outlined">
          star
        </span>
      ); // 빈 별
    }
  }
  return stars;
};

const InfoBox: React.FC<InfoBoxProps> = ({ profileImg, name, email, rate }) => {
  return (
    <div className="flex w-[298px] h-[793px] flex-col items-center px-20 py-40 gap-10 text-20 border border-warmGray200 rounded-12">
      <div className="flex-shrink-0">
        <Image
          src={profileImg}
          alt={`${name} profile`}
          width={150}
          height={150}
          className="rounded-round"
        />
      </div>

      <div className="flex flex-col py-10 gap-10 justify-center text-center">
        <div className="font-semibold">{name}</div>
        <div className="text-warmGray500">{email}</div>
      </div>
      <div className="flex p-10 justify-center items-center gap-10">
        {renderStars(rate)}
      </div>

      <div className="flex w-full border-t border-warmGray-200"></div>

      <div className="flex w-full flex-col p-14 gap-14 items-start">
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

      <div className="flex flex-col justify-end items-center flex-[1_0_0] text-15 text-warmGray300 self-stretch">
        <button className="cursor-pointer">로그아웃</button>
      </div>
    </div>
  );
};

export default InfoBox;
