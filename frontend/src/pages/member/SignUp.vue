<template>
	<div class="container w-100 mt-4">
		<component :is="steps[step]" :setUserInfo="setUserInfo" />
	</div>
</template>

<script>
/*
	회원가입은 이용자의 경우는 4단계, 제공자의 경우는 5단계로 이루어진다.
	
	1단계
	시설 이용자인지 제공자인지 선택한다.

	2단계
	이메일 인증을 진행한다.

	3단계
	비밀번호를 설정한다.

	4단계
	추가 정보를 입력한다.
	추가 정보 : 이름, 생년월일, 성별, 전화번호, 우편번호, 도로명 / 지번 주소, 상세 주소

	5단계 (시설 제공자)
	사업자명, 사업자 등록번호, 계좌번호를 입력한다.

	각 단계별로 컴포넌트가 존재한다.
	백엔드로 회원가입 요청 시 전달하는 객체는 SignUp 페이지에서 관리한다.
	각 자식 컴포넌트로 회원 데이터를 저장하는 메서드를 props로 넘긴다.
	각 자식 컴포넌트는 해당 props를 통해 객체의 데이터를 업데이트한다.

	각 역할 별로 마지막 단계까지 끝나면 객체를 담아 백엔드로 회원가입 요청을 전송한다.
*/
import InputRole from "../../components/member/signup/InputRole.vue";
import InputEmail from "../../components/member/signup/InputEmail.vue";
import InputPassword from "../../components/member/signup/InputPassword.vue";
import InputDetail from "../../components/member/signup/InputDetail.vue";
import InputAdditional from "../../components/member/signup/InputAdditional.vue";

import { post } from "../../apis/axios";
import Swal from "sweetalert2";

export default{
    name: "SignUp",
    components: {
		InputRole,
		InputEmail,
		InputPassword,
		InputDetail,
		InputAdditional
	},
	data(){
		return{
			role: "",
			step: 0,
			steps: [
				InputRole,
				InputEmail,
				InputPassword,
				InputDetail,
				InputAdditional
			]
		}
	},
	computed:{
		maxStep(){
			return this.role === 'provider' ? 4 : 3;
		},
		user(){
			let user = {
				email: "",
				memName: "",
				password: "",
				memPhone: "",
				birthDate: "",
				gender: "",
				memPostalCode: "",
				memAddress: "",
				memDetailAddress: "",
				signUpValidationCode: ""
			};
			if(this.role === 'provider'){
				user = { ...user, 
					bizName: "",
					bizRegNum: "",
					bankCode: "",
					accountNumber: "",
					isAgreedInfo: "Y"
				}
			}
			return user;
		}
	},
	methods:{
		async setUserInfo(data = [{}]){
			if(this.step === 0){
				this.role = data[0]['value'];
				this.step++;
				return;
			}

			data.forEach(input => {
				this.user[input.key] = input.value;
			});
			try {
				if (this.step === this.maxStep) {
					Swal.fire({
						title: '정보 제공 동의',
						text: '제 3자 정보 제공에 동의하십니까?\n제공에 동의하지 않으면 회원가입이 불가능합니다.',
						width: '300px',
						icon: 'warning',
						showCancelButton: true,
						confirmButtonColor: "#3085d6",
						cancelButtonColor: "#d33",
						confirmButtonText: "YES",
						cancelButtonText: "NO"
						}).then(async result => {
						if(result.isConfirmed){
							const response = await post(`/members/${this.role}`, this.user);
							if(response.status === 200) {
								Swal.fire({
									icon: "success",
									width: '300px',
									title: '회원가입 완료',
									text: this.role === 'provider' ? '시설을 등록해보세요!' : '시설을 이용해보세요!'
								});
								this.$router.push('/');			
								return;
							}else{
								this.step--;
								return;
							}
						}else{
							Swal.fire({
								icon: "success",
								width: '300px',
								title: "회원가입 취소.",
								text: "회원가입이 취소되었습니다."
							});
							this.$router.push('/');
						}
					})
				}else{
					this.step++;
				}
			}catch(exception){
				console.log(exception);
			}			
		}
	}
}
</script>

<style>
.custom-btn {
  height: 45px;
  font-size: 16px;
  border-radius: 8px;
}
</style>