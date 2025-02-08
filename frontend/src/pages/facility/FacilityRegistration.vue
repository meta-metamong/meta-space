<template>
    <div class="container">
        <h1 class="header">{{ $t("facility.facilityRegistration") }}</h1>
    </div>
    <div class="container long-input">
        <div class="input-box">
            <input type="text" :placeholder="$t('facility.facilityName')">
        </div>
        <div class="input-box">
            <h2 class="box-title">{{ $t("facility.category") }}</h2>
            <div>
                <select @change="onChangeMajorCategory">
                    <option>{{ $t("facility.majorCategory") }}</option>
                    <option :value="category.catId" v-for="category in categories">
                        {{ category.catName }}
                    </option>
                </select>
                <select>
                    <option>{{ $t("facility.minorCategory") }}</option>
                    <option v-for="minorCategory in selectedMinorCategories"
                            :value="minorCategory.catId">
                        {{ minorCategory.catName }}
                    </option>
                </select>
            </div>
        </div>
        <div class="input-box">
            <h2 class="box-title">{{ $t("facility.facilityAddress") }}</h2>
            <div>
                <div>
                    <input type="text" :placeholder="$t('facility.postalCode')" readonly>
                    <button>{{ $t("facility.search") }}</button>
                </div>
                <input type="text" :placeholder="$t('facility.address')" readonly>
                <input type="text" :placeholder="$t('facility.detailAddress')" readonly>
            </div>
        </div>
        <div class="input-box">
            <h2 class="box-title">{{ $t("facility.facilityTel") }}</h2>
            <div class="tel">
                <select>
                    <option value="010">02</option>
                    <option value="010">010</option>
                    <option value="010">011</option>
                    <option value="010">016</option>
                    <option value="010">017</option>
                </select>
                <p>-</p>
                <input type="number">
                <p>-</p>
                <input type="number">
            </div>
        </div>
        <div class="input-box">
            <h2 class="box-title">{{ $t("facility.facilityOperationTime") }}</h2>
            <div>
                <select>
                    <option v-for="operationTimeSelection in operationTimeSelections"
                            :value="operationTimeSelection">
                        {{ operationTimeSelection }}
                    </option>
                </select>
                <p>~</p>
                <select>
                    <option v-for="operationTimeSelection in operationTimeSelections"
                            :value="operationTimeSelection">
                        {{ operationTimeSelection }}
                    </option>
                </select>
            </div>
        </div>
        <div class="input-box">
            <div>
                <p>{{ $t("facility.isOpenOnHolidays") }}</p>
            </div>
            <div>
                <input type="radio">
                <input type="radio">
            </div>
        </div>
        <div class="input-box">
            <input type="number" :placeholder="$t('facility.unitUsageTime')">
        </div>
        <div class="input-box">
            <h2 class="box-title">{{ $t("facility.facilityImage") }}</h2>
            <div>
                <button>{{ $t("facility.addImage") }}</button>
                <p>{{ $t("facility.imageLimitDescription") }}</p>
            </div>
            <div></div>
        </div>
        <div calss="input-box">
            <h2>{{ $t("facility.facilityGuide") }}</h2>
            <textarea></textarea>
        </div>
        <button type="button">{{ $t("facility.next") }}</button>
    </div>
</template>

<script>
import { get } from "../../apis/axios";

export default {
    data() {
        const operationTimeSelections = [];
        let minutes = 0;
        const convertMinutesTo24HourTime = (minute) => {
            const h = Math.floor(minutes / 60);
            const m = minute % 60;
            return `${h < 10 ? `0${h}` : h}:${m < 10 ? `0${m}` : m}`
        }
        while (minutes < 24 * 60) {
            operationTimeSelections.push(convertMinutesTo24HourTime(minutes));
            minutes += 30;
        }

        return {
            categories: [],
            selectedMinorCategories: [],
            operationTimeSelections
        }
    },
    mounted() {
        get("/categories").then((response) => {
            this.categories = response.data.content;
            console.log(this.categories);
        });
    },
    methods: {
        onChangeMajorCategory(e) {
            console.log(e.target.value);
            for (const major of this.categories) {
                console.log(major);
                if (major.catId === e.target.value) {
                    this.selectedMinorCategories = major.children;
                    console.log(this.selectedMinorCategories);
                    return;
                }
            }
            return [];
        }
    }
};
</script>

<style scoped>
.header {
    font-size: 24px;
    font-weight: bold;
    text-align: center;
}

.long-input {
    display: flex;
    flex-direction: column;
}
</style>