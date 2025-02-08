<template>
    <div class="container w-100 mt-4">
      <h2 class="text-center mb-3" v-text="$t('member.findPw')"></h2>

      <form @submit.prevent="handleSubmit">	
          <!-- 비밀번호 입력 -->
          <div class="mb-4">
              <input type="text" id="email" name="email" class="form-control signup-input mb-2" v-model="email" :placeholder="$t('member.email')" required />
          </div>	

          <button type="submit" class="w-100 signup-btn rounded-pill mb-3" v-text="$t('button.check')" />
          <button type="button" class="signup-btn w-100 mb-3 rounded-pill" @click="$router.push('/')">{{ $t('button.cancel') }}</button>
      </form>
    </div>
</template>

<script>
import { post } from '../../apis/axios';
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
          console.log(response);
          if(response.status === 200){
              alert(response.data.message);
              this.$router.push("/");
          }else{
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