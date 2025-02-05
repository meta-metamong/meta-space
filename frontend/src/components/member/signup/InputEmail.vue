<template>
    <div class="container">
        <!-- 제목 -->
        <h2 class="text-center mb-4">이메일 입력입니다</h2>
        <!-- 이메일 입력 -->
        <div class="mb-3">
            <input type="email" id="email" name="email" class="w-100" v-model="email" :placeholder="$t('member.email')">
            <h3 class="error-message mt-2" v-if="email !== '' && !isValidated" v-text="$t('signupError.notValidatedEmail')" />
        </div>
        <button type="button" class="w-100 h-75 custom-btn mt-2" @click="" v-text="$t('signup.checkEmail')" />

        <!-- 로그인 버튼 -->
        <button type="button" class="w-100 h-75 custom-btn mt-2" @click="nextStep()">다음 단계로</button>
    </div>
</template>

<script>
// 이메일 인증 기능 구현 후 isAuthenticated 변수를 추가해서 isAuthenticated와 isValidated를 동시에 만족하는 경우 nextStep() 함수를 진행할 수 있게 한다.

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
	},
    computed: {
        isValidated(){
            return /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i.test(this.email);
        }
    }
}
</script>

<style>
input{
	border: none;
	border-radius: 0px;
	border-bottom: 1px solid #999;
}
</style>