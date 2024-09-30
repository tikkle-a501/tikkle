const NavigationLoading: React.FC = () => {
  return (
    <nav className="sticky top-0 z-50 flex h-[85px] items-center justify-between border-b border-warmGray300 bg-warmGray50 px-40">
      <div className="h-16 w-16 animate-spin rounded-full border-4 border-warmGray400 border-t-transparent"></div>
    </nav>
  );
};

export default NavigationLoading;
