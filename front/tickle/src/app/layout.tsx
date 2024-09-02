import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import { RecoilRoot } from "recoil";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "TICKLE", // 페이지의 기본 제목
  description: "시간 거래 플랫폼, 티끌", // 페이지의 설명
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko">
      <body className={inter.className}>
        <RecoilRoot>{children}</RecoilRoot>
      </body>
    </html>
  );
}
