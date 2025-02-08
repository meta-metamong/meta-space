<template>
    <div class="container w-100 mt-4">
      <h2 class="text-center mb-3" v-text="$t('member.changePw')"></h2>

      <form @submit.prevent="handleSubmit">	
          <!-- 비밀번호 입력 -->
          <div class="mb-4">
              <input type="password" id="password" name="password" class="form-control signup-input mb-2" v-model="newPassword" :placeholder="$t('member.password')" required />
              <h3 class="error-message mb-2" v-if="newPassword !== '' && !isValidatedPassword" v-text="$t('signupError.notValidatedPassword')" />
              <input type="password" id="passwordRe" name="passwordRe" class="form-control signup-input mb-2" v-model="newPasswordConfirm" :placeholder="$t('member.passwordRe')" required />
              <h3 class="error-message mt-2" v-if="newPasswordConfirm !== '' && !isPasswordEqual" v-text="$t('signupError.passwordNotMatched')" />
          </div>	
          <button type="submit" :disabled="!isValidatedPassword || !isPasswordEqual" class="w-100 mb-3 signup-btn rounded-pill" v-text="$t('button.check')" />
          <button type="button" class="signup-btn w-100 mb-3 rounded-pill" @click="$router.push('/profile')">{{ $t('button.cancel') }}</button>
      </form>
    </div>
</template>

<script>
import { put } from '../../apis/axios';
export default {
    name: 'ChangePassword',
    data() {
        return {
            newPassword: "",
            newPasswordConfirm: ""
        }
    },
    methods: {
      async handleSubmit(){
          const requestDto = {
              newPassword: this.newPassword,
              newPasswordConfirm: this.newPasswordConfirm
          }
          const response = await put("/members/password", requestDto);
          if(response.status === 200){
              alert(response.data.message);
              this.$router.push('/profile')
          }else{
              alert(response.response.data.message);
          }
      }
    },
    computed: {
        isPasswordEqual(){
            return this.newPassword === this.newPasswordConfirm;
        },
        isValidatedPassword(){
            return /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/.test(this.newPassword);
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