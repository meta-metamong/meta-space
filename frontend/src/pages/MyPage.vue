<template>
	<div class="container w-100 mt-4">
		<h2 class="text-center mb-3" v-text="$t('member.profile')"></h2>
		<div class="text-center">
            <img src="https://cdn.pixabay.com/photo/2023/02/18/11/00/icon-7797704_1280.png" alt="Profile Image" class="profile-img">
        </div>
        <div class="profile-item">
            <span class="profile-label">{{ $t('member.email') }}</span>
            <span>{{ memberInfo.email }}</span>
        </div>
        <div class="profile-item">
            <span class="profile-label">{{ $t('member.name') }}</span>
            <span>{{ memberInfo.name }}</span>
        </div>
        <div class="profile-item">
            <span class="profile-label">{{ $t('member.phoneNumber') }}</span>
            <span>{{ memberInfo.phone }}</span>
        </div>
        <div class="profile-item">
            <span class="profile-label">{{ $t('member.birth') }}</span>
            <span>{{ memberInfo.birth }}</span>
        </div>
        <div class="profile-item">
            <span class="profile-label">{{ $t('member.gender') }}</span>
            <span>남</span>
        </div>
        <div class="profile-item">
            <span class="profile-label">{{ $t('member.address') }}</span>
            <span>{{ address }}</span>
        </div>
        <div class="profile-item">
            <span class="profile-label">{{ $t('member.membershipDate') }}</span>
            <span>2025-02-19</span>
        </div>
		<button class="btn btn-primary mt-3" @click="route('/update')">{{ $t('button.save') }}</button>
	</div>
</template>

<script>
import { get } from "../apis/axios";
import { toRaw } from 'vue';
export default {
    name: "MyPage",
    data() {
        return {
            memberInfo: [],
        };
    },
    computed: {
        user() {
            return toRaw(this.$store.state.user);
        },
        address() {
            return `(${this.memberInfo.postalCode}) ${this.memberInfo.address} ${this.memberInfo.detailAddress}`;
        }
    },
    methods: {
        async getMemberInfo() {
            // const response = await get(`/members/${this.user.userId}`);
            const response = await get(`/members/admin`);
            this.memberInfo = response.data.content;
			console.log(this.memberInfo)
        },
        route(page){
			this.$router.push(page);
		},
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
.profile-item {
	margin-bottom: 15px;
}
.profile-label {
	font-weight: bold;
	margin-bottom: 5px;
	display: block;
}
</style>