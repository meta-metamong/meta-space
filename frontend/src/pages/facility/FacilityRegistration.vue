<template>
    <!-- <facility-registration-input v-if="currentComponent === 'facilityRegistrationInput'"
                                 @component-change="changeComponent" />
    <zone-registration-input
        v-if="currentComponent === 'zoneRegistrationInput'"
        @component-change="changeComponent" /> -->
    <zone-registration-input />
</template>

<script>
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
        ZoneRegistrationInput
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