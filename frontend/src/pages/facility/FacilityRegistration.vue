<template>
    <facility-registration-input
        v-if="currentComponent === 'facilityRegistrationInput'"
        @component-change="changeComponent"
        :facility-registration="inputs.facilityRegistration" />
    <zone-registration-input
        v-if="currentComponent === 'zoneRegistrationInput'"
        @component-change="changeComponent"
        :zone-registration="inputs.zoneRegistration" />
    <additional-information
        v-if="currentComponent === 'addinfoRegistrationInput'"
        @component-change="changeComponent"
        :addinfo-registration="inputs.addinfoRegistration" />
</template>

<script>
import AdditionalInformation from "../../components/facility/AdditionalInformation.vue";
import FacilityRegistrationInput from "../../components/facility/FacilityRegistrationInput.vue";
import ZoneRegistrationInput from "../../components/facility/ZoneRegistrationInput.vue";

export default {
    data() {
        return {
            currentComponent: "facilityRegistrationInput",
            inputs: {
                "facilityRegistration": {
                    majorCatId: "0",
                    minorCatId: "0",
                    addr: {
                        postalCode: "",
                        address: "",
                        detailAddress: ""
                    },
                    tel: {
                        first: "02",
                        second: "",
                        third: ""
                    },
                    operationTime: {
                        openTime: "",
                        closeTime: ""
                    },
                    isOpenOnHolidays: true,
                    unitUsageTime: "",
                    images: [],
                    guide: ""
                },
                "zoneRegistration": {
                    zoneName: "",
                    maxUserCount: "",
                    hourlyRate: "",
                    isSharedZone: false,
                    images: []
                },
                "addinfoRegistration": []
            }
        };
    },
    components: {
        FacilityRegistrationInput,
        ZoneRegistrationInput,
        AdditionalInformation
    },
    methods: {
        changeComponent(componentName) {
            console.log(componentName);
            this.currentComponent = componentName;
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