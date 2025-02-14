<template>
    <div class="container d-flex flex-column gap-4">
        <div class="banner w-100">
            <img :class="{ active: bannerStep === 0 }" class="banner-img" src="../resource/image/banner1.png" alt="banner1">
            <img :class="{ active: bannerStep === 1 }" class="banner-img" src="../resource/image/banner2.png" alt="banner2">
        </div>
        <div class="d-flex flex-column gap-2" v-if="userId !== null && recommendFct.length != 0">
            <h4 class="fw-bold">👍 {{ $t('main.best') }}</h4>
            <div class="card-list d-flex gap-4">
                <FctCard v-for="fctData in recommendFct" :key="fctData.fctId" :fctData="fctData" />
            </div>
        </div>
        <div class="d-flex flex-column gap-2">
            <h4 class="fw-bold">🔥{{ $t('main.hot') }}</h4>
            <div class="card-list d-flex gap-4">
                <FctCard v-for="fctData in topFct" :key="fctData.fctId" :fctData="fctData" />
            </div>
        </div>
        <div class="d-flex flex-column gap-2">
            <h4 class="fw-bold">👟{{ $t('main.close') }}</h4>
            <div class="card-list d-flex gap-4">
                <FctCard v-for="fctData in closeFct" :key="fctData.fctId" :fctData="fctData" />
            </div>
        </div>
    </div>
</template>

<script>
import FctCard from '../components/facility/FctCard.vue';
import { get, post } from '../apis/axios'
import { getUserIdInLocal } from "../store";

export default{
    name: "Home",
    data(){
        return{
            topFct: [],
            recommendFct: [],
            closeFct: [],
            bannerStep: 0
        }
    },
    components: {
        FctCard
    },
    methods: {
        async getTopFct() {
            const response = await get('/top-facilities');
            this.topFct = response.data.content;
        },
        async getRecommendFct() {
            try {
                const response = await post(`/recommends/${this.userId}`);

                const facilityIds = response.data.recommended_facilities;

                const facilityRequests = facilityIds.map(fctId => get(`/facilities/${fctId}`));

                // 모든 요청이 끝날 때까지 기다림
                const facilityResponses = await Promise.all(facilityRequests);

                this.recommendFct = facilityResponses.map(res => res.data.content);
            } catch (error) {
                console.error("추천 시설 정보를 가져오는 중 오류 발생:", error);
            }
        }, 
        async getCloseFct(){
            const response = await get(`/facilities?center-latitude=${this.$store.state.loc.lat}&center-longitude=${this.$store.state.loc.lon}`);
            this.closeFct = response.data.content.facilities;
            this.closeFct = this.closeFct.map(fct => ({...fct, distance: this.getDistance(fct.fctLatitude, fct.fctLongitude)}));
            this.closeFct.sort((a, b) => a.distance - b.distance);
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
        toRad(value) {
            return value * Math.PI / 180;
        },
    },
    computed: {
        userId() {
            return this.$store.state.userId;
        }
    },
    mounted() {
        this.getTopFct();
        this.getCloseFct();
        if (getUserIdInLocal() !== null) {
            this.getRecommendFct();
        }
        setInterval(() => {
            this.bannerStep++;
            if(this.bannerStep === 2){
                this.bannerStep = 0;
            }
        }, 5000)
    }
}
</script>
<style scoped>
.container{
    overflow-y: auto;
}
.banner{
    position: relative;
    height: 190px;
    line-height: 150px;
    font-size: 50px;
}
.banner img{
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    opacity: 0;
    transition: opacity 1s ease-in-out;
}
.banner .active{
    opacity: 1;
}

.card-list{
    overflow-x: auto;
    
}
/* edge, chrome */
*::-webkit-scrollbar {
    display: none;
    /* firefox 스크롤바 숨기기 */
    scrollbar-width: none;
}
</style>