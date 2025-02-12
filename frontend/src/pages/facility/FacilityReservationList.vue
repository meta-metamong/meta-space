<template>
    <div class="container root-container">
        <h2 class="text-center mb-4" v-text="$t('myFacility.facilityReservationList')"></h2>
        <div class="list-item" v-for="(rvt, index) in reservations">
            <table class="table table-borderless">
                <tbody>
                    <tr>
                        <td colspan="2" class="reservation-date">{{ rvt.rvtDate }} {{ rvt.usageStartTime }}~{{ rvt.usageEndTime }}</td> 
                    </tr>
                    <tr>
                        <td width="35%">Zone</td>
                        <td>{{ rvt.zoneName }}</td>
                    </tr>
                    <tr>
                        <td>{{ $t('reservation.reservationName') }}</td>
                        <td>{{ rvt.memName }}</td>
                    </tr>
                    <tr>
                        <td>{{ $t('reservation.usageCount') }}</td>
                        <td>{{ rvt.usageCount }}</td>
                    </tr>
                    <tr>
                        <td>{{ $t('payment.payPrice') }}</td>
                        <td>{{ rvt.payPrice }}</td>
                    </tr>
                    <tr>
                        <td>{{ $t('reservation.state') }}</td>
                        <td>{{ rvt.rvtState }}</td>
                    </tr>
                </tbody>
            </table>
            <hr v-if="index !== reservations.length - 1">
        </div>
    </div>
</template>

<script>
import { get } from "../../apis/axios";

export default {
    name: "FacilityReservationList",
	props: ['fctId'],
    data() {
        return {
            reservations: []
        }
    },
    computed: {
        userId() {
            return this.$store.state.userId;
        }
    },
    mounted() {
        get(`/reservations/facilities/${this.fctId}`).then((response) => {
            const responseBody = response.data;
            const content = responseBody.content;
            console.log(content);
            this.reservations = content;
        });
    }
}
</script>

<style scoped>
.reservation-date {
    font-size: 18px;
    font-weight: 600;
}
</style>