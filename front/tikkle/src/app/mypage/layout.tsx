import InfoBox from "@/components/box/InfoBox";

export default function ChatLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  // 더미 데이터
  const testData = {
    profileImg: "/profile.png", // 프로필 이미지 URL
    name: "Jane Doe", // 이름
    email: "jane.doe@example.com", // 이메일
    rate: 4.2, // 평점
  };

  return (
    <>
      <div className="text-40 font-bold text-teal900">마이페이지</div>
      <div className="flex gap-36">
        <InfoBox
          profileImg={testData.profileImg}
          name={testData.name}
          email={testData.email}
          rate={testData.rate}
        />
        <div>{children}</div>
      </div>
    </>
  );
}
