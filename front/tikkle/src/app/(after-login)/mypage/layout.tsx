"use client";

import { useEffect, useState } from "react";
import InfoBox from "@/components/box/InfoBox";
import InfoBoxLoading from "@/components/loading/InfoBoxLoading";
import { useMypageStore } from "@/store/mypageStore";

export default function MypageLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const [isMounted, setIsMounted] = useState(false);

  const member = useMypageStore((state) => state.member);
  const setMember = useMypageStore((state) => state.setMember);

  useEffect(() => {
    setIsMounted(true);

    if (!member) {
      setMember({
        id: "1",
        name: "Unknown",
        nickname: "anonymous",
        email: "unknown@example.com",
      });
    }
  }, [member, setMember]);

  return (
    <>
      <div className="text-40 font-bold text-teal900">마이페이지</div>
      <div className="flex gap-56">
        {isMounted ? (
          <InfoBox
            profileImg={"/profile.png"}
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
