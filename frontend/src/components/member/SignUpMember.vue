<template>
	<div class="container mt-5">
		<div class="p-5 mx-3" style="width: 850px;">
			<h3 class="mb-5"><strong>회원가입</strong></h3>
			<form @submit.prevent="handleSubmit">
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="userid">아이디</label>
					<div class="col-sm-8">
						<input class="form-control" type="text" name="id" id="userid" placeholder="아이디" v-model="id" required />
					</div>
					<div class="col-sm-2">
						<button class="btn btn-secondary" type="button">중복 확인</button>
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="password">비밀번호</label>
					<div class="col-sm-10">
						<input class="form-control" type="password" name="password" id="password" placeholder="비밀번호" v-model="password" autocomplete="new-password" required />
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="password2">비밀번호 확인</label>
					<div class="col-sm-10">
						<input class="form-control" type="password" name="password2" id="password2" placeholder="비밀번호 확인" v-model="password2" autocomplete="new-password" required />
						<div class="error-message text-start mt-2" v-if="passwordMismatch">비밀번호가 일치하지 않습니다.</div>
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="name">이름</label>
					<div class="col-sm-10">
						<input class="form-control" type="text" name="name" id="name" placeholder="이름" v-model="name" required />
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="email">이메일</label>
					<div class="col-sm-10">
						<input class="form-control" type="email" name="email" id="email" placeholder="이메일" v-model="email" required />
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="gender">성별</label>
					<div class="col-sm-2 d-flex align-items-center">
						<div class="form-check">
							<input class="form-check-input" type="radio" name="gender" id="male" value="male" v-model="gender" required />
							<label class="form-check-label" for="male">남성</label>
						</div>
					</div>
					<div class="col-sm-2 d-flex align-items-center">
						<div class="form-check">
							<input class="form-check-input" type="radio" name="gender" id="female" value="female" v-model="gender" required />
							<label class="form-check-label" for="female">여성</label>
						</div>
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="birthDate">생년월일</label>
					<div class="col-sm-10">
						<input class="form-control" type="date" name="birthDate" id="birthDate" v-model="birthDate" required />
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="phoneNumber">전화번호</label>
					<div class="col-sm-10">
						<input class="form-control" type="text" name="phoneNumber" id="phoneNumber" placeholder="전화번호" v-model="phoneNumber" required />
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="zipcode">우편번호</label>
					<div class="col-sm-10">
						<div class="input-group">
							<input type="text" class="form-control" id="zipcode" name="zipcode" placeholder="우편번호" readonly />
							<button type="button" class="btn btn-secondary" onclick="openPostcode()">우편번호 찾기</button>
						</div>
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="address">기본 주소</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="address" name="address" placeholder="기본 주소" readonly />
					</div>
				</div>
				<div class="row mb-4">
					<label class="col-form-label col-sm-2" for="addressDetail">상세 주소</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="addressDetail" name="addressDetail" placeholder="상세 주소" />
					</div>
				</div>
				<button class="btn btn-primary w-100 p-2 mt-3" type="submit">회원가입</button>
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