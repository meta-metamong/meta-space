<template>
    <div class="container mt-2 d-flex justify-content-center">
        <div id="highchart-container"></div>
    </div>
</template>

<script>
import { get } from '../../apis/axios';
import Highcharts from "highcharts";
export default {
    name: "LineChart",
    data() {
        return {
            yearMonthPurchase: [],
        }
    },
    methods: {
        async fetchData() {
            // this.yearMonthPurchase = await get('http://localhost:8080/api/purchases');
        },
        drawChart() {
            Highcharts.chart("highchart-container", {
                title: {
                    text: "월별 판매 금액",
                },
                xAxis: {
                    // categories: this.yearMonthPurchase.map(item => item.yearMonth),
                    categories: ["2025/01", "2024/12", "2024/11", "2024/10", "2024/09"],
                },
                yAxis: {
                    title: {
                        text: "Value",
                    },
                },
                series: [
                    {
                        name: "총 판매 금액",
                        // data: this.yearMonthPurchase.map(item => item.totalAmount),
                        data: [1058000, 928000, 1183000, 932000, 665000],
                    },
                ],
                exporting: {
                    enabled: true, // 내보내기 활성화
                },
                accessibility: {
                    enabled: false, // 접근성 기능 비활성화
                },
            });
        }
    },
    mounted() {
        this.fetchData();
        this.drawChart();
    },
};
</script>

<style>
#highchart-container {
    margin: 20px auto;
}
</style>