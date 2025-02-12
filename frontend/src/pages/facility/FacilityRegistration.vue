<template>
    <div class=container>
        <facility-registration-input
            v-if="currentComponent === 'facilityRegistrationInput'"
            @component-change="changeComponent"
            :facility-registration="inputs.facilityRegistration"
            is-edit="false" />
        <zone-registration-input
            v-if="currentComponent === 'zoneRegistrationInput'"
            @component-change="changeComponent"
            :zone-registration="inputs.zoneRegistration"
            is-edit="false" />
        <additional-information
            v-if="currentComponent === 'addinfoRegistrationInput'"
            @component-change="changeComponent"
            :addinfo-registration="inputs.addinfoRegistration"
            is-edit="false" />

        <div style="display: flex; justify-content: center;">
            <button v-if="currentComponent === 'addinfoRegistrationInput'"
                    class="w-100 signup-btn rounded-pill mb-3 me-2 ms-2"
                    type="button"
                    id="register-button"
                    @click="registerFacility">
                {{ $t("facility.register") }}
            </button>
        </div>
    </div>
</template>

<script>
import { computed } from "vue";
import AdditionalInformation from "../../components/facility/AdditionalInformation.vue";
import FacilityRegistrationInput from "../../components/facility/FacilityRegistrationInput.vue";
import ZoneRegistrationInput from "../../components/facility/ZoneRegistrationInput.vue";
import apiClient, { post } from "../../apis/axios";
import axios from "axios";

export default {
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
                        openTime: "00:00",
                        closeTime: "00:00"
                    },
                    isOpenOnHolidays: true,
                    unitUsageTime: "",
                    images: [],
                    guide: "",
                    selectedMinorCategories: []
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
    computed: {
        userId() {
            // return this.$store.state.userId;
            return 21;
        }
    },
    methods: {
        changeComponent(componentName) {
            console.log(componentName);
            this.currentComponent = componentName;
        },
        registerFacility() {
            const requestBody = {
                fctName: this.inputs.facilityRegistration.fctName,
                fctPostalCode: this.inputs.facilityRegistration.addr.postalCode,
                fctAddress: this.inputs.facilityRegistration.addr.address,
                fctDetailAddress: this.inputs.facilityRegistration.addr.postalCode,
                catId: this.inputs.facilityRegistration.minorCatId ? this.inputs.facilityRegistration.minorCatId : this.inputs.facilityRegistration.majorCatId,
                provId: this.userId,
                fctTel: `${this.inputs.facilityRegistration.tel.first}-${this.inputs.facilityRegistration.tel.second}-${this.inputs.facilityRegistration.tel.third}`,
                fctGuide: this.inputs.facilityRegistration.guide,
                openOnHolidays: this.inputs.facilityRegistration.isOpenOnHolidays ? "Y" : "N",
                fctOpenTime: this.inputs.facilityRegistration.operationTime.openTime,
                fctCloseTime: this.inputs.facilityRegistration.operationTime.closeTime,
                unitUsageTime: this.inputs.facilityRegistration.unitUsageTime,
                images: this.inputs.facilityRegistration.images.map((img, idx) => {
                    return {
                        fileType: img.fileExtension,
                        order: idx + 1
                    };
                }),
                zones: this.inputs.zoneRegistration.map((z, idx) => {
                    return {
                        zoneNo: idx + 1,
                        zoneName: z.zoneName,
                        maxUserCount: z.maxUserCount,
                        hourlyRate: z.hourlyRate,
                        isSharedZone: z.isSharedZone ? "Y" : "N",
                        images: z.images.map((img, imgIdx) => {
                            return {
                                fileType: img.fileExtension,
                                order: imgIdx + 1
                            };
                        })
                    }
                }),
                addinfos: this.inputs.addinfoRegistration
            }

            console.log(requestBody);

            post("/facilities", requestBody).then((response) => {
                const responseBody = response.data;
                console.log(responseBody);
                const content = responseBody.content;
                const fctImageUploadUrls = content.fctImageUploadUrls;
                const zoneImageUploadUrls = content.zoneImageUploadUrls;
                for (const fctImageUploadUrl of fctImageUploadUrls) {
                    const image = this.inputs.facilityRegistration.images[fctImageUploadUrl.order - 1];
                    console.log("fctImage", image);
                    axios.put(
                        fctImageUploadUrl.uploadUrl,
                        image.fileData,
                        {
                            headers: {
                                "Content-Type": `image/${image.fileExtension}`
                            }
                        }
                    )
                }
                for (const zoneImageUploadUrl of zoneImageUploadUrls) {
                    const zoneNo = zoneImageUploadUrl.zoneNo;
                    const uploadUrls = zoneImageUploadUrl.uploadUrls;
                    const zoneImages = this.inputs.zoneRegistration[zoneNo - 1].images;
                    for (const uploadUrl of uploadUrls) {
                        const zoneImage = zoneImages[uploadUrl.order - 1];
                        const imageData = zoneImage.fileData;
                        console.log("zoneImage", zoneImage);
                        axios.put(
                            uploadUrl.uploadUrl,
                            imageData,
                            {
                                headers: {
                                    "Content-Type": `image/${zoneImage.fileExtension}`
                                }
                            }
                        )
                    }
                }
            });
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