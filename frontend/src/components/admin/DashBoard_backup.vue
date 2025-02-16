<template>
    <div>
        <div class="chart-container">
  <div id="week-reservation-chart" class="chart-large"></div>
  <div id="growth-rate-card" class="chart-small">
    <div class="growth-rate-title">이번달 총 매출</div>
    <div class="growth-rate-content">
      <div class="growth-rate-main">
        <span class="growth-rate-value">{{ totalSales }}원</span>
        <div class="growth-rate-details">
          <span class="growth-rate-label">전월 대비</span>
          <div class="growth-rate-numbers">
            <span class="growth-rate-percentage">{{ growthRate }}%</span>
            <span :style="{ color: arrowColor }" class="growth-rate-arrow">{{ arrow }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

      <div class="ranking-container">
        <div id="ranking-chart" class="ranking-smaller"></div>
        <div id="payment-ranking-chart" class="ranking-smaller"></div>
      </div>
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
        totalReservations: [], // 시설별 예약 건수
        cancelledReservations: [], // 시설별 취소된 예약 건수
        totalRevenue: [], // 시설별 매출
        rankedReservation: [], // 예약 랭킹 데이터
        rankedPayment: [], // 매출 랭킹 데이터
        monthlySalesGrowth: {}, // 매출 증감율 데이터
  
        // 매출 증감율 카드 관련 변수
        arrow: "", // 화살표 (↑, ↓, →)
        arrowColor: "", // 화살표 색상 (green, red, gray)
        growthRate: 0, // 매출 증감율
        totalSales: 0 // 이번 달 총 매출
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
            this.totalReservations = response.data.totalReservations; // 시설별 예약 건수
            this.cancelledReservations = response.data.cancelledReservations; // 시설별 취소된 예약 건수
            this.totalRevenue = response.data.totalRevenue; // 시설별 매출
            this.rankedReservation = response.data.rankedReservation; // 예약 랭킹 데이터
            this.rankedPayment = response.data.rankedPayment; // 매출 랭킹 데이터
            this.monthlySalesGrowth = response.data.monthlySalesGrowth; // 매출 증감율 데이터
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
        // 주간 예약 현황
        const weekSeriesData = this.weekReservations.map((reservation) => ({
          name: `시설 ${reservation.fctId}`, // 시설명
          data: [
            reservation.sun || 0,
            reservation.mon || 0,
            reservation.tues || 0,
            reservation.wednes || 0,
            reservation.thurs || 0,
            reservation.fri || 0,
            reservation.satur || 0
          ],
          lineWidth: 2, // 선 두께
        }));
  
        // 매출 증감율 계산
        const growthRate = this.monthlySalesGrowth.growthRate; // 매출 증감율
        const totalSales = this.monthlySalesGrowth.totalSales; // 이번 달 총 매출
  
        let arrow = "";
        let arrowColor = "";
        if (growthRate > 0) {
          arrow = "↑"; // 위 화살표
          arrowColor = "green"; // 초록색
        } else if (growthRate < 0) {
          arrow = "↓"; // 아래 화살표
          arrowColor = "red"; // 빨간색
        } else {
          arrow = "→"; // 변화 없음
          arrowColor = "gray"; // 회색
        }
  
        this.arrow = arrow; // 화살표
        this.arrowColor = arrowColor; // 화살표 색상
        this.growthRate = growthRate; // 매출 증감율
        this.totalSales = totalSales; // 이번 달 총 매출
  
        // 주간 예약 현황 선 차트
        Highcharts.chart("week-reservation-chart", {
          title: {
            text: "주간 요일별 예약 현황"
          },
          xAxis: {
            categories: ["일", "월", "화", "수", "목", "금", "토"], // 요일 설정
            title: { text: "요일" }
          },
          yAxis: {
            title: {
              text: "예약 건수"
            }
          },
          series: weekSeriesData,
          tooltip: {
            shared: true,
            valueSuffix: " 건"
          },
          credits: { enabled: false },
          exporting: { enabled: true }
        });
  
        // 시설별 예약/취소/매출 현황 복합 막대 그래프
        Highcharts.chart("facility-stats-chart", {
          chart: {
            type: 'column'
          },
          title: {
            text: "시설별 예약/예약취소/매출 현황"
          },
          xAxis: {
            categories: this.totalReservations.map(stat => stat.fctName),
            title: { text: "시설" },
            labels: {
              style: {
                width: '120px',
                whiteSpace: 'normal',
                textOverflow: 'ellipsis',
              },
              rotation: -45
            }
          },
          yAxis: [
            { title: { text: "예약 건수" } },
            { title: { text: "매출 (원)" }, opposite: true }
          ],
          series: [
            {
              name: "시설별 예약 건수",
              data: this.totalReservations.map(stat => stat.totalReservations),
              pointWidth: 30
            },
            {
              name: "시설별 취소된 예약 건수",
              data: this.cancelledReservations.map(stat => stat.cancelledReservations),
              pointWidth: 30
            },
            {
              name: "시설별 매출",
              data: this.totalRevenue.map(stat => stat.totalRevenue),
              type: 'line',
              yAxis: 1
            }
          ],
          tooltip: {
            shared: true,
            valueSuffix: " 건"
          },
          credits: { enabled: false },
          exporting: { enabled: true }
        });
  
        // 예약 랭킹 차트
        const rankedData = this.rankedReservation.map((stat) => ({
          name: stat.fctName,
          y: stat.totRvt
        }));
  
        Highcharts.chart("ranking-chart", {
          chart: {
            type: 'pie'
          },
          title: {
            text: "이번 달 시설 예약 랭킹 TOP 5"
          },
          series: [{
            name: "예약 건수",
            data: rankedData,
            size: '80%',
            innerSize: '60%',
            dataLabels: {
              formatter: function () {
                return `${this.point.name}: ${this.point.y} 건`;
              }
            }
          }],
          credits: { enabled: false },
          exporting: { enabled: true }
        });
  
        // 매출 랭킹 차트
        const paymentRankedData = this.rankedPayment.map((stat) => ({
          name: stat.fctName,
          y: stat.totPay
        }));
  
        Highcharts.chart("payment-ranking-chart", {
          chart: {
            type: 'pie'
          },
          title: {
            text: "이번 달 시설 매출 랭킹 TOP 5"
          },
          series: [{
            name: "매출 금액",
            data: paymentRankedData,
            size: '80%',
            innerSize: '60%',
            dataLabels: {
              formatter: function () {
                return `${this.point.name}: ${this.point.y} 원`;
              }
            }
          }],
          credits: { enabled: false },
          exporting: { enabled: true }
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
  justify-content: space-between; /* 요소들을 양쪽 끝으로 정렬 */
  align-items: center; /* 세로 중앙 정렬 */
  gap: 20px; /* 요소 간격 */
  margin-bottom: 20px;
  flex-wrap: wrap; /* 화면이 좁을 경우 줄 바꿈 허용 */
}

.chart-large {
  flex-grow: 1; /* 가변 크기 조정 */
  width: 700px;
  height: 300px;
}

.chart-small {
  width: 350px;
  height: 150px;
  padding: 20px;
  background-color: #f5f5f5;
  border: 1px solid #ccc;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

  
  .growth-rate-title {
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 10px;
  }
  
  .growth-rate-content {
    font-size: 20px;
    display: flex;
    flex-direction: column;
  }
  
  .growth-rate-main {
    display: flex;
    align-items: center;
  }
  
  .growth-rate-value {
    font-size: 36px;
    font-weight: bold;
    margin-right: 15px;
  }
  
  .growth-rate-details {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
  }
  
  .growth-rate-label {
    font-size: 14px;
    color: gray;
    margin-bottom: 2px;
  }
  
  .growth-rate-numbers {
    display: flex;
    align-items: center;
  }
  
  .growth-rate-percentage {
    font-size: 24px;
    font-weight: bold;
    margin-right: 5px;
  }
  
  .growth-rate-arrow {
    font-size: 24px;
    font-weight: bold;
    margin-left: 5px;
  }
  
  .ranking-container {
    display: flex;
    justify-content: center;
    gap: 20px;
    margin-bottom: 20px;
  }
  
  .ranking-smaller {
    width: 35%;
    min-width: 300px;
    height: 240px;
  }
  </style>
  