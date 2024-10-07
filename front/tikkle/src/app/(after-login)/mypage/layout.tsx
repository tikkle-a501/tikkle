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
  const testData = {
    profileImg: "/profile.png",
    name: "Jane Doe",
    email: "jane.doe@example.com",
    rate: 4.2,
  };

  const [isMounted, setIsMounted] = useState(false);

  const [isInfoBoxLoaded] = useState(true);

  const member = useMypageStore((state) => state.member);
  const setMember = useMypageStore((state) => state.setMember);

  useEffect(() => {
    setIsMounted(true);

    if (!member) {
      setMember({
        id: "1",
        name: testData.name,
        nickname: "jane_doe",
        email: testData.email,
      });
    }
  }, [member, setMember]);

  return (
    <>
      <div className="text-40 font-bold text-teal900">마이페이지</div>
      <div className="flex gap-56">
        {isMounted ? (
          <InfoBox
            profileImg={testData.profileImg}
            name={member?.name ?? "이름 없음"}
            email={member?.email ?? testData.email}
            rate={3}
          />
        ) : (
          <InfoBoxLoading />
        )}

        <div className="flex w-full justify-between gap-10">{children}</div>
      </div>
    </>
  );
}
