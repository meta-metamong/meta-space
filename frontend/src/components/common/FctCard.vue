<template>
    <div class="card card-box mb-3">
        <img :src="fctData.repImgUrl" class="card-thumbnail mx-auto" alt="대표 이미지" />
        <div class="card-detail d-flex flex-column pt-2 ps-2">
            <p class="card-category mb-1">{{ fctData.catName }}</p>
            <h5 class="card-name fs-3">{{ fctData.fctName }}</h5>
        </div>
    </div>
</template>
<script>
export default{
    name: 'FctCard',
    props: {
        fctData: {
            type: Object,
            required: true
        }
    },
    mounted(){
        if(this.fctData.fctName.length > 9){
            this.fctData.fctName = this.fctData.fctName.substring(0, 9) + '..';
        }
    }
}
</script>
<style scoped>
.card-thumbnail{
    height: 60%;
}
.card-box{
    min-width: 200px;
    max-width: 200px;
    max-height: 200px;
}
.card-detail{
    border-top: 1px solid #ddd;
    font-size: 15px;
}
</style>