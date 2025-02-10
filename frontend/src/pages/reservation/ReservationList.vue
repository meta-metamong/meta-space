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
		<div class="d-flex justify-content-center">
			<Pagination :current-page="currentPage"
						:start-page-no="startPageNo"
						:end-page-no="endPageNo"
						:is-previous-available="startPageNo !== 1"
						:is-next-available="endPageNo < totalPages"
						@click-page-item="changePage"
						@click-previous="() => { startPageNo -= 5; endPageNo -= 5 }"
						@click-next="() => { startPageNo += 5; Math.min(endPageNo + 5, totalPages) }" />
		</div>
	</div>
</template>

<script>
import { get } from "../../apis/axios";
import Pagination from "../../components/common/Pagination.vue";
export default {
	name: "ReservationList",
	components: {
		Pagination
	},
	data() {
		return {
			rvtInfo: [],
			currentPage: 1,
            startPageNo: 1,
			totalPages: 10,
			pageSize: 1,
		};
	},
	computed: {
		endPageNo() {
			return Math.min(this.startPageNo + 4, this.totalPages);
		}
	},
	methods: {
		async getRvtInfo(page = 1) {
			const response = await get(`/members/${this.$store.state.userId}/reservations?page=${page}`);
			const content = response.data.content;
			this.rvtInfo = content.rvtInfo;
			this.currentPage = content.currentPage;
			this.totalPages = content.totalPages;
			this.pageSize = content.pageSize;
		},
		changePage(page) {
			if (page !== this.currentPage) {
				this.getRvtInfo(page);
			}
		},
	},
	mounted() {
		this.getRvtInfo();
	},
};
</script>

<style scoped>

</style>