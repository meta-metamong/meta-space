<template>
    <div class="container w-100 mt-4">
        <h2 class="text-center mb-4" v-text="$t('reservation.reserve')"></h2>
        <div class="text-center mb-4">
            <VDatePicker v-model="date" mode="date" />
        </div>
        <div class="mb-5">
            <p class="ms-4 text-secondary">Zone</p>
            <p class="profile-content w-75 mx-auto px-3 fs-5">
                <select class="form-select">
                    <option>1</option>
                    <option>2</option>
                    <option>3</option>
                </select>
            </p>
        </div>
        <div class="time-buttons mb-4">
            <button v-for="time in timeSlots" :key="time" :class="{ selected: selectedTimes.includes(time) }"
                @click="selectTime(time)" :disabled="isUnavailable(time)">
                {{ time }}
            </button>
        </div>
        <div class="mb-4">
            <p class="ms-4 text-secondary">{{ $t('reservation.fctName') }}</p>
            <p class="profile-content w-75 mx-auto px-3 fs-5">ccc</p>
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
                <button class="btn btn-outline-secondary" @click="decreaseCount" :disabled="count <= min">−</button>
                <input type="text" class="form-control text-center mx-2" v-model="count" readonly style="width: 50px;" />
                <button class="btn btn-outline-primary" @click="increaseCount" :disabled="count >= max">+</button>
            </div>
        </div>
        <div class="mb-4">
            <p class="ms-4 text-secondary">{{ $t('reservation.additionalInfo') }}</p>
            <p class="profile-content w-75 mx-auto px-3 fs-5">
                <select class="form-select">
                    <option>1</option>
                    <option>2</option>
                    <option>3</option>
                </select>
            </p>
        </div>
        <div class="mb-4">
            <p class="ms-4 text-secondary">{{ $t('reservation.totalPrice') }}</p>
            <p class="profile-content w-75 mx-auto px-3 fs-5">{{ }}</p>
        </div>
        <div class="w-100 text-center mb-2">
            <button class="signup-btn w-75 h-75 mb-3 rounded-pill" @click="$router.push('/update')">{{
                $t('reservation.reserve') }}</button>
        </div>
    </div>
</template>

<script>
import { get } from "../../apis/axios";
import { ref } from 'vue';

export default {
    data() {
        return {
            unitTime: 30,
            startTime: new Date(0, 0, 0, 9, 0),
            endTime: new Date(0, 0, 0, 18, 0),
            unavailableTimes: ['12:00', '12:30', '13:00', '13:30'],
            selectedTimes: [],
            firstTime: null,
            secondTime: null,
            fctInfo: [],
            date: ref(new Date()),
            reservationTime: "",
            count: 1,
        };
    },
    computed: {
        timeSlots() {
            let times = [];
            let currentTime = new Date(this.startTime);

            while (currentTime < this.endTime) {
                times.push(this.formatTime(currentTime)); // 시간 문자열로 변환
                currentTime.setMinutes(currentTime.getMinutes() + this.unitTime); // 단위시간씩 더하기
            }
            return times;
        },
        formattedDate() {
            return this.date.toISOString().split("T")[0]; // YYYY-MM-DD 형식 변환
        },
    },
    props: {
        min: { type: Number, default: 1 },
        max: { type: Number, default: 10 },
    },
    methods: {
        async getFctInfo() {
            const response = await get(`/facilities/1`);
            this.fctInfo = response.data.content;
            console.log(response);
        },
        formatTime(date) {
            const hours = date.getHours().toString().padStart(2, '0');
            const minutes = date.getMinutes().toString().padStart(2, '0');
            return `${hours}:${minutes}`;
        },
        isUnavailable(time) {
            return this.unavailableTimes.includes(time);
        },
        selectTime(time) {
            if (!this.firstTime) {
                this.firstTime = time;
                this.selectedTimes = [time];
            } else if (!this.secondTime) {
                this.secondTime = time;
                const startIndex = this.timeSlots.indexOf(this.firstTime);
                const endIndex = this.timeSlots.indexOf(this.secondTime);
                const [minIndex, maxIndex] = [startIndex, endIndex].sort((a, b) => a - b);

                // 선택된 범위 내에서 이용 불가능한 시간 제외하고 남은 시간들 선택
                this.selectedTimes = this.timeSlots
                    .slice(minIndex, maxIndex + 1)
                    .filter((time) => !this.isUnavailable(time));

                this.calculateSecondTime();
                this.reservationTime = this.firstTime + '~' + this.secondTime;
            } else {
                this.firstTime = time;
                this.secondTime = null;
                this.selectedTimes = [time];
            }
            console.log(this.selectedTimes)
        },
        calculateSecondTime() {
            const [hours, minutes] = this.secondTime.split(":").map(Number);
            const secondTimeDate = new Date(0, 0, 0, hours, minutes);

            secondTimeDate.setMinutes(secondTimeDate.getMinutes() + this.unitTime);

            this.secondTime = secondTimeDate.toTimeString().slice(0, 5);
        },
        increaseCount() {
            if (this.count < this.max) this.count++;
        },
        decreaseCount() {
            if (this.count > this.min) this.count--;
        },
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
    gap: 5px;
    justify-content: center;
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
    /* Disabled button styling */
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
</style>