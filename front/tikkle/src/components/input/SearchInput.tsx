"use client";

import { useState, useEffect } from "react";

interface SearchInputProps {
  width: string;
  label?: string;
  placeholder?: string;
  leftIcon?: boolean; // 왼쪽 아이콘 여부
  rightIcon?: boolean; // 오른쪽 아이콘 여부
  warningMessage?: string;
  onSearch: (value: string) => void; // 검색어를 상위 컴포넌트로 전달하는 함수
}

const SearchInput: React.FC<SearchInputProps> = ({
  width,
  label,
  placeholder,
  leftIcon,
  rightIcon,
  warningMessage,
  onSearch,
}) => {
  const [inputValue, setInputValue] = useState(""); // 입력 값 상태 관리

  // 입력 값 변경 핸들러
  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setInputValue(event.target.value); // 입력 값 업데이트
    onSearch(event.target.value);
  };

  // 입력 필드 초기화 핸들러
  const handleClearInput = () => {
    setInputValue(""); // 입력 필드 초기화
    onSearch("");
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
      {label && <label className="text-14 text-warmGray500">{label}</label>}

      <div
        className="h-42 flex items-center justify-center gap-10 rounded-10 border border-coolGray400 p-10"
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
          className="text-17` flex-1 appearance-none placeholder-warmGray300 focus:outline-none"
        />

        {/* 오른쪽 아이콘: 입력 값이 있을 때만 표시 */}
        {rightIcon && inputValue && (
          <span
            className="material-symbols-outlined cursor-pointer pl-2 text-warmGray300"
            onClick={handleClearInput} // 아이콘 클릭 시 입력 필드 초기화
          >
            cancel
          </span>
        )}
      </div>

      {/* warning */}
      {warning && (
        <label className="text-14 text-rose-500">{warningMessage}</label>
      )}
    </div>
  );
};

export default SearchInput;
