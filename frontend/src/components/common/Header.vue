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
                <button @click="$router.push('/notification/list')" class="noti-btn me-3">
                    <i class="bi bi-bell-fill text-primary"></i> <!-- 부트스트랩 아이콘 -->
                    <div class="noti-count-box" v-if="unreadNotiCount > 0">
                        <div class="noti-count">{{ unreadNotiCount < 10 ? unreadNotiCount : "9+" }}</div>
                    </div>
                </button> <!-- TODO: 알림이 있으면 채워지고 없으면 안 채워지고 -->
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
import { get } from "../../apis/axios";
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
            return userId;
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
        },
        findLocation() {
            // HTML5의 geolocation으로 사용할 수 있는지 확인합니다 
            if (navigator.geolocation) {

                // GeoLocation을 이용해서 접속 위치를 얻어옵니다
                navigator.geolocation.getCurrentPosition((position) => {

                    this.$store.commit('saveLoc', {
                        lat: position.coords.latitude,
                        lon: position.coords.longitude
                    });
                });

            }
        },
        fetchNotificationCount(memId) {
            if (memId) {
                get(`/members/${memId}/unread-notification-count`)
                        .then((response) => {
                            const unreadNotiCount = response.data.content;
                            console.log("unreadNotificationCount=" + unreadNotiCount);
                            this.$store.commit("setUnreadNotificationCount", unreadNotiCount);
                        });
            }
        }
    },
    computed: {
        isLogin() {
            return this.$store.state.userId !== null && this.$store.state.userId !== undefined;
        },
        unreadNotiCount() {
            return this.$store.state.unreadNotiCount;
        }
    },
    mounted() {
        const userId = this.initUserId();
        this.initUserRole();
        this.initUserName();
        if (this.isLogin) {
            console.log("logged in");
            this.$store.commit("openWebSocket");
            this.fetchNotificationCount(userId);
        } else {
            console.log("not logged in");
        }
        this.findLocation();
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
    position: relative;
}

.noti-count-box {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 20px;
    height: 20px;
    border-radius: 30px;
    background-color: salmon;
    position: absolute;
    top: 0;
    right: -3px;
}

.noti-count {
    color: white;
    font-size: 12px;
    font-weight: 500;
    text-align: center;
    padding: 0;
    margin: 0;
    height: fit-content;
}
</style>