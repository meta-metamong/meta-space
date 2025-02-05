<template>
	<div class="container w-100 mt-4">
		<h2 class="text-center mb-3" v-text="$t('reservation.list')"></h2>
        <div class="card mb-4" v-for="rvt in rvtInfo" :key="rvt.rvtId">
            <div class="card-body">
                <div class="mb-4"><h4>배드민턴장</h4></div>
                <div class="d-flex align-items-center mb-4">
					<div class="col-sm-4 me-4">
						<span class="info-label">{{ $t('reservation.id') }}</span>
					</div>
					<div class="col-sm-8">
						<span>{{ rvt.rvtId }}</span>
					</div>
				</div>
                <div class="d-flex align-items-center mb-4">
					<div class="col-sm-4 me-4">
						<span class="info-label">{{ $t('reservation.dateTime') }}</span>
					</div>
					<div class="col-sm-8">
						<span>{{ rvt.rvtDate }} {{ rvt.usageStartTime }}~{{ rvt.usageEndTime }}</span>
					</div>
				</div>
                <div class="d-flex align-items-center mb-4">
					<div class="col-sm-4 me-4">
						<span class="info-label">Zone</span>
					</div>
					<div class="col-sm-8">
						<span>{{ rvt.zoneName }}</span>
					</div>
				</div>
                <div class="d-flex align-items-center mb-4">
					<div class="col-sm-4 me-4">
						<span class="info-label">{{ $t('reservation.cancelableDate') }}</span>
					</div>
					<div class="col-sm-8">
						<span>{{ rvt.rvtDate }} {{ rvt.usageStartTime }}까지</span>
					</div>
				</div>
                <div class="d-flex align-items-center">
					<div class="col-sm-4 me-4">
						<span class="info-label">{{ $t('reservation.state') }}</span>
					</div>
					<div class="col-sm-8">
						<span>{{ rvt.state }}</span>
					</div>
				</div>
            </div>
        </div>
	</div>
</template>

<script>
import { get } from "../apis/axios";
import { toRaw } from 'vue';
export default {
    name: "Reservation",
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
            // const response = await get(`/members/${this.user.userId}`);
            const response = await get(`/members/1/reservations`);
            this.rvtInfo = response.data.content;
			console.log(this.rvtInfo)
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