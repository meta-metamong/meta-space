<template>
    <div class="card card-box mb-3 d-flex flex-column">
        <img :src="fctData.repImgUrl" class="card-thumbnail mx-auto" alt="대표 이미지" />
        <div class="card-detail d-flex flex-column p-2">
            <h5 class="card-name fw-bold mb-2">{{ fctData.fctName }}</h5>
            <p class="card-category">{{ fctData.catName }}</p>
            <p class="card-address mt-1"><i class="bi bi-geo-alt"></i> {{ fctData.fctAddress }}</p>
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
    width: 100%;
    max-width: 185px;
    min-width: 185px;
    display: flex;
    flex-direction: column;
}
.card-thumbnail{
    height: 60%;
    width: 100%;
    aspect-ratio: 16 / 9;
    object-fit: cover;
    border-top-left-radius: 0.375rem;
    border-top-right-radius: 0.375rem;
}
.card-category{
    color: #555;
}
.card-address{
    margin-bottom: 3px;
    color: #555;
}
.card-name{
    color: #333;
}
.card-detail{
    flex-grow: 1;
    border-top: 1px solid #ddd;
}
</style>