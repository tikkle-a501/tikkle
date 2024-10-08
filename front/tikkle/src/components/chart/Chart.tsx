"use client";

import { useEffect } from "react";
import { useFetchRate } from "@/hooks";
import * as d3 from "d3";

const Chart: React.FC = () => {
  const { data: exchangeRates, isLoading, isError } = useFetchRate();

  useEffect(() => {
    // 데이터가 배열인지 확인
    if (
      isLoading ||
      isError ||
      !exchangeRates ||
      !Array.isArray(exchangeRates)
    ) {
      console.log("조건에 맞지 않아 렌더링 중지");
      return;
    }
    // 이전 SVG 삭제
    d3.select("#chart").selectAll("*").remove();

    // SVG 설정
    // 차트의 부모 요소 너비를 가져옴
    const container = d3.select("#chart").node() as HTMLElement;
    const containerWidth = container.getBoundingClientRect().width;
    const margin = { top: 20, right: 30, bottom: 30, left: 50 };
    const width = containerWidth - margin.left - margin.right;
    const height = 450 - margin.top - margin.bottom;

    const svg = d3
      .select("#chart")
      .append("svg")
      .attr("width", width + margin.left + margin.right)
      .attr("height", height + margin.top + margin.bottom)
      .append("g")
      .attr("transform", `translate(${margin.left},${margin.top})`);

    // x축: 시간
    const xDomain = d3.extent(exchangeRates, (d) => new Date(d.createdAt)) as
      | [Date, Date]
      | [undefined, undefined];

    // 기본값을 설정해 undefined 값을 방지
    const x = d3
      .scaleTime()
      .domain(xDomain[0] && xDomain[1] ? xDomain : [new Date(), new Date()]) // undefined일 경우 기본값 설정
      .range([0, width]);

    // x축 추가
    svg
      .append("g")
      .attr("transform", `translate(0,${height})`)
      .call(d3.axisBottom(x).tickSize(-height)) // x축 보조선 길이 설정
      .style("font-family", "Pretendard")
      .style("font-style", "normal")
      .style("font-size", "0.75rem")
      .selectAll("line") // x축 보조선 스타일 설정
      .attr("stroke", "#e0e0e0")
      .attr("stroke-dasharray", "4 4"); // 점선으로 설정

    // y축: 환율 비율
    const yMin = d3.min(exchangeRates, (d) => d.timeToRank) ?? 0;
    const yMax = d3.max(exchangeRates, (d) => d.timeToRank) ?? 0;

    const y = d3
      .scaleLinear()
      .domain([yMin - 10, yMax + 10]) // 최소값에서 10을 빼고 최대값에 10을 더해 여유 공간을 줌
      .range([height, 0]);

    // y축 추가
    svg
      .append("g")
      .call(
        d3.axisLeft(y).tickSize(-width), // 보조선을 생성하기 위한 설정
      )
      .style("font-family", "Pretendard")
      .style("font-style", "normal")
      .style("font-size", "0.75rem")
      .selectAll("line") // 보조선 스타일 설정
      .attr("stroke", "#e0e0e0");

    // x축의 맨 오른쪽 선(domain)을 제거
    svg.selectAll(".domain").attr("stroke", "none"); // 맨 위와 오른쪽 선을 보이지 않도록 설정

    // 라인 그리기
    const line = d3
      .line<{ createdAt: string; timeToRank: number }>()
      .x((d) => x(new Date(d.createdAt)))
      .y((d) => y(d.timeToRank))
      .curve(d3.curveCardinal); // 부드럽고 자연스러운 곡선, 포인트를 통과함

    svg
      .append("path")
      .datum(exchangeRates) // 데이터 바인딩
      .attr("fill", "none")
      .attr("stroke", "#14B8A6") // 라인 색상
      .attr("stroke-width", 2) // 라인 두께
      .attr("stroke-linejoin", "round") // 모서리를 둥글게 처리
      .attr("stroke-linecap", "round") // 라인 끝을 둥글게 처리
      .attr("d", line); // 라인 그리기

    // 세로선 추가
    const verticalLine = svg
      .append("line")
      .attr("stroke", "gray")
      .attr("stroke-width", 2) // 굵기를 2로 설정
      .attr("stroke-dasharray", "4 4") // 점선 설정, "4 4"는 4px 길이의 선과 4px 간격을 의미
      .attr("y1", 0)
      .attr("y2", height)
      .style("opacity", 0); // 기본적으로 보이지 않도록 설정

    // 초록색 점 추가
    const valueDot = svg
      .append("circle")
      .attr("r", 5) // 반지름 5
      .attr("fill", "#14B8A6") // 초록색 점
      .style("opacity", 0); // 기본적으로 보이지 않도록 설정

    // 툴팁 추가
    const tooltip = d3
      .select("body")
      .append("div")
      .style("position", "absolute")
      .style("opacity", 0)
      .style("background", "#f9f9f9")
      .style("padding", "5px")
      .style("border", "1px solid #ccc")
      .style("border-radius", "4px");

    // 차트 위에 투명한 레이어를 만들어 마우스 이벤트를 처리
    svg
      .append("rect")
      .attr("width", width)
      .attr("height", height)
      .style("fill", "none")
      .style("pointer-events", "all")
      .on("mousemove", (event) => {
        const [mouseX] = d3.pointer(event);
        const dateAtMouseX = x.invert(mouseX);

        // 가까운 데이터 포인트 찾기
        const closestData = exchangeRates.reduce((prev, curr) =>
          Math.abs(
            new Date(curr.createdAt).getTime() - dateAtMouseX.getTime(),
          ) <
          Math.abs(new Date(prev.createdAt).getTime() - dateAtMouseX.getTime())
            ? curr
            : prev,
        );

        // 세로선 이동
        verticalLine
          .attr("x1", x(new Date(closestData.createdAt)))
          .attr("x2", x(new Date(closestData.createdAt)))
          .style("opacity", 1);

        // 툴팁 위치 및 내용 설정
        tooltip
          .html(
            `시간: ${new Date(closestData.createdAt).toLocaleString()}<br/>환율: ${closestData.timeToRank}`,
          )
          .style("left", event.pageX + 15 + "px")
          .style("top", event.pageY - 30 + "px")
          .style("opacity", 1);

        // 초록색 점 위치 업데이트
        valueDot
          .attr("cx", x(new Date(closestData.createdAt)))
          .attr("cy", y(closestData.timeToRank))
          .style("opacity", 1);
      })
      .on("mouseout", () => {
        verticalLine.style("opacity", 0);
        tooltip.style("opacity", 0);
        valueDot.style("opacity", 0); // 초록색 점 숨기기
      });
  }, [exchangeRates, isLoading, isError]);

  return <div id="chart" className="flex w-full"></div>;
};
export default Chart;
