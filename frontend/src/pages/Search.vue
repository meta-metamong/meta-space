<template>
    <div class="container">
        <SearchBox @do-search="search" />
        <div class="hot-keywords mt-5 me-3 ms-3">
            <h4 class="fw-bold">{{ $t('search.hotKeywords') }}</h4>
            <div class="popular-searches">
                <ul class="d-flex flex-column gap-4 fs-5">
                    <li v-for="(keyword, index) in popularKeywords" :key="index" @click="search(keyword)">
                        <span class="rank">{{ index + 1 }}</span>&nbsp;&nbsp;{{ keyword }}
                    </li>
                </ul>
            </div>
        </div>
    </div>
</template>

<script>
import SearchBox from '../components/common/SearchBox.vue';
import { get } from "../apis/axios";
import Swal from 'sweetalert2';
export default{
    name: 'Search',
    data(){
        return{
            popularKeywords: [],
        }
    },
    components:{
        SearchBox
    },
    async mounted(){
        const response = await get("/search/popular");
        if(response.status === 200){
            this.popularKeywords = response.data.content.popularKeywords;
            if(this.popularKeywords.length === 0){
                this.popularKeywords.push("수영");
                this.popularKeywords.push("골프");
                this.popularKeywords.push("축구");
                this.popularKeywords.push("테니스");
                this.popularKeywords.push("배드민턴");
                this.popularKeywords.push("운동장");
                this.popularKeywords.push("대강당");
                this.popularKeywords.push("헬스");
                this.popularKeywords.push("야구");
                this.popularKeywords.push("필라테스");
            }
        }else{
            Swal.fire({
                text: "인기 검색어 조회가 실패됐습니다.",
                width: "300px",
                icon: "error"
            });
            return;
        }
    },
    methods: {
        search(keyword) {
            this.$router.push({ path: "/facilities" , query: { "search-keyword": keyword} });
        },
    }
}
</script>

<style scoped>
.container{
    height: 780px;
}
.hot-keywords{
    height: 80%;
}
.popular-searches{
    height: 90%;
    margin-top: 30px;
}
ul, li{
    list-style: none;
    padding: 0;
    margin: 0;
}
.rank {
    margin-right: 7px;
    color: #19319D;
}
</style>