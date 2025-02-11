<template>
    <div class="container d-flex flex-column gap-4">
        <div class="banner w-100">
            <img :class="{ active: bannerStep === 0 }" class="banner-img" src="../resource/image/banner1.png" alt="banner1">
            <img :class="{ active: bannerStep === 1 }" class="banner-img" src="../resource/image/banner2.png" alt="banner2">
        </div>
        <div class="d-flex flex-column gap-2">
            <h2 class="fw-bold">👍 {{ $t('main.best') }}</h2>
            <div class="card-list d-flex gap-4">
                <FctCard v-for="fctData in fctDatas" :key="fctData.fctId" :fctData="fctData" />
            </div>
        </div>
        <div class="d-flex flex-column gap-2">
            <h2 class="fw-bold">🔥{{ $t('main.hot') }}</h2>
            <div class="card-list d-flex gap-4">
                <FctCard v-for="fct in topFct" :key="fct.fctId" :fctData="fct" />
            </div>
        </div>
        <div class="d-flex flex-column gap-2">
            <h2 class="fw-bold">👟{{ $t('main.close') }}</h2>
            <div class="card-list d-flex gap-4">
                <FctCard v-for="fctData in fctDatas" :key="fctData.fctId" :fctData="fctData" />
            </div>
        </div>
    </div>
</template>

<script>
import FctCard from '../components/facility/FctCard.vue';
import { get } from '../apis/axios'
export default{
    name: "Home",
    data(){
        return{
            fctDatas : [],
            topFct: [],
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
                    repImgUrl: 'https://cdn.pixabay.com/photo/2023/02/18/11/00/icon-7797704_1280.png'
                })
                if(i % 2 == 0){
                    this.fctDatas[i].fctName += ' 크아아아아악'
                }
            }
        },
        async getTopFct() {
            const response = await get('/facilities/top');
            this.topFct = response.data.content;
            console.log(this.topFct)
        }
    },
    mounted() {
        this.testDataInit();
        this.getTopFct();
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