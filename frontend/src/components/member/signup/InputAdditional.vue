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
        <select v-model="bankCode" class="form-select mb-4">
            <option value="" selected disabled>{{ $t('signup.selectBank') }}</option>
            <option v-for="bank in bankList" :key="bank" :value="bank">{{ bank }}</option>
        </select>
        <!-- 계좌 -->
        <div class="mb-4">
            <input class="signup-input w-100" type="text" v-model="accountNumber" :placeholder="$t('member.account')" required />
        </div>
        <button type="button" class="w-100 signup-btn rounded-pill" :disabled="isInputEmpty" @click="nextStep()">{{ $t('signup.next') }}</button>    </div>
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
            accountNumber: "",
            bankList: bankList
        }
    },
    methods:{
		nextStep(){
			this.setUserInfo([{
                key: 'bizName',
                value: this.businessName
            },{
                key: 'bizRegNum',
                value: this.businessNumber
            },{
                key: 'bankCode',
                value: this.bankCode
            },{
                key: 'accountNumber',
                value: this.accountNumber
            }]);
		}
	},
    computed:{
        isValidatedName(){
            return /^[a-zA-Z가-힣]+$/.test(this.accountOwner);
        },
        isInputEmpty(){
            return this.businessName === "" || this.businessNumber === "" || this.bankCode === "" || this.accountNumber === "";
        }
    }
}
</script>