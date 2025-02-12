<template>
	<div class="container w-100 mt-4">
		<h2 class="text-center mb-4" v-text="$t('reservation.reservationDetails')"></h2>
		<div class="mb-4">
			<p class="ms-4 text-secondary">{{ $t('reservation.id') }}</p>
			<p class="profile-content w-75 mx-auto px-3">{{ id }}</p>
		</div>
		<div class="mb-4">
			<p class="ms-4 text-secondary">{{ $t('reservation.fctName') }}</p>
			<p class="profile-content w-75 mx-auto px-3">{{ rvtInfo.fctName }}</p>
		</div>
		<div class="mb-4">
			<p class="ms-4 text-secondary">Zone</p>
			<p class="profile-content w-75 mx-auto px-3">{{ rvtInfo.zoneName }}</p>
		</div>
		<div class="mb-4">
			<p class="ms-4 text-secondary">{{ $t('reservation.address') }}</p>
			<p class="profile-content w-75 mx-auto px-3">{{ rvtInfo.fctAddress }} {{ rvtInfo.fctDetailAddress }}
			</p>
		</div>
		<div class="mb-4">
			<p class="ms-4 text-secondary">{{ $t('reservation.reservationDate') }}</p>
			<p class="profile-content w-75 mx-auto px-3">{{ rvtInfo.rvtDate }}</p>
		</div>
		<div class="mb-4">
			<p class="ms-4 text-secondary">{{ $t('reservation.reservationTime') }}</p>
			<p class="profile-content w-75 mx-auto px-3">{{ rvtInfo.usageStartTime }}~{{ rvtInfo.usageEndTime }}
			</p>
		</div>
		<div class="mb-4">
			<p class="ms-4 text-secondary">{{ $t('reservation.usageCount') }}</p>
			<p class="profile-content w-75 mx-auto px-3">{{ rvtInfo.usageCount }}</p>
		</div>
		<div class="mb-4">
			<p class="ms-4 text-secondary">{{ $t('reservation.state') }}</p>
			<p class="profile-content w-75 mx-auto px-3">{{ rvtInfo.rvtState }}</p>
		</div>
		<div class="mb-4">
			<p class="ms-4 text-secondary">{{ $t('reservation.cancelableDate') }}</p>
			<p class="profile-content w-75 mx-auto px-3">{{ rvtInfo.rvtDate }} {{ rvtInfo.cancelableTime }}까지</p>
		</div>
		<div class="text-center mt-4 mb-3" v-if="isCancelable">
			<button class="signup-btn cancel-btn w-75 mb-3 rounded-pill" @click="showCancelationReason = true">{{
				$t('reservation.cancelReservation') }}</button>
		</div>
		<div class="mb-3 m-4" v-if="showCancelationReason">
			<label for="cancelationReason" class="form-label">{{ $t('reservation.selectReason') }}</label>
			<select id="cancelReason" class="form-select" v-model="rvtCancelationReason">
				<option disabled value="">{{ $t('reservation.cancelationReason') }}</option>
				<option value="CHANGE">{{ $t('reservation.changeOfMind') }}</option>
				<option value="MISTAKE">{{ $t('reservation.incorrectReservation') }}</option>
				<option value="DOUBLE">{{ $t('reservation.duplcateReservation') }}</option>
				<option value="PAYMENT">{{ $t('reservation.paymentIssue') }}</option>
				<option value="WEATHER">{{ $t('reservation.weatherIssue') }}</option>
				<option value="FACILITY">{{ $t('reservation.facilityUnavailable') }}</option>
			</select>
			<div class="text-center mt-2 mb-3">
				<button class="signup-btn w-75 mb-3 rounded-pill" @click="submitCancelation">{{ $t('button.check')
					}}</button>
			</div>
		</div>
		<div class="text-center mt-5 mb-3" v-if="rvtInfo.rvtState == '이용 완료'">
			<textarea class="form-control mb-3" placeholder="신고할 내용을 입력해주세요."></textarea>
			<button class="signup-btn w-75 mb-3 rounded-pill" @click="">{{ $t('button.report')
				}}</button>
		</div>
	</div>
</template>

<script>
import { get, put } from "../../apis/axios";
import { toRaw } from 'vue';
export default {
	name: "DetailReservation",
	props: ['id'],
	data() {
		return {
			rvtInfo: [],
			showCancelationReason: false,
			rvtCancelationReason: "",
		};
	},
	computed: {
		isCancelable() {
			const now = new Date();
			const cancelableUntilString = `${this.rvtInfo.rvtDate}T${this.rvtInfo.cancelableTime}`;
			const cancelableUntil = new Date(cancelableUntilString);
			return now < cancelableUntil && this.rvtInfo.rvtState != '예약 취소';
		}
	},
	methods: {
		async getRvtInfo() {
			const response = await get(`/reservations/${this.id}`);
			this.rvtInfo = response.data.content;
		},
		async submitCancelation() {
			if (this.rvtCancelationReason) {
				await put(`/reservations/${this.id}`, {
					rvtCancelationReason: this.rvtCancelationReason
				}, {
					headers: {
						"Content-Type": "application/json"
					}
				});
				window.location.reload();
			} else {
				alert(this.$t("reservation.selectReason"));
			}
		}
	},
	mounted() {
		this.getRvtInfo();
	},
};
</script>

<style scoped>
.profile-content {
	border-bottom: 1px solid #999;
}

textarea {
	height: 100px;
}

.cancel-btn {
	background: #ec7461;
	border: 1px solid #fff;
	color: #fff;
}
</style>