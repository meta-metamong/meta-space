<template>
	<div class="container mt-2 d-flex justify-content-center">
		<div class="p-5 mx-3" style="width: 850px;">
			<h3 class="mb-5"><strong>{{ $t('member.update') }}</strong></h3>
			<form @submit.prevent="handleSubmit">
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="userId">{{ $t('member.id') }}</label>
					<div class="col-sm-10">
						<input class="form-control" type="text" name="userId" id="userId" v-model="memberInfo.userId" disabled required />
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="password">{{ $t('member.newPassword') }}</label>
					<div class="col-sm-10">
						<input class="form-control" type="password" name="password" id="password" :placeholder="$t('member.newPassword')" v-model="password" autocomplete="new-password" />
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="confirmPassword">{{ $t('member.newPasswordRe') }}</label>
					<div class="col-sm-10">
						<input class="form-control" type="password" name="confirmPassword" id="confirmPassword" :placeholder="$t('member.newPasswordRe')" v-model="confirmPassword" autocomplete="new-password" />
						<div class="error-message text-start mt-2" v-if="passwordMismatch">{{ $t('error.passwordConfirm') }}</div>
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="name">{{ $t('member.name') }}</label>
					<div class="col-sm-10">
						<input class="form-control" type="text" name="name" id="name" v-model="memberInfo.name" required />
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="email">{{ $t('member.email') }}</label>
					<div class="col-sm-10">
						<input class="form-control" type="email" name="email" id="email" v-model="memberInfo.email" required />
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="gender">{{ $t('member.gender') }}</label>
					<div class="col-sm-2 d-flex align-items-center">
						<div class="form-check">
							<input class="form-check-input" type="radio" name="gender" id="male" value="male" v-model="memberInfo.gender" required />
							<label class="form-check-label" for="male">{{ $t('member.male') }}</label>
						</div>
					</div>
					<div class="col-sm-2 d-flex align-items-center">
						<div class="form-check">
							<input class="form-check-input" type="radio" name="gender" id="female" value="female" v-model="memberInfo.gender" required />
							<label class="form-check-label" for="female">{{ $t('member.female') }}</label>
						</div>
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="birth">{{ $t('member.birth') }}</label>
					<div class="col-sm-10">
						<input class="form-control" type="date" name="birth" id="birth" v-model="memberInfo.birth" required />
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="phone">{{ $t('member.phoneNumber') }}</label>
					<div class="col-sm-10">
						<input class="form-control" type="text" name="phone" id="phone" v-model="memberInfo.phone" required />
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="postalCode">{{ $t('member.zipcode') }}</label>
					<div class="col-sm-5">
						<div class="input-group">
							<input type="text" class="form-control" id="postalCode" name="postalCode" v-model="memberInfo.postalCode" readonly required/>
							<button type="button" class="btn btn-secondary" @click="openPostCode">{{ $t('button.search') }}</button>
						</div>
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="address">{{ $t('member.addressBasic') }}</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="address" name="address" v-model="memberInfo.address" readonly required />
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="detailAddress">{{ $t('member.addressDetail') }}</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="detailAddress" name="detailAddress" v-model="memberInfo.detailAddress" required />
					</div>
				</div>
				<div class="row mb-4" v-if="role === 'ROLE_OWNER'">
					<label class="col-form-label col-sm-2" for="businessName">{{ $t('member.businessName') }}</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="businessName" name="businessName" v-model="memberInfo.businessName" required />
					</div>
				</div>
				<div class="row mb-4" v-if="role === 'ROLE_OWNER'">
					<label class="col-form-label col-sm-2" for="businessRegistrationNumber">{{ $t('member.businessRegistrationNumber') }}</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="businessRegistrationNumber" name="businessRegistrationNumber" v-model="memberInfo.businessRegistrationNumber" required />
					</div>
				</div>
				<button class="btn btn-primary w-100 p-2 mt-3" type="submit">{{ $t('button.save') }}</button>
			</form>
		</div>
	</div>
</template>
<script>
import { get, post, put } from "../../apis/axios";
import { toRaw } from 'vue';
export default {
	name: 'UpdateMember',
	data() {
		return {
			memberInfo: [],
			password: "",
			confirmPassword: "",
			errorMessage: ""
		}
	},
	computed: {
		passwordMismatch() {
			return this.password !== this.confirmPassword;
		},
		user(){
            return toRaw(this.$store.state.user);
        },
		role(){
            return toRaw(this.$store.state.user.role);
        },
	},
	methods: {
		async handleSubmit() {
			if (this.passwordMismatch || this.memberInfo.password === null) {
				alert("입력한 비밀번호가 다릅니다.");
				return;
			}
			// gender 추가해야 함
			let updateDto = {
				name: this.memberInfo.name,
				password: this.password || undefined,
				email: this.memberInfo.email,
				confirmPassword: this.memberInfo.confirmPassword,
				phone: this.memberInfo.phone,
				birth: this.memberInfo.birth,
				postalCode: this.memberInfo.postalCode,
				address: this.memberInfo.address,
				detailAddress: this.memberInfo.detailAddress,
			}

			if(this.role === 'ROLE_OWNER'){
				updateDto = {
					...updateDto,
					businessName: this.memberInfo.businessName,
					businessRegistrationNumber: this.memberInfo.businessRegistrationNumber
				}
			}

			const response = await put(`/members/${this.user.userId}`, updateDto);
			if (response.status === 200) {
				alert(response.data.message);
				this.$router.push("/mypage")
			} else if (response.status === 400) {
				alert("회원정보 수정 오류 " + response.response.data.message);
			} else {
				return;
			}
		},
		async getMemberInfo() {
			const response = await get("/members/" + this.user.userId);
			this.memberInfo = response.data.content;
		},
		openPostCode(){
			new daum.Postcode({
				oncomplete: (data) => {
					this.memberInfo.postalCode = data.zonecode;
					this.memberInfo.address = data.userSelectedType === 'R' ? data.address : data.jibunAddress;
				}
			}).open();
		},
	},
	mounted() {
		this.getMemberInfo();
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