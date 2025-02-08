<template>
    <div>홈 화면입니다!</div>
    <button @click="test">테스트</button>
</template>

<script>
export default{
    name: "Home",
    methods:{
        test(){
            alert('테스트');
        }
    }
}
</script>

<style scoped>
    
</style>