import type { Metadata } from "next";
import localFont from "next/font/local";
import "./globals.css";
import Navigation from "@/components/navigation/Navigation";
import QueryProvider from "@/components/QueryProvider";

const pretendard = localFont({
  src: "./_font/PretendardVariable.woff2",
  display: "swap",
  weight: "45 920",
});
export const metadata: Metadata = {
  title: "TIKKLE", // 페이지의 기본 제목
  description: "시간 거래 플랫폼, 티끌", // 페이지의 설명
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko">
      <head>
        <link
          href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined"
          rel="stylesheet"
        />
      </head>
      <body className={pretendard.className}>
        <Navigation />
        <QueryProvider>
          <div className="flex flex-col gap-[10px] pb-[40px] pl-[48px] pr-[48px] pt-[28px]">
            {children}
          </div>
        </QueryProvider>
      </body>
    </html>
  );
}
