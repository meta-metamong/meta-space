<template>
    <div class="container">
        <div>
            <div>{{ "대분류" }} / {{ "소분류" }}</div>
            <img class="w-100" id="fct-img">
            <div id="title-container">
                <h2 id="fct-title">{{ "광장동실내배드민턴장" }}</h2>
                <p id="addr">{{ "서울시 OO구" }}</p>
            </div>
            <table class="table">
                <tbody>
                    <tr>
                        <td>{{ $t("facility.facilityOperationTime") }}</td>
                        <td>{{ "opernTime" }} ~ {{ "closeTime" }}</td>
                    </tr>
                    <tr>
                        <td>{{ $t("facility.unitUsageTime") }}</td>
                        <td>{{ "1시간" }}</td>
                    </tr>
                    <tr>
                        <td>{{ $t("facility.isOpenOnHolidays") }}</td>
                        <td>{{ "영업" }}</td>
                    </tr>
                    <tr>
                        <td>{{ $t("facility.facilityTel") }}</td>
                        <td>{{ "02-1234-1234" }}</td>
                    </tr>
                </tbody>
            </table>

            <div class="w-100" id="reserve-button-container">
                <button type="button"
                        class="btn btn-primary"
                        id="reserve-button">{{ $t("reservation.reserve") }}</button>
            </div>
        </div>
        <hr>
        <div id="available-zones">
            <h1>{{ $t("facility.availableZones") }}</h1>
            <div class="zone-item-container"> <!-- TODO: API -->
                <img>
                <div class="zone-desc-container">
                    <h3>{{ "코트 1" }}</h3>
                    <table>
                        <tbody>
                            <tr>
                                <td>{{ $t("facility.hourlyRate") }}</td>
                                <td>{{ "5,000" }}{{ $t("facility.won") }}</td>
                            </tr>
                            <tr>
                                <td>{{ $t("facility.maxUserCount") }}</td>
                                <td>{{ "5" }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="zone-item-container"> <!-- TODO: API -->
                <img>
                <div class="zone-desc-container">
                    <h3>{{ "코트 2" }}</h3>
                    <table>
                        <tbody>
                            <tr>
                                <td>{{ $t("facility.hourlyRate") }}</td>
                                <td>{{ "5,000" }}{{ $t("facility.won") }}</td>
                            </tr>
                            <tr>
                                <td>{{ $t("facility.maxUserCount") }}</td>
                                <td>{{ "5" }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div id="guide-container">
            <h1>{{ $t("facility.facilityGuide") }}</h1>
            <ul>
                <li>{{ "이용수칙 1" }}</li>
                <li>{{ "이용수칙 2" }}</li>
                <li>{{ "이용수칙 3" }}</li>
            </ul>
        </div>
    </div>
</template>

<script>
export default {
    props: ["fctId"],
    data() {
        console.log("fctId=" + this.fctId);
        return {};
    }
};
</script>

<style lang="css" scoped>
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

.zone-item-container img, .zone-item-container div {
    flex: 1;
    padding: 3px;
}

.zone-desc-container td {
    font-size: 16px;
    padding: 4px;
}

.zone-desc-container td:nth-child(odd) {
    font-size: 14px;
    color: #999999;
}

#available-zones {
    margin-top: 8px;
    display: flex;
    flex-direction: column;
    gap: 24px;
}

#guide-container {
    margin-top: 32px;
}

h1 {
    font-weight: 600;
}

#guide-container ul {
    margin-top: 24px;
    list-style: none;
    display: flex;
    flex-direction: column;
    gap: 12px;
}

#guide-container ul li {
    font-size: 18px;
}
</style>
