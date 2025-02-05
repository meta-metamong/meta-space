<template>
    <div class="container">
        <!-- 제목 -->
        <h2 class="text-center mb-4">비밀번호 입력입니다</h2>
        <button type="button" @click="nextStep('비밀번호업니다')">다음 단계로</button>
    </div>
</template>

<script>
export default{
    name: 'InputPassword',
    props: ['setUserInfo'],
	methods:{
		nextStep(password){
			this.setUserInfo([{
                key: 'password',
                value: password
            }]);
		}
	}
}
</script>