<template>
    <div class="search-box">
        <input type="text" class="search-input text-center fs-3" v-model="keyword" placeholder="시설명을 입력하세요">
        <button class="search-btn">🔍</button>
    </div>
</template>
<script>
export default{
    name: 'SearchBox',
    data(){
        return{
            keyword: ""
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