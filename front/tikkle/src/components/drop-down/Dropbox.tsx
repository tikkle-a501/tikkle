"use client";
import DropboxBtn from "./DropboxBtn";

interface DropdownProps {
  items: { label: string; href?: string }[]; // 각 항목의 레이블과 이동할 경로를 포함
  onClick?: (item: string) => void; // 각 항목 클릭 시 호출될 함수
}

const Dropbox: React.FC<DropdownProps> = ({ items, onClick }) => {
  return (
    <div className="inline-flex flex-col gap-4 rounded-4 bg-white p-6 shadow-s">
      {items.map((item, index) => (
        <DropboxBtn
          key={index}
          href={item.href} // 각 항목의 링크 경로
          onClick={() => onClick?.(item.label)} // 클릭 시 항목 레이블을 onClick에 전달
        >
          {item.label}
        </DropboxBtn>
      ))}
    </div>
  );
};

export default Dropbox;
