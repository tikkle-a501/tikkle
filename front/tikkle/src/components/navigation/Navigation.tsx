"use client";

import Link from "next/link";
import Image from "next/image";
import { useState, useEffect } from "react";
import NavigationItem from "./NavigationItem";
import { useMypageStore } from "@/store/mypageStore";
import { useFetchMypageMember } from "@/hooks";

const Navigation = () => {
  const member = useMypageStore((state) => state.member); // zustand에서 현재 member 상태 가져오기
  const setMember = useMypageStore((state) => state.setMember); // zustand에서 setMember 가져오기
  const [fetchData, setFetchData] = useState(false); // API 호출 여부를 제어하기 위한 로컬 상태

  useEffect(() => {
    if (!member) {
      setFetchData(true); // member가 없으면 데이터를 받아오도록 설정
    }
  }, [member]);

  const { data: memberData, isLoading, error } = useFetchMypageMember(); // 인자 없이 호출

  useEffect(() => {
    if (memberData && !member && fetchData) {
      setMember(memberData); // member 상태가 없을 때만 zustand에 저장
      setFetchData(false); // 데이터를 받아온 후 다시 API 호출을 막기 위해 설정
    }
  }, [memberData, member, setMember, fetchData]);

  return (
    <nav className="sticky top-0 z-50 flex h-[85px] items-center justify-between border-b border-warmGray300 bg-warmGray50 px-40">
      {/* 로고 영역 */}
      <Link href="/home">
        <Image src="/logo.png" alt="Logo" width={92} height={28} priority />
      </Link>

      {/* 메뉴 영역 */}
      <div className="flex gap-36 p-10">
        <NavigationItem href="/board">게시판</NavigationItem>
        <NavigationItem href="/ranking">랭킹</NavigationItem>
        <NavigationItem href="/exchange">환전</NavigationItem>
        <NavigationItem href="/chat">채팅</NavigationItem>
      </div>

      {/* 프로필 영역 */}
      <div className="flex gap-36 p-10">
        {memberData ? (
          <div className="flex items-center">
            <Link href="/mypage/activity">
              <Image
                src={
                  memberData.image
                    ? `data:image/png;base64,${memberData.image}`
                    : "/profile.png"
                }
                alt={memberData.name}
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
