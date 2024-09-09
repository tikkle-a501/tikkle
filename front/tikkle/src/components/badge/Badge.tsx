interface BadgeProps {
  size: "l" | "m" | "s";
  color: "teal" | "red" | "yellow" | "gray";
  children: React.ReactNode;
}

const Badge: React.FC<BadgeProps> = ({ size, color, children }) => {
  const sizeClasses = {
    l: "p-4 text-14 rounded-4",
    m: "p-4 text-13 rounded-4",
    s: "p-4 text-12 rounded-4",
  };

  const colorClasses = {
    teal: "text-teal500 bg-teal100",
    red: "text-rose500 bg-rose100",
    yellow: "text-yellow500 bg-yellow50",
    gray: "text-coolGray500 bg-coolGray100",
  };

  return (
    <span className={`${sizeClasses[size]} ${colorClasses[color]}`}>
      {children}
    </span>
  );
};

export default Badge;
