<template>
    <h2>결제 진행중입니다..</h2>
</template>
<script>
import Swal from 'sweetalert2';
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
            Swal.fire({
                text: params.get('message'),
                width: '300px',
                icon: 'info'
            });
            this.$router.push(`/reserve/${fctId}`);
            return;
        }

        const response = await post(`/reservations`, requestDto);
        if (response.status === 201) {
            Swal.fire({
                text: '예약되었습니다.',
                width: '300px',
                icon: 'success'
            })
            this.$router.push('/reservation/list')
        } else if (response.status === 409) {
            Swal.fire({
                text: '예약이 불가능한 시간입니다. 다른 시간을 선택해주세요.',
                width: '300px',
                icon: 'error'
            })
            this.$router.push(`/reserve/${fctId}`);
        }
    }
}

</script>
<style scoped>
</style>