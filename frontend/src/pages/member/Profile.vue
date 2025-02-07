<template>
	<div class="container w-100 mt-4">
		<h2 class="text-center mb-3" v-text="$t('member.profile')"></h2>
		<div class="text-center mb-3">
            <img src="https://cdn.pixabay.com/photo/2023/02/18/11/00/icon-7797704_1280.png" alt="Profile Image" class="profile-img">
        </div>
        <div class="mb-4">
            <p class="ms-4 text-secondary">{{ $t('member.email') }}</p>
            <p class="profile-content w-75 mx-auto px-3 fs-5">{{ memberInfo.email }}</p>
        </div>
        <div class="mb-4">
            <p class="ms-4 text-secondary">{{ $t('member.name') }}</p>
            <p class="profile-content w-75 mx-auto px-3 fs-5">{{ memberInfo.memName }}</p>
        </div>
        <div class="mb-4">
            <p class="ms-4 text-secondary">{{ $t('member.phone') }}</p>
            <p class="profile-content w-75 mx-auto px-3 fs-5">{{ memberInfo.memPhone }}</p>
        </div>
        <div class="mb-4">
            <p class="ms-4 text-secondary">{{ $t('member.birth') }}</p>
            <p class="profile-content w-75 mx-auto px-3 fs-5">{{ memberInfo.birthDate }}</p>
        </div>
        <div class="mb-4">
            <p class="ms-4 text-secondary">{{ $t('member.gender') }}</p>
            <p class="profile-content w-75 mx-auto px-3 fs-5">{{ gender }}</p>
        </div>
        <div class="mb-4">
            <p class="ms-4 text-secondary">{{ $t('member.address') }}</p>
            <p class="profile-content w-75 mx-auto px-3">{{ memberInfo.memAddress }}</p>
            <p class="profile-content w-75 mx-auto px-3">{{ memberInfo.memDetailAddress }}</p>
        </div>
        <div v-if="memberInfo.role==='ROLE_PROV'">
            <div class="mb-4">
                <p class="ms-4 text-secondary">{{ $t('member.businessName') }}</p>
                <p class="profile-content w-75 mx-auto px-3 fs-5">{{ memberInfo.bizName }}</p>
            </div>
            <div class="mb-4">
                <p class="ms-4 text-secondary">{{ $t('member.businessNumber') }}</p>
                <p class="profile-content w-75 mx-auto px-3 fs-5">{{ memberInfo.bizRegNum }}</p>
            </div>
            <div class="mb-4">
                <p class="ms-4 text-secondary">{{ $t('member.bank') }}</p>
                <p class="profile-content w-75 mx-auto px-3 fs-5">{{ memberInfo.bankCode }}</p>
            </div>
            <div class="mb-4">
                <p class="ms-4 text-secondary">{{ $t('member.account') }}</p>
                <p class="profile-content w-75 mx-auto px-3 fs-5">{{ memberInfo.provAccount }}</p>
            </div>
            <div class="mb-4">
                <p class="ms-4 text-secondary">{{ $t('member.accountOwner') }}</p>
                <p class="profile-content w-75 mx-auto px-3 fs-5">{{ memberInfo.provAccountOwner }}</p>
            </div>
        </div>
        <div class="w-100 text-center mb-2">
            <button class="signup-btn w-75 h-75 mb-3 rounded-pill" @click="$router.push('/update')">{{ $t('button.update') }}</button>
            <button class="signup-btn w-75 h-75 mb-3 rounded-pill" @click="$router.push('/confirm-pw/change')">{{ $t('member.changePw') }}</button>
            <button class="signup-btn exit-btn w-75 h-75 rounded-pill" @click="$router.push('/confirm-pw/exit')">{{ $t('member.exit') }}</button>
        </div>
	</div>
</template>

<script>
import { del, get } from "../../apis/axios";
export default {
    name: "Profile",
    data() {
        return {
            memberInfo: [],
        };
    },
    computed: {
        gender(){
            return this.memberInfo.gender === 'M' ? this.$t('male') : this.$t('member.female');
        }
    },
    methods: {
        async getMemberInfo() {
            const response = await get(`/members/${this.$store.state.userId}`);
            this.memberInfo = response.data.content;
        }
    },
    mounted() {
        this.getMemberInfo();
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