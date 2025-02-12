<template>
    <div class="container w-100 mt-4">
      <h2 class="text-center mb-3" v-text="$t('member.findPw')"></h2>

      <form class="text-center" @submit.prevent="handleSubmit">	
          <!-- 비밀번호 입력 -->
          <div class="mb-5 w-100 text-center">
              <input type="text" id="email" name="email" class="form-control w-100 signup-input mb-2" v-model="email" :placeholder="$t('member.email')" required />
          </div>	

          <button type="submit" class="w-100 signup-btn rounded-pill mt-3 mb-3" v-text="$t('button.check')" />
          <button type="button" class="w-100 signup-btn mb-3 rounded-pill" @click="$router.push('/')">{{ $t('button.cancel') }}</button>
      </form>
    </div>
</template>

<script>
import { post } from '../../apis/axios';
import Swal from 'sweetalert2';
export default {
  name: 'FindPassword',
  data() {
      return {
          email: ""
      }
  },
  methods: {
      async handleSubmit(){
          const requestDto = {
              email: this.email
          }
          const response = await post("/members/find-password", requestDto);
          
          if(response.status === 200){
            Swal.fire({
                width: "300px",
                title: "전송 완료",
                text: "재설정 링크를 전송했습니다.",
                icon: "success"
            })
            this.$router.push("/");
          }else{
            Swal.fire({
                width: "300px",
                title: "전송 실패",
                text: "재설정 링크 전송이 실패했습니다.",
                icon: "error"
            });
            alert(response.response.data.message);
          }
      }
  }
};
</script>

<style scoped>
input{
  border: none;
  border-radius: 0px;
  border-bottom: 1px solid #999;
}

.error-message {
  font-size: 16px;
  color: #ff0101;
}
</style>