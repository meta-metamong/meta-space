<template>
    <div class="container mt-2 d-flex justify-content-center">
      <div id="highchart-container"></div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  import { get } from '../apis/axios';
  import Highcharts from "highcharts";
  
  export default {
    name: "ReservationChart",
    data() {
      return {
        // 주간 예약 데이터
        reservationData: [],
        // 요일 배열
        daysOfWeek: ["월", "화", "수", "목", "금", "토","일"]
      };
    },
    methods: {
      // 백엔드에서 주간 예약 데이터 가져오는 메서드
      async fetchData() {
        try {
          // 예약 데이터 API 호출
          const response = await get('/admin/reservations/this-week');
          this.reservationData = response.data;
  
          // 라인 차트 그리기
          this.drawChart();
        } catch (error) {
          console.error("데이터를 가져오는 데 실패했습니다:", error);
        }
      },
  
      // Highcharts로 라인 차트 그리기
      drawChart() {
        // 각 시설별 예약 데이터를 시리즈로 나누기
        const seriesData = this.reservationData.map((reservation) => {
          return {
            name: reservation.fctId, // 시설명
            data: [
              reservation.mon,    // 월요일
              reservation.tues,   // 화요일
              reservation.wednes, // 수요일
              reservation.thurs,  // 목요일
              reservation.fri,    // 금요일
              reservation.satur,   // 토요일
              reservation.sun,    // 일요일
            ]
          };
        });
  
        // Highcharts 라인 차트 설정
        Highcharts.chart("highchart-container", {
          title: {
            text: "이번 주 시설별 예약 현황"
          },
          xAxis: {
            categories: this.daysOfWeek // 요일
          },
          yAxis: {
            title: {
              text: "예약 건수"
            },
            tickInterval: 1,  // Y축을 1씩 증가하도록 설정
          },
          series: seriesData, // 시설별 예약 건수 데이터
          exporting: {
            enabled: true, // 내보내기 활성화
          },
          accessibility: {
            enabled: false, // 접근성 기능 비활성화
          }
        });
      }
    },
    mounted() {
      this.fetchData();  // 컴포넌트가 마운트될 때 데이터 가져오기
    }
  };
  </script>
  
  <style scoped>
  #highchart-container {
    margin: 20px auto;
  }
  </style>
  