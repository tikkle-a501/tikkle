"use client";

import Image from "next/image";

interface LoginButtonProps {
  title: "Slack" | "Mattermost";
  onClick?: () => void;
}

const colorClasses: Record<string, string> = {
  Mattermost: "bg-mattermost text-white",
  Slack: "border border-warmGray200",
};

const LoginButton: React.FC<LoginButtonProps> = ({ title, onClick }) => {
  const color = `${colorClasses[title]}`;
  const content =
    title === "Mattermost" ? "Mattermost로 로그인" : "Slack으로 로그인";

  // 동적으로 이미지 경로 설정
  const imageSrc = title === "Mattermost" ? "/Mattermost.png" : "/Slack.png";

  return (
    <button
      className={`${color} flex h-56 w-[296px] items-center justify-center gap-12 rounded-4 p-10 text-18 font-600`}
      onClick={onClick}
    >
      <Image src={imageSrc} alt={`${title} Logo`} width={24} height={24} />
      {content}
    </button>
  );
};

export default LoginButton;
