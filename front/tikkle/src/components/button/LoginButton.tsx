"use client";

import Image from "next/image";
import Link from "next/link";

interface LoginButtonProps {
  title: "Slack" | "Mattermost";
}

const colorClasses: Record<string, string> = {
  Mattermost: "bg-mattermost text-white",
  Slack: "border border-warmGray200",
};

const LoginButton: React.FC<LoginButtonProps> = ({ title }) => {
  const color = `${colorClasses[title]}`;
  const content =
    title === "Mattermost" ? "Mattermost로 로그인" : "Slack으로 로그인";

  // 동적으로 이미지 경로 설정
  const imageSrc = title === "Mattermost" ? "/Mattermost.png" : "/Slack.png";

  const login =
    title === "Mattermost" ? "/api/v1/login" : "/api/v1/login/slack";

  return (
    <Link href={login} passHref>
      <button
        className={`${color} flex h-56 w-[296px] items-center justify-center gap-12 rounded-4 p-10 text-18 font-600`}
      >
        <Image src={imageSrc} alt={`${title} Logo`} width={24} height={24} />
        {content}
      </button>
    </Link>
  );
};

export default LoginButton;
