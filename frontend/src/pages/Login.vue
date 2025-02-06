<template>
	  <div class="container w-100 mt-4">
		<h2 class="text-center mb-3" v-text="$t('member.login')"></h2>
  
		<form @submit.prevent="handleLogin">
			<!-- 이메일 입력 -->
			<div class="mb-3">
				<input type="email" id="email" name="email" class="form-control signup-input" v-model="email" :placeholder="$t(('member.email'))" required />
			</div>
	
			<!-- 비밀번호 입력 -->
			<div class="mb-4">
				<input type="password" id="password" name="userId" class="form-control signup-input" v-model="password" :placeholder="$t('member.password')" required />
			</div>	
	
			<!-- 로그인 버튼 -->
			<button type="submit" class="w-100 signup-btn" @click="" v-text="$t('member.login')" />
	
			<!-- 하단 링크 (회원가입 / 비밀번호 찾기) -->
			<div class="text-center mt-3">
				<router-link to="/signup" class="text-muted text-decoration-none">{{ $t('member.signup') }}</router-link>
				<span class="mx-4">|</span>
				<router-link to="/find-password" class="text-muted text-decoration-none">{{ $t('member.findPw') }}</router-link>
			</div>
		</form>
	  </div>
  </template>

<script>
export default {
	name: 'Login',
	data() {
		return {
			email: "",
			password: ""			
		}
	},
	methods: {
		async handleLogin() {
			const loginDto = {
				"email": this.email,
				"password": this.password
			}

			await this.$store.dispatch("loginRequest", loginDto);

			if(this.$store.state.userId === 1) {
				this.$router.push("/admin");
			}
		},
		route(page){
			this.$router.push(page);
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

</style>