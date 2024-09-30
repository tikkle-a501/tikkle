import LoginButton from "@/components/button/LoginButton";
import Image from "next/image";

export default function Login() {
  return (
    <>
      <div className="flex flex-col items-center justify-center gap-[130px] p-[200px]">
        <Image src="/logo.png" alt="Logo" width={328} height={101} />
        <div className="flex flex-col gap-[20px]">
          <LoginButton title="Slack"></LoginButton>
          <LoginButton title="Mattermost"></LoginButton>
        </div>
      </div>
    </>
  );
}
