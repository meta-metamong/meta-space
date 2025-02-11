<template>
    <div class="card card-box mb-3 d-flex flex-column">
        <img :src="fctData.repImgUrl" class="card-thumbnail mx-auto" alt="대표 이미지" />
        <div class="card-detail d-flex flex-column p-2">
            <p class="card-category">{{ fctData.catName }}</p>
            <p class="card-address">{{ fctData.fctAddress }}</p>
            <h5 class="card-name fs-3 fw-bold">{{ fctData.fctName }}</h5>
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
*{
    margin: 0;
    padding: 0;
}
.card-box{
    min-width: 200px;
    max-width: 200px;
    max-height: 250px;
    height: 250px;
}
.card-thumbnail{
    height: 60%;
    max-width: 200px;
    object-fit: cover;
}
.card-category{
    color: #555;
}
.card-address{
    margin-bottom: 5px;
    color: #555;
}
.card-name{
    color: #333;
}
.card-detail{
    border-top: 1px solid #ddd;
}
</style>