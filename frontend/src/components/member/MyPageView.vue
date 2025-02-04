<template>
    <div class="container mt-5">
        <div class="row">
            <!-- 프로필 정보 -->
            <div class="col-md-4">
                <div class="card shadow-sm h-100">
                    <div class="card-body text-center">
                        <img src="https://cdn.pixabay.com/photo/2023/02/18/11/00/icon-7797704_1280.png"
                            alt="Profile Image" class="profile-img">
                        <h4 class="mt-4">홍길동</h4>
                        <p class="text-muted">@honggildong</p>
                    </div>
                </div>
            </div>

            <!-- 개인정보 -->
            <div class="col-md-8">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <h5 class="mb-3"><strong>{{ $t('member.information') }}</strong></h5>
                        <div class="row mb-2">
                            <div class="col-sm-4 info-label">{{ $t('member.name') }}</div>
                            <div class="col-sm-8">{{ memberInfo.name }}</div>
                        </div>
                        <div class="row mb-2">
                            <div class="col-sm-4 info-label">{{ $t('member.email') }}</div>
                            <div class="col-sm-8">{{ memberInfo.email }}</div>
                        </div>
                        <div class="row mb-2">
                            <div class="col-sm-4 info-label">{{ $t('member.phoneNumber') }}</div>
                            <div class="col-sm-8">{{ memberInfo.phone }}</div>
                        </div>
                        <div class="row mb-2">
                            <div class="col-sm-4 info-label">{{ $t('member.address') }}</div>
                            <div class="col-sm-8">{{ address }}</div>
                        </div>
                        <button class="btn btn-primary mt-3" @click="route('/update')">{{ $t('button.save') }}</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- 구매 내역 -->
        <div class="row mt-5">
            <div class="col-md-12">
                <div>
                    <h5 class="mb-4"><strong>예약 내역</strong></h5>
                    <table class="table table-hover">
                        <thead class="table-light">
                            <tr>
                                <th>#</th>
                                <th>상품명</th>
                                <th>이용 시간</th>
                                <th>가격</th>
                                <th>상태</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>1</td>
                                <td>공유오피스 회의실</td>
                                <td>2025.02.10 (토) 11시 ~ 16시, 5시간</td>
                                <td>10,000원</td>
                                <td><span class="badge bg-success">예약확정</span></td>
                            </tr>
                            <tr>
                                <td>2</td>
                                <td>랭스터디카페 대학로점</td>
                                <td>2025.02.03 (토) 15시 ~ 17시, 2시간</td>
                                <td>6,000원</td>
                                <td><span class="badge bg-danger">예약취소</span></td>
                            </tr>
                            <tr>
                                <td>3</td>
                                <td>스터디룸제이 1호점</td>
                                <td>2025.02.04 (토) 12시 ~ 13시, 1시간</td>
                                <td>3,000원</td>
                                <td><span class="badge bg-secondary">이용완료</span></td>
                            </tr>
                        </tbody>
                    </table>
                    <a href="#" class="btn btn-outline-secondary">더 보기</a>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import { get } from "../../apis/axios";
import { toRaw } from 'vue';
export default {
    name: "MyPageView",
    data() {
        return {
            memberInfo: [],
        };
    },
    computed: {
        user() {
            return toRaw(this.$store.state.user);
        },
        address() {
            return `(${this.memberInfo.postalCode}) ${this.memberInfo.address} ${this.memberInfo.detailAddress}`;
        }
    },
    methods: {
        async getMemberInfo() {
            const response = await get(`/members/${this.user.userId}`);
            this.memberInfo = response.data.content;
        },
        route(page){
			this.$router.push(page);
		},
    },
    mounted() {
        this.getMemberInfo();
    },
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

.info-label {
    font-weight: bold;
    color: #555;
}

.purchase-card {
    border: 1px solid #ddd;
    border-radius: 8px;
    padding: 16px;
    display: flex;
    align-items: center;
    margin-bottom: 16px;
}

.purchase-card img {
    width: 60px;
    height: 60px;
    object-fit: cover;
    border-radius: 4px;
    margin-right: 16px;
}

.purchase-card .badge {
    font-size: 0.9rem;
}

.purchase-card .details {
    flex-grow: 1;
}
</style>