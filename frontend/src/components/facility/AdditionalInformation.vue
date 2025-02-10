<template>
    <div class="container header-container">
        <div class="arrow-container">
            <i class="bi bi-arrow-left disabled-arrow" id="left-arrow"
               @click="$emit('component-change', 'zoneRegistrationInput')"></i>
        </div>
        <h1 class="header">{{ $t("facility.addinfoRegistration") }}</h1>
    </div>
    <div class="container long-input">
        <p class="form-text">
            {{ $t("facility.addinfoDesc1") }}<br>
            {{ $t("facility.addinfoDesc2") }}<br>
            {{ $t("facility.addinfoDesc3") }}<br>
            {{ $t("facility.addinfoDesc4") }}
        </p>
        <div class="input-box" v-for="(item, idx) in data">
            <div class="text-input-set">
                <input type="text"
                       :value="item"
                       @change="(e) => data[idx] = e.target.value">
                <button type="button"
                        class="btn btn-danger"
                        @click="() => data.splice(idx, 1)"><i class="bi bi-trash3-fill"></i></button>
            </div>
        </div>
        <button id="addinfo-button"
                type="button" @click="() => data.push('')">{{ $t("facility.add") }}</button>
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
            data: this.addinfoRegistration
        }
    }
}
</script>

<style scoped>

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

#addinfo-button {
    border-radius: 100px;
    background-color: #19319d;
    font-size: 18px;
    padding: 6px 36px;
    margin-left: auto;
    margin-right: auto;
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
    margin-top: 16px;
    display: flex;
    justify-content: center;
    flex-direction: column;
}

.text-input-set {
    display: flex;
    gap: 8px;
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