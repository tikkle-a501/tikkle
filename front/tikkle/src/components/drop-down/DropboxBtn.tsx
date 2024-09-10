"use client";

interface DropdownBtnProps {
  children: React.ReactNode;
  onClick?: () => void;
}

const DropboxBtn: React.FC<DropdownBtnProps> = ({ children }) => {
  return (
    <div className="flex w-[153px] px-16 py-10 rounded-4 items-center gap-10 bg-white text-normal text-14 hover:bg-warmGray100 cursor-pointer">
      {children}
    </div>
  );
};

export default DropboxBtn;
