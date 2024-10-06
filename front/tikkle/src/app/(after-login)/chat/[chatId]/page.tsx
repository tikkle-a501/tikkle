"use client";

import { useFetchChatroomById } from "@/hooks/chat/useFetchChatroomById";
import Loading from "@/components/loading/Loading";
import { useState, useEffect, useRef } from "react";
import { usePathname } from "next/navigation";
import Image from "next/image";
import Button from "@/components/button/Button";
import Badge from "@/components/badge/Badge";
import Link from "next/link";
import ChatList from "@/components/chat/ChatList";
import PromiseDropdown from "@/components/drop-down/PromiseDropdown";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import { Chat } from "@/types/chat";
import { useFetchAppointmentByRoomId } from "@/hooks/appointment/useFetchAppointmentByRoomId";
import { useDeleteAppointmentById } from "@/hooks/appointment/useDeleteAppointmentById";

export default function ChatId() {
  const pathname = usePathname();

  // URL에서 roomId 추출 (예: '/chat/31000000-0000-0000-0000-000000000000')
  const roomId = pathname.split("/").pop()!; // 경로의 마지막 부분이 roomId, Non-null assertion 사용

  // 특정 유저 ID 설정
  // TODO: 하드코딩된 유저ID를 로그인된 유저ID 받아오는 로직
  const memberId = "150e6552-807e-11ef-896d-0242ac120005";
  const {
    data: chatroomData,
    error: chatroomError,
    isLoading: isChatroomLoading,
  } = useFetchChatroomById(roomId!);

  /////////////////// 채팅 로직
  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setInputValue(event.target.value);
  };

  const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === "Enter") {
      handleSendMessage();
    }
  };

  const [inputValue, setInputValue] = useState(""); // 메시지 입력 값
  const [messages, setMessages] = useState<Chat[]>([]); // 메시지를 Chat[] 형식으로 관리
  const stompClientRef = useRef<Client | null>(null);

  useEffect(() => {
    // const socket = new SockJS("http://localhost:8080/ws");
    const socket = new SockJS("https://j11a501.p.ssafy.io/ws");
    const stompClient = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000, // 5초 후에 재연결 시도
      onConnect: () => {
        console.log("WebSocket 연결 성공 및 STOMP 연결 확립");
        stompClientRef.current = stompClient;
      },
      onStompError: (frame) => {
        console.error("STOMP 에러:", frame.headers["message"]);
      },
    });

    // WebSocket 연결이 성공했을 때
    stompClient.onConnect = () => {
      console.log("WebSocket 연결됨");

      stompClient.subscribe(`/topic/chatroom.${roomId}`, (message) => {
        const receivedMessage = JSON.parse(message.body);
        const newChat = {
          content: receivedMessage.content,
          timestamp: receivedMessage.timestamp,
          senderId: receivedMessage.senderId,
        };

        setMessages((prevMessages) => [...prevMessages, newChat]);
        console.log("수신된 메시지:", newChat); // 수신된 메시지 로그
      });
    };

    // WebSocket 연결이 종료되었을 때
    stompClient.onDisconnect = () => {
      console.log("WebSocket 연결이 종료됨");
    };

    stompClient.activate();
    stompClientRef.current = stompClient;

    return () => {
      stompClient.deactivate();
    };
  }, [roomId]);

  const handleSendMessage = () => {
    if (stompClientRef.current && inputValue.trim() !== "") {
      const chatMessage = {
        chatroomId: roomId,
        // senderId: memberId,
        content: inputValue,
      };

      const sendMessage = {
        destination: "/app/sendMessage",
        body: chatMessage,
      };
      console.log("chatMessage는 말이죠 : ", chatMessage);
      console.log(
        "chatroomId의 타입은 말이죠 : ",
        typeof chatMessage.chatroomId,
      );
      stompClientRef.current.publish(sendMessage);
      console.log("메시지 전송:", sendMessage); // 전송한 메시지 로그
      setInputValue("");
    }
  };

  const combinedMessages = [...(chatroomData?.chats || []), ...messages];

  ////////////////// 약속잡기 로직
  // TODO: 게시글 작성자만 약속을 잡을 수 있도록 하는 로직
  const [showPromiseDropdown, setShowPromiseDropdown] = useState(false); // 약속 잡기 드롭다운 상태 관리
  // "약속잡기" 버튼 클릭 시 상태 변경
  const handleTogglePromiseDropdown = () => {
    setShowPromiseDropdown((prevState) => !prevState);
  };

  ////////////////// 약속조회 로직
  const {
    data: appointmentData,
    error: appointmentError,
    isLoading: isAppointmentLoading,
  } = useFetchAppointmentByRoomId(roomId);

  console.log(appointmentData);

  ////////////////// 약속삭제 로직
  const deleteAppointmentMutation = useDeleteAppointmentById();

  const handleDeleteAppointment = (appointmentId: string) => {
    if (window.confirm("정말로 약속을 취소하시겠습니까?")) {
      deleteAppointmentMutation.mutate(appointmentId, {
        onSuccess: () => {
          alert("약속이 성공적으로 취소되었습니다.");
        },
        onError: (error) => {
          alert(error.response.data.message);
          console.error("Error deleting appointment:", error);
        },
      });
    }
  };

  ////////// 아래부터 컴포넌트

  // 로딩 중일 때 보여줄 내용
  if (isChatroomLoading) {
    return (
      <>
        <Loading />
      </>
    );
  }

  // 에러 시 보여줄 내용
  if (chatroomError) {
    return <p>Error: {chatroomError.message}</p>;
  }

  return (
    <>
      {/* 채팅 헤더 */}
      <div className="item flex items-start justify-between self-stretch px-10 pb-0 pt-10">
        <div className="flex items-center gap-10">
          <Image
            src="/profile.png"
            alt={`${chatroomData?.partnerName} profile`}
            width={41}
            height={41}
            className="rounded-round"
          />
          <div className="flex py-10 text-28 font-bold text-teal-900">
            {chatroomData?.partnerName}님과의 대화
          </div>
        </div>
        <div className="relative flex h-full items-center justify-center">
          {/* 약속 관련 로직 */}
          {isAppointmentLoading ? (
            // 로딩 중일 때
            <div className="flex h-full w-full items-center justify-center">
              <Loading />
            </div>
          ) : appointmentError ? (
            // 에러가 있을 때
            <p>Error: {appointmentError.message}</p>
          ) : appointmentData?.appointmentId &&
            appointmentData?.appointmentTime ? (
            // 약속이 있을 때 (appointmentId와 appointmentTime이 null이 아닐 때)
            <div className="flex items-center justify-end gap-6 self-stretch p-10">
              <div>
                {new Date(appointmentData?.appointmentTime).toLocaleString(
                  "ko-KR",
                  {
                    month: "long", // 월을 '9월'과 같이 표시
                    day: "numeric", // 일을 숫자로 표시
                    hour: "2-digit", // 시간을 두 자리로 표시
                    minute: "2-digit", // 분을 두 자리로 표시
                    hour12: false, // 24시간 형식을 사용
                  },
                )}
                에
              </div>
              <div className="font-bold text-teal700">
                {appointmentData?.timeQnt}시간
              </div>
              <div>약속</div>
              {/* TODO: 게시글 작성자에게만 약속취소 버튼 표시 */}
              <Button
                size="s"
                variant="primary"
                design="fill"
                main="약속취소"
                onClick={() =>
                  handleDeleteAppointment(appointmentData.appointmentId)
                }
              />
            </div>
          ) : (
            // 약속이 없을 때 '약속잡기' 버튼 표시
            // TODO: 게시글 작성자에게만 약속잡기 버튼 표시
            <div>
              <Button
                size="m"
                variant="primary"
                design="fill"
                main="약속잡기"
                onClick={handleTogglePromiseDropdown}
              />
              {/* PromiseDropdown 버튼 아래 표시 */}
              {showPromiseDropdown && (
                <div className="mt-12">
                  <PromiseDropdown roomId={roomId} />
                </div>
              )}
            </div>
          )}
        </div>
      </div>
      <div className="flex items-center gap-6 self-stretch border-b border-b-coolGray300 p-10">
        <Badge size="l" color="yellow">
          {chatroomData?.status}
        </Badge>
        <Link href={`/board/${chatroomData?.boardId}`}>
          <div className="text-15">{chatroomData?.boardTitle}</div>
        </Link>
      </div>

      {/* 채팅 내용 */}
      <div className="scrollbar-custom flex flex-1 flex-col self-stretch overflow-y-auto">
        {combinedMessages.length > 0 ? (
          combinedMessages.map((chat, index) => (
            <ChatList
              key={index}
              content={chat.content}
              createdAt={chat.timestamp}
              senderId={chat.senderId}
              isMine={chat.senderId === memberId}
            />
          ))
        ) : (
          <div className="flex h-full items-center justify-center">
            <p className="text-center text-warmGray500">
              아직 메시지가 없습니다.
            </p>
          </div>
        )}
      </div>

      {/* 채팅 인풋 */}
      <div className="h-42 flex items-center justify-center self-stretch rounded-10 border border-coolGray400 p-10">
        <input
          type="text"
          placeholder="내용을 입력하세요."
          value={inputValue}
          onChange={handleInputChange}
          onKeyDown={handleKeyDown}
          className="flex-1 appearance-none bg-coolGray100 text-17 placeholder-warmGray300 focus:outline-none"
        />
      </div>
    </>
  );
}
