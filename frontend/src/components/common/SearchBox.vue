<template>
    <div class="search-box">
        <input type="text" class="search-input text-center fs-4" v-model="keyword" :placeholder="$t('search.enterFct')">
        <button class="search-btn" @click="search">🔍</button>
    </div>
</template>
<script>
import { post } from '../../apis/axios';
export default{
    name: 'SearchBox',
    data(){
        return{
            keyword: ""
        }
    },
    props: {
        keywordProp: {
            type: String,
            required: false
        }
    },
    methods: {
        async search(){
            if(this.keyword === ""){
                alert(this.$t('search.enterFct'));
                return;
            }
            const requestDto = {
                keyword: this.keyword
            }
            const response = await post("/search", requestDto);
            if(response.status === 200){
                this.$router.push(`/search-fct-list/${this.keyword}`);
            }else{
                alert("검색어 저장에 실패했습니다.");
                return;
            }
        }
    },
    mounted(){
        if(this.keywordProp !== null || this.keywordProp !== undefined) {
            this.keyword = this.keywordProp;
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