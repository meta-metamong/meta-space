<template>
    <div class="container w-100 mt-4">
        <h2 class="text-center mb-4" v-text="$t('reservation.reserve')"></h2>
        <div class="text-center mb-4">
            <VDatePicker v-model="date" mode="date" :min-date="new Date()"/>
        </div>
        <div class="mb-5">
            <p class="ms-4 text-secondary">Zone</p>
            <p class="profile-content w-75 mx-auto px-3 fs-5">
                <select class="form-select" v-model="selectedZoneId">
                    <option value="" selected disabled >Zone을 선택하세요</option>
                    <option v-for="zone in zoneInfo" :key="zone.zoneId" :value="zone.zoneId">{{ zone.zoneName }}</option>
                </select>
            </p>
        </div>
        <div class="mb-3 d-flex justify-content-center align-item-center">
            <div class="time-buttons">
                <button v-for="(time, index) in timeSlots.slice(0, timeSlots.length - 1)" :key="time" :class="{ selected: selectedTimes.includes(time) }"
                    @click="selectTime(time)" :disabled="isUnavailable(time)">
                    {{ formatTimeRange(index) }}
                </button>
            </div>
        </div>
        <div class="text-center mb-4">
            <button class="btn btn-secondary" @click="resetSelection">초기화</button>
        </div>
        <div class="mb-4">
            <p class="ms-4 text-secondary">{{ $t('reservation.fctName') }}</p>
            <p class="profile-content w-75 mx-auto px-3 fs-5">{{ fctName }}</p>
        </div>
        <div class="mb-4">
            <p class="ms-4 text-secondary">{{ $t('reservation.reservationDate') }}</p>
            <p class="profile-content w-75 mx-auto px-3 fs-5">{{ formattedDate }}</p>
        </div>
        <div class="mb-4">
            <p class="ms-4 text-secondary">{{ $t('reservation.reservationTime') }}</p>
            <p class="profile-content w-75 mx-auto px-3 fs-5">{{ reservationTime }}</p>
        </div>
        <div class="mb-4">
            <p class="ms-4 text-secondary">{{ $t('reservation.usageCount') }}</p>
            <div class="d-flex align-items-center w-75 mx-auto px-3 fs-5">
                <button class="btn btn-outline-secondary" @click="decreaseCount" :disabled="usageCount <= 1">−</button>
                <input type="text" class="form-control text-center mx-2" v-model="usageCount" readonly style="width: 50px;" />
                <button class="btn btn-outline-primary" @click="increaseCount" :disabled="usageCount >= maxCount">+</button>
            </div>
        </div>
        <div class="mb-4">
            <p class="ms-4 text-secondary">{{ $t('reservation.totalPrice') }}</p>
            <p class="profile-content w-75 mx-auto px-3 fs-5">{{ payPrice }}원</p>
            <p class="helper-text w-75 mx-auto px-3">{{ $t('reservation.priceDescription') }}</p>
        </div>
        <div v-if="!isPayOpen" class="w-100 text-center mb-2">
            <button class="rvt-btn w-75 mb-3 rounded-pill" @click="toggleIsPayOpen">{{ $t('reservation.reserve') }}</button>
        </div>
        <div class="w-75 mx-auto text-center" v-if="isPayOpen">
            <div class="w-100 text-start mb-1">{{ $t('payment.payMethod') }}</div>
            <select class="form-select pay-select w-100" v-model="payMethod">
                <option value="" selected disabled >{{ $t('payment.selectPayMethod') }}</option>
                <option value='카드'>{{ $t('payment.card') }}</option>
                <option value='계좌이체'>{{ $t('payment.account_transfer') }}</option>
                <option value='가상계좌'>{{ $t('payment.virtual_account') }}</option>
                <option value='토스머니'>{{ $t('payment.toss_money') }}</option>
                <option value='휴대폰'>{{ $t('payment.mobile_phone') }}</option>
                <option value='간편결제'>{{ $t('payment.easy_pay') }}</option>
            </select>
            <button class="rvt-btn w-100 mt-3 rounded-pill" @click="handleSubmit">{{ $t('payment.pay') }}</button>
        </div>
    </div>
</template>

