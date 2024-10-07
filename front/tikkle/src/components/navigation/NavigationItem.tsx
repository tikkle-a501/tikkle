"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";

interface CustomLinkProps {
  href: string;
  children: React.ReactNode;
}

const NavigationItem: React.FC<CustomLinkProps> = ({ href, children }) => {
  const pathname = usePathname();
  const isActive = pathname.startsWith(href); // 경로가 특정 경로로 시작하는지 확인

  return (
    <Link href={href} className={`p-10 ${isActive ? "font-semibold" : ""}`}>
      {children}
    </Link>
  );
};

export default NavigationItem;
