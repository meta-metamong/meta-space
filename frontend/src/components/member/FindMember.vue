<template>
    <div class="container mt-5 d-flex justify-content-center">
        <!-- 아이디 찾기 -->
        <div class="card p-4 mx-3" style="width: 400px;">
            <h3 class="text-center mb-4">{{ title }} 찾기</h3>
            <form @submit.prevent="handleSubmit">
                <div class="mb-3" v-if="idOrPw === 'pw'">
                    <input type="text" class="form-control" placeholder="아이디" name="id" v-model="id" required />
                </div>
                <div class="mb-3">
                    <input type="text" class="form-control" placeholder="이름" name="name" v-model="name" required />
                </div>
                <div class="mb-3">
                    <input type="email" class="form-control" placeholder="이메일" name="email" v-model="email" required />
                </div>
                    <input type="hidden" name="idOrPw" v-bind:value="idOrPw" />
                <button type="submit" class="btn btn-primary w-100">{{ title }} 찾기</button>
            </form>
        </div>
    </div>  
</template>
<script>
export default {
    name: "FindMember",
    data() {
        return {
            name: "",
            email: "",
            id: "",
            title: ""
        };
    },
    props: {
        idOrPw: {
            type: String,
            required: true
        }
    },
    methods: {
        handleSubmit(e) {
            console.log("아이디:", this.id);
            console.log("이름:", this.name);
            console.log("이메일:", this.email);
        }
    },
    mounted(){
        this.title = this.idOrPw === 'id' ? '아이디' : '비밀번호'
    }
};
</script>

<style scoped>
button{
    background: #19319D;
}
</style>
  