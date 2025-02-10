<template>
	<div class="container w-100 mt-4">
		<h2 class="text-center mb-4" v-text="$t('reservation.list')"></h2>
		<div class="col-md-6 mb-4" v-for="rvt in rvtInfo" :key="rvt.rvtId">
			<div class="card" @click="$router.push(`/reservation/${rvt.rvtId}`)">
				<div class="card-header fw-bold"><h5 class="mb-1 mt-1">{{ rvt.fctName }}</h5></div>
				<div class="card-body">
					<table class="table table-borderless mb-0">
						<tbody>
							<tr>
								<td class="fw-bold" style="width: 36%;">{{ $t('reservation.id') }}</td>
								<td>{{ rvt.rvtId }}</td>
							</tr>
							<tr>
								<td class="fw-bold">{{ $t('reservation.dateTime') }}</td>
								<td>{{ rvt.rvtDate }} {{ rvt.usageStartTime }}~{{ rvt.usageEndTime }}</td>
							</tr>
							<tr>
								<td class="fw-bold">Zone</td>
								<td>{{ rvt.zoneName }}</td>
							</tr>
							<tr>
								<td class="fw-bold">{{ $t('reservation.cancelableDate') }}</td>
								<td>{{ rvt.rvtDate }} {{ rvt.usageStartTime }}까지</td>
							</tr>
							<tr>
								<td class="fw-bold">{{ $t('reservation.state') }}</td>
								<td>{{ rvt.rvtState }}</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import { get } from "../../apis/axios";
import { toRaw } from 'vue';
export default {
	name: "ReservationList",
	data() {
		return {
			rvtInfo: [],
		};
	},
	computed: {
		user() {
			return toRaw(this.$store.state.user);
		},
	},
	methods: {
		async getRvtInfo() {
			const response = await get(`/members/3/reservations`);
			this.rvtInfo = response.data.content;
		},
	},
	mounted() {
		this.getRvtInfo();
	},
};
</script>

<style scoped>

</style>