"use client";
import { useState, useEffect } from "react";
import Navigation from "@/components/navigation/Navigation";
import QueryProvider from "@/components/QueryProvider";
import localFont from "next/font/local";
import Head from "next/head";

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
  const [isMounted, setIsMounted] = useState(false);

  useEffect(() => {
    setIsMounted(true);
  }, []);

  return (
    <>
      <Head>
        <link
          href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined"
          rel="stylesheet"
        />
      </Head>
      <div className={pretendard.className}>
        {isMounted && <Navigation />}
        <QueryProvider>
          <div className="flex flex-col gap-[10px] pb-[40px] pl-[48px] pr-[48px] pt-[28px]">
            {children}
          </div>
        </QueryProvider>
      </div>
    </>
  );
}
