<template>
    <div class="container">
        <h1 class="header">{{ $t("facility.facilityRegistration") }}</h1>
    </div>
    <div class="container long-input">
        <div class="input-box">
            <input type="text"
                   :placeholder="$t('facility.facilityName')"
                   :value="data.fctName"
                   @change="(e) => onValueChange(e.target.value, 'fctName')">
        </div>
        <div class="input-box">
            <h2 class="box-title">{{ $t("facility.category") }}</h2>
            <div class="category-select-box">
                <select class="form-select" @change="onChangeMajorCategory">
                    <option>{{ $t("facility.majorCategory") }}</option>
                    <option :value="category.catId" v-for="category in categories"
                            :selected="data.majorCatId === category.catId">
                        {{ category.catName }}
                    </option>
                </select>
                <select class="form-select" @change="(e) => onValueChange(e.target.value, 'minorCatId')">
                    <option>{{ $t("facility.minorCategory") }}</option>
                    <option v-for="minorCategory in selectedMinorCategories"
                            :value="minorCategory.catId"
                            :selected="data.minorCatId === minorCategory.catId">
                        {{ minorCategory.catName }}
                    </option>
                </select>
            </div>
        </div>
        <div class="input-box">
            <h2 class="box-title">{{ $t("facility.facilityAddress") }}</h2>
            <div class="facility-address-container">
                <div class="postal-code">
                    <input type="text"
                           :placeholder="$t('facility.postalCode')"
                           :value="data.addr.postalCode"
                           readonly>
                    <button>{{ $t("facility.search") }}</button>
                </div>
                <input type="text"
                       :placeholder="$t('facility.address')"
                       :value="data.addr.address"
                       readonly>
                <input type="text"
                       :placeholder="$t('facility.detailAddress')"
                       :value="data.addr.detailAddress"
                       @change="(e) => onValueChange(e.target.value, 'addr.detailAddress')">
            </div>
        </div>
        <div class="input-box">
            <h2 class="box-title">{{ $t("facility.facilityTel") }}</h2>
            <div class="horizontal-input">
                <select class="form-select"
                        @change="(e) => onValueChange(e.target.value, 'tel.first')">
                    <option value="02"
                            :selected="data.tel.first === '02'">02</option>
                    <option value="010"
                            :selected="data.tel.first === '010'">010</option>
                    <option value="011"
                            :selected="data.tel.first === '011'">011</option>
                    <option value="016"
                            :selected="data.tel.first === '016'">016</option>
                    <option value="017"
                            :selected="data.tel.first === '017'">017</option>
                </select>
                <p>-</p>
                <input class="form-control"
                       type="number"
                       :value="data.tel.second"
                       @change="(e) => onValueChange(e.target.value, 'tel.second')">
                <p>-</p>
                <input class="form-control"
                       type="number"
                       :value="data.tel.third"
                       @change="(e) => onValueChange(e.target.value, 'tel.third')">
            </div>
        </div>
        <div class="input-box">
            <h2 class="box-title">{{ $t("facility.facilityOperationTime") }}</h2>
            <div class="horizontal-input">
                <select class="form-select"
                        @change="(e) => onValueChange(e.target.value, 'operationTime.openTime')">
                    <option v-for="operationTimeSelection in operationTimeSelections"
                            :value="operationTimeSelection"
                            :selected="data.operationTime.openTime === operationTimeSelection">
                        {{ operationTimeSelection }}
                    </option>
                </select>
                <p>~</p>
                <select class="form-select"
                        @change="(e) => onValueChange(e.target.value, 'operationTime.closeTime')">
                    <option v-for="operationTimeSelection in operationTimeSelections"
                            :value="operationTimeSelection"
                            :selected="data.operationTime.closeTime === operationTimeSelection">
                        {{ operationTimeSelection }}
                    </option>
                </select>
            </div>
        </div>
        <div class="input-box">
            <div class="label-with-input">
                <label for="is-open-on-holidays">{{ $t("facility.isOpenOnHolidays") }}</label>
                <input id="is-open-on-holidays"
                       class="form-check"
                       type="checkbox"
                       name="isOpenOnHolidays"
                       :checked="data.isOpenOnHolidays"
                       @change="(e) => onValueChange(e.target.checked, 'isOpenOnHolidays')">
            </div>
        </div>
        <div class="input-box">
            <input type="number" :placeholder="$t('facility.unitUsageTime')"
                   @change="(e) => onValueChange(e.target.value, 'unitUsageTime')"
                   :value="data.unitUsageTime">
        </div>
        <div class="input-box">
            <h2 class="box-title">{{ $t("facility.facilityImage") }}</h2>
            <div>
                <button class="image-add-button" @click="onAddImageBtnClick">{{ $t("facility.addImage") }}</button>
                <p style="color: #999999;">{{ $t("facility.imageLimitDescription") }}</p>
            </div>
            <input v-for="(_, index) in fileInput"
                       type="file"
                       :ref="`file-${index}`"
                       @change="(e) => onImageUpload(e, index)"
                       hidden>
            <div class="image-list">
                <div class="image-box" v-for="image in data.images">
                    <img :src="image.fileData">
                </div>
            </div>
        </div>
        <div class="input-box form-floating">
            <textarea class="form-control"
                      id="guide-input"
                      :value="data.guide"
                      @change="(e) => onValueChange(e.target.value, 'guide')"></textarea>
            <label for="guide-input">{{ $t("facility.facilityGuide") }}</label>
        </div>
        <button class="btn-page-move"
                type="button"
                @click="$emit('component-change', 'zoneRegistrationInput')">
            {{ $t("facility.next") }}
        </button>
    </div>
