<template>
	<div class="container w-100 mt-4">
		<h2 class="text-center mb-3" v-text="$t('member.update')"></h2>
		<div class="text-center mb-3">
            <img :src="memImage.fileDataInBase64" @click="uploadImage" alt="Profile Image" class="profile-img">
			<input type="file" :ref="'file-member-image'" @change="(e) => onImageUpload(e)" hidden/>
        </div>
        <div class="mb-4">
            <p class="ms-4 text-secondary">{{ $t('member.email') }}</p>
            <input type="text" class="signup-input w-75 text-secondary ms-5 mx-auto px-3 fs-5" v-model="memberInfo.email" disabled />
        </div>
        <div class="mb-4">
            <p class="ms-4 text-secondary">{{ $t('member.name') }}</p>
            <input type="text" class="signup-input w-75 text-secondary ms-5 px-3 fs-5" v-model="memberInfo.memName" />
			<h3 class="error-message ms-5 mt-2" v-if="memberInfo.memName !== '' && !isValidatedName" v-text="$t('signupError.notValidatedName')" />
        </div>
        <div class="mb-4">
            <p class="ms-4 text-secondary">{{ $t('member.phone') }}</p>
            <input type="text" class="signup-input w-75 text-secondary ms-5 px-3 fs-5" v-model="memberInfo.memPhone" />
			<h3 class="error-message ms-5 mt-2" v-if="memberInfo.memPhone !== '' && !isValidatedPhone" v-text="$t('signupError.notValidatedPhone')" />
        </div>
        <div class="mb-4">
            <p class="ms-4 text-secondary">{{ $t('member.birth') }}</p>
            <input type="date" class="signup-input w-75 text-secondary ms-5 px-3 fs-5" v-model="memberInfo.birthDate" />
        </div>
		<div class="mb-4">
			<p class="ms-4 text-secondary">{{ $t('member.gender') }}</p>
            <p class="w-75 mx-auto px-3 fs-5">
				<input class="form-check-input" type="radio" value="M" v-model="memberInfo.gender" :checked="memberInfo.gender === 'M'" />
				<label class="form-check-label" for="male">{{ $t('member.male') }}</label>
				<input class="form-check-input ms-5" type="radio" value="W" v-model="memberInfo.gender" :checked="memberInfo.gender === 'W'"/>
				<label class="form-check-label" for="female">{{ $t('member.female') }}</label>
			</p>
		</div>
        <div class="mb-4">
            <p class="ms-4 text-secondary">{{ $t('member.address') }}</p>
			<div class="mb-2">
				<input class="signup-input w-50 text-secondary ms-5 px-3" type="text" v-model="memberInfo.memPostalCode" disabled readonly />
				<button type="button" class="w-25 h-50 custom-btn" @click="searchPostCode">{{ $t('button.search') }}</button>
			</div>
            <input type="text" class="signup-input w-75 text-secondary ms-5 px-3" v-model="memberInfo.memAddress" disabled readonly />
            <input type="text" class="signup-input w-75 text-secondary ms-5 px-3" v-model="memberInfo.memDetailAddress" />
        </div>
        <div v-if="memberInfo.role==='ROLE_PROV'">
            <div class="mb-4">
                <p class="ms-4 text-secondary">{{ $t('member.businessName') }}</p>
                <input type="text" class="signup-input w-75 text-secondary ms-5 px-3 fs-5" v-model="memberInfo.bizName" />
            </div>
            <div class="mb-4">
                <p class="ms-4 text-secondary">{{ $t('member.businessNumber') }}</p>
                <input type="text" class="signup-input w-75 text-secondary ms-5 px-3 fs-5" v-model="memberInfo.bizRegNum" />
            </div>
            <div class="mb-4">
                <p class="ms-4 text-secondary">{{ $t('member.bank') }}</p>
				<select class="ms-5 w-75 form-select" @change="(e) => test(e)" v-model="memberInfo.bankCode">
					<option v-for="bank in bankList" :key="bank.bankCode" :value="bank.bankCode">{{ bank.bankName }}</option>
				</select>
            </div>
            <div class="mb-4">
                <p class="ms-4 text-secondary">{{ $t('member.account') }}</p>
                <input type="text" class="signup-input w-75 text-secondary ms-5 px-3 fs-5" v-model="memberInfo.accountNumber" />
            </div>
            <div class="mb-4">
                <p class="ms-4 text-secondary">{{ $t('member.balance') }}</p>
                <input type="text" class="signup-input w-75 text-secondary ms-5 px-3 fs-5" v-model="memberInfo.balance" />
            </div>
        </div>
        <div class="w-100 text-center mb-2 pt-3">
            <button class="signup-btn w-75 mb-3 rounded-pill" :disabled="!isValidatedName || !isValidatedPhone || isInputEmpty" @click="handleSubmit">{{ $t('button.save') }}</button>
			<button class="signup-btn w-75 mb-3 rounded-pill" @click="$router.push('/profile')">{{ $t('button.cancel') }}</button>
        </div>
	</div>
