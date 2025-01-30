<template>
	<div class="container mt-4 box">
		<div class="d-flex flex-column gap-3 flex-md-row justify-content-center">
			<LineChart />
			<!-- <PieChart /> -->
		</div>
	</div>
</template>

<script>

import LineChart from '../components/chart/LineChart.vue';
import PieChart from '../components/chart/PieChart.vue';

export default{
    name: "UserStatistics",
    components:{
        LineChart,
		PieChart,
	}
}
</script>