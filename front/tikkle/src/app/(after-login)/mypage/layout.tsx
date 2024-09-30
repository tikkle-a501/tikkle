"use client";

import { useEffect, useState } from "react";
import InfoBox from "@/components/box/InfoBox";
import InfoBoxLoading from "@/components/loading/InfoBoxLoading";

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

  useEffect(() => {
    setIsMounted(true);
  }, []);

  return (
    <>
      <div className="text-40 font-bold text-teal900">마이페이지</div>
      <div className="flex gap-56">
        {isMounted ? (
          <InfoBox
            profileImg={testData.profileImg}
            name={testData.name}
            email={testData.email}
            rate={testData.rate}
          />
        ) : (
          <InfoBoxLoading />
        )}

        <div className="flex w-full justify-between gap-10">{children}</div>
      </div>
    </>
  );
}
