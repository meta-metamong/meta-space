<template>
    <div class="container">
        <h1 class="header">{{ $t("facility.zoneRegistration") }}</h1>
    </div>
    <div class="container long-input">
        <div v-for="(zone, zoneIdx) in data">
            <div class="input-box">
                <input type="text"
                    :placeholder="$t('facility.zoneName')"
                    :value="zone.zoneName"
                    @change="(e) => onValueChange(zoneIdx, e.target.value, 'zoneName')">
            </div>
            <div class="input-box">
                <input type="text"
                    :placeholder="$t('facility.maxUserCount')"
                    :value="data.maxUserCount"
                    @change="(e) => onValueChange(zoneIdx, e.target.value, 'maxUserCount')">
            </div>
            <div class="input-box">
                <input type="text"
                    :placeholder="$t('facility.hourlyRate')"
                    :value="data.hourlyRate"
                    @change="(e) => onValueChange(zoneIdx, e.target.value, 'hourlyRate')">
            </div>
            <div class="input-box">
                <div>
                    <input type="checkbox"
                        :checked="data.isSharedZone"
                        @change="(e) => onValueChange(zoneIdx, e.target.checked, 'isSharedZone')">
                    <label>{{ $t("facility.isSharedZone") }}</label>
                </div>
                <p>{{ $t("facility.sharedZoneDescription") }}</p>
            </div>
            <div class="input-box">
                <h2 class="box-title">{{ $t("facility.facilityImage") }}</h2>
                <div>
                    <button @click="() => onAddImageBtnClick(zoneIdx)">{{ $t("facility.addImage") }}</button>
                    <p>{{ $t("facility.imageLimitDescription") }}</p>
                </div>
                <div>
                    <input v-for="(_, idx) in fileInputs"
                        type="file"
                        :ref="`file-${zoneIdx}-${idx}`"
                        @change="(e) => onImageUpload(zoneIdx, e)"
                        hidden>
                    <img v-for="image in data[zoneIdx].images" :src="image.fileData">
                </div>
            </div>
        </div>
        <button type="button" @click="onAddZoneButtonClick">{{ $t("facility.addZone") }}</button>
        <div>
            <button type="button" @click="$emit('component-change', 'facilityRegistrationInput')">{{ $t("facility.previous") }}</button>
            <button type="button" @click="$emit('component-change', 'addinfoRegistrationInput')">{{ $t("facility.next") }}</button>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        zoneRegistration: {
            type: Object,
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
            this.$refs[`file-${zoneIdx}-${indexToOpen}`][0].click();
        },
        onImageUpload(zoneIdx, e) {
            const fileReader = new FileReader();
            fileReader.onload = () => {
                const filename = e.target.files[0].name;
                this.data[zoneIdx].images.push({
                    fileExtension: filename.substring(filename.lastIndexOf(".") + 1),
                    fileData: fileReader.result
                });
                this.fileInputs[zoneIdx].push(0);
                console.log("zoneIdx=" + zoneIdx);
                console.log(this.data[zoneIdx].images);
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