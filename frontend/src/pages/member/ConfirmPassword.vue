<template>
	  <div class="container w-100 mt-4">
		<h2 class="text-center mb-3" v-text="$t('member.confirmPw')"></h2>
  
		<form @submit.prevent="handleSubmit">	
			<!-- 비밀번호 입력 -->
			<div class="mb-4">
				<input type="password" id="password" name="password" class="form-control signup-input mb-2" v-model="password" :placeholder="$t('member.password')" required />
			</div>	

			<button type="submit" class="w-100 signup-btn rounded-pill mb-3" v-text="$t('button.check')" />
			<button type="button" class="signup-btn w-100 mb-3 rounded-pill" @click="$router.push('/profile')">{{ $t('button.cancel') }}</button>
		</form>
	  </div>
  </template>

<script>
import { post, del } from '../../apis/axios';
export default {
	name: 'ConfirmPassword',
	data() {
		return {
			password: ""
		}
	},
	props: ['type'],
	methods: {
		async handleSubmit(){
			const requestDto = {
				password: this.password
			}
			const response = await post("/members/password", requestDto);
			if(response.status === 200){
				alert(response.data.message);
				if(this.type === 'exit'){
					this.exitMember();
				}else{
					this.$router.push("/change-pw")
				}
			}else{
				alert(response.response.data.message);
			}
		},
		async exitMember(){
            if(confirm("MetaSpace를 정말 탈퇴하시겠습니까?")){
                const response = await del('/members');
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