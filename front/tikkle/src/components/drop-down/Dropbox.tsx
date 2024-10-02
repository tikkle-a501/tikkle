"use client";
import DropboxBtn from "./DropboxBtn";

interface DropdownProps {
  items: string[]; // 드롭다운에 표시될 항목들
  onClick?: (item: string) => void; // 각 항목 클릭 시 호출될 함수
}

const Dropbox: React.FC<DropdownProps> = ({ items, onClick }) => {
  return (
    <div className="inline-flex flex-col gap-4 rounded-4 bg-white p-6 shadow-s">
      {items.map((item, index) => (
        <DropboxBtn
          key={index}
          onClick={() => onClick?.(item)} // 클릭 시 항목 값을 onClick에 전달
        >
          {item}
        </DropboxBtn>
      ))}
    </div>
  );
};

export default Dropbox;