<script>
import Swal from "sweetalert2";
import { get, post } from "../../apis/axios";
import { ref, toRaw } from 'vue';

export default {
    data() {
        return {
            unitTime: 0,
            openTime: null,
            closeTime: null,
            timeInfo: [],
            selectedTimes: [],
            firstTime: null,
            secondTime: null,
            fctName: "",
            zoneInfo: [],
            selectedZoneId: "",
            date: ref(new Date()),
            reservationTime: "",
            usageCount: 1,
            maxCount: 1,
            isSharedZone: "",
            hourlyRate: 0,
            payMethod: "",
            isPayOpen: false
        };
    },
    computed: {
        timeSlots() {
            let times = [];
            let currentTime = new Date("1970-01-01T" + this.openTime);
            let closeTime = new Date("1970-01-01T" + this.closeTime);
            
            while (currentTime <= closeTime) {
                times.push(this.formatTime(currentTime)); // 시간 문자열로 변환
                currentTime.setMinutes(currentTime.getMinutes() + this.unitTime); // 단위시간씩 더하기
            }
            return times;
        },
        formattedDate() {
            return this.date?.toISOString().split("T")[0]; // YYYY-MM-DD 형식 변환
        },
        payPrice() {
            if (this.isSharedZone === 'N') {
                return this.selectedTimes.length * this.hourlyRate;
            }
            return this.selectedTimes.length * this.usageCount * this.hourlyRate;
        },
        unavailableTimes() {
            return this.timeInfo.filter(time => time.remainUsageCount === 1).map(time => time.usageStartTime);
        },
        fctId() {
            return this.$route.params.fctId;
        },
    },
    watch: {
        selectedZoneId(newZoneId) {
            this.resetSelection();
            const selectedZone = this.zoneInfo.find(zone => zone.zoneId == newZoneId);
            this.maxCount = selectedZone ? selectedZone.maxUserCount : 1;
            this.hourlyRate = selectedZone ? selectedZone.hourlyRate : 0;
            this.isSharedZone = selectedZone ? selectedZone.isSharedZone : "";
        },
    },
    methods: {
        async getFctInfo() {
            const response = await get(`/facilities/${this.fctId}`);
            const fctInfo = response.data.content;
            this.fctName = fctInfo.fctName;
            this.zoneInfo = fctInfo.zones;
            this.openTime = fctInfo.fctOpenTime;
            this.closeTime = fctInfo.fctCloseTime;
            this.unitTime = fctInfo.unitUsageTime;
        },
        async getTimeInfo() {
            const requestDto = {
                rvtDate: this.date,
                zoneId: this.selectedZoneId,
                fctId: this.fctId
            }
            const response = await post(`/reservations/remain`, requestDto);
            this.timeInfo = response.data.content;
        },
        formatTime(date) {
            const hours = date.getHours().toString().padStart(2, '0');
            const minutes = date.getMinutes().toString().padStart(2, '0');
            return `${hours}:${minutes}`;
        },
        formatTimeRange(index) {
            const startTime = this.timeSlots[index];
            const endTime = this.timeSlots[index + 1];
            return `${startTime}~${endTime}`;
        },
        isUnavailable(time) {
            if (this.selectedZoneId === "") {
                return true;
            }
            const now = new Date();
            now.setMinutes(now.getMinutes() + 60);

            const selectedDate = new Date(this.date);
            const [hours, minutes] = time.split(":").map(Number);
            const timeDate = new Date(selectedDate);
            timeDate.setHours(hours, minutes, 0, 0);

            // 오늘 날짜인 경우에만 현재 시간 + 1시간 이후의 시간 비활성화
            const isPastTime = selectedDate.toDateString() === now.toDateString() && timeDate < now;

            return this.unavailableTimes.includes(time) || isPastTime;
        },
        selectTime(time) {
            if (!this.firstTime) {
                this.firstTime = time;
                this.selectedTimes = [time];
            } else if (!this.secondTime) {
                this.secondTime = time;
                const [start, end] = [this.firstTime, this.secondTime].sort();
                this.firstTime = start;
                this.secondTime = end;

                const startIndex = this.timeSlots.indexOf(this.firstTime);
                const endIndex = this.timeSlots.indexOf(this.secondTime);

                // 선택된 범위 내에서 이용 불가능한 시간 제외하고 남은 시간들 선택
                this.selectedTimes = this.timeSlots
                    .slice(startIndex, endIndex + 1)
                    .filter((time) => !this.isUnavailable(time));

                this.calculateSecondTime();
                this.reservationTime = this.firstTime + '~' + this.secondTime;
            } else {
                this.firstTime = time;
                this.secondTime = null;
                this.selectedTimes = [time];
            }
        },
        calculateSecondTime() {
            const [hours, minutes] = this.secondTime.split(":").map(Number);
            const secondTimeDate = new Date(0, 0, 0, hours, minutes);

            secondTimeDate.setMinutes(secondTimeDate.getMinutes() + this.unitTime);

            this.secondTime = secondTimeDate.toTimeString().slice(0, 5);
        },
        resetSelection() {
            this.selectedTimes = [];
            this.firstTime = null;
            this.secondTime = null;
            this.reservationTime = "";
            this.getTimeInfo();
        },
        increaseCount() {
            if (this.usageCount < this.maxCount) this.usageCount++;
        },
        decreaseCount() {
            if (this.usageCount > 1) this.usageCount--;
        },
        async handleSubmit() {
            const reservationDto = {
                zoneId: this.selectedZoneId,
                consId: this.$store.state.userId,
                rvtDate: this.date,
                usageStartTime: this.firstTime,
                usageEndTime: this.secondTime,
                usageCount: this.usageCount,
                unitUsageTime: this.unitTime
            }

            const paymentDto = {
                payPrice: this.payPrice,
                payMethod: this.payMethod
            }

            const requestDto = {
                reservation: reservationDto,
                payment: paymentDto
            }

            sessionStorage.setItem('reservation', JSON.stringify(requestDto));
            sessionStorage.setItem('fctId', this.fctId);
            console.log(this.zoneInfo.filter(info => info.zoneId = this.selectedZoneId)[0].zoneName)
            var tossPayments = TossPayments(import.meta.env.VITE_TOSS_CLIENT_KEY);
            const tossRequestObject = {
                amount: this.payPrice,
                orderId: 12345678,
                orderName: this.zoneInfo.filter(info => info.zoneId = this.selectedZoneId)[0].zoneName,
                customerName: this.$store.state.userName,
                successUrl: 'http://localhost:3000/payment/result',
                failUrl: 'http://localhost:3000/payment/result'
            }
            tossPayments.requestPayment(this.payMethod, tossRequestObject);
        },
        toggleIsPayOpen(){
            if (this.secondTime === null) {
                Swal.fire({
                    text: "종료 시간을 선택해주세요.",
                    width: "300px",
                    icon: "warning"
                });
                return;
            }
            this.isPayOpen = !this.isPayOpen;
        }
    },
    mounted() {
        this.getFctInfo();
    },
};
</script>

<style scoped>
::v-deep(.vc-arrow),
::v-deep(.vc-title) {
    background: #fff;
}

::v-deep(.vc-arrow),
::v-deep(.vc-title),
::v-deep(.vc-popover-content button) {
    background: transparent;
    color: #000;
}

::v-deep(.vc-nav-item.is-active) {
    background: var(--vc-nav-item-active-bg);
    color: #fff;
}

.time-buttons {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    justify-content: flex-start;
    max-width: 100%;
    margin-left: 6px;
    box-sizing: border-box;
}

.time-buttons button {
    padding: 10px 15px;
    border: 1px solid #ccc;
    border-radius: 7px;
    background: #fff;
    color: #000;
    cursor: pointer;
}

.time-buttons button.selected {
    background: #4a66e6;
    color: white;
}

button:disabled {
    background: #ddd;
    cursor: not-allowed;
    color: #aaa;
}

.profile-content {
    border-bottom: 1px solid #999;
}

select {
    border: none;
}

select option {
    background: white;
    color: black;
    font-size: 16px;
}

.rvt-btn {
    height: 45px;
}

.helper-text {
  font-size: 0.9em;
  color: #6c757d;
  margin-top: 5px;
}

.pay-select{
    border: 1px solid #333;
}
</style>