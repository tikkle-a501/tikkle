"use client";

import Link from "next/link";

interface DropdownBtnProps {
  children: React.ReactNode;
  href?: string; // 링크 경로를 받아오기 위한 prop 추가
  onClick?: () => void;
}

const DropboxBtn: React.FC<DropdownBtnProps> = ({
  children,
  href,
  onClick,
}) => {
  // href가 존재하는지 확인하여 Link로 감싸거나, 그렇지 않으면 div로 처리
  if (href) {
    return (
      <Link href={href} passHref legacyBehavior>
        <a
          onClick={onClick}
          className="text-normal flex w-[153px] cursor-pointer items-center gap-10 rounded-4 bg-white px-16 py-10 text-14 hover:bg-warmGray100"
        >
          {children}
        </a>
      </Link>
    );
  }

  // href가 없으면 단순 div로 처리
  return (
    <div
      onClick={onClick}
      className="text-normal flex w-[153px] cursor-pointer items-center gap-10 rounded-4 bg-white px-16 py-10 text-14 hover:bg-warmGray100"
    >
      {children}
    </div>
  );
};

export default DropboxBtn;
