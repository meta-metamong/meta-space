<template>
	<div class="container mt-2 d-flex justify-content-center">
		<div class="card p-3 mx-3" style="width: 800px;">
			<h3 class="text-center mb-4"><strong>{{ $t('member.login') }}</strong></h3>
			<form @submit.prevent="handleSubmit">
				<div class="mb-4">
					<input class="form-control" type="text" name="id" :placeholder="$t('member.id')" v-model="id" required />
				</div>
				<div>
					<input class="form-control" type="password" name="password" :placeholder="$t('member.password')" v-model="password" required />
				</div>
				<div class="text-start error-message mt-1">{{ errorMessage }}</div>
				<button class="btn btn-primary w-100 p-2 mt-3" type="submit" v-text="$t('member.login')" />
				<button class="btn btn-primary w-50 p-2 mt-2" type="button" @click="route('/find')"> {{ $t('member.findId') }} / PW</button>
				<button class="btn btn-primary w-50 p-2 mt-2" type="button" v-text="$t('member.signUp')" @click="route('/signup')" />
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
			this.errorMessage = this.$t('error.login');
		},
		route(page){
			this.$router.push(page);
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