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

    <div style="display: flex; justify-content: center;">
        <button v-if="currentComponent === 'addinfoRegistrationInput'"
                class="btn btn-outline-success"
                type="button"
                id="register-button"
                @click="registerFacility">
            {{ $t("facility.register") }}
        </button>
    </div>
</template>

<script>
import { get } from "../../apis/axios";
import AdditionalInformation from "../../components/facility/AdditionalInformation.vue";
import FacilityRegistrationInput from "../../components/facility/FacilityRegistrationInput.vue";
import ZoneRegistrationInput from "../../components/facility/ZoneRegistrationInput.vue";

export default {
    props: ["fctId"],
    data() {
        get(`/facilities/${this.fctId}`).then((response) => {
            const responseBody = response.data;
            const content = responseBody.content;
            
        });


        return {
            currentComponent: "facilityRegistrationInput",
            inputs: {
                "facilityRegistration": {
                    fctName: "",
                    majorCatId: "0",
                    minorCatId: "0",
                    addr: {
                        // postalCode: "",
                        // address: "",
                        postalCode: "01234",
                        address: "ADDR",
                        detailAddress: ""
                    },
                    tel: {
                        first: "02",
                        second: "",
                        third: ""
                    },
                    operationTime: {
                        openTime: "00:00",
                        closeTime: "00:00"
                    },
                    isOpenOnHolidays: true,
                    unitUsageTime: "",
                    images: [],
                    guide: ""
                },
                "zoneRegistration": [
                    {
                        zoneName: "",
                        maxUserCount: "",
                        hourlyRate: "",
                        isSharedZone: false,
                        images: []
                    }
                ],
                "addinfoRegistration": []
            }
        }
    },
    components: {
        FacilityRegistrationInput,
        ZoneRegistrationInput,
        AdditionalInformation
    },
    mounted() {
        
    }
}
</script>