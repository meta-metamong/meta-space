<template>
	<div class="container mt-2 d-flex justify-content-center">
		<div class="p-5 mx-3" style="width: 850px;">
			<h3 class="mb-5"><strong>{{ $t('member.signUp') }}</strong></h3>
			<form @submit.prevent="handleSubmit">
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="userId">{{ $t('member.id') }}</label>
					<div class="col-sm-8">
						<input class="form-control" :disabled="!idDuplicated" type="text" name="userId" id="userId" :placeholder="$t('member.id')" v-model="userId" required />
						<div class="error-message text-start mt-2" v-if="userId !== '' && !idValidation">{{ $t('validation.id') }}</div>
					</div>
					<div class="col-sm-2">
						<button class="btn btn-secondary" :disabled="!idDuplicated" type="button" @click="checkDuplicated('id')">{{ $t('button.check') }}</button>
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="password">{{ $t('member.password') }}</label>
					<div class="col-sm-10">
						<input class="form-control" type="password" name="password" id="password" :placeholder="$t('member.password')" v-model="password" autocomplete="new-password" required />
						<div class="error-message text-start mt-2" v-if="password !== '' && !passwordValidation">{{ $t('validation.password') }}</div>
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="confirmPassword">{{ $t('member.passwordRe') }}</label>
					<div class="col-sm-10">
						<input class="form-control" type="password" name="confirmPassword" id="confirmPassword" :placeholder="$t('member.passwordRe')" v-model="confirmPassword" autocomplete="new-password" required />
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
					<div class="col-sm-8">
						<input class="form-control" type="email" :disabled="!emailDuplicated" name="email" id="email"  :placeholder="$t('member.email')" v-model="email" required />
						<div class="error-message text-start mt-2" v-if="email !== '' && !emailValidation">{{ $t('validation.email') }}</div>
					</div>
					<div class="col-sm-2">
						<button class="btn btn-secondary" type="button" :disabled="!emailDuplicated"  @click="checkDuplicated('email')">{{ $t('button.check') }}</button>
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
					<label class="col-form-label col-sm-2" for="birth">{{ $t('member.birth') }}</label>
					<div class="col-sm-10">
						<input class="form-control" type="date" name="birth" id="birth" v-model="birth" required />
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="phone">{{ $t('member.phoneNumber') }}</label>
					<div class="col-sm-10">
						<input class="form-control" type="text" name="phone" id="phone" :placeholder="$t('member.phoneNumber')" v-model="phone" required />
						<div class="error-message text-start mt-2" v-if="phone !== '' && !phoneValidation">{{ $t('validation.phone') }}</div>
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="postalCode">{{ $t('member.zipcode') }}</label>
					<div class="col-sm-5">
						<div class="input-group">
							<input type="text" class="form-control" id="postalCode" name="postalCode" v-model="postalCode" :placeholder="$t('member.zipcode')" readonly required/>
							<button type="button" class="btn btn-secondary" @click="openPostCode">{{ $t('button.search') }}</button>
						</div>
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="address">{{ $t('member.address') }}</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="address" name="address" v-model="address" :placeholder="$t('member.address')" readonly required />
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="detailAddress">{{ $t('member.addressDetail') }}</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="detailAddress" name="detailAddress" v-model="detailAddress" :placeholder="$t('member.addressDetail')" required />
					</div>
				</div>
				<div class="row mb-4" v-if="role === 'owner'">
					<label class="col-form-label col-sm-2" for="businessName">{{ $t('member.businessName') }}</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="businessName" name="businessName" v-model="businessName" :placeholder="$t('member.businessName')" required />
					</div>
				</div>
				<div class="row mb-4" v-if="role === 'owner'">
					<label class="col-form-label col-sm-2" for="businessRegistrationNumber">{{ $t('member.businessRegistrationNumber') }}</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="businessRegistrationNumber" name="businessRegistrationNumber" v-model="businessRegistrationNumber" :placeholder="$t('member.businessRegistrationNumber')" required />
					</div>
				</div>
				<button class="btn btn-primary w-100 p-2 mt-3" type="submit">{{ $t('member.signUp') }}</button>
			</form>
		</div>
	</div>
</template>
<script>
import { get, post } from "../../apis/axios";

export default {
	name: 'SignUpMember',
	data() {
		return {
			userId: "",
			password: "",
			confirmPassword: "",
			name: "",
			email: "",
			gender: "",
			birth: "",
			phone: "",
			postalCode: "",
			address: "",
			detailAddress: "",
			businessName: "",
			businessRegistrationNumber: "",
			idDuplicated: true,
			emailDuplicated: true,
		}
	},
	computed: {
		passwordMismatch() {
			return this.password !== "" && this.confirmPassword !== "" && this.password !== this.confirmPassword;
		},
		role() {
			return this.$route.params.role;
		},
		idValidation(){
			return /^[a-z]+[a-z0-9]{5,19}$/.test(this.userId);
		},
		passwordValidation(){
			return /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/.test(this.password);
		},
		emailValidation(){
			return /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i.test(this.email);
		},
		phoneValidation(){
			return /^01(?:0|1|[6-9])-(?:\d{3}|\d{4})-\d{4}$/.test(this.phone);
		}		
	},
	methods: {
		async handleSubmit() {
			if (this.passwordMismatch) {
				alert("입력한 비밀번호가 다릅니다.");
				return;
			}else if(!this.idDuplicated && !this.emailDuplicated){
				alert("아이디와 이메일을 확인해주세요.");
				return;
			}
			// gender 추가해야 함
			let signupDto = {
				userId: this.userId,
				name: this.name,
				email: this.email,
				password: this.password,
				confirmPassword: this.confirmPassword,
				phone: this.phone,
				birth: this.birth,
				postalCode: this.postalCode,
				address: this.address,
				detailAddress: this.detailAddress
			}

			if(this.role === 'owner'){
				signupDto = {
					...signupDto,
					businessName: this.businessName,
					businessRegistrationNumber: this.businessRegistrationNumber
				}
			}
			const requestUrl = this.role === 'user' ? "/members/user" : "/members/owner";

			const response = await post(requestUrl, signupDto);
			if(response.status === 201) {
				alert(response.data.message);
				location.href = "/login";
			}else{
				return;
			}
		},
		openPostCode(){
			new daum.Postcode({
				oncomplete: (data) => {
					this.postalCode = data.zonecode;
					this.address = data.userSelectedType === 'R' ? data.address : data.jibunAddress;
				}
			}).open();
		},
		async checkDuplicated(type){
			if(type === 'id'){
				const response = await get(`/members/dup-id/${this.userId}`);
				if(response.status === 200) {
					if(response.data.content){
						alert("이미 존재하는 아이디입니다.");
					}else{
						alert("사용 가능한 아이디입니다");
						this.idDuplicated = false;
					}
				}
			}else{
				const requestDto = {
					email: this.email
				};
				const response = await post('/members/dup-email', requestDto);
				if(response.status === 200) {
					if(response.data.content){
						alert("이미 존재하는 이메일입니다.");
					}else{
						alert("사용 가능한 이메일입니다");
						this.emailDuplicated = false;
					}
				}
			}
		}
	}
}
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