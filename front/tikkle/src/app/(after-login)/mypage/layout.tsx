"use client";

import { useEffect, useState } from "react";
import InfoBox from "@/components/box/InfoBox";
import InfoBoxLoading from "@/components/loading/InfoBoxLoading";
import { useMypageStore } from "@/store/mypageStore";
import { useFetchMypageMember } from "@/hooks"; // API 훅 임포트

export default function MypageLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const [isMounted, setIsMounted] = useState(false);

  const member = useMypageStore((state) => state.member);
  const setMember = useMypageStore((state) => state.setMember);

  const { data: memberData } = useFetchMypageMember();

  useEffect(() => {
    setIsMounted(true);

    if (!member && memberData) {
      setMember(memberData);
    }
  }, [member, memberData, setMember]);

  console.log(member);

  return (
    <>
      <div className="text-40 font-bold text-teal900">마이페이지</div>
      <div className="flex gap-56">
        {isMounted ? (
          <InfoBox
            profileImg={`data:image/png;base64,${member?.image}`}
            name={member?.name ?? "이름 없음"}
            email={member?.email ?? "알 수 없음"}
            rate={0}
          />
        ) : (
          <InfoBoxLoading />
        )}

        <div className="flex w-full justify-between gap-10">{children}</div>
      </div>
    </>
  );
}
