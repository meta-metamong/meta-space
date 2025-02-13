<template>
    <div @click="moveToDetail" class="card card-box mb-3 d-flex flex-column">
        <img :src="fctData.repImgUrl" class="card-thumbnail mx-auto" alt="대표 이미지" />
        <div class="card-detail d-flex flex-column p-2">
            <p class="card-name fw-bold mb-2">{{ fctData.fctName }}</p>
            <p class="card-category">{{ fctData.catName }}</p>
            <p class="card-address mt-1"><i class="bi bi-geo-alt"></i> {{ fctData.fctAddress }}</p>
            <p v-if="fctData.distance" class="card-address mt-1"><i class="bi bi-compass"></i> {{ convertDistance(fctData.distance) }}</p>
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
    methods: {
        moveToDetail(){
            this.$router.push(`/facilities/${this.fctData.fctId}`);
        },
        convertDistance(dist){
            if(dist > 1000) return Math.ceil((dist / 1000)) + 'km'
            return dist + 'm';
        }
    },
    mounted(){
        if(this.fctData.fctName.length > 9){
            this.fctData.fctName = this.fctData.fctName.substring(0, 9) + '..';
        }
        if(this.fctData.fctAddress.length > 9){
            this.fctData.fctAddress = this.fctData.fctAddress.substring(0, 9) + '..';
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
    width: 100%;
    aspect-ratio: 16 / 9;
    object-fit: contain;
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
    font-size: 17px;
}
.card-detail{
    flex-grow: 1;
    border-top: 1px solid #ddd;
}
</style>