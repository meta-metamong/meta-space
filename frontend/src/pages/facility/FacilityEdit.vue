<template>
    <div class=container>
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
                    class="w-100 signup-btn rounded-pill mb-3 me-2 ms-2"
                    type="button"
                    @click="submitEdit">
                {{ $t("editFacility.editComplete") }}
            </button>
        </div>
    </div>
</template>

<script>
import { get, put } from "../../apis/axios";
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
        }

        getData();
    },
    methods: {
        changeComponent(componentName) {
            this.currentComponent = componentName;
        },
        submitEdit() {
            const orig = this.original;
            const tarFct = this.inputs.facilityRegistration;
            const tarZone = this.inputs.zoneRegistration;
            const tarAddinfo = this.inputs.addinfoRegistration;

            const editInfo = {}

            if (orig.fctName !== tarFct.fctName) {
                editInfo.fctName = tarFct.fctName;
            }

            if (orig.catId !== tarFct.minorCatId) {
                editInfo.catId = tarFct.minorCatId;
            }

            if (orig.fctAddress !== tarFct.addr.address) {
                editInfo.fctAddress = tarFct.addr.address;
            }

            if (orig.fctDetailAddress !== tarFct.addr.detailAddress) {
                editInfo.fctDetailAddress = tarFct.addr.detailAddress;
            }

            if (orig.fctPostalCode !== tarFct.addr.postalCode) {
                editInfo.fctPostalCode = tarFct.addr.postalCode;
            }

            const tels = orig.fctTel.split("-");
            if (tels[0] !== tarFct.tel.first
                || tels[1] !== tarFct.tel.second
                || tels[2] !== tarFct.tel.third
            ) {
                editInfo.fctTel = `${tels[0]}-${tels[1]}-${tels[2]}`;
            }

            if (orig.fctGuide !== tarFct.guide) {
                editInfo.fctGuide = tarFct.guide;
            }

            if ((orig.isOpenOnHolidays === "Y") !== tarFct.isOpenOnHolidays) {
                editInfo.isOpenOnHolidays = tarFct.isOpenOnHolidays ? "Y" : "N";
            }

            if (orig.fctOpenTime.substring(0, orig.fctOpenTime.lastIndexOf(":")) !== tarFct.operationTime.openTime) {
                editInfo.fctOpenTime = tarFct.operationTime.openTime;
            }

            if (orig.fctClosetime.substring(0, orig.fctClosetime.lastIndexOf(":")) !== tarFct.operationTime.closeTime) {
                editInfo.fctCloseTime = tarFct.operationTime.closeTime;
            }

            if (orig.unitUsageTime !== tarFct.unitUsageTime) {
                editInfo.unitUsageTime = tarFct.unitUsageTime;
            }

            // TODO: How about latitude and longitude?

            editInfo.zones = {};

            editInfo.zones.create = [];

            tarZone.forEach((e) => {
                if (!e.zoneId) {
                    editInfo.zones.create.push({
                        // TODO: images
                        zoneName: e.zoneName,
                        maxUserCount: e.maxUserCount,
                        isSharedZone: e.isSharedZone ? "Y" : "N",
                        hourlyRate: e.hourlyRate
                    });
                }
            });

            editInfo.zones.update = [];
            const originalZones = {}
            for (const zone of orig.zones) {
                originalZones[zone.zoneId] = zone;
            }
            
            tarZone.forEach((e) => {
                if (originalZones[e.zoneId]) {
                    const originalZone = originalZones[e.zoneId];
                    const obj = {
                        id: e.zoneId,
                        to: {}
                    }

                    if (e.isSharedZone !== (originalZone.isSharedZone === "Y")) {
                        obj.to.isSharedZone = e.isSharedZone === "Y";
                    }

                    if (e.hourlyRate !== originalZone.hourlyRate) {
                        obj.to.hourlyRate = e.hourlyRate;
                    }

                    if (e.maxUserCount !== originalZone.maxUserCount) {
                        obj.to.maxUserCount = e.maxUserCount;
                    }

                    if (e.zoneName !== originalZone.zoneName) {
                        obj.to.zoneName = e.zoneName;
                    }

                    if (Object.keys(obj.to).length > 0) {
                        editInfo.zones.update.push(obj);
                    }

                    // TODO: images
                }
            });

            const remainZoneIds = new Set();
            tarZone.forEach((e) => e.zoneId && remainZoneIds.add(e.zoneId));

            editInfo.zones.delete = orig.zones.filter((e) => !remainZoneIds.has(e.zoneId)).map((e) => e.zoneId);

            editInfo.addinfos = {};

            editInfo.addinfos.create = tarAddinfo.filter((e) => !e.addinfoId).map((e) => e.desc);

            const originalAddinfos = {};
            for (const origAddinfo of orig.additionalInfos) {
                originalAddinfos[origAddinfo.addinfoId] = origAddinfo;
            }

            editInfo.addinfos.update = [];

            for (const tarAddinfoItem of tarAddinfo) {
                const obj = originalAddinfos[tarAddinfoItem.addinfoId];
                if (obj && obj.addinfoDesc !== tarAddinfoItem.desc) {
                    editInfo.addinfos.update.push({
                        id: tarAddinfoItem.addinfoId, to: tarAddinfoItem.desc
                    });
                }
            }

            // editInfo.addinfos.update = tarAddinfo.filter((e) => originalAddinfos[e.addinfoId] && originalAddinfos[e.addinfoId] !== e.desc)
            //         .map((e) => ({ id: e.addinfoId, to: e.desc }));

            const tarAddInfoIds = new Set();
            for (const tarAddinfoItem of tarAddinfo) {
                if (tarAddinfoItem.addinfoId) {
                    tarAddInfoIds.add(tarAddinfoItem.addinfoId);
                }
            }

            console.log(originalAddinfos);
            console.log(tarAddInfoIds);

            console.log(Object.keys(originalAddinfos));

            

            editInfo.addinfos.delete = orig.additionalInfos.filter((e) => !tarAddInfoIds.has(e.addinfoId)).map((e) => e.addinfoId);

            console.log(editInfo);

            put(`/facilities/${orig.fctId}`, editInfo);
        }
    }
}
</script>

<style scoped>
#submit-container {
    position: absolute;
    bottom: 10%;
    left: 50%;
    transform: translateX(-50%);
    width: 100%;
    display: flex;
    justify-content: center;
}

#submit-button {
    background-color: #4a66e6;
    border-radius: 80px;
    font-size: 18px;
}
</style>