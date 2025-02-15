<template>
    <div class="container">
        <div class="arrow-container">
            <i class="bi bi-arrow-left disabled-arrow" id="left-arrow"
               @click="$emit('component-change', 'zoneRegistrationInput')"></i>
        </div>
        <h2 class="text-center mb-4" style="margin-top: 15px" v-text="$isEdit === 'true' ? $t('facility.additionalInfoEdit') : $t('facility.addinfoRegistration')"></h2>
        <p class="helper-text">
            {{ $t("facility.addinfoDesc1") }}<br>
            {{ $t("facility.addinfoDesc2") }}<br>
            {{ $t("facility.addinfoDesc3") }}<br>
            {{ $t("facility.addinfoDesc4") }}
        </p>
        <div class="input-box" v-for="(item, idx) in data">
            <div class="text-input-set mb-3">
                <input type="text"
                       :value="isEdit === 'true' ? item.desc : item"
                       @change="(e) => isEdit === 'true' ? data[idx].desc = e.target.value : data[idx] = e.target.value">
                <div class="btn-delete"
                        @click="() => data.splice(idx, 1)"><i class="bi bi-trash3-fill"></i></div>
            </div>
        </div>
        <div class="mt-4">
            <button id="addinfo-button" class="w-100 signup-btn rounded-pill mb-3"
                    type="button" @click="insertAddinfo">{{ $t("facility.add") }}</button>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        addinfoRegistration: {
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
            data: this.addinfoRegistration
        }
    },
    methods: {
        insertAddinfo() {
            if (this.isEdit === "true") {
                this.data.push({ desc: "" });
            } else {
                this.data.push("");
            }
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
    background-color: #fff;
    color: #4a66e6;
    border: 1px solid #4a66e6;
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

.helper-text {
  font-size: 0.9em;
  color: #6c757d;
  margin-top: 5px;
}

.arrow-container {
    position: absolute;
    left: 5%;
    display: flex;
    gap: 8px;
}

.arrow-container i {
    font-size: 24px;
}

.btn-delete {
    font-size: 20px;
    color: #ee0000;
}
</style>