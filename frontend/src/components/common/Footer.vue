<template>
     <!-- 하단 네비게이션 바 -->
     <nav class="navbar fixed-bottom navbar-light bg-white border-top shadow-sm">
        <div class="container-fluid d-flex justify-content-around">
          <router-link to="/search" class="nav-link text-center">
            <i class="bi bi-search text-primary"></i><br>{{ $t('footer.search') }}
          </router-link>
          <router-link to="/map" class="nav-link text-center">
            <i class="bi bi-map text-primary"></i><br>{{ $t('footer.map') }}
          </router-link>
          <router-link to="/" class="nav-link text-center">
            <i class="bi bi-house text-primary"></i><br>{{ $t('footer.home') }}
          </router-link>
          <a href="/facilities" class="nav-link text-center">
            <i class="fa-solid fa-person-running text-primary"></i><br>{{ $t('footer.facility') }}
          </a>
          <div class="profile-menu text-center" v-if="isProfileMenuVisible">
            <ul class="list-group" @click="toggleIsProfileMenuVisible">
              <li v-if="userRole && userRole === 'ROLE_PROV'" class="list-group-item"><router-link to="/facilities/register">{{ $t('facility.facilityRegistration') }}</router-link></li>
              <li v-if="userRole && userRole === 'ROLE_PROV'" class="list-group-item"><router-link to="/facilities/my-facility-list">{{ $t('myFacility.myFacilityList') }}</router-link></li>
              <li class="list-group-item"><router-link to="/profile">{{ $t('member.profile') }}</router-link></li>
              <li v-if="userRole && userRole === 'ROLE_CONS'" class="list-group-item"><router-link to="/reservation/list">{{ $t('reservation.list') }}</router-link></li>
              <li v-if="userRole && userRole === 'ROLE_CONS'" class="list-group-item"><router-link to="/payment/list">{{ $t('payment.list') }}</router-link></li>
            </ul>
          </div>
          <div class="nav-link text-center" @click="toggleIsProfileMenuVisible"  v-if="isLogin">
            <i class="bi bi-person text-primary"></i><br>{{ $t('footer.profile') }}
          </div>
          <router-link to="/login" class="nav-link text-center" v-if="!isLogin">
            <i class="bi bi-person text-primary"></i><br>{{ $t('member.login')}}
          </router-link>
        </div>
      </nav>
</template>

<script>
    export default{
      name: "Footer",
      data(){
        return{
          isProfileMenuVisible: false
        }
      },
      computed:{
          isLogin(){
            return this.$store.state.userId !== null && this.$store.state.userId !== undefined;
          },
          userRole() {
            return this.$store.state.userRole;
          }
      },
      methods: {
        toggleIsProfileMenuVisible(){
          this.isProfileMenuVisible = !this.isProfileMenuVisible;
        }
      }
    }
</script>

<style scoped>
.disabled-link {
  pointer-events: none; /* 클릭 방지 */
  opacity: 0.5; /* 흐리게 표시 */
  cursor: not-allowed; /* 마우스 커서 변경 */
}

.navbar.sticky-bottom {
  padding: 10px 0;
}

.nav-link {
  font-size: 1rem;
}

.profile-menu{
  width: 140px;
  height: 130px;
  position: fixed;
  right: 0;
  bottom: 40px;
}

.list-group-item {
  border: 1px solid #4a66e6;
  padding: 5px 5px;
  text-align: center;
  cursor: pointer;
}

ul, li{
  list-style: none;
  padding: 0px;
}

a{
  text-decoration: none;
  color: #333;
}

</style>