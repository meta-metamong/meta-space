<template>
    <facility-registration-input v-if="currentComponent === 'facilityRegistrationInput'"
                                 @component-change="changeComponent" />
    <zone-registration-input
        v-if="currentComponent === 'zoneRegistrationInput'"
        @component-change="changeComponent" />
    <additional-information
        v-if="currentComponent === 'addinfoRegistrationInput'"
        @component-change="changeComponent" />
</template>

<script>
import AdditionalInformation from "../../components/facility/AdditionalInformation.vue";
import FacilityRegistrationInput from "../../components/facility/FacilityRegistrationInput.vue";
import ZoneRegistrationInput from "../../components/facility/ZoneRegistrationInput.vue";

export default {
    data() {
        return {
            currentComponent: "facilityRegistrationInput"
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