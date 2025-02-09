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
            <div>
                <select @change="onChangeMajorCategory">
                    <option>{{ $t("facility.majorCategory") }}</option>
                    <option :value="category.catId" v-for="category in categories"
                            :selected="data.majorCatId === category.catId">
                        {{ category.catName }}
                    </option>
                </select>
                <select @change="(e) => onValueChange(e.target.value, 'minorCatId')">
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
            <div>
                <div>
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
            <div class="tel">
                <select @change="(e) => onValueChange(e.target.value, 'tel.first')">
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
                <input type="number"
                       :value="data.tel.second"
                       @change="(e) => onValueChange(e.target.value, 'tel.second')">
                <p>-</p>
                <input type="number"
                       :value="data.tel.third"
                       @change="(e) => onValueChange(e.target.value, 'tel.third')">
            </div>
        </div>
        <div class="input-box">
            <h2 class="box-title">{{ $t("facility.facilityOperationTime") }}</h2>
            <div>
                <select @change="(e) => onValueChange(e.target.value, 'operationTime.openTime')">
                    <option v-for="operationTimeSelection in operationTimeSelections"
                            :value="operationTimeSelection"
                            :selected="data.operationTime.openTime">
                        {{ operationTimeSelection }}
                    </option>
                </select>
                <p>~</p>
                <select @change="(e) => onValueChange(e.target.value, 'operationTime.closeTime')">
                    <option v-for="operationTimeSelection in operationTimeSelections"
                            :value="operationTimeSelection"
                            :selected="data.operationTime.closeTime">
                        {{ operationTimeSelection }}
                    </option>
                </select>
            </div>
        </div>
        <div class="input-box">
            <div>
                <p>{{ $t("facility.isOpenOnHolidays") }}</p>
                <input type="checkbox"
                        name="isOpenOnHolidays"
                        :checked="data.isOpenOnHolidays"
                        @change="(e) => onValueChange(e.target.checked, 'isOpenOnHolidays')">
            </div>
        </div>
        <div class="input-box">
            <input type="number" :placeholder="$t('facility.unitUsageTime')"
                   :value="data.unitUsageTime">
        </div>
        <div class="input-box">
            <h2 class="box-title">{{ $t("facility.facilityImage") }}</h2>
            <div>
                <button @click="onAddImageBtnClick">{{ $t("facility.addImage") }}</button>
                <p>{{ $t("facility.imageLimitDescription") }}</p>
            </div>
            <div>
                <input type="file"
                       ref="file-0"
                       @change="(e) => onImageUpload(e, 0)"
                       hidden>
                <input v-for="(image, index) in this.data.images"
                       type="file"
                       :ref="`file-${index + 1}`"
                       @change="(e) => onImageUpload(e, index)"
                       hidden>
                <img v-for="image in data.images" :src="image.fileData">
            </div>
        </div>
        <div calss="input-box">
            <h2>{{ $t("facility.facilityGuide") }}</h2>
            <textarea :value="data.guide" @change="(e) => onValueChange(e.target.value, 'guide')"></textarea>
        </div>
        <button type="button" @click="$emit('component-change', 'zoneRegistrationInput')">{{ $t("facility.next") }}</button>
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
            data: this.facilityRegistration
        }
    },
    mounted() {
        get("/categories").then((response) => {
            this.categories = response.data.content;
        });
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
            this.$refs[`file-${indexToOpen}`].click();
        },
        onImageUpload(e, imageNo) {
            const fileReader = new FileReader();
            fileReader.onload = () => {
                const filename = e.target.files[0].name;
                this.data.images.push({
                    fileExtension: filename.substring(filename.lastIndexOf(".") + 1),
                    fileData: fileReader.result
                });
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
            console.log(this.data);
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