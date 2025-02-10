<template>
    <div class="container header-container">
        <div class="arrow-container">
            <i class="bi bi-arrow-left disabled-arrow" id="left-arrow"
            @click="$emit('component-change', 'facilityRegistrationInput')"></i>
            <i class="bi bi-arrow-right" id="right-arrow"
            @click="$emit('component-change', 'addinfoRegistrationInput')"></i>
        </div>
        <h1 class="header">{{ $t("facility.zoneRegistration") }}</h1>
    </div>
    <div class="container zone-container">
        <div class="zone-wrapper long-input" v-for="(zone, zoneIdx) in data">
            <div class="input-box">
                <input type="text"
                    :placeholder="$t('facility.zoneName')"
                    :value="zone.zoneName"
                    @change="(e) => onValueChange(zoneIdx, e.target.value, 'zoneName')">
            </div>
            <div class="input-box">
                <input type="text"
                    :placeholder="$t('facility.maxUserCount')"
                    :value="zone.maxUserCount"
                    @change="(e) => onValueChange(zoneIdx, e.target.value, 'maxUserCount')">
            </div>
            <div class="input-box">
                <input type="text"
                    :placeholder="$t('facility.hourlyRate')"
                    :value="zone.hourlyRate"
                    @change="(e) => onValueChange(zoneIdx, e.target.value, 'hourlyRate')">
            </div>
            <div class="input-box">
                <div class="label-with-input">
                    <input type="checkbox"
                        class="form-check"
                        :checked="zone.isSharedZone"
                        @change="(e) => onValueChange(zoneIdx, e.target.checked, 'isSharedZone')">
                    <label>{{ $t("facility.isSharedZone") }}</label>
                </div>
                <p class="form-text">{{ $t("facility.sharedZoneDescription") }}</p>
            </div>
            <div class="input-box">
                <h2 class="box-title">{{ $t("facility.facilityImage") }}</h2>
                <div>
                    <button class="image-add-button"
                            @click="() => onAddImageBtnClick(zoneIdx)">
                        {{ $t("facility.addImage") }}
                    </button>
                    <p>{{ $t("facility.imageLimitDescription") }}</p>
                </div>
                <input v-for="(_, idx) in fileInputs[zoneIdx]"
                        type="file"
                        :ref="`file-${zoneIdx}-${idx}`"
                        @change="(e) => onImageUpload(zoneIdx, e)"
                        hidden>
                <div class="image-list">
                    <img v-for="image in data[zoneIdx].images" :src="image.fileDataInBase64">
                </div>
            </div>
            <div v-if="zoneIdx !== 0">
                <button type="button"
                        class="btn btn-danger"
                        @click="() => data.splice(zoneIdx, 1)"
                ><i class="bi bi-trash3-fill"></i></button>
            </div>
        </div>
        <div id="zone-button-wrapper">
            <button type="button"
                    id="zone-add-button"
                    @click="onAddZoneButtonClick">
                {{ $t("facility.addZone") }}
            </button>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        zoneRegistration: {
            type: Object,
            required: true
        },
        isEdit: {
            type: Boolean,
            required: true
        }
    },
    data() {
        return {
            data: this.zoneRegistration,
            fileInputs: []
        }
    },
    mounted() {
        const a = [];
        for (let i = 0; i < this.data.length; i++) {
            const cache = [];
            for (let j = 0; j < this.data[i].images.length + 1; j++) {
                cache.push(0);
            }
            a.push(cache);
        }
        this.fileInputs = a;
    },
    methods: {
        onAddImageBtnClick(zoneIdx) {
            const indexToOpen = this.data[zoneIdx].images.length;
            console.log(indexToOpen);
            console.log(this.$refs);
            console.log(this.fileInputs);
            this.$refs[`file-${zoneIdx}-${indexToOpen}`][0].click();
        },
        onImageUpload(zoneIdx, e) {
            const fileReader = new FileReader();
            fileReader.onload = () => {
                const filename = e.target.files[0].name;
                this.data[zoneIdx].images.push({
                    fileExtension: filename.substring(filename.lastIndexOf(".") + 1),
                    fileDataInBase64: fileReader.result,
                    fileData: e.target.files[0]
                });
                this.fileInputs[zoneIdx] = [...this.fileInputs[zoneIdx], 0];
                console.log(this.fileInputs[zoneIdx]);
            }
            fileReader.readAsDataURL(e.target.files[0]);
        },
        onValueChange(zoneIdx, val, dataRef) {
            const ladder = dataRef.split(".");
            let obj = this.data[zoneIdx];
            for (let i = 0; i < ladder.length - 1; i++) {
                obj = obj[ladder[i]];
            }
            obj[ladder[ladder.length - 1]] = val;
            console.log(this.data);
        },
        onAddZoneButtonClick() {
            this.data.push({
                zoneName: '',
                maxUserCount: '',
                hourlyRate: '',
                isSharedZone: false,
                images: []
            });
            this.fileInputs.push([0]);
        }
    }
}
</script>

<style lang="css" scoped>
.zone-container {
    display: flex;
    flex-direction: column;
    gap: 8px;
}

.zone-wrapper {
    padding: 16px 12px;
    border: 1px solid #999999;
    border-radius: 8px;
}

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

#zone-button-wrapper {
    display: flex;
    justify-content: center;
}

#zone-add-button {
    border-radius: 100px;
    background-color: #19319d;
    font-size: 18px;
    padding: 6px 36px;
    margin-left: auto;
    margin-right: auto;
}

.btn-page-move {
    background-color: #19319d;
    padding-top: 8px;
    padding-bottom: 8px;
    border-radius: 8px;
    font-size: 18px;
}

#page-move-button-container {
    display: flex;
    justify-content: center;
    flex-direction: column;
}
.header-container {
    position: relative;
    margin-bottom: 18px;
}

.arrow-container {
    position: absolute;
    left: 5%;
    display: flex;
    gap: 8px;
}

.arrow-container i {
    font-size: 24px;
    background-color: #19319d;
    color: #fff;
    padding: 0px 6px;
    border-radius: 5px;
}
</style>