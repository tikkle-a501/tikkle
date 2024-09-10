"use client";

import Link from "next/link";
import Image from "next/image";
import { useState } from "react";
import NavigationItem from "./NavigationItem";

const Navigation = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false); // 임시 로그인 상태관리

  return (
    <nav className="sticky top-0 flex h-[85px] px-40 justify-between items-center bg-warmGray50 border-b border-warmGray300">
      {/* 로고 영역 */}
      <Link href="/">
        <Image src="/logo.png" alt="Logo" width={92} height={28} />
      </Link>

      {/* 메뉴 영역 */}
      <div className="flex p-10 gap-36">
        <NavigationItem href="/board">게시판</NavigationItem>
        <NavigationItem href="/ranking">랭킹</NavigationItem>
        <NavigationItem href="/exchange">환전</NavigationItem>
        <NavigationItem href="/chat">채팅</NavigationItem>
      </div>

      {/* 프로필 영역 */}
      <div className="flex p-10 gap-36">
        {isLoggedIn ? (
          <div className="flex items-center">
            <Link href="/mypage">
              <Image
                src="/profile.png" // 추후 수정 필요
                alt="Profile"
                width={41}
                height={41}
                className="rounded-round"
              />
            </Link>
          </div>
        ) : (
          <NavigationItem href="/login">login</NavigationItem>
        )}
      </div>
    </nav>
  );
};

export default Navigation;
