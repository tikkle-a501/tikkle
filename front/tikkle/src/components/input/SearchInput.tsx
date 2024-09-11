"use client";

import { useState, useEffect } from "react";

interface SearchInputProps {
  width: string;
  label?: string;
  placeholder?: string;
  leftIcon?: boolean; // 왼쪽 아이콘 여부
  rightIcon?: boolean; // 오른쪽 아이콘 여부
  warningMessage?: string;
}

const SearchInput: React.FC<SearchInputProps> = ({
  width,
  label,
  placeholder,
  leftIcon,
  rightIcon,
  warningMessage,
}) => {
  const [inputValue, setInputValue] = useState(""); // 입력 값 상태 관리

  // 입력 값 변경 핸들러
  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setInputValue(event.target.value); // 입력 값 업데이트
  };

  // 입력 필드 초기화 핸들러
  const handleClearInput = () => {
    setInputValue(""); // 입력 필드 초기화
  };

  // 경고 필드
  const [warning, setWarning] = useState(false);
  useEffect(() => {
    if (inputValue === "아니") {
      setWarning(true);
    } else {
      setWarning(false);
    }
  }, [inputValue]); // inputValue가 변경될 때만 실행

  return (
    <div className="inline-flex flex-col gap-4">
      {/* 라벨 */}
      {label && <label className="text-warmGray500 text-14">{label}</label>}

      <div
        className="flex justify-center items-center h-42 p-10 gap-10 rounded-10 border border-coolGray400"
        style={{ width }} // 부모 div에 width 적용
      >
        {/* 왼쪽 아이콘 */}
        {leftIcon && (
          <span className="material-symbols-outlined text-warmGray300">
            search
          </span>
        )}

        {/* 입력 필드 */}
        <input
          type="text"
          placeholder={placeholder}
          value={inputValue}
          onChange={handleInputChange}
          className="appearance-none flex-1 text-17` placeholder-warmGray300 focus:outline-none"
        />

        {/* 오른쪽 아이콘: 입력 값이 있을 때만 표시 */}
        {rightIcon && inputValue && (
          <span
            className="material-symbols-outlined text-warmGray300 pl-2 cursor-pointer"
            onClick={handleClearInput} // 아이콘 클릭 시 입력 필드 초기화
          >
            cancel
          </span>
        )}
      </div>

      {/* warning */}
      {warning && (
        <label className="text-rose-500 text-14">{warningMessage}</label>
      )}
    </div>
  );
};

export default SearchInput;
