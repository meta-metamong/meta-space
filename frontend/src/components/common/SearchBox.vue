<template>
    <div class="search-box">
        <input type="text" class="search-input text-center fs-4" v-model="keyword" :placeholder="$t('search.enterFct')">
        <button class="search-btn" @click="search">🔍</button>
    </div>
</template>
<script>
export default{
    props: {
        searchKeyword: {
            type: String,
            required: false
        }
    },
    name: 'SearchBox',
    data(){
        return{
            keyword: this.searchKeyword || ""
        }
    },
    methods: {
        search(){
            if(this.keyword === ""){
                alert(this.$t('search.enterFct'));
                return;
            }
            // TODO: 검색어를 레디스에 저장
            this.$emit("doSearch", this.keyword);
        }
    }
}
</script>
<style scoped>
.search-box{
    width: 100%;
    height: 50px;
    border: 1px solid #bbb;
    border-radius: 15px;
    line-height: 50px;
    position: relative;
}
.search-input{
    position: absolute;
    display: inline-block;
    border: none;
    width: 100%;
    border-radius: 15px;
    height: 100%;
    padding-right: 35px;
}
.search-btn{
    position: absolute;
    right: 0;
    border-top-right-radius: 15px;
    border-bottom-right-radius: 15px;
    overflow: hidden;
    line-height: 45px;
    background: transparent;
    font-size: 30px;
}
</style>