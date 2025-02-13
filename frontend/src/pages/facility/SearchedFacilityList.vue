<template>
    <div class="container searched-list">
        <div class="search-box mb-2">
            <SearchBox :searchKeyword="searchKeyword" @do-search="search" />
        </div>
        <div class="d-flex justify-content-between mt-3 mb-4" style="font-size: 17px; margin: 0 10px;">
            <span>{{ $t('search.searchedCount') }} {{ fctContent.totalElementCount }}{{ $t('search.count') }}</span>
            <span class="sort-toggle" @click="toggleSort">{{ $t('search.distance') }} {{ ascOrDesc }}</span>
        </div>
        <div class="searched-body row row-cols-2" >
            <div class="card card-box mb-3" v-for="fctData in fctContent.facilities" :key="fctData.fctId" @click="$router.push(`/facilities/${fctData.fctId}`)">
                <img :src="fctData.repImgUrl" class="card-thumbnail mx-auto" alt="대표 이미지" />
                <div class="card-detail d-flex flex-column p-2">
                    <p class="card-name fw-bold">{{ truncateText(fctData.fctName, 8) }}</p>
                    <p class="card-category">{{ fctData.catName }}</p>
                    <p class="card-address mt-1"><i class="bi bi-geo-alt"></i> {{ truncateText(fctData.fctAddress, 7) }}</p>
                    <p class="card-address mt-1"><i class="bi bi-compass"></i> {{ convertDistance(fctData.distance) }}</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { toRaw } from "vue";
import apiClient from "../../apis/axios";
import SearchBox from '../../components/common/SearchBox.vue';
import FctCard from '../../components/facility/FctCard.vue';

export default {
    name: 'SearchedFacilityList',
    data() {
        const searchKeywordParam = this.$route.query["search-keyword"];
        return {
            fctContent: {
                facilities: [],
                page: 1,
                pageSize: 10,
                totalElementCount: 0,
                totalPageCount: 1,
                isFirst: true,
                isLast: true
            },
            isDistanceAsc: true,
            searchKeyword: searchKeywordParam || ""
        };
    },
    computed: {
        ascOrDesc() {
            return this.isDistanceAsc ? '↑' : '↓';
        }
    },
    components: {
        SearchBox,
    },
    methods: {
        toggleSort() {
            this.isDistanceAsc = !this.isDistanceAsc;
            if(this.isDistanceAsc){
                this.fctContent.facilities.sort((a, b) => a.distance - b.distance);
            }else{
                this.fctContent.facilities.sort((a, b) => b.distance - a.distance);
            }
        },
        async search(keyword) {
            this.searchKeyword = keyword;
            const params = {};
            if (this.searchKeyword && this.searchKeyword !== "") {
                params["search-keyword"] = this.searchKeyword;
                params["search-condition"] = "FACILITY_NAME";
            }
            const responseBody = (await apiClient.get("/facilities", { params })).data;
            this.fctContent = responseBody.content;
            this.insertDistance();
        },
        getDistance(lat, lon){
            const R = 6371;
            const dLat = this.toRad(this.$store.state.loc.lat -lat);
            const dLon = this.toRad(this.$store.state.loc.lon - lon);

            const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(this.toRad(lat)) * Math.cos(this.toRad(this.$store.state.loc.lat)) *
                    Math.sin(dLon / 2) * Math.sin(dLon / 2);

            const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            return Math.ceil(R * c * 1000);
        },
        convertDistance(dist){
            if(dist > 1000) return Math.ceil((dist / 1000)) + 'km'
            return dist + 'm';
        },
        toRad(value) {
            return value * Math.PI / 180;
        },
        insertDistance(){
            this.fctContent.facilities = this.fctContent.facilities.map(fct => ({...fct, distance: this.getDistance(fct.fctLatitude, fct.fctLongitude)}));
            this.fctContent.facilities.sort((a, b) => a.distance - b.distance);
        },
        truncateText(text, length) {
            return text.length > length ? text.substring(0, length) + '...' : text;
        }
    },
    async mounted() {
        const params = {};
        if (this.searchKeyword && this.searchKeyword !== "") {
            params["search-keyword"] = this.searchKeyword;
            params["search-condition"] = "FACILITY_NAME";
        }
        const responseBody = (await apiClient.get("/facilities", { params })).data;
        this.fctContent = responseBody.content;
        this.insertDistance();
    }
};
</script>

<style scoped>
.searched-list {
    max-width: 100%;
    padding: 10px;
}

.sort-toggle {
    cursor: pointer;
    color: #007bff;
    font-weight: bold;
}

.searched-body {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    gap: 10px;
    margin: 0 10px;
}

.card-box {
    width: calc(50% - 5px);
}

.card-thumbnail{
    width: 100%;
    aspect-ratio: 16 / 9;
    object-fit: cover;
    border-top-left-radius: 0.375rem;
    border-top-right-radius: 0.375rem;
}
.card-category{
    color: #555;
    margin-bottom: 3px;
}
.card-address{
    margin-bottom: 3px;
    color: #555;
}
.card-name{
    color: #333;
    margin-bottom: 13px;
}

.card-detail{
    flex-grow: 1;
    border-top: 1px solid #ddd;
}

.row>* {
    padding-right: 0;
    padding-left: 0;
}
</style>
