import MenuList from "@/components/list/MenuList";

export default function ChatLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <>
      <div className="text-40 text-teal900 font-bold">마이페이지</div>
      <div className="flex gap-36">
        <div>
          <div className="flex flex-col p-14 gap-14">
            <MenuList href="activity" icon="edit">
              나의 게시글
            </MenuList>
            <MenuList href="trade" icon="history_toggle_off">
              나의 거래 내역
            </MenuList>
            <MenuList href="exchange" icon="swap_horiz">
              나의 환전 내역
            </MenuList>
          </div>
        </div>
        <div>{children}</div>
      </div>
    </>
  );
}
