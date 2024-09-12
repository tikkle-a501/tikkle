"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";

interface MenuListProps {
  href: string;
  icon: string;
  children: React.ReactNode;
}

const MenuList: React.FC<MenuListProps> = ({ href, icon, children }) => {
  const pathname = usePathname();
  const isActive = pathname.includes(href);

  return (
    <Link
      href={`/mypage/${href}`}
      className={` ${isActive ? "font-semibold" : ""}`}
    >
      <div className="flex h-48 items-center gap-20 p-10 text-20 text-warmGray900">
        <span className="material-symbols-outlined">{icon}</span>
        <div>{children}</div>
      </div>
    </Link>
  );
};

export default MenuList;
