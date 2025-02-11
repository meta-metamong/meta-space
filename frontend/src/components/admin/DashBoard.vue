<template>
  <div>
    <!-- 주간 예약 현황과 시간대별 예약 현황을 나란히 배치 -->
    <div class="chart-container">
      <!-- 주간 예약 현황 차트 -->
      <div id="week-reservation-chart" class="chart-half"></div>

      <!-- 시간대별 예약 현황 차트 -->
      <div id="hourly-reservation-chart" class="chart-half"></div>
    </div>

    <!-- 시설별 예약/취소/매출 현황 차트 -->
    <div id="facility-stats-chart"></div>

    <!-- 이번 달 예약 랭킹 차트 (파이 차트) -->
    <div id="ranking-chart"></div>
  </div>
</template>

<script>
import { defineComponent } from "vue";
import { get } from "../../apis/axios"; // 공통 API 호출 함수 import
import Highcharts from "highcharts";

export default defineComponent({
  data() {
    return {
      weekReservations: [], // 주간 예약 데이터를 담을 변수
      weekHourReservations: [], // 시간대별 예약 현황
      totalReservations: [], // 시설별 예약 건수
      cancelledReservations: [], // 시설별 취소된 예약 건수
      totalRevenue: [], // 시설별 매출
      rankedReservation: [] // 예약 랭킹 데이터
    };
  },
  methods: {
    async fetchWeekReservations() {
      try {
        // 주간 예약 현황 및 시설별 통계 데이터를 한번에 가져옴
        const response = await get("/admin/dashboard");
        console.log("주간 예약 및 시설별 통계 데이터:", response.data);

        if (response.data) {
          this.weekReservations = response.data.weekReservations; // 주간 예약 현황
          this.weekHourReservations = response.data.weekHourReservations; // 시간대별 예약 현황
          console.log("시간대별 예약 현황:", this.weekHourReservations); 
          this.totalReservations = response.data.totalReservations; // 시설별 예약 건수
          this.cancelledReservations = response.data.cancelledReservations; // 시설별 취소된 예약 건수
          this.totalRevenue = response.data.totalRevenue; // 시설별 매출
          this.rankedReservation = response.data.rankedReservation; // 예약 랭킹 데이터
        } else {
          console.error("데이터가 없습니다.");
        }

        // 차트 그리기
        this.drawCharts();
      } catch (error) {
        console.error("데이터를 가져오는 데 실패했습니다:", error);
      }
    },

    drawCharts() {
      // 주간 예약 현황 선 차트 그리기
      const weekSeriesData = this.weekReservations.map((reservation) => ({
        name: `시설 ${reservation.fctId}`, // 시설명
        type: 'area', // 선채우기
        data: [
          reservation.sun,    // 일요일
          reservation.mon,    // 월요일
          reservation.tues,   // 화요일
          reservation.wednes, // 수요일
          reservation.thurs,  // 목요일
          reservation.fri,    // 금요일
          reservation.satur,  // 토요일
        ],
        lineWidth: 2, // 선 두께
      }));

      // 주간 예약 현황 선 차트
      Highcharts.chart("week-reservation-chart", {
        title: {
          text: "주간 예약 현황"
        },
        xAxis: {
          categories: ["일", "월", "화", "수", "목", "금", "토"], // 요일 설정
          title: { text: "요일" }
        },
        yAxis: {
          title: {
            align: 'high',
            offset: 0,
            rotation: 0,
            y: -10,
            text: "예약 건수"
          }
        },
        series: weekSeriesData, // 시설별 예약 건수 데이터
        tooltip: {
          shared: true,
          valueSuffix: " 건"
        },
        credits: { enabled: false }, // 크레딧 비활성화
        exporting: { enabled: true } // 내보내기 활성화
      });

      // 시간대별 예약 현황 선 차트
      const hourlySeriesData = Array.from({ length: 24 }, (_, hour) => {
        // 0시부터 24시까지 예약 데이터를 채웁니다.
        const reservation = this.weekHourReservations.find(res => res.rvtHour === hour);
        
        return {
          name: `${hour}시`,
          type: 'line', // 선 차트
          data: [reservation ? reservation.rvtCount : 0], // 예약 건수가 없으면 0
        };
      });

      // 시간대별 예약 현황 차트 그리기
      Highcharts.chart("hourly-reservation-chart", {
        title: {
          text: "시간대별 예약 현황"
        },
        xAxis: {
          categories: Array.from({ length: 24 }, (_, hour) => `${hour}시`), // 0시부터 24시까지 시간대별 X축
          title: { text: "시간" }
        },
        yAxis: {
          title: {
            text: "예약 건수"
          }
        },
        series: hourlySeriesData, // 시간대별 예약 건수 데이터
        tooltip: {
          shared: true,
          valueSuffix: " 건"
        },
        credits: { enabled: false }, // 크레딧 비활성화
        exporting: { enabled: true } // 내보내기 활성화
      });

      // 시설별 예약/취소/매출 현황 복합 막대 그래프
      Highcharts.chart("facility-stats-chart", {
        chart: {
          type: 'column' // 복합 막대 그래프
        },
        title: {
          text: "시설별 예약/예약취소/매출 현황"
        },
        xAxis: {
          categories: this.totalReservations.map(stat => stat.fctName), // 시설 이름
          title: { text: "시설" }
        },
        yAxis: [{
          title: { text: "예약 건수" },
        }, {
          title: { text: "매출 (원)" },
          opposite: true // 매출은 오른쪽 세로축
        }],
        series: [
          {
            name: "시설별 예약 건수",
            data: this.totalReservations.map(stat => stat.totalReservations), // 시설별 예약 건수
          },
          {
            name: "시설별 취소된 예약 건수",
            data: this.cancelledReservations.map(stat => stat.cancelledReservations), // 시설별 취소된 예약 건수
          },
          {
            name: "시설별 매출",
            data: this.totalRevenue.map(stat => stat.totalRevenue), // 시설별 매출
            type: 'line', // 선 차트로 매출 표시
            yAxis: 1 // 매출은 두 번째 축에 표시
          }
        ],
        tooltip: {
          shared: true,
          valueSuffix: " 건"
        },
        credits: { enabled: false }, // 크레딧 비활성화
        exporting: { enabled: true } // 내보내기 활성화
      });

      // 예약 랭킹 차트 (파이 차트)
      const rankedData = this.rankedReservation.map((stat) => ({
        name: stat.fctName,  // 시설 이름
        y: stat.totRvt     // 예약 건수
      }));

      Highcharts.chart("ranking-chart", {
        chart: {
          type: 'pie' // 파이 차트
        },
        title: {
          text: "이번 달 예약 랭킹"
        },
        series: [{
          name: "예약 건수",
          data: rankedData,
          size: '80%',
          innerSize: '60%',
          dataLabels: {
            formatter: function () {
              return `${this.point.name}: ${this.point.y} 건`; // 데이터 라벨 표시
            }
          }
        }],
        credits: { enabled: false }, // 크레딧 비활성화
        exporting: { enabled: true } // 내보내기 활성화
      });
    }
  },
  mounted() {
    this.fetchWeekReservations(); // 컴포넌트가 마운트될 때 데이터 가져오기
  }
});
</script>

<style scoped>
.chart-container {
  display: flex;
  justify-content: center;
  gap: 50px;
  margin-bottom: 20px;
}

.chart-half {
  width: 550px; /* 차트 너비를 40%로 설정 */
  height: 100%; /* 부모 요소의 높이를 따라가도록 설정 */
}

/* 시설별 예약/취소/매출 현황 차트 */
#facility-stats-chart {
  width: 100%;
  height: 270px; /* 화면의 1/3 높이 */
  margin: 0 auto;
  overflow: hidden; /* 스크롤 방지 */
}

/* 예약 랭킹 차트 */
#ranking-chart {
  width: 100%;
  height: 270px; /* 화면의 1/3 높이 */
  margin: 0 auto;
  overflow: hidden; /* 스크롤 방지 */
}

/* 다른 차트의 높이도 동일하게 설정 */
#week-reservation-chart,
#hourly-reservation-chart {
  height: 270px; /* 화면의 1/3 높이 */
  overflow: hidden; /* 스크롤 방지 */
}


</style>
