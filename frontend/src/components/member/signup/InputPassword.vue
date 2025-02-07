<template>
   <div class="container">
        <!-- 제목 -->
        <h1 class="text-center mb-4">{{ $t('signup.signup') }} (2/3)</h1>
        <!-- 비밀번호호 입력 -->
        <div class="mb-3">
            <input type="password" class="w-100 mb-2 signup-input" v-model="password" :placeholder="$t('member.newPassword')" />
            <h3 class="error-message mt-2" v-if="password !== '' && !isValidatedPassword" v-text="$t('signupError.notValidatedPassword')" />
        </div>
        <div class="mb-3">
            <input type="password" class="w-100 mb-2 signup-input" v-model="confirmPassword" :placeholder="$t('member.newPasswordRe')" />
            <h3 class="error-message mt-2" v-if="confirmPassword !== '' && !isPasswordEqual" v-text="$t('signupError.passwordNotMatched')" />
        </div>

        <!-- 다음 단계 진행행 버튼 -->
        <button type="button" :disabled="!isValidatedPassword || !isPasswordEqual" class="w-100 h-75 signup-btn mt-2 rounded-pill" @click="nextStep()">{{ $t('signup.next') }}</button>
    </div>
</template>

<script>
export default{
    name: 'InputPassword',
    props: ['setUserInfo'],
    data(){
        return{
            password: "",
            confirmPassword: ""
        }
    },
	methods:{
		nextStep(){
			this.setUserInfo([{
                key: 'password',
                value: this.password
            }]);
		}
	},
    computed:{
        isPasswordEqual(){
            return this.password === this.confirmPassword;
        },
        isValidatedPassword(){
            return /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/.test(this.password);
        }
    }
}
</script>