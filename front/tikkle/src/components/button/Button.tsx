"use client";

interface ButtonProps {
  size: "l" | "m" | "s";
  variant: "primary" | "secondary" | "tertiary";
  design: "fill" | "outline";
  disabled?: boolean;
  onClick?: () => void;
  left?: string; // Left 텍스트 (옵셔널)
  main: string; // Main 텍스트 (필수)
  right?: string; // Right 텍스트 (옵셔널)
  children?: React.ReactNode;
}

const colorClasses: Record<string, { fill: string; outline?: string }> = {
  primary: {
    fill: "text-white bg-teal500 hover:bg-teal700 disabled:bg-coolGray300",
    outline:
      "text-teal500 bg-white border border-teal500 hover:text-teal700 hover:border-teal700 disabled:text-coolGray300 disabled:border-coolGray300",
  },
  secondary: {
    fill: "text-white bg-coolGray400 hover:bg-coolGray600 disabled:bg-coolGray300",
  },
  tertiary: {
    fill: "text-teal500 bg-white disabled:text-coolGray300",
  },
};

const Button: React.FC<ButtonProps> = ({
  size,
  variant,
  design,
  disabled = false, // 기본값으로 false
  onClick,
  left,
  main,
  right,
  children,
}) => {
  const sizeClasses = {
    l: "flex whitespace-nowrap justify-center items-center h-56 p-16 text-17 items-center gap-8 rounded-12",
    m: "flex whitespace-nowrap justify-center items-center h-48 p-16 text-16 items-center gap-8 rounded-10",
    s: "flex whitespace-nowrap justify-center items-center h-40 p-16 text-15 items-center gap-8 rounded-8",
  };

  const className = `${sizeClasses[size]} ${
    colorClasses[variant][design] || ""
  } ${disabled ? "cursor-not-allowed" : "cursor-pointer"}`;

  return (
    <button className={className} disabled={disabled} onClick={onClick}>
      {/* Children(아이콘 사용 시) 을 왼쪽에 추가 */}
      {children && (
        <div className="flex items-center justify-center">{children}</div>
      )}
      {/* Left 텍스트 (옵셔널) */}
      {left && <div className="font-normal">{left}</div>}
      {/* Main 텍스트 (필수) */}
      <div className="ont-semibold">{main}</div>
      {/* Right 텍스트 (옵셔널) */}
      {right && <div className="font-normal">{right}</div>}
    </button>
  );
};

export default Button;
