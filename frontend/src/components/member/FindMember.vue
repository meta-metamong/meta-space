<template>
    <div class="container mt-2 d-flex justify-content-center">
        <!-- 아이디 찾기 -->
        <div class="card p-4" style="width: 400px;">
            <h3 class="text-center mb-3">{{ title }}</h3>
            <form @submit.prevent="handleSubmit">
                <div class="mb-2" v-if="idOrPw === 'pw'">
                    <input type="text" class="form-control" :placeholder="$t('member.id')" name="userid" v-model="userid" required />
                </div>
                <div class="mb-2">
                    <input type="text" class="form-control" :placeholder="$t('member.name')" name="name" v-model="name" required />
                </div>
                <div class="mb-2">
                    <input type="email" class="form-control" :placeholder="$t('member.email')" name="email" v-model="email" required />
                </div>
                    <input type="hidden" name="idOrPw" v-bind:value="idOrPw" />
                <button type="submit" class="btn btn-primary w-100">{{ title }}</button>
            </form>
        </div>
    </div>  
</template>
<script>
import { post } from "../../apis/axios"
export default {
    name: "FindMember",
    data() {
        return {
            name: "",
            email: "",
            userid: ""
        };
    },
    props: {
        idOrPw: {
            type: String,
            required: true
        }
    },
    computed: {
        title() {
            return this.idOrPw === 'id' 
                ? this.$t('member.findId') 
                : this.$t('member.findPw');
        }
    },
    methods: {
        async handleSubmit(e) {
            const requestObject = {
                "idOrPw": this.idOrPw,
                "userid": this.userid,
                "name": this.name,
                "email": this.email
            }
            const data = await post("/members/find-member", requestObject);
            alert(data);
        }
    }
};
</script>

<style scoped>
</style>
  