<template>
    <div class="container">
        <h1 class="header">{{ $t("facility.zoneRegistration") }}</h1>
    </div>
    <div class="container long-input">
        <div class="input-box">
            <input type="text" :placeholder="$t('facility.zoneName')" :value="data.zoneName">
        </div>
        <div class="input-box">
            <input type="text" :placeholder="$t('facility.maxUserCount')" :value="data.maxUserCount">
        </div>
        <div class="input-box">
            <input type="text" :placeholder="$t('facility.hourlyRate')" :value="data.hourlyRate">
        </div>
        <div class="input-box">
            <div>
                <input type="checkbox" :checked="data.isSharedZone">
                <label>{{ $t("facility.isSharedZone") }}</label>
            </div>
            <p>{{ $t("facility.sharedZoneDescription") }}</p>
        </div>
        <div class="input-box">
            <h2 class="box-title">{{ $t("facility.facilityImage") }}</h2>
            <div>
                <button @click="onAddImageBtnClick">{{ $t("facility.addImage") }}</button>
                <p>{{ $t("facility.imageLimitDescription") }}</p>
            </div>
            <div>
                <input v-for="(_, idx) in fileInputs"
                       type="file"
                       :ref="`file-${idx}`"
                       @change="(e) => onImageUpload(e, index)"
                       hidden>
                <img v-for="image in data.images" :src="image.fileData">
            </div>
        </div>
        <!-- TODO: Zone 추가 -->
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
            data: {...this.zoneRegistration},
            fileInputs: [0]
        }
    },
    methods: {
        onAddImageBtnClick() {
            const indexToOpen = this.data.images.length;
            console.log(indexToOpen);
            console.log(this.$refs);
            this.$refs[`file-${indexToOpen}`][0].click();
        },
        onImageUpload(e) {
            const fileReader = new FileReader();
            fileReader.onload = () => {
                const filename = e.target.files[0].name;
                this.data.images.push({
                    fileExtension: filename.substring(filename.lastIndexOf(".") + 1),
                    fileData: fileReader.result
                });
                this.fileInputs.push(0);
                console.log(this.data.images);
            }
            fileReader.readAsDataURL(e.target.files[0]);
        }
    }
}
</script>