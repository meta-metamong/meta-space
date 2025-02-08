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
                <button>{{ $t("facility.addImage") }}</button>
                <p>{{ $t("facility.imageLimitDescription") }}</p>
            </div>
            <div>
                <img v-for="image in data.images">
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
            data: {...this.zoneRegistration}
        }
    }
}
</script>