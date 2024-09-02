import react from "eslint-plugin-react";
import typescriptEslint from "@typescript-eslint/eslint-plugin";
import reactHooks from "eslint-plugin-react-hooks";
import jsxA11Y from "eslint-plugin-jsx-a11y";
import { fixupPluginRules } from "@eslint/compat";
import globals from "globals";
import tsParser from "@typescript-eslint/parser";
import path from "node:path";
import { fileURLToPath } from "node:url";
import js from "@eslint/js";
import { FlatCompat } from "@eslint/eslintrc";

// 현재 파일 경로와 디렉토리명 설정
const filename = fileURLToPath(import.meta.url);
const dirname = path.dirname(filename);

// FlatCompat 인스턴스 생성, 추천 구성 사용
const compat = new FlatCompat({
  baseDirectory: dirname,
  recommendedConfig: js.configs.recommended,
  allConfig: js.configs.all,
});

export default [
  {
    ignores: ["**/dist/**/*", "**/.eslintrc.js"], // 특정 디렉토리 및 파일 무시
  },
  ...compat.extends(
    "eslint:recommended",
    "plugin:react/recommended",
    "plugin:storybook/recommended",
    "prettier",
    "plugin:@typescript-eslint/recommended"
  ),
  {
    plugins: {
      react,
      "@typescript-eslint": typescriptEslint,
      "react-hooks": fixupPluginRules(reactHooks),
      "jsx-a11y": jsxA11Y,
    },

    languageOptions: {
      globals: {
        ...globals.browser,
        ...globals.node,
      },

      parser: tsParser,
      ecmaVersion: 5,
      sourceType: "commonjs",

      parserOptions: {
        project: "./.tsconfig.eslint.json", // TypeScript ESLint 구성 파일 경로
        tsconfigRootDir: dirname, // TypeScript ESLint 구성 파일의 루트 디렉토리
      },
    },

    settings: {
      "import/resolver": {
        typescript: {},
      },
    },

    rules: {
      "react/prop-types": "off", // PropTypes 체크 규칙 비활성화
      "react-hooks/rules-of-hooks": "error", // React Hooks 규칙 위반 시 에러를 발생시킴. 모든 Hook은 최상위에서 호출되어야 하며, 조건문이나 반복문 내부에서 호출하면 안 됨.
      "react-hooks/exhaustive-deps": "warn", // React Hooks 의존성 검사 경고. useEffect, useCallback 등에서 의존성 배열이 올바르게 설정되어 있는지 검사하고, 누락된 의존성이 있으면 경고를 발생시킴.
      "react/react-in-jsx-scope": "off", // React가 JSX 스코프 내에 있는지 검사 비활성화. React 17부터는 JSX 파일에 React를 import하지 않아도 되므로 이 규칙을 비활성화함.
      "comma-dangle": "off", // 후행 쉼표 검사 비활성화
      "react/display-name": "off", // React 디스플레이 이름 검사 비활성화

      "@typescript-eslint/no-unused-vars": [
        "error", // 사용되지 않는 변수 검사
        {
          argsIgnorePattern: "^_", // 언더스코어로 시작하는 변수는 검사에서 제외
        },
      ],

      "storybook/prefer-pascal-case": "off", // Storybook PascalCase 검사 비활성화
      "no-console": "warn", // 콘솔 로그 사용 시 경고
    },
  },
  {
    files: ["**/*.ts", "**/*.tsx"], // TypeScript 및 TSX 파일에 대한 설정

    rules: {
      "no-undef": "off", // 정의되지 않은 변수 검사 비활성화
    },
  },
];
