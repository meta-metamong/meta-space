<template>
    <div class="container d-flex flex-column gap-4">
        <div class="banner w-100">
            <img :class="{ active: bannerStep === 0 }" class="banner-img" src="../resource/image/banner1.png" alt="banner1">
            <img :class="{ active: bannerStep === 1 }" class="banner-img" src="../resource/image/banner2.png" alt="banner2">
        </div>
        <div class="d-flex flex-column gap-2" v-if="userId !== null">
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
                <FctCard v-for="fctData in fctDatas" :key="fctData.fctId" :fctData="fctData" />
            </div>
        </div>
    </div>
</template>

<script>
import FctCard from '../components/facility/FctCard.vue';
import { get, post } from '../apis/axios'
import repImgUrl from '../assets/fct_default.jpg';
export default{
    name: "Home",
    data(){
        return{
            fctDatas : [],
            topFct: [],
            recommendFct: [],
            bannerStep: 0
        }
    },
    components: {
        FctCard
    },
    methods: {
        testDataInit(){
            for(var i = 0; i < 10; i++){
                this.fctDatas.push({
                    fctId: i,
                    fctName: `시설 이름${i}`,
                    catName: `카테고리${i}`,
                    fctAddress: '배고파시 밥먹자동',
                    repImgUrl: repImgUrl
                })
                if(i % 2 == 0){
                    this.fctDatas[i].fctName += ' 크아아아아악'
                }
            }
        },
        async getTopFct() {
            const response = await get('/facilities/top');
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
        }
    },
    computed: {
        userId() {
            return this.$store.state.userId;
        }
    },
    mounted() {
        this.testDataInit();
        this.getTopFct();
        if (this.userId !== null) {
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