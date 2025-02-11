<template>
    <div class="container d-flex flex-column gap-4">
        <div class="banner w-100 text-center text-white bg-primary">
            배너입니다.
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
                <FctCard v-for="fctData in fctDatas" :key="fctData.fctId" :fctData="fctData" />
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
export default{
    name: "Home",
    data(){
        return{
            fctDatas : []
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
        }
    },
    mounted() {
        this.testDataInit();
    }
}
</script>
<style scoped>
.container{
    overflow-y: auto;
}
.banner{
    height: 150px;
    line-height: 150px;
    font-size: 50px;
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