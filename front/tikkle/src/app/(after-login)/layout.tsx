"use client";
import { useState, useEffect } from "react";
import Navigation from "@/components/navigation/Navigation";
import QueryProvider from "@/components/QueryProvider";
import localFont from "next/font/local";
import Head from "next/head";
import NavigationLoading from "@/components/loading/NavigationLoading";

// Pretendard font 설정
const pretendard = localFont({
  src: "../_font/PretendardVariable.woff2",
  display: "swap",
  weight: "45 920",
});

export default function AfterLoginLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const [isNavigationLoaded] = useState(true);

  return (
    <>
      <Head>
        <link
          href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined"
          rel="stylesheet"
        />
      </Head>
      <div className={pretendard.className}>
        {isNavigationLoaded ? <Navigation /> : <NavigationLoading />}
        <QueryProvider>
          <div className="flex flex-col gap-[10px] pb-[40px] pl-[48px] pr-[48px] pt-[28px]">
            {children}
          </div>
        </QueryProvider>
      </div>
    </>
  );
}
