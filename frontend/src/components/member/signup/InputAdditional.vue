<template>
    <div class="container">
        <!-- 제목 -->
        <h1 class="text-center mb-4">{{ $t('signup.additionalInfo') }}</h1>
        <!-- 사업자명 -->
        <div class="mb-4">
            <input class="signup-input w-100" type="text" v-model="businessName" :placeholder="$t('member.businessName')" required />
        </div>
        <!-- 사업자 등록번호 -->
        <div class="mb-4">
            <input class="signup-input w-100" type="text" v-model="businessNumber" :placeholder="$t('member.businessNumber')" required />
        </div>
        <!-- 은행명 -->
        <select class="form-select mb-4">
            <option value="" selected disabled>{{ $t('signup.selectBank') }}</option>
            <option v-for="bank in bankList" :key="bank" :value="bank">{{ bank }}</option>
        </select>
        <!-- 계좌 -->
        <div class="mb-4">
            <input class="signup-input w-100" type="text" v-model="account" :placeholder="$t('member.account')" required />
        </div>
        <!-- 예금주주 -->
        <div class="mb-4">
            <input class="signup-input w-100" type="text" v-model="accountOwner" :placeholder="$t('member.accountOwner')" required />
            <h3 class="error-message mt-2" v-if="accountOwner !== '' && !isValidatedName" v-text="$t('signupError.notValidatedName')" />
        </div>

        <button type="button" class="w-100 signup-btn" :disabled="isInputEmpty" @click="nextStep()">{{ $t('signup.next') }}</button>
    </div>
</template>

<script>
import { bankList } from '../../../assets/banks';

export default{
    name: 'InputAdditional',
    props: ['setUserInfo'],
    data(){
        return{
            businessName: "",
            businessNumber:"",
            bankCode: "",
            account: "",
            accountOwner: "",
            bankList: bankList
        }
    },
    methods:{
		nextStep(){
			this.setUserInfo([{
                key: 'bizName',
                value: this.businessName
            },{
                key: 'bizRegNumber',
                value: this.businessNumber
            },{
                key: 'bankCode',
                value: this.bankCode
            },{
                key: 'provAccount',
                value: this.account
            },{
                key: 'provAccountOwner',
                value: this.accountOwner
            }]);
		}
	},
    computed:{
        isValidatedName(){
            return /^[a-zA-Z가-힣]+$/.test(this.accountOwner);
        },
        isInputEmpty(){
            return this.businessName === "" || this.businessNumber === "" || this.bankCode === "" || this.account === "" || this.accountOwner === "";
        }
    }
}
</script>