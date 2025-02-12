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
			<p class="profile-content w-75 mx-auto px-3">~{{ rvtInfo.rvtDate }} {{ rvtInfo.cancelableTime }}</p>
		</div>
		<div class="text-center mt-4 mb-3" v-if="isCancelable && !showCancelationReason">
			<button class="signup-btn w-75 mb-3 rounded-pill" @click="showCancelationReason = true">
				{{$t('reservation.cancelReservation') }}	
			</button>
		</div>
		<hr v-if="showCancelationReason" />
		<div class="mb-4 ms-4 w-75" v-if="showCancelationReason">
			<label for="cancelationReason" class="form-label text-secondary">{{ $t('reservation.cancelationReason') }}</label>
			<select id="cancelReason" class="form-select mb-3 ms-3" v-model="rvtCancelationReason">
				<option disabled selected value="">{{ $t('reservation.selectReason') }}</option>
				<option value="CHANGE">{{ $t('reservation.changeOfMind') }}</option>
				<option value="MISTAKE">{{ $t('reservation.incorrectReservation') }}</option>
				<option value="DOUBLE">{{ $t('reservation.duplcateReservation') }}</option>
				<option value="PAYMENT">{{ $t('reservation.paymentIssue') }}</option>
				<option value="WEATHER">{{ $t('reservation.weatherIssue') }}</option>
				<option value="FACILITY">{{ $t('reservation.facilityUnavailable') }}</option>
			</select>
			<div class="mb-3">
                <p class="text-secondary">{{ $t('payment.refundBankCode') }}</p>
				<select class="form-select ms-3" v-model="refundBankCode">
					<option disabled selected value="">{{ $t('signup.selectBank') }}</option>
					<option v-for="bank in bankList" :key="bank.bankCode" :value="bank.bankCode">{{ bank.bankName }}</option>
				</select>
            </div>
			<div class="mb-3">
                <p class="text-secondary">{{ $t('payment.refundAccount') }}</p>
                <input type="text" class="signup-input w-100 text-secondary fs-5 ms-3" v-model="refundAccount" />
            </div>
            <div class="mb-4">
                <p class="text-secondary">{{ $t('payment.refundAccountOwner') }}</p>
                <input type="text" class="signup-input w-100 text-secondary fs-5 ms-3" v-model="refundAccountOwner" />
            </div>
			<div class="text-center mx-auto mb-3">
				<button class="signup-btn w-75 mb-3 rounded-pill" @click="submitCancelation">{{ $t('reservation.cancelReservation')}}</button>
			</div>
		</div>
		<div class="text-center mt-5 mb-3" v-if="rvtInfo.rvtState == '이용 완료'">
			<textarea class="form-control mb-3" placeholder="신고할 내용을 입력해주세요."></textarea>
			<button class="signup-btn w-75 mb-3 rounded-pill" @click="">
				{{ $t('button.report')}}
			</button>
		</div>
	</div>
</template>

<script>
import { get, put } from "../../apis/axios";
export default {
	name: "DetailReservation",
	props: ['id'],
	data() {
		return {
			rvtInfo: [],
			showCancelationReason: false,
			rvtCancelationReason: "",
			bankList: [],
			refundBankCode: "",			
			refundAccount: "",
			refundAccountOwner: ""
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
			if (this.rvtCancelationReason && this.refundBankCode && this.refundAccount && this.refundAccountOwner) {
				await put(`/reservations/${this.id}`, {
					rvtCancelationReason: this.rvtCancelationReason,
					refundBankCode: this.refundBankCode,
					refundAccount: this.refundAccount,
					refundAccountOwner: this.refundAccountOwner
				}, {
					headers: {
						"Content-Type": "application/json"
					}
				});
				this.$router.push('/reservation/list');
			} else {
				alert(this.$t("signupError.emptyInput"));
			}
		},
		async getBankList(){
			const response = await get('/banks');
			this.bankList = response.data.content;
		}
	},
	async mounted() {
		await this.getRvtInfo();
		if(this.isCancelable){
			this.getBankList();
		}
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