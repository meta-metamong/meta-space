<template>
		<div class="container">
			<!-- 제목 -->
			<h2 class="text-center mb-4" v-text="$t('signup.preInfoInput')"></h2>

			<!-- 시설 이용자 -->
			<div class="mb-4">
				<p class="fw-bold">시설을 이용하러 오셨나요?</p>
				<button type="button" @click="nextStep('consumer')" class="w-100">시설 이용자로 회원가입하기</button>
			</div>

			<!-- 시설 제공자 -->
			<div>
				<p class="fw-bold">시설을 등록하러 오셨나요?</p>
				<button type="button" @click="nextStep('provider')" class="w-100">시설 제공자로 회원가입하기</button>
			</div>
		</div>
</template>


<script>
export default{
    name: 'InputRole',
	props: ['setUserInfo'],
	methods:{
		nextStep(role){
			this.setUserInfo([{
				key: 'role',
				value: role
			}]);
		}
	}
}
</script>

<style>
button{
	border-radius: 5px;
}
</style>