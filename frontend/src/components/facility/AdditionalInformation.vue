<template>
    <div class="container">
        <h1 class="header">{{ $t("facility.addinfoRegistration") }}</h1>
    </div>
    <div class="container long-input">
        <p>
            {{ $t("facility.addinfoDesc1") }}<br>
            {{ $t("facility.addinfoDesc2") }}<br>
            {{ $t("facility.addinfoDesc3") }}<br>
            {{ $t("facility.addinfoDesc4") }}
        </p>
        <div class="input-box" v-for="item in data">
            <input type="text">
        </div>
        <button type="button">{{ $t("facility.add") }}</button>
    </div>
    <div>
        <button type="button" @click="$emit('component-change', 'zoneRegistrationInput')">{{ $t("facility.previous") }}</button>
        <button type="button" @click="registerFacility">{{ $t("facility.register") }}</button>
    </div>
</template>

<script>
export default {
    props: {
        addinfoRegistration: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            data: {...this.addinfoRegistration}
        }
    },
    methods: {
        registerFacility() {

        }
    }
}
</script>