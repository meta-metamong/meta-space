<template>
    <div class="container w-100">
        <SearchBox @do-search="search" />
        <div class="hot-keywords mt-5">
            <h1 class="fw-bold">{{ $t('search.hotKeywords') }}</h1>
            <div class="mt-5 popular-searches">
                <ul class="d-flex flex-column gap-4 fs-4">
                    <li v-for="(keyword, index) in popularKeywords" :key="index">
                        {{ index + 1 }}&nbsp;&nbsp;{{ keyword }}
                    </li>
                </ul>
            </div>
        </div>
    </div>
</template>

<script>
import SearchBox from '../components/common/SearchBox.vue';
export default{
    name: 'Search',
    data(){
        return{
            popularKeywords: []
        }
    },
    components:{
        SearchBox
    },
    mounted(){
        for(let i = 0; i < 10; i++){
            this.popularKeywords.push(`인기검색어`);
        }
    },
    methods: {
        search(keyword) {
            this.$router.push({ path: "/facilities" , query: { "search-keyword": keyword} });
        }
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
}
ul, li{
    list-style: none;
    padding: 0;
    margin: 0;
}
</style>