"use client";

import { usePathname } from "next/navigation";

export default function ChatId() {
  const pathname = usePathname();

  return <div>{pathname}</div>;
}
