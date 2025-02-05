<template>
    <div class="container">
        <!-- 제목 -->
        <h2 class="text-center mb-4">상세정보 입력입니다</h2>
        <button type="button" @click="nextStep('상세 정보입니다.')">다음 단계로</button>
    </div>
</template>

<script>
export default{
    name: 'InputDetail',
    props: ['setUserInfo'],
    methods:{
		nextStep(detail){
            this.setUserInfo([{
                key: 'name',
                value: '김한수'
            },{
                key: 'gender',
                value: '남자'
            }, {
                key: 'birthDate',
                value: '1999-06-14'
            },{
                key: 'postalCode',
                value: '24209'
            },{
                key: 'address',
                value: '춘천시 소양강로 104'
            },{
                key: 'detailAddress',
                value: '101-702'
            }]);
		}
	}
}
</script>