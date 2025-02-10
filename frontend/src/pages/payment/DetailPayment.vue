<template>
	<div class="container w-100 mt-4">
        <h2 class="text-center mb-4" v-text="$t('payment.detail')"></h2>
        <div class="mb-4">
            <p class="ms-4 text-secondary">{{ $t('payment.payId') }}</p>
            <p class="profile-content w-75 mx-auto px-3 fs-5">{{ payInfo.rvtId }}</p>
        </div>
        <div class="mb-4">
            <p class="ms-4 text-secondary">{{ $t('payment.payPrice') }}</p>
            <p class="profile-content w-75 mx-auto px-3 fs-5">{{ payInfo.payPrice }}</p>
        </div>
        <div class="mb-4">
            <p class="ms-4 text-secondary">{{ $t('payment.payMethod') }}</p>
            <p class="profile-content w-75 mx-auto px-3 fs-5">{{ payInfo.payMethod }}</p>
        </div>
        <div class="mb-4">
            <p class="ms-4 text-secondary">{{ $t('payment.payDate') }}</p>
            <p class="profile-content w-75 mx-auto px-3 fs-5">{{ payInfo.payDate}}</p>
        </div>
        <div class="mb-4">
            <p class="ms-4 text-secondary">{{ $t('payment.payState') }}</p>
            <p class="profile-content w-75 mx-auto px-3 fs-5">{{ $t(`payment.${payInfo.payState}`) }}</p>
        </div>
        <div v-if="payInfo.payState!=='P'">
            <div class="mb-4">
                <p class="ms-4 text-secondary">{{ $t('payment.refundBankCode') }}</p>
                <p class="profile-content w-75 mx-auto px-3 fs-5">{{ payInfo.refundBankCode }}</p>
            </div>
            <div class="mb-4">
                <p class="ms-4 text-secondary">{{ $t('payment.refundAccount') }}</p>
                <p class="profile-content w-75 mx-auto px-3 fs-5">{{ payInfo.refundAccount }}</p>
            </div>
            <div class="mb-4">
                <p class="ms-4 text-secondary">{{ $t('payment.refundAccountOwner') }}</p>
                <p class="profile-content w-75 mx-auto px-3 fs-5">{{ payInfo.refundAccountOwner }}</p>
            </div>
            <div class="mb-4">
                <p class="ms-4 text-secondary">{{ $t('payment.rvtCancelationReason') }}</p>
                <p class="profile-content w-75 mx-auto px-3 fs-5">{{ payInfo.rvtCancelationReason }}</p>
            </div>
        </div>
        <div class="w-100 text-center mt-2 mb-2 pt-3">
            <button class="signup-btn w-75 mb-3 rounded-pill" @click="$router.push('/payment/list')">{{ $t('button.check') }}</button>
        </div>
	</div>
</template>

<script>
import { get } from "../../apis/axios";
export default {
    name: "DetailPayment",
    props: ['rvtId'],
    data() {
        return {
            payInfo: [],
        };
    },
    methods: {
        async getPaymentInfo() {
            const response = await get(`/payments/${this.rvtId}`);
            this.payInfo = response.data.content;
        }
    },
    mounted() {
        this.getPaymentInfo();
    },
};
</script>

<style scoped>
.profile-img {
	width: 100px;
	height: 100px;
    object-fit: cover;
    border-radius: 50%;
    border: 3px solid #fff;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

.exit-btn{
    background: #ec7461;
    border: 1px solid #fff;
    color: #fff;
}

.profile-content{
    border-bottom: 1px solid #999;
}
</style>