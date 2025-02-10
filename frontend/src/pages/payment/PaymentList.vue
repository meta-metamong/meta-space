<template>
	<div class="container w-100 mt-4">
		<h2 class="text-center mb-4" v-text="$t('payment.list')"></h2>
		<div class="col-md-6 mb-4" v-for="pay in payInfo" :key="pay.rvtId">
			<router-link to="#" class="card">
				<div class="card-header text-white fw-bold" :class="getHeaderColor(pay.payState)"><h5 class="mb-1 mt-1">{{ $t(`payment.${pay.payState}`) }}</h5></div>
				<div class="card-body">
					<table class="table table-borderless mb-0">
						<tbody>
							<tr>
								<td class="fw-bold" style="width: 36%;">{{ $t('payment.payId') }}</td>
								<td>{{ pay.rvtId }}</td>
							</tr>
							<tr>
								<td class="fw-bold">{{ $t('payment.payPrice') }}</td>
								<td>{{ pay.payPrice }}</td>
							</tr>
							<tr>
								<td class="fw-bold">{{ $t('payment.payMethod') }}</td>
								<td>{{ pay.payMethod }}</td>
							</tr>
							<tr>
								<td class="fw-bold">{{ $t('payment.payDate') }}</td>
								<td>{{ pay.payDate }}</td>
							</tr>
						</tbody>
					</table>
				</div>
			</router-link>
		</div>
	</div>
</template>

<script>
import { get } from "../../apis/axios";

export default {
	name: "PaymentList",
	data() {
		return {
			payInfo: [],
		};
	},
	computed: {
		user() {
			return this.$store.state.userId;
		},
	},
	methods: {
		async getPayInfo() {
			const response = await get(`/payments`);
            console.log(response);
			this.payInfo = response.data.content;
		},
        getHeaderColor(payState){
            switch(payState){
                case "P":
                    return "bg-primary";
                case "Q":
                    return "bg-warning";
                default:
                    return "bg-success";
            }
        }
	},
	mounted() {
		this.getPayInfo();
	},
};
</script>

<style scoped>
*{
    text-decoration: none;
}
</style>