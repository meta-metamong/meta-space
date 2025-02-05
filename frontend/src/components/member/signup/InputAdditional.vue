<template>
    <div class="container">
        <!-- 제목 -->
        <h2 class="text-center mb-4">추가정보 입력입니다</h2>
        <button type="button" @click="nextStep('추가 정보입니다.')">다음 단계로</button>
    </div>
</template>

<script>
export default{
    name: 'InputAdditional',
    props: ['setUserInfo'],
    methods:{
		nextStep(additional){
			this.setUserInfo([{
                key: 'businessName',
                value: '김한수 주식회사'
            },{
                key: 'businessNumber',
                value: '12312123'
            },{
                key: 'account',
                value: '농협 312-0023-9217-11'
            }]);
		}
	}
}
</script>