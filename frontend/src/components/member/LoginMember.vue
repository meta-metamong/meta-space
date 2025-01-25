<template>
	<div class="container mt-5 d-flex justify-content-center">
		<div class="card p-5 mx-3" style="width: 800px;">
			<h3 class="text-center mb-5"><strong>로그인</strong></h3>
			<form @submit.prevent="handleSubmit">
				<div class="mb-4">
					<input class="form-control" type="text" name="id" placeholder="아이디" v-model="id" required />
				</div>
				<div>
					<input class="form-control" type="password" name="password" placeholder="비밀번호" v-model="password" required />
				</div>
				<div class="text-start error-message mt-3">{{ errorMessage }}</div>
				<button class="btn btn-primary w-100 p-2 mt-5" type="submit">로그인</button>
			</form>
		</div>
	</div>
</template>

<script>
export default {
	name: 'LoginMember',
	data() {
		return {
			id: "",
			password: "",
			errorMessage: "",
		}
	},
	methods: {
		handleSubmit() {
			console.log(this.id, this.password);
			this.errorMessage = "아이디 또는 비밀번호가 잘못되었습니다.";
		}
	}
};
</script>

<style scoped>
.btn {
	background-color: #19319D;
}

.error-message {
	font-size: 14px;
	color: #ff0101;
}
</style>