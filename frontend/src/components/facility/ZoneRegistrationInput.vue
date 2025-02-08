<template>
    <div class="container">
        <h1 class="header">{{ $t("facility.zoneRegistration") }}</h1>
    </div>
    <div class="container long-input">
        <div class="input-box">
            <input type="text" :placeholder="$t('facility.zoneName')">
        </div>
        <div class="input-box">
            <input type="text" :placeholder="$t('facility.maxUserCount')">
        </div>
        <div class="input-box">
            <input type="text" :placeholder="$t('facility.hourlyRate')">
        </div>
        <div class="input-box">
            <div>
                <input type="checkbox">
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
            <div></div>
        </div>
        <!-- TODO: Zone 추가 -->
        <div>
            <button type="button">{{ $t("facility.previous") }}</button>
            <button type="button">{{ $t("facility.next") }}</button>
        </div>
    </div>
</template>

<script>
export default {
    
}
</script>