</template>
<script>
import { get, put } from "../../apis/axios";
import axios from "axios";
export default {
	name: 'UpdateMember',
	data() {
		return {
			memberInfo: {},
			errorMessage: "",
			memImage: {
				fileExtension: "png",
				fileDataInBase64: "https://cdn.pixabay.com/photo/2023/02/18/11/00/icon-7797704_1280.png",
				fileData: {}
			},
			bankList: []
		}
	},
	methods: {
		async handleSubmit() {
			let updateDto = {
				memName: this.memberInfo.memName,
				memPhone: this.memberInfo.memPhone,
				gender: this.memberInfo.gender,
				birthDate: this.memberInfo.birthDate,
				memPostalCode: this.memberInfo.memPostalCode,
				memAddress: this.memberInfo.memAddress,
				memDetailAddress: this.memberInfo.memDetailAddress,
			}

			if(this.memberInfo.role === 'ROLE_PROV'){
				updateDto = {
					...updateDto,
					bizName: this.memberInfo.bizName,
					bizRegNum: this.memberInfo.bizRegNum,
					bankCode: this.memberInfo.bankCode,
					accountNumber: this.memberInfo.accountNumber,
					memImage: (this.memImage.fileData === null || this.memImage.fileData === undefined) ? null : { fileType: this.memImage.fileExtension, order: 1 }
				}
			}
			await put(`/members`, updateDto)
			.then(response => {
				const uploadUrl = response.data.content.uploadUrl
				axios.put(uploadUrl, this.memImage.fileData, {
					headers: {
						"Content-Type": `image/${this.memImage.fileExtension}`
					}
				}).then(response => {
				if (response.status === 200) {
					alert("정보가 수정되었습니다.");
					this.$router.push("/profile")
				} else if (response.status === 400) {
					alert("회원정보 수정 오류 " + response.response.data.message);
				} else {
					return;
				}
			}).catch(error => {
				console.error(error);
			})
		})},
		async getMemberInfo() {
			const response = await get(`/members/${this.$store.state.userId}`);
			this.memberInfo = response.data.content;
			if(this.memberInfo.imgPath !== null) this.memImage.fileDataInBase64 = this.memberInfo.imgPath
		},
		searchPostCode(){
			new daum.Postcode({
				oncomplete: (data) => {
					this.memberInfo.memPostalCode = data.zonecode;
					this.memberInfo.memAddress = data.userSelectedType === 'R' ? data.address : data.jibunAddress;
				}
			}).open();
		},
		async getAllBanks(){
			const response = await get('/banks');
			this.bankList = response.data.content;
		},
		/*
			<이미지 업로드 프로세스>
			1. 프로필 이미지 클릭
			2. 등록할 파일 입력
			3. 파일의 확장자을 값으로 요청 후 파일 주소를 응답으로 받는다.
		*/
		uploadImage(){
			this.$refs['file-member-image'].click();
		},
		onImageUpload(e){
			const fileReader = new FileReader();
			fileReader.onload = () => {
				const filename = e.target.files[0].name;
				this.memImage = {
					fileExtension: filename.substring(filename.lastIndexOf(".") + 1),
                    fileDataInBase64: fileReader.result,
                    fileData: e.target.files[0]
				}
			};
			fileReader.readAsDataURL(e.target.files[0]);
		},
		test(e){
			console.log(e.target.value);
		}
	},
	computed:{
		isValidatedName(){
            return /^[a-zA-Z가-힣]+$/.test(this.memberInfo.memName);
        },
        isValidatedPhone(){
            return /^01(?:0|1|[6-9])-(?:\d{3}|\d{4})-\d{4}$/.test(this.memberInfo.memPhone);
        },
		isInputEmpty(){
			let isEmpty = this.memberInfo.name === "" || this.memberInfo.memPhone === "" || this.memberInfo.birthgDate === "" || this.memberInfo.gender === "" || this.memberInfo.memDetailAddress === "";
			if(this.memberInfo.role === "ROLE_PROV"){
				isEmpty = isEmpty || this.memberInfo.bizName === "" || this.memberInfo.bizRegNum === "" || this.memberInfo.bankCode === "" || this.memberInfo.provAccount === "" || this.memberInfo.provAccountOwner === "";
			}
			return this.isEmpty;
		}	
	},
	mounted() {
		this.getMemberInfo();
		this.getAllBanks();
	}
};
</script>

<style scoped>
.profile-img {
	width: 100px;
	height: 100px;
    object-fit: cover;
    border-radius: 50%;
    border: 3px solid #fff;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}
.profile-content{
    border-bottom: 1px solid #999;
}
.error-message {
	font-size: 14px;
	color: #ff0101;
}
</style>