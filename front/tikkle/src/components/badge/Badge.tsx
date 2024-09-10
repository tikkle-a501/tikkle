interface BadgeProps {
  size: "l" | "m" | "s";
  color: "teal" | "red" | "yellow" | "gray";
  children: React.ReactNode;
}

const Badge: React.FC<BadgeProps> = ({ size, color, children }) => {
  const sizeClasses = {
    l: "inline-flex justify-center items-center p-4 h-[27px] text-14 rounded-4",
    m: "inline-flex justify-center items-center p-4 h-[22px] text-13 rounded-4",
    s: "inline-flex justify-center items-center p-4 h-[20px] text-12 rounded-4",
  };

  const colorClasses = {
    teal: "text-teal500 bg-teal100",
    red: "text-rose500 bg-rose100",
    yellow: "text-yellow500 bg-yellow50",
    gray: "text-coolGray500 bg-coolGray100",
  };

  return (
    <div className={`${sizeClasses[size]} ${colorClasses[color]}`}>
      {children}
    </div>
  );
};

export default Badge;
