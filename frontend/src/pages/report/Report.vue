<template>
    <div class="container w-100 mt-3">
		<h2 class="text-center mb-4" v-text="$t('report.report')"></h2>
        <div class="mb-4">
            <select v-model="reportType" class="form-select border-secondary mb-4">
                <option value="" selected disabled>{{ $t('report.selectReportType') }}</option>
                <option v-for="type in reportTypes" :key="type.reportType" :value="type.reportType">{{ type.description }}</option>
            </select>
            <textarea class="w-100" rows="5" v-model="reportMsg" :placeholder="$t('report.reportMsg')" required></textarea>
        </div>
        <div>
            <button @click="report" class="report-btn w-100 signup-btn">{{ $t('report.report') }}</button>
        </div>
    </div>
</template>

<script>
import { get, post } from '../../apis/axios';
import Swal from 'sweetalert2';

export default{
    name: "Report",
    props:{
        zoneId: {
            type: Number,
            required: true
        }
    }, 
    data(){
        return{
            reportMsg: "",
            reportType: "",
            reportTypes: []
        }
    },
    methods:{
        async report(){
            if(!this.reportMsg || !this.reportType){
                Swal.fire({
                    width: '300px',
                    title: '신고 실패',
                    text: '신고 유형과 신고 내용은 모두 입력되어야 합니다.',
                    icon: 'error'
                });
                return;
            }
            const requestDto = {
                zoneId: this.zoneId,
                reportMsg: this.reportMsg,
                reportType: this.reportType
            }
            const response = await post('/reports', requestDto);
            if(response.status === 200){
                Swal.fire({
                    width: '300px',
                    title: '신고 성공',
                    text: '신고가 접수되었습니다.',
                    icon: 'success'
                });
                this.$router.push(`/reservation/${this.zoneId}`);
            }else{
                Swal.fire({
                    width: '300px',
                    title: '신고 실패',
                    text: '신고 접수에 실패했습니다.',
                    icon: 'error'
                });
                return;
            }
        }
    },
    async mounted(){
        const response = await get("/reports/types");
        this.reportTypes = response.data.content;
    }

}
</script>

<style scoped>
.report-btn{
    border-radius: 15px;
}
</style>