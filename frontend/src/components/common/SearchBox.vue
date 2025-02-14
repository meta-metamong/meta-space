<template>
    <div class="search-box mt-2">
        <input type="text" class="search-input text-center fs-5" v-model="keyword" :placeholder="$t('search.enterFct')" @keyup.enter="search">
        <button class="search-btn" @click="search">🔍</button>
    </div>
</template>
<script>
import Swal from 'sweetalert2';
import { post } from '../../apis/axios';
export default{
    props: {
        searchKeyword: {
            type: String,
            required: true
        }
    },
    name: 'SearchBox',
    data(){
        return{
            keyword: ""
        }
    },
    methods: {
        async search(){
            if(!this.keyword){
                Swal.fire({
                    text: this.$t('search.enterFct'),
                    width: '300px',
                    icon: 'warning'
                })
                return;
            }
            const requestDto = {
                keyword: this.keyword
            }
            const response = await post("/search", requestDto);
            if(response.status === 200){
                this.$emit("doSearch", this.keyword);
            }else{
                Swal.fire({
                    text: "검색어 저장에 실패했습니다.",
                    width: '300px',
                    icon: 'error'
                })
                return;
            }
        }
    },
    mounted(){
        if(this.searchKeyword) {
            this.keyword = this.searchKeyword;
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
    border: none;
    overflow: hidden;
    line-height: 45px;
    background: transparent;
    font-size: 30px;
}
</style>