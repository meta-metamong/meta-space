<template>
    <div class="container">
        <h2 class="text-center mb-4" style="margin-top: 15px" v-text="isEdit === 'true' ? $t('facility.facilityEdit') : $t('facility.facilityRegistration')"></h2>

        <div class="mb-4">
            <input class="signup-input w-100" type="text" :placeholder="$t('facility.facilityName')"
                :value="data.fctName" @change="(e) => onValueChange(e.target.value, 'fctName')">
        </div>
        <div class="mb-4">
            <p class="text-black">{{ $t('facility.category') }}</p>
            <select class="form-select mb-2" @change="onChangeMajorCategory">
                <option>{{ $t("facility.majorCategory") }}</option>
                <option :value="category.catId" v-for="category in categories"
                    :selected="data.majorCatId === category.catId">
                    {{ category.catName }}
                </option>
            </select>
            <select class="form-select mb-4" @change="(e) => onValueChange(e.target.value, 'minorCatId')">
                <option>{{ $t("facility.minorCategory") }}</option>
                <option v-for="minorCategory in data.selectedMinorCategories" :value="minorCategory.catId"
                    :selected="data.minorCatId === minorCategory.catId">
                    {{ minorCategory.catName }}
                </option>
            </select>
        </div>
        <div class="mb-3">
            <p class="text-black">{{ $t('facility.facilityAddress') }}</p>
            <input type="text" class="signup-input w-75" :placeholder="$t('facility.postalCode')"
                :value="data.addr.postalCode" readonly>
            <button type="button" class="w-25 h-50 custom-btn" @click="searchPostCode">{{ $t('button.search')
                }}</button>
        </div>
        <div class="mb-3">
            <input type="text" class="signup-input w-100" :placeholder="$t('facility.address')"
                :value="data.addr.address" readonly>
        </div>
        <div class="mb-4">
            <input type="text" class="signup-input w-100" :placeholder="$t('facility.detailAddress')"
                :value="data.addr.detailAddress" @change="(e) => onValueChange(e.target.value, 'addr.detailAddress')">
        </div>
        <div class="mb-4">
            <p class="text-black">{{ $t('facility.facilityTel') }}</p>
            <div class="horizontal-input">
                <select class="form-select" @change="(e) => onValueChange(e.target.value, 'tel.first')">
                    <option value="02" :selected="data.tel.first === '02'">02</option>
                    <option value="010" :selected="data.tel.first === '010'">010</option>
                    <option value="011" :selected="data.tel.first === '011'">011</option>
                    <option value="016" :selected="data.tel.first === '016'">016</option>
                    <option value="017" :selected="data.tel.first === '017'">017</option>
                </select>
                <p>-</p>
                <input class="form-control" type="number" :value="data.tel.second"
                    @change="(e) => onValueChange(e.target.value, 'tel.second')">
                <p>-</p>
                <input class="form-control" type="number" :value="data.tel.third"
                    @change="(e) => onValueChange(e.target.value, 'tel.third')">
            </div>
        </div>
        <div class="mb-4">
            <p class="text-black">{{ $t('facility.facilityOperationTime') }}</p>
            <div class="horizontal-input">
                <select class="form-select" @change="(e) => onValueChange(e.target.value, 'operationTime.openTime')">
                    <option v-for="operationTimeSelection in operationTimeSelections" :value="operationTimeSelection"
                        :selected="data.operationTime.openTime === operationTimeSelection">
                        {{ operationTimeSelection }}
                    </option>
                </select>
                <p>~</p>
                <select class="form-select" @change="(e) => onValueChange(e.target.value, 'operationTime.closeTime')">
                    <option v-for="operationTimeSelection in operationTimeSelections" :value="operationTimeSelection"
                        :selected="data.operationTime.closeTime === operationTimeSelection">
                        {{ operationTimeSelection }}
                    </option>
                </select>
            </div>
        </div>
        <div class="mb-4">
            <input class="signup-input w-100" type="number" :placeholder="$t('facility.unitUsageTime')"
                @change="(e) => onValueChange(e.target.value, 'unitUsageTime')" :value="data.unitUsageTime">
        </div>
        <div class="form-section mb-4">
            <input id="is-open-on-holidays" type="checkbox" name="isOpenOnHolidays" :checked="data.isOpenOnHolidays"
                @change="(e) => onValueChange(e.target.checked, 'isOpenOnHolidays')">
            {{ $t("facility.isOpenOnHolidays") }}
        </div>
        <div class="mb-4">
            <label>{{ $t('facility.facilityImage') }}</label>
            <div class="image-upload-area" @click="onAddImageBtnClick">
                <p class="mb-0 mt-2">{{ $t("facility.addImage") }}</p>
                <p class="helper-text">{{ $t("facility.imageLimitDescription") }}</p>
            </div>
            <div class="uploaded-images">
                <input v-for="(_, index) in fileInput" type="file" :ref="`file-${index}`"
                    @change="(e) => onImageUpload(e, index)" hidden>
                <div class="image-list">
                    <div class="image-box" v-for="(image, idx) in data.images">
                        <img :src="image.fileDataInBase64">
                        <i class="bi bi-x-circle-fill del-button"
                           style="color: red;"
                           @click="() => data.images.splice(idx, 1)"></i>
                    </div>
                </div>
            </div>
        </div>
        <div class="mb-4">
            <p class="text-black">{{ $t('facility.facilityGuide') }}</p>
            <textarea class="form-control" id="guide-input" :value="data.guide"
                @change="(e) => onValueChange(e.target.value, 'guide')" placeholder="이용 수칙을 입력해주세요."></textarea>
        </div>
        <button type="button" class="w-100 signup-btn rounded-pill mb-3"
            @click="$emit('component-change', 'zoneRegistrationInput')">{{ $t('signup.next') }}</button>
    </div>
</template>

<script>
import { get } from "../../apis/axios";

export default {
    props: {
        facilityRegistration: {
            type: Object,
            required: true
        },
        isEdit: {
            type: Boolean,
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
            this.onValueChange(e.target.value, "majorCatId");
            for (const major of this.categories) {
                if (major.catId === e.target.value) {
                    this.data.selectedMinorCategories = major.children;
                    return;
                }
            }
            return [];
        },
        onAddImageBtnClick() {
            const indexToOpen = this.data.images.length;
            this.$refs[`file-${indexToOpen}`][0].click();
        },
        onImageUpload(e, imageNo) {
            const fileReader = new FileReader();
            fileReader.onload = () => {
                const filename = e.target.files[0].name;
                this.data.images.push({
                    fileExtension: filename.substring(filename.lastIndexOf(".") + 1),
                    fileDataInBase64: fileReader.result,
                    fileData: e.target.files[0]
                });
                this.fileInput.push(0);
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
        },
        searchPostCode() {
            new daum.Postcode({
                oncomplete: (data) => {
                    this.data.addr.postalCode = data.zonecode;
                    this.data.addr.address = data.userSelectedType === 'R' ? data.address : data.jibunAddress;
                }
            }).open();
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

.input-box input[type="text"],
.input-box input[type="number"] {
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

/* 이미지 업로드 영역 */
.image-upload-area {
    padding: 10px;
    border: 2px dashed #ddd;
    border-radius: 5px;
    text-align: center;
    margin-top: 10px;
}

.uploaded-images {
    display: flex;
    gap: 10px;
    margin-top: 10px;
}

.helper-text {
    font-size: 0.9em;
    color: #6c757d;
    margin-top: 5px;
}

.image-box {
    position: relative;
}

.image-box .del-button {
    position: absolute;
    left: 3px;
    font-size: 25px;
    padding: 0px;
    margin: 0px;
}
</style>