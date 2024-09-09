"use client";

import React from "react";

interface ButtonProps {
  size: "l" | "m" | "s";
  variant: "primary" | "secondary" | "tertiary";
  design: "fill" | "outline";
  disabled?: boolean;
  textAlign?: "left" | "center" | "right";
  onClick?: () => void;
  children: React.ReactNode;
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
  textAlign = "center", // 기본값으로 'center'
  onClick,
  children,
}) => {
  const sizeClasses = {
    l: "flex p-16 text-17 items-center gap-8 rounded-12",
    m: "flex p-16 text-16 items-center gap-8 rounded-10",
    s: "flex p-16 text-15 items-center gap-8 rounded-8",
  };

  const textAlignClasses = {
    left: "justify-start",
    center: "justify-center",
    right: "justify-end",
  };

  const className = `${sizeClasses[size]} ${
    colorClasses[variant][design] || ""
  } ${textAlignClasses[textAlign]} font-semibold ${
    disabled ? "cursor-not-allowed" : "cursor-pointer"
  }`;

  return (
    <button className={className} disabled={disabled} onClick={onClick}>
      {children}
    </button>
  );
};

export default Button;