</template>

<script>
import { get } from "../../apis/axios";

export default {
    props: {
        facilityRegistration: {
            type: Object,
            required: true
        }
    },
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
            operationTimeSelections,
            data: this.facilityRegistration,
            fileInput: []
        }
    },
    mounted() {
        get("/categories").then((response) => {
            this.categories = response.data.content;
        });
        const a = [];
        for (let i = 0; i < this.data.images.length + 1; i++) {
            a.push(0);
        }
        this.fileInput = a;
    },
    methods: {
        onChangeMajorCategory(e) {
            console.log(e.target.value);
            this.onValueChange(e.target.value, "majorCatId");
            for (const major of this.categories) {
                console.log(major);
                if (major.catId === e.target.value) {
                    this.selectedMinorCategories = major.children;
                    console.log(this.selectedMinorCategories);
                    return;
                }
            }
            return [];
        },
        onAddImageBtnClick() {
            const indexToOpen = this.data.images.length;
            console.log(this.$refs);
            this.$refs[`file-${indexToOpen}`][0].click();
        },
        onImageUpload(e, imageNo) {
            const fileReader = new FileReader();
            fileReader.onload = () => {
                const filename = e.target.files[0].name;
                this.data.images.push({
                    fileExtension: filename.substring(filename.lastIndexOf(".") + 1),
                    fileData: fileReader.result
                });
                this.fileInput.push(0);
                console.log(this.data.images);
            }
            fileReader.readAsDataURL(e.target.files[0]);
        },
        onValueChange(val, dataRef) {
            const ladder = dataRef.split(".");
            let obj = this.data;
            for (let i = 0; i < ladder.length - 1; i++) {
                obj = obj[ladder[i]];
            }
            obj[ladder[ladder.length - 1]] = val;
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
    padding-left: 5%;
    padding-right: 5%;
    gap: 24px;
}

.input-box input[type="text"], .input-box input[type="number"] {
    border: none;
    border-bottom: 1px solid #999999;
    font-size: 1rem;
    padding-bottom: 0.3rem;
    padding-left: 0.5rem;
    padding-right: 0.5rem;
    width: 100%;
}

.box-title {
    font-size: 1rem;
}

.category-select-box {
    display: flex;
    flex-direction: column;
    gap: 12px
}

.category-select-box select {
    color: #999999
}

.postal-code {
    display: flex;
    gap: 15px;
}

.postal-code button {
    width: 150px;
    border-radius: 10px;
    background-color: #19319d;
}

.facility-address-container {
    display: flex;
    flex-direction: column;
    gap: 8px
}

.horizontal-input {
    display: flex;
    width: 100%;
    justify-content: center;
    align-items: center;
    gap: 8px
}

.horizontal-input p {
    padding: 0;
    margin: 0;
}

.horizontal-input .form-select {
    width: fit-content;
}

.label-with-input {
    display: flex;
    align-items: center;
    gap: 15px;
}

.label-with-input input[type="checkbox"] {
    width: 1.1rem;
}

.image-add-button {
    border-radius: 10px;
    background-color: #19319d;
}

.image-list {
    display: flex;
    flex-direction: column;
    gap: 8px;
}

.image-list img {
    width: 70%;
    max-height: 500px;
}

#guide-input {
    width: 100%;
    height: 400px;
}

.btn-page-move {
    background-color: #19319d;
    padding-top: 8px;
    padding-bottom: 8px;
    border-radius: 8px;
    font-size: 18px;
}
</style>