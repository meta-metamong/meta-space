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
import Swal from 'sweetalert2';
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
				Swal.fire({
					width: "300px",
					title: "인증 성공",
					text: "변경할 비밀번호를 입력해주세요!",
					icon: "success"
				});
				if(this.type === 'exit'){
					this.exitMember();
				}else{
					this.$router.push("/change-pw")
				}
			}else{
				Swal.fire({
					width: "300px",
					title: "인증 실패",
					text: "비밀번호를 확인해주세요!",
					icon: "error"
				});
			}
		},
		async exitMember(){
			Swal.fire({
				title: '탈퇴 확인',
				text: '정말 메타 스페이스를 탈퇴하시겠습니까?',
				width: '300px',
				icon: 'warning',
				showCancelButton: true,
				confirmButtonColor: "#3085d6",
				cancelButtonColor: "#d33",
				confirmButtonText: "YES",
				cancelButtonText: "NO"
			}).then(async result => {
				if(result.isConfirmed){
					this.$store.dispatch('logoutRequest');
					const response = await del('/members');
					if(response.status === 200){
						Swal.fire({
							width: "300px",
							title: "탈퇴 완료",
							text: "이용해주셔서 감사했습니다!",
							icon: "success"
						})
					}else{
						Swal.fire({
							width: "300px",
							title: "탈퇴 실패",
							text: "회원 탈퇴를 실패했습니다.",
							icon: "error"
						});
					}
				}
				
			});
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