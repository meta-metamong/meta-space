<template>
    <h2>결제 진행중입니다..</h2>
</template>
<script>
import { post } from '../../apis/axios';
export default{
    name: "PaymentResult",
    async mounted(){
        const requestDto = JSON.parse(sessionStorage.getItem('reservation'));
        const fctId = sessionStorage.getItem('fctId');
        
        sessionStorage.removeItem('reservation');
        sessionStorage.removeItem('fctId');

        const params = new URL(window.location.href).searchParams;
        if(params.get('code') !== null && params.get('code') !== undefined){
            console.log(code);
            alert(params.get('message'));
            this.$router.push(`/reserve/${fctId}`);
            return;
        }

        const response = await post(`/reservations`, requestDto);
        if (response.status === 201) {
            alert('예약 성공')
            this.$router.push('/reservation/list')
        } else if (response.status === 409) {
            alert('예약이 불가능한 시간입니다. 다른 시간을 선택해주세요.');
            this.$router.push(`/reserve/${fctId}`);
        }
    }
}

</script>
<style scoped>
</style>