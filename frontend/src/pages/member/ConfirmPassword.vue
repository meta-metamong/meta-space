<template>
	  <div class="container w-100 mt-4">
		<h2 class="text-center mb-3" v-text="$t('member.confirmPw')"></h2>
  
		<form @submit.prevent="handleConfirm">	
			<!-- 비밀번호 입력 -->
			<div class="mb-4">
				<input type="password" id="password" name="password" class="form-control signup-input mb-2" v-model="password" :placeholder="$t('member.password')" required />
			</div>	
	
			<!-- 로그인 버튼 -->
			<button type="submit" class="w-100 signup-btn rounded-pill" v-text="$t('button.check')" />
		</form>
	  </div>
  </template>

<script>
export default {
	name: 'Login',
	data() {
		return {
			password: ""
		}
	},
	methods: {
		handleConfirm(){

		},
		async exitMember(){
            if(confirm("MetaSpace를 정말 탈퇴하시겠습니까?")){
                const response = await(post('/members'));
                if(response.status === 200){
                    this.$store.dispatch('logoutRequest');
                }
            }
        }
	}
};
</script>

<style scoped>
input{
	border: none;
	border-radius: 0px;
	border-bottom: 1px solid #999;
}

.error-message {
	font-size: 16px;
	color: #ff0101;
}
</style>