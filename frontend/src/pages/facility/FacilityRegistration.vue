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
                <select>
                    
                </select>
            </div>
        </div>
    </div>
</template>

<script>
export default {

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