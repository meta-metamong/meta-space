<template>
    <div class="container mt-2 d-flex justify-content-center">
        <div id="highchart-container"></div>
    </div>
</template>

<script>
import { get } from '../../apis/axios';
import Highcharts from "highcharts";
export default {
    name: "PieChart",
    data() {
        return {
            pieChartData: [],
        }
    },
    methods: {
        async fetchData() {
            // const response = await get('http://localhost:8080/api/purchases');
            // this.pieChartData = response.map((item) => ({
            //     name: item.name,
            //     y: item.value,
            // }));
            this.pieChartData = [
                { name: "Water", y: 55.02 },
                { name: "Fat", sliced: true, selected: true, y: 26.71 },
                { name: "Carbohydrates", y: 1.09 },
                { name: "Protein", y: 15.5 },
                { name: "Ash", y: 1.68 },
            ];
        },
        drawChart() {
            Highcharts.chart("highchart-container", {
                chart: {
                    type: 'pie'
                },
                title: {
                    text: "연령대별 판매량",
                },
                tooltip: {
                    valueSuffix: '%'
                },
                subtitle: {
                    text:
                    'Source:<a href="https://www.mdpi.com/2072-6643/11/3/684/htm" target="_default">MDPI</a>'
                },
                plotOptions: {
                    series: {
                        cursor: 'pointer',
                        dataLabels: [{
                            enabled: true,
                            distance: 20
                        }, {
                            enabled: true,
                            distance: -40,
                            format: '{point.percentage:.1f}%',
                            style: {
                                fontSize: '1.2em',
                                textOutline: 'none',
                                opacity: 0.7
                            },
                            filter: {
                                operator: '>',
                                property: 'percentage',
                                value: 10
                            }
                        }]
                    }
                },
                series: [
                    {
                        name: 'Percentage',
                        colorByPoint: true,
                        data: this.pieChartData,
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