<template>
     <!-- 하단 네비게이션 바 -->
     <nav class="navbar sticky-bottom navbar-light bg-white border-top shadow-sm">
        <div class="container-fluid d-flex justify-content-around">
          <router-link to="/" class="nav-link text-center">
            <i class="bi bi-search text-primary"></i><br>{{ $t('footer.search') }}
          </router-link>
          <router-link to="/" class="nav-link text-center">
            <i class="bi bi-map text-primary"></i><br>{{ $t('footer.map') }}
          </router-link>
          <router-link to="/" class="nav-link text-center">
            <i class="bi bi-house text-primary"></i><br>{{ $t('footer.home') }}
          </router-link>
          <router-link to="/" class="nav-link text-center" :class="{ 'disabled-link': !isLogin }">
            <i class="bi bi-chat text-primary"></i><br>{{ $t('footer.chat') }}
          </router-link>
          <router-link to="/" class="nav-link text-center"  v-if="isLogin">
            <i class="bi bi-person text-primary"></i><br>{{ $t('footer.profile') }}
          </router-link>
          <router-link to="/login" class="nav-link text-center" v-if="!isLogin">
            <i class="bi bi-person text-primary"></i><br>{{ $t('member.login')}}
          </router-link>
        </div>
      </nav>
</template>

<script>
    export default{
        name: "Footer",
        methods:{

        },
        computed:{
            isLogin(){
                return this.$store.state.isLogin;
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

</style>