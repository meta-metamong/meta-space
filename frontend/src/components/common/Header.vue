<template>
    <!-- 상단 네비게이션 -->
    <nav class="navbar fixed-top navbar-light bg-white border-bottom shadow-sm">
        <div class="container-fluid d-flex justify-content-between">
            <!-- 중앙 로고 -->
            <router-link to="/" class="text-center me-5">
                <img src="../../resource/image/logo.png" alt="MetaSpace Logo" class="navbar-logo">
            </router-link>

            <!-- 우측 알림 아이콘 & 로그아웃 버튼 -->
            <div class="d-flex align-items-center" v-if="isLogin">
                <button @click="$router.push('/notification/list')" class="noti-btn me-2">
                    <i class="bi bi-bell-fill text-primary"></i> <!-- 부트스트랩 아이콘 -->
                </button>
                <button class="btn" @click="logout" v-text="$t('member.logout')" />
            </div>
            <div>
                <select class="custom-select" @change="handleLanguageChange">
                    <option value="ko">KO</option>
                    <option value="en">EN</option>
                </select>
            </div>
        </div>
    </nav>
</template>

<script>
import { getUserIdInLocal, getUsernameInLocal, getUserRoleInLocal } from '../../store';
import Swal from 'sweetalert2';

export default {
    name: 'Header',
    data() {
        return {
            socketClient: null,
            message: ""
        }
    },
    methods: {
        logout() {
            Swal.fire({
                title: '로그아웃\n하시겠습니까?',
                text: '작업 중인 내용을 잃을 수 있습니다.',
                width: '300px',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "YES",
                cancelButtonText: "NO"
            }).then(result => {
                if (result.isConfirmed) {
                    this.$store.dispatch('logoutRequest');
                }
            })
        },
        handleLanguageChange(event) {
            this.$i18n.locale = event.target.value;
        },
        initUserId() {
            const userId = getUserIdInLocal();
            if (userId !== null && userId !== undefined) {
                this.$store.commit('initUserId', userId);
            }
        },
        initUserRole() {
            const userRole = getUserRoleInLocal();
            if (userRole) {
                this.$store.commit("initUserRole", userRole);
            }
        },
        initUserName() {
            const userName = getUsernameInLocal();
            if (userName) {
                this.$store.commit("initUserName", userName);
            }
        }
    },
    computed: {
        isLogin() {
            return this.$store.state.userId !== null && this.$store.state.userId !== undefined;
        }
    },
    mounted() {
        this.initUserId();
        this.initUserRole();
        this.initUserName();
        if (this.isLogin) {
            console.log("logged in");
            this.$store.commit("openWebSocket");
        } else {
            console.log("not logged in");
        }
    }
}
</script>

<style scoped>
button {
    color: #19319D;
    border: 1px solid #19319D;
}

.navbar {
    height: 55px;
}

.navbar-logo {
    height: 25px;
    /* 로고 크기 조절 */
}

.custom-select {
    width: 50px;
    /* 적당한 너비 */
    height: 30px;
    /* 네비게이션 바 높이에 맞춤 */
    border: 1px solid #19319D;
    /* 부드러운 테두리 */
    padding-left: 5px;
    border-radius: 5px;
    /* 둥근 모서리 */
    background-color: white;
    font-size: 14px;
    color: #19319D;
    cursor: pointer;
    outline: none;
}

.noti-btn{
    border-radius: 30px;
    border: none;
    font-size: 25px;
    background: none;
}
</style>