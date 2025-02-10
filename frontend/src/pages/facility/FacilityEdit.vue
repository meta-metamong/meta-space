<template>
    <facility-registration-input
        v-if="currentComponent === 'facilityRegistrationInput'"
        @component-change="changeComponent"
        :facility-registration="inputs.facilityRegistration"
        is-edit="true" />
    <zone-registration-input
        v-if="currentComponent === 'zoneRegistrationInput'"
        @component-change="changeComponent"
        :zone-registration="inputs.zoneRegistration"
        is-edit="true" />
    <additional-information
        v-if="currentComponent === 'addinfoRegistrationInput'"
        @component-change="changeComponent"
        :addinfo-registration="inputs.addinfoRegistration"
        is-edit="true" />

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
            },
            original: null
        }
    },
    components: {
        FacilityRegistrationInput,
        ZoneRegistrationInput,
        AdditionalInformation
    },
    mounted() {
        const getData = async () => {
            const categories = (await get("/categories")).data.content;

            const fct = (await get(`/facilities/${this.fctId}`)).data.content;

            console.log(categories);
            console.log(fct);

            this.original = fct;

            let originalMajorCatId = "0";
            let selectedMinorCategories = [];
            for (let category of categories) {
                if (originalMajorCatId !== "0") {
                    break;
                }
                for (let subCategory of category.children) {
                    if (fct.catId === subCategory.catId) {
                        originalMajorCatId = category.catId;
                        selectedMinorCategories = category.children;
                        break;
                    }
                }
            }

            console.log("originalMajorCatId=" + originalMajorCatId);

            const tels = fct.fctTel.split("-");

            this.inputs.facilityRegistration.fctName = fct.fctName;
            this.inputs.facilityRegistration.majorCatId = originalMajorCatId;
            this.inputs.facilityRegistration.minorCatId = fct.catId;
            this.inputs.facilityRegistration.addr = {
                postalCode: fct.fctPostalCode,
                address: fct.fctAddress,
                detailAddress: fct.fctDetailAddress
            };
            this.inputs.facilityRegistration.tel = {
                first: tels[0],
                second: tels[1],
                third: tels[2]
            };
            this.inputs.facilityRegistration.operationTime = {
                openTime: fct.fctOpenTime.substring(0, fct.fctOpenTime.lastIndexOf(":")),
                closeTime: fct.fctClosetime.substring(0, fct.fctClosetime.lastIndexOf(":"))
            };
            this.inputs.facilityRegistration.isOpenOnHolidays = fct.isOpenOnHolidays === "Y";
            this.inputs.facilityRegistration.unitUsageTime = fct.unitUsageTime;
            this.inputs.facilityRegistration.images = fct.fctImages.map((e) => ({
                imgId: e.imgId,
                fileExtension: e.fctImgUrl.substring(e.fctImgUrl.lastIndexOf(".") + 1),
                fileDataInBase64: e.fctImgUrl
            })),
            this.inputs.facilityRegistration.guide = fct.fctGuide;

            this.inputs.facilityRegistration.selectedMinorCategories = selectedMinorCategories;

            this.inputs.zoneRegistration.splice(0, 1);
            for (const singleZone of fct.zones) {
                this.inputs.zoneRegistration.push({
                    zoneId: singleZone.zoneId,
                    zoneName: singleZone.zoneName,
                    maxUserCount: singleZone.maxUserCount,
                    hourlyRate: singleZone.hourlyRate,
                    isSharedZone: singleZone.isSharedZone === "Y",
                    images: singleZone.zoneImgs.map((e) => ({
                        imgId: e.imgId,
                        fileExtension: e.zoneImgUrl.substring(e.fctImgUrl.lastIndexOf(".") + 1),
                        fileDataInBase64: e.zoneImgUrl
                    }))
                });
            }

            for (const addinfo of fct.additionalInfos) {
                this.inputs.addinfoRegistration.push({
                    addinfoId: addinfo.addinfoId,
                    desc: addinfo.addinfoDesc
                });
            }

            console.log(this.inputs);
        }

        getData();
    },
    methods: {
        changeComponent(componentName) {
            console.log(componentName);
            this.currentComponent = componentName;
        }
    }
}
</script>