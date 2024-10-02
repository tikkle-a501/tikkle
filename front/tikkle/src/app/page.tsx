import Image from "next/image";
import LoginButton from "@/components/button/LoginButton";

export default function Landing() {
  return (
    <>
      {/* 로그인 영역 */}
      <div className="flex h-[90vh] flex-col items-center justify-center gap-[60px]">
        <div className="flex items-center justify-center gap-32">
          <Image src="/icon.png" alt="Logo" width={124} height={160} priority />
          <Image src="/logo.png" alt="Logo" width={487} height={150} priority />
        </div>
        <div className="text-15 text-warmGray500">
          티끌은 단체나 회사에서 시간 재화를 통해 구성원 간 도움을 주고 받는
          플랫폼입니다.
        </div>
        <div>
          <LoginButton title="Mattermost"></LoginButton>
        </div>
      </div>
      <div className="flex h-[90vh] flex-col items-center justify-center">
        시간이 곧 기회!
      </div>
      <div className="flex h-[90vh] flex-col items-center justify-center">
        랭킹과 보상!
      </div>
    </>
  );
}
