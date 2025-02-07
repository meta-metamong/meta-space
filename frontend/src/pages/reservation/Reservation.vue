<template>
    <div class="container w-100 mt-4">
        <div class="text-center mb-5">
            <VDatePicker v-model="date" mode="date" />
        </div>
        <div class="time-buttons mb-4">
            <button v-for="time in timeSlots" :key="time" :class="{ selected: selectedTimes.includes(time) }"
                @click="selectTime(time)" :disabled="isUnavailable(time)">
                {{ time }}
            </button>
        </div>
        <div class="d-flex align-items-center mb-4">
            <div class="col-sm-4 me-4">
                <span class="info-label">{{ $t('reservation.id') }}</span>
            </div>
            <div class="col-sm-8">
                <!-- <span>{{ rvt.rvtId }}</span> -->
            </div>
        </div>
        <div>{{ date }}</div>
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
        availableTimeSlots() {
            return this.timeSlots;
        },
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
            } else {
                this.firstTime = time;
                this.secondTime = null;
                this.selectedTimes = [time];
            }
            console.log(this.selectedTimes)
            console.log(this.date)
        },
    },
    mounted() {
        this.getFctInfo();
    },
};
</script>

<style>
.time-buttons {
    display: flex;
    flex-wrap: wrap;
    gap: 5px;
    justify-content: center;
}

button {
    padding: 10px 15px;
    border: 1px solid #ccc;
    background: #fff;
    cursor: pointer;
}

button.selected {
    background: #4caf50;
    color: white;
}

button:disabled {
    background: #ddd;
    cursor: not-allowed;
    color: #aaa;
    /* Disabled button styling */
}

.info-item {
	margin-bottom: 15px;
}
.info-label {
	font-weight: bold;
	/* margin-bottom: 5px; */
	display: block;
}
</style>