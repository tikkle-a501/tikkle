"use client";

interface ChipsProps {
  size: "l" | "m" | "s";
  variant: "primary" | "secondary";
  design: "fill" | "outline";
  disabled?: boolean;
  onClick?: () => void;
  children?: React.ReactNode;
}

const colorClasses: Record<string, { fill: string; outline?: string }> = {
  primary: {
    fill: "text-white bg-teal500 hover:bg-teal700 ",
    outline:
      "text-teal500 bg-white border border-teal500 hover:text-teal700 hover:border-teal700 ",
  },
  secondary: {
    fill: "text-white bg-coolGray400 hover:bg-coolGray600",
  },
};

const Chips: React.FC<ChipsProps> = ({
  size,
  variant,
  design,
  disabled = false,
  onClick,
  children,
}) => {
  const sizeClasses = {
    l: "flex justify-center items-center h-30 p-10 text-15 leading-20 items-center rounded-round",
    m: "flex justify-center items-center h-28 p-10 text-14 items-center rounded-round",
    s: "flex justify-center items-center h-20 p-10 text-12 items-center rounded-round",
  };

  const className = `${sizeClasses[size]} ${
    colorClasses[variant][design] || ""
  } ${
    disabled
      ? design === "outline"
        ? "border-coolGray300 text-coolGray300 hover:border-coolGray300 hover:text-coolGray300 cursor-not-allowed"
        : "bg-coolGray300 text-baseWhite hover:bg-coolGray300 hover: text-baseWhite cursor-not-allowed"
      : "cursor-pointer"
  } ${disabled && design !== "outline" ? "bg-coolGray300" : ""}`;

  return (
    <div className={className} onClick={onClick}>
      {children && (
        <div className="flex justify-center items-center">{children}</div>
      )}
    </div>
  );
};

export default Chips;
