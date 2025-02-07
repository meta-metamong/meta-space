<template>
		<div class="container">
			<!-- 제목 -->
			<h1 class="text-center mb-4" v-text="$t('signup.preInfo')"></h1>

			<!-- 시설 이용자 -->
			<div class="mb-4">
				<p class="fw-bold">{{ $t('signup.isConsumer') }}</p>
				<button type="button" @click="nextStep('consumer')" class="w-100 h-75 signup-btn rounded-pill">{{ $t('signup.ConsumerSignup') }}</button>
			</div>

			<!-- 시설 제공자 -->
			<div>
				<p class="fw-bold">{{ $t('signup.isProvider') }}</p>
				<button type="button" @click="nextStep('provider')" class="w-100 h-75 signup-btn rounded-pill">{{ $t('signup.ProviderSignup') }}</button>
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

</style>