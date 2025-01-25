<template>
	<div class="container mt-5 d-flex justify-content-center">
		<div class="p-5 mx-3" style="width: 850px;">
			<h3 class="mb-5"><strong>{{ $t('member.signUp') }}</strong></h3>
			<form @submit.prevent="handleSubmit">
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="userid">{{ $t('member.id') }}</label>
					<div class="col-sm-8">
						<input class="form-control" type="text" name="id" id="userid" :placeholder="$t('member.id')" v-model="id" required />
					</div>
					<div class="col-sm-2">
						<button class="btn btn-secondary" type="button">{{ $t('button.check') }}</button>
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="password">{{ $t('member.password') }}</label>
					<div class="col-sm-10">
						<input class="form-control" type="password" name="password" id="password" :placeholder="$t('member.password')" v-model="password" autocomplete="new-password" required />
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="password2">{{ $t('member.passwordRe') }}</label>
					<div class="col-sm-10">
						<input class="form-control" type="password" name="password2" id="password2" :placeholder="$t('member.passwordRe')" v-model="password2" autocomplete="new-password" required />
						<div class="error-message text-start mt-2" v-if="passwordMismatch">{{ $t('error.passwordConfirm') }}</div>
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="name">{{ $t('member.name') }}</label>
					<div class="col-sm-10">
						<input class="form-control" type="text" name="name" id="name" :placeholder="$t('member.name')" v-model="name" required />
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="email">{{ $t('member.email') }}</label>
					<div class="col-sm-10">
						<input class="form-control" type="email" name="email" id="email" :placeholder="$t('member.email')" v-model="email" required />
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="gender">{{ $t('member.gender') }}</label>
					<div class="col-sm-2 d-flex align-items-center">
						<div class="form-check">
							<input class="form-check-input" type="radio" name="gender" id="male" value="male" v-model="gender" required />
							<label class="form-check-label" for="male">{{ $t('member.male') }}</label>
						</div>
					</div>
					<div class="col-sm-2 d-flex align-items-center">
						<div class="form-check">
							<input class="form-check-input" type="radio" name="gender" id="female" value="female" v-model="gender" required />
							<label class="form-check-label" for="female">{{ $t('member.female') }}</label>
						</div>
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="birthDate">{{ $t('member.birth') }}</label>
					<div class="col-sm-10">
						<input class="form-control" type="date" name="birthDate" id="birthDate" v-model="birthDate" required />
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="phoneNumber">{{ $t('member.phoneNumber') }}</label>
					<div class="col-sm-10">
						<input class="form-control" type="text" name="phoneNumber" id="phoneNumber" :placeholder="$t('member.phoneNumber')" v-model="phoneNumber" required />
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="zipcode">{{ $t('member.zipcode') }}</label>
					<div class="col-sm-5">
						<div class="input-group">
							<input type="text" class="form-control" id="zipcode" name="zipcode" :placeholder="$t('member.zipcode')" readonly />
							<button type="button" class="btn btn-secondary" onclick="openPostcode()">{{ $t('button.search') }}</button>
						</div>
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="address">{{ $t('member.address') }}</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="address" name="address" :placeholder="$t('member.address')" readonly />
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="addressDetail">{{ $t('member.addressDetail') }}</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="addressDetail" name="addressDetail" :placeholder="$t('member.addressDetail')" />
					</div>
				</div>
				<button class="btn btn-primary w-100 p-2 mt-3" type="submit">{{ $t('member.signUp') }}</button>
			</form>
		</div>
	</div>
</template>

<script>
export default {
	name: 'SignUpMember',
	data() {
		return {
			id: "",
			password: "",
			password2: "",
			name: "",
			email: "",
			gender: "",
			birthDate: "",
			phoneNumber: "",
			errorMessage: "",
		}
	},
	computed: {
		passwordMismatch() {
			return this.password !== this.password2;
		}
	},
	methods: {
		handleSubmit() {
			if (!this.passwordMismatch) {
				console.log(this.id, this.password, this.password2);
				console.log(this.name, this.email);
				console.log(this.gender, this.birthDate, this.phoneNumber);
			}
		}
	}
};
</script>

<style scoped>
.btn-primary {
	background-color: #19319D;
	border: #19319D;
}

.error-message {
	font-size: 14px;
	color: #ff0101;
}
</style>