<template>
    <div class="container">
        <!-- 제목 -->
        <h1 class="text-center mb-4">{{ $t('signup.signup') }} (1/3)</h1>
        <!-- 이메일 입력 -->
        <div class="mb-3">
            <input type="email" class="w-100 mb-2 signup-input" v-model="email" :disabled="isCodeSent" :placeholder="$t('member.email')" />
            <h3 class="error-message mb-2" v-if="email !== '' && !isValidatedEmail" v-text="$t('signupError.notValidatedEmail')" />
            <button type="button" class="w-100 signup-btn rounded-pill" :disabled="isCodeSent || !isValidatedEmail" @click="sendCode" v-text="$t('signup.checkEmail')" />
        </div>

        <div class="row mt-4 mb-4" v-if="isCodeSent">
            <input class="w-75 signup-input" type="text" :placeholder="$t('signup.inputCode')" v-model="code" :disabled="isCorrectCode" />
            <button class="w-25 h-50 signup-btn" type="button" :disabled="isCorrectCode || !isValidatedCode" @click="codeValidation">{{ $t('button.check') }}</button>
            <h3 class="error-message mt-2" v-if="code !== '' && !isValidatedCode" v-text="$t('signupError.notValidatedCode')" />
        </div>

        <!-- 다음 단계 진행행 버튼 -->
        <button type="button" v-if="isCorrectCode" class="w-100 signup-btn mt-2 rounded-pill" @click="nextStep()">{{ $t('signup.next') }}</button>
    </div>
</template>

<script>
/*
    이메일 검증 통과 시 인증 코드 전송 버튼이 활성화된다.
    인증 코드 전송이 성공적으로 처리되면 인증 코드 입력 버튼이 활성화된다.
    인증 코드 검증 후 성공하면 다음 단계 진행 버튼이 활성화된다.
*/
import { post } from '../../../apis/axios';
import Swal from 'sweetalert2';

export default{
    name: 'InputEmail',
    props: ['setUserInfo'],
    data(){
        return{
            email: "",
            isCodeSent: false,
            code: "",
            answer: "1234",
            isCorrectCode: false,
            verificationCode: ""
        }
    },
	methods:{
        async sendCode(){
            const requestDto = {
                email: this.email
            }
            
            this.isCodeSent = true;
            // 인증 번호 전송 코드 작성
            const response = await post("/members/send-validation-emails", requestDto);
            if(response.status === 200){
                Swal.fire({
                    icon: "success",
                    width: '300px',
                    title: "전송 완료",
                    text: "인증코드를 입력해주세요!"
                });
                this.verificationCode = response.data.content;
            }else{
                Swal.fire({
                    icon: "error",
                    width: '300px',
                    title: "전송 실패",
                    text: "올바른 이메일인가요?"
                });
                this.isCodeSent = false;
            }
        },
        codeValidation(){
            // 코드 검증 성공 여부에 따라 true로 설정할지 안할지 결정
            if(this.verificationCode === this.code){
                Swal.fire({
                    icon: "success",
                    width: '300px',
                    title: "인증 완료",
                    
                });
                this.isCorrectCode = true;
            }else{
                Swal.fire({
                    icon: "error",
                    width: '300px',
                    title: "코드 인증 실패",
                    text: "전송된 인증코드와 다릅니다."
                });
            }
        },
        nextStep(){
                this.setUserInfo([{
                key: 'email',
                value: this.email
            },{
                key: 'signUpValidationCode',
                value: this.verificationCode
            }]);
        }
	},
    computed: {
        isValidatedEmail(){
            return /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i.test(this.email);
        },
        isValidatedCode(){
            return this.code.length === 6;
        }
    }
}
</script>
