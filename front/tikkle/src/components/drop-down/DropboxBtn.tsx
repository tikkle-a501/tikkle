"use client";

interface DropdownBtnProps {
  children: React.ReactNode;
  onClick?: () => void;
}

const DropboxBtn: React.FC<DropdownBtnProps> = ({ children }) => {
  return (
    <div className="text-normal flex w-[153px] cursor-pointer items-center gap-10 rounded-4 bg-white px-16 py-10 text-14 hover:bg-warmGray100">
      {children}
    </div>
  );
};

export default DropboxBtn;
