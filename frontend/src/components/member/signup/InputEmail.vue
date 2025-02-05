<template>
    <div class="container">
        <!-- 제목 -->
        <h2 class="text-center mb-4">이메일 입력입니다</h2>
        <form @submit.prevent="handleLogin">
			<!-- 이메일 입력 -->
			<div class="mb-3">
				<input type="email" id="email" name="email" class="form-control" v-model="email" :placeholder="$t(('member.email'))">
			</div>
        </form>

        <!-- 로그인 버튼 -->
        <button type="submit" class="w-100 custom-btn" @click="" v-text="$t('member.login')" />
        <button type="button" @click="nextStep()">다음 단계로</button>
    </div>
</template>

<script>
export default{
    name: 'InputEmail',
    props: ['setUserInfo'],
    data(){
        return{
            email: ""
        }
    },
	methods:{
		nextStep(){
			this.setUserInfo([{
                key: 'email',
                value: this.email
            }]);
		}
	}
}
</script>