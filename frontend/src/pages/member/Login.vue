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
				<input type="password" id="password" name="password" class="form-control signup-input mb-2" v-model="password" :placeholder="$t('member.password')" required />
				<span class="error-message">{{ errorMessage }}</span>
			</div>	
	
			<!-- 로그인 버튼 -->
			<button type="submit" class="w-100 signup-btn rounded-pill" @click="" v-text="$t('member.login')" />
	
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
			password: "",
			errorMessage: ""
		}
	},
	methods: {
		async handleLogin() {
			const loginDto = {
				"email": this.email,
				"password": this.password
			}

			const message = await this.$store.dispatch("loginRequest", loginDto);
			this.errorMessage = message;

			if(this.$store.state.userId === 1) {
				this.$router.push("/admin");
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