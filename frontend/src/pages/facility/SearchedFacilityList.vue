<template>
    <div class="searched-list">
        <div class="search-box ps-2 mb-2">
            <SearchBox :searchKeyword="searchKeyword" @do-search="search" />
        </div>
        <div class="searched-body">
            <div class="d-flex justify-content-between mt-2 mb-4 fs-5 text-muted w-100">
                <span class="">{{ $t('search.searchedCount') }} {{ fctContent.totalElementCount }}{{ $t('search.count') }}</span>
                <span class="" @click="toggleSort">{{ $t('search.distance') }}{{ ascOrDesc }}</span> <!-- TODO -->
            </div>
            <div class="card-box d-flex flex-row gap-2">
                <FctCard v-for="fctData in fctContent.facilities" :fctData="fctData" :key="fctData.fctId" />
            </div>
        </div>
    </div>
</template>
<script>
import apiClient from "../../apis/axios";
import SearchBox from '../../components/common/SearchBox.vue';
import FctCard from '../../components/facility/FctCard.vue';
export default{
    name: 'SearchedFacilityList',
    data() {
        const searchKeywordParam = this.$route.query["search-keyword"];
        this.$router.push({ path: "/facilities", query: {} });
        return{
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
        toggleSort(){
            this.isDistanceAsc = !this.isDistanceAsc;
        },
        async search(keyword) {
            this.searchKeyword = keyword;
            console.log(keyword);
            const params = {};
            if (this.searchKeyword && this.searchKeyword !== "") {
                params["search-keyword"] = this.searchKeyword;
                params["search-condition"] = "FACILITY_NAME"; // TODO: search condition variation
            }
            console.log(params);
            const responseBody = (await apiClient.get("/facilities", {params})).data;
            const content = responseBody.content;
            console.log(content);
            this.fctContent = content;
        }
    },
    async mounted(){
        const params = {};
        if (this.searchKeyword && this.searchKeyword !== "") {
            params["search-keyword"] = this.searchKeyword;
            params["search-condition"] = "FACILITY_NAME"; // TODO: search condition variation
        }
        console.log(params);
        const responseBody = (await apiClient.get("/facilities", {params})).data;
        const content = responseBody.content;
        console.log(content);
        this.fctContent = content;
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