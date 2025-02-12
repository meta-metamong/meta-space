<template>
    <div class="container">
        <div>
            <!-- 대분류 넣기 -->
            <nav style="--bs-breadcrumb-divider: '>';" aria-label="breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item">대분류</li>
                    <li class="breadcrumb-item active" aria-current="page">{{ fct.catName }}</li>
                </ol>
            </nav>
            <img class="w-100" id="fct-img" :src="fct.fctImage">
            <div id="title-container">
                <h2 id="fct-title">{{ fct.fctName }}</h2>
                <p id="addr">{{ fct.fctAddress }}</p>
            </div>
            <table class="table table-borderless">
                <tbody>
                    <tr>
                        <td style="width: 45%;">{{ $t("facility.facilityOperationTime") }}</td>
                        <td>{{ fct.fctOpenTime }} ~ {{ fct.fctCloseTime }}</td>
                    </tr>
                    <tr>
                        <td>{{ $t("facility.unitUsageTime") }}</td>
                        <td>{{ formatUnitUsageTime(fct.unitUsageTime, $t("facility.hour"), $t("facility.minutes")) }}</td>
                    </tr>
                    <tr>
                        <td>{{ $t("facility.isOpenOnHolidays") }}</td>
                        <td>{{ fct.isOpenOnHolidays ? "영업" : "영업하지 않음" }}</td>
                    </tr>
                    <tr>
                        <td>{{ $t("facility.facilityTel") }}</td>
                        <td>{{ fct.fctTel }}</td>
                    </tr>
                </tbody>
            </table>
            <div class="w-100 mb-4" id="reserve-button-container">
                <button v-if="userRole === 'ROLE_CONS'"
                        type="button"
                        class="btn btn-primary"
                        @click="goToReservationPage()"
                        id="reserve-button">{{ $t("reservation.reserve") }}</button>
            </div>
        </div>
        <hr>
        <div id="available-zones" class="mt-4 mb-4">
            <h5>{{ $t("facility.availableZones") }}</h5>
            <div class="zone-item-container" v-for="zone in fct.zones"> <!-- TODO: API -->
                <div class="w-25">
                    <img :src="zone.imgUrl" class="rounded float-start">
                </div>
                <div class="zone-desc-container">
                    <h3>{{ zone.zoneName }}</h3>
                    <ul class="zone-info-ul">
                        <li>{{ $t("facility.hourlyRate") }}</li>
                        <li>{{ formatNumber(zone.hourlyRate) }}{{ $t("facility.won") }}</li>
                        <li>{{ $t("facility.maxUserCount") }}</li>
                        <li>{{ formatNumber(zone.maxUserCount) }}</li>
                    </ul>
                </div>
            </div>
        </div>
        <div id="addinfo-container" class="mb-4">
            <h5>{{ $t("reservation.additionalInfo") }}</h5>
            <ul>
                <li v-for="addinfo in fct.additionalInfos">
                    {{ addinfo.addinfoDesc }}
                </li>
            </ul>
        </div>
        <div id="guide-container" class="mb-4">
            <h5>{{ $t("facility.facilityGuide") }}</h5>
            <div class="w-100 mt-3">
                <textarea class="additional-info form-control" rows="10" disabled>{{ fct.fctGuide }}</textarea>
            </div>
        </div>
    </div>
</template>

<script>
import { get } from "../../apis/axios";

export default {
    props: ["fctId"],
    data() {
        return {
            fct: {
                fctId: 0,
                additionalInfos: [],
                catName: "",
                fctAddress: "",
                fctOpenTime: "00:00",
                unitUsageTime: "",
                fctCloseTime: "18:00",
                fctGuide: "",
                fctImages: "",
                fctLatitude: 0,
                fctLongitude: 0,
                fctName: "",
                fctTel: "",
                isOpenOnHolidays: false,
                zones: []
            }
        };
    },
    computed: {
        userRole() {
            return this.$store.state.userRole;
        }
    },
    async mounted() {
        const responseBody = (await get(`/facilities/${this.fctId}`)).data;
        const content = responseBody.content;

        this.fct.catName = content.catName;
        this.fct.additionalInfos = content.additionalInfos;
        this.fct.fctAddress = content.fctAddress;
        this.fct.fctGuide = content.fctGuide;
        this.fct.fctId = content.fctId;
        this.fct.fctImage = content.fctImages.filter((e) => e.fctImgDisplayOrder === 1).map((e) => e.fctImgUrl)[0];
        this.fct.fctLatitude = content.fctLatitude;
        this.fct.fctLongitude = content.fctLongitude;
        this.fct.unitUsageTime = content.unitUsageTime;
        this.fct.fctName = content.fctName;
        this.fct.fctOpenTime =
                content.fctOpenTime.substring(0, content.fctOpenTime.lastIndexOf(":"));
        this.fct.fctCloseTime =
                content.fctCloseTime.substring(0, content.fctCloseTime.lastIndexOf(":"));
        this.fct.fctTel = content.fctTel;
        this.fct.isOpenOnHolidays = content.isOpenOnHolidays === "Y";
        this.fct.zones = [];

        for (const zone of content.zones) {
            const imgUrls = zone.zoneImgs.filter((e) => e.zoneImgDisplayOrder === 1).map((e) => e.zoneImgUrl);
            const imgUrl = imgUrls.length === 0 ? "" : imgUrls[0];

            this.fct.zones.push({
                hourlyRate: zone.hourlyRate,
                isSharedZone: zone.isSharedZone === "Y",
                maxUserCount: zone.maxUserCount,
                zoneId: zone.zoneId,
                zoneImg: imgUrl,
                zoneName: zone.zoneName
            });
        }
    },
    methods: {
        formatUnitUsageTime(uut, hUnit, mUnit) {
            const hour = Math.floor(uut / 60);
            const minutes = uut % 60;
            return `${hour > 0 ? `${hour}${hUnit} ` : ""}${minutes}${mUnit}`;
        },
        formatNumber(num) {
            const numStr = num + "";
            let idx = 0;
            let formatted = "";
            while (true) {
                if (numStr.length - 3 - idx <= 0) {
                    formatted = numStr.substring(0, numStr.length - idx) + formatted;
                    break;
                }
                formatted = "," + numStr.substring(numStr.length - 3 - idx, numStr.length - idx) + formatted;
                idx += 3;
            }
            return formatted;
        },
        goToReservationPage() {
            this.$router.push(`/reserve/${this.fctId}`);
        },
    }
};
</script>

<style lang="css" scoped>
.container {
    padding: 0 20px;
}

#fct-img {
    height: 200px;
    overflow: hidden;
    margin-top: 12px;
}

#fct-title {
    margin-top: 18px;
    font-size: 28px;
}

#addr {
    font-size: 16px;
    color: #999999;
}

#title-container {
    margin-bottom: 24px;
}

#reserve-button-container {
    display: flex;
    justify-content: center;
    margin-top: 24px;
}

#reserve-button {
    padding: 6px 32px;
    border-radius: 80px;
    background-color: #4a66e6;
}

.zone-item-container {
    display: flex;
}

.zone-item-container img {
    border-radius: 18px;
}

.zone-info-ul {
    padding: 0 12px;
    margin: 0;
    display: flex;
    flex-direction: column;
    list-style: none;
}
.zone-info-ul li:nth-child(odd) {
    align-self: self-start;
    font-size: 14px;
    color: #999999;
}

.zone-info-ul li:nth-child(even) {
    align-self: self-end;
    margin-bottom: 8px;
    font-size: 16px;
}

.zone-item-container img, .zone-item-container div {
    flex: 1;
    padding: 3px;
}

#available-zones {
    margin-top: 8px;
    display: flex;
    flex-direction: column;
    gap: 24px;
}

#addinfo-container {
    margin-top: 32px;
    display: flex;
    flex-direction: column;
}

h5 {
    font-weight: 600;
}

#addinfo-container ul {
    margin: 24px 24px;
    padding: 0;
    list-style: none;
    display: flex;
    flex-direction: column;
    gap: 12px;
    align-self: self-start;
}

#addinfo-container ul li {
    font-size: 18px;
}

#guide-container {
    display: flex;
    flex-direction: column;
}

table td {
    padding-left: 0px;
}

.zone-name {
    font-size: 18px;
    color: #000;
}
.additional-info:disabled{
    background: #fff;
}
</style>
