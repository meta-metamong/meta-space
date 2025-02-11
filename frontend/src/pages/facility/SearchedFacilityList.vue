<template>
    <div class="searched-list">
        <div class="search-box ps-2 mb-2">
            <SearchBox :searchKeyword="searchKeyword" @do-search="search" />
        </div>
        <div class="searched-body">
            <div class="d-flex justify-content-between mt-2 mb-4 fs-5 text-muted w-100">
                <span class="">{{ $t('search.searchedCount') }} {{ fctDatas.length }}{{ $t('search.count') }}</span>
                <span class="" @click="toggleSort">{{ $t('search.distance') }}{{ ascOrDesc }}</span>
            </div>
            <div class="card-box d-flex flex-row gap-2">
                <FctCard v-for="fctData in fctDatas" :fctData="fctData" :key="fctData.fctId" />
            </div>
        </div>
    </div>
</template>
<script>
import SearchBox from '../../components/common/SearchBox.vue';
import FctCard from '../../components/facility/FctCard.vue';
export default{
    name: 'SearchedFacilityList',
    data() {
        const searchKeywordParam = this.$route.query["search-keyword"];
        this.$router.push({ path: "/facilities", query: {} });
        return{
            fctDatas: [],
            isDistanceAsc: true,
            searchKeyword: searchKeywordParam || ""
        }
    },
    props:{
        keyword: {
            type: String,
            required: true
        }
    },
    computed: {
        ascOrDesc(){
            return this.isDistanceAsc ? '↑' : '↓';
        }
    },
    components: {
        SearchBox,
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
        toggleSort(){
            this.isDistanceAsc = !this.isDistanceAsc;
        },
        search(keyword) {
            console.log(keyword);
        }
    },
    mounted(){
        this.testDataInit();
        // TODO: 검색어로 시설 목록 요청하기
    }
}
</script>
<style scoped>
.searched-list{
    max-width: 410px;
}

.card-box{
    width: 410px;
    margin-left: 1px;
    flex-wrap: wrap;
    overflow: auto;
}
</style>