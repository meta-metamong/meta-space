<template>
    <div class="container">
        <!-- 제목 -->
        <h1 class="text-center mb-5">{{ $t('signup.signup') }} (3/3)</h1>
        <div class="mb-4">
            <input class="signup-input w-100" type="text" :placeholder="$t('member.name')" v-model="name" />
            <h3 class="error-message mt-2" v-if="name !== '' && !isValidatedName" v-text="$t('signupError.notValidatedName')" />
        </div>

        <div class="d-flex gap-4 mb-4">
            <div>
                <input class="form-check-input" type="radio" value="M" v-model="gender" />
                <label class="form-check-label" for="male">{{ $t('member.male') }}</label>
            </div>
            <div>
                <input class="form-check-input" type="radio" value="W" v-model="gender" />
                <label class="form-check-label" for="female">{{ $t('member.female') }}</label>
            </div>
        </div>

        <div class="mb-4">
            <input class="signup-input w-100" type="date" v-model="birthDate" />
        </div>

        <div class="mb-4">
            <input class="signup-input w-100" type="text" v-model="phone" :placeholder="$t('member.phone')" />
            <h3 class="error-message mt-2" v-if="phone !== '' && !isValidatedPhone" v-text="$t('signupError.notValidatedPhone')" />
        </div>

        <div class="mb-4">
            <input class="signup-input w-75" type="text" v-model="postalCode" :placeholder="$t('member.zipcode')" disabled />
            <button type="button" class="w-25 h-50 custom-btn" @click="searchPostCode">{{ $t('button.search') }}</button>
        </div>

        <div class="mb-4">
            <input class="signup-input w-100" type="text" v-model="address" :placeholder="$t('member.address')" disabled />
        </div>

        <div class="mb-4">
            <input class="signup-input w-100" type="text" v-model="detailAddress" :placeholder="$t('member.detailAddress')" />
        </div>

        <button type="button" class="w-100 signup-btn rounded-pill" :disabled="!isValidatedName || !isValidatedPhone || isInputEmpty" @click="nextStep()">{{ $t('signup.next') }}</button>
    </div>
</template>

<script>
import Swal from 'sweetalert2';
export default{
    name: 'InputDetail',
    props: ['setUserInfo'],
    data(){
        return{
            name: "",
            gender: "",
            birthDate: "",
            phone: "",
            postalCode: "",
            address: "",
            detailAddress: ""
        }
    },
    methods:{
		nextStep(){
            if(this.isInputEmpty){
                Swal.fire({
                    title: '세부정보 입력 실패',
                    width: '300px',
                    text: this.$t('signupError.emptyInput'),
                    icon: 'error'
                });
                return;
            }
            this.setUserInfo([{
                key: 'memName',
                value: this.name
            },{
                key: 'gender',
                value: this.gender
            }, {
                key: 'birthDate',
                value: this.birthDate
            },{
                key: 'memPhone',
                value: this.phone
            },{
                key: 'memPostalCode',
                value: this.postalCode,
            },{
                key: 'memAddress',
                value: this.address,
            },{
                key: 'memDetailAddress',
                value: this.detailAddress
            }]);
		},
        searchPostCode(){
            new daum.Postcode({
				oncomplete: (data) => {
					this.postalCode = data.zonecode;
					this.address = data.userSelectedType === 'R' ? data.address : data.jibunAddress;
				}
			}).open();
        }
	},
    computed:{
        isValidatedName(){
            return /^[a-zA-Z가-힣]+$/.test(this.name);
        },
        isValidatedPhone(){
            return /^01(?:0|1|[6-9])-(?:\d{3}|\d{4})-\d{4}$/.test(this.phone);
        },
        isInputEmpty(){
            return this.name.length === "" || this.gender === "" || this.birthDate === "" || this.phone === "" || this.postalCode === "" || this.address === "" || this.detailAddress === "";
        }
    }
}
</script>