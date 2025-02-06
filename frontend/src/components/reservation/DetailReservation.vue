<template>
	<div class="container w-100 mt-4">
		<h2 class="text-center mb-3" v-text="$t('reservation.reservationDetails')"></h2>
		<div class="d-flex align-items-center mb-4">
			<div class="col-sm-4 me-4">
				<span class="info-label">{{ $t('reservation.id') }}</span>
			</div>
			<div class="col-sm-8">
				<span>{{ id }}</span>
			</div>
		</div>
		<div class="d-flex align-items-center mb-4">
			<div class="col-sm-4 me-4">
				<span class="info-label">{{ $t('reservation.fctName') }}</span>
			</div>
			<div class="col-sm-8">
				<span>{{ rvtInfo.fctName }}</span>
			</div>
		</div>
		<div class="d-flex align-items-center mb-4">
			<div class="col-sm-4 me-4">
				<span class="info-label">Zone</span>
			</div>
			<div class="col-sm-8">
				<span>{{ rvtInfo.zoneName }}</span>
			</div>
		</div>
		<div class="d-flex align-items-center mb-4">
			<div class="col-sm-4 me-4">
				<span class="info-label">{{ $t('reservation.address') }}</span>
			</div>
			<div class="col-sm-8">
				<span>{{ rvtInfo.fctAddress }} {{ rvtInfo.fctDetailAddress }}</span>
			</div>
		</div>
		<div class="d-flex align-items-center mb-4">
			<div class="col-sm-4 me-4">
				<span class="info-label">{{ $t('reservation.reservationDate') }}</span>
			</div>
			<div class="col-sm-8">
				<span>{{ rvtInfo.rvtDate }}</span>
			</div>
		</div>
		<div class="d-flex align-items-center mb-4">
			<div class="col-sm-4 me-4">
				<span class="info-label">{{ $t('reservation.reservationTime') }}</span>
			</div>
			<div class="col-sm-8">
				<span>{{ rvtInfo.usageStartTime }}~{{ rvtInfo.usageEndTime }}</span>
			</div>
		</div>
		<div class="d-flex align-items-center mb-4">
			<div class="col-sm-4 me-4">
				<span class="info-label">{{ $t('reservation.usageCount') }}</span>
			</div>
			<div class="col-sm-8">
				<span>{{ rvtInfo.usageCount }}</span>
			</div>
		</div>
		<div class="d-flex align-items-center mb-4">
			<div class="col-sm-4 me-4">
				<span class="info-label">{{ $t('reservation.state') }}</span>
			</div>
			<div class="col-sm-8">
				<span>{{ rvtInfo.rvtState }}</span>
			</div>
		</div>
		<div class="d-flex align-items-center">
			<div class="col-sm-4 me-4">
				<span class="info-label">{{ $t('reservation.cancelableDate') }}</span>
			</div>
			<div class="col-sm-8">
				<span>{{ rvtInfo.rvtDate }} {{ rvtInfo.usageStartTime }}까지</span>
			</div>
		</div>
		<div class="text-center mt-4"><button class="btn btn-primary">취소하기</button></div>
	</div>
</template>

<script>
import { get } from "../../apis/axios";
import { toRaw } from 'vue';
export default {
    name: "DetailReservation",
	props: ['id'],
    data() {
        return {
            rvtInfo: [],
        };
    },
    computed: {
        user() {
            return toRaw(this.$store.state.user);
        },
        address() {
            return `(${this.rvt.postalCode}) ${this.rvt.address} ${this.rvt.detailAddress}`;
        }
    },
    methods: {
        async getRvtInfo() {
            const response = await get(`/reservations/${this.id}`);
            this.rvtInfo = response.data.content;
        },
        route(page){
			this.$router.push(page);
		},
    },
    mounted() {
        this.getRvtInfo();
    },
};
</script>

<style scoped>
.info-item {
	margin-bottom: 15px;
}
.info-label {
	font-weight: bold;
	/* margin-bottom: 5px; */
	display: block;
}
</style>