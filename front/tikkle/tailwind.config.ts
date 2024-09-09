import {
  colors,
  fontSize,
  fontWeight,
  lineHeight,
  fontFamily,
  radius,
  shadow,
  spacing,
} from "./src/styles";

/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./app/**/*.{js,ts,jsx,tsx,mdx}",
    "./pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./components/**/*.{js,ts,jsx,tsx,mdx}",

    // Or if using `src` directory:
    "./src/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      colors: {
        ...colors, // 사용자 정의 색상 확장
      },
      fontSize: {
        ...fontSize, // 사용자 정의 폰트 크기 확장
      },
      fontWeight: {
        ...fontWeight, // 사용자 정의 폰트 두께 확장
      },
      lineHeight: {
        ...lineHeight, // 사용자 정의 줄 높이 확장
      },
      fontFamily: {
        ...fontFamily, // 사용자 정의 폰트 패밀리 확장
      },
      borderRadius: {
        ...radius, // 사용자 정의 보더 반경 확장
      },
      boxShadow: {
        ...shadow, // 사용자 정의 그림자 확장
      },
      spacing: {
        ...spacing, // 사용자 정의 여백 확장
      },
    },
  },
  plugins: [],
};
