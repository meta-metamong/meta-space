<template>
    <!-- 하단 네비게이션 바 -->
    <nav class="navbar fixed-bottom navbar-light bg-white border-top shadow-sm">
       <div class="container-fluid d-flex justify-content-around">
         <router-link to="/admin/chat" class="nav-link text-center" :class="{ 'disabled-link': !isLogin }">
           <i class="bi bi-chat text-primary"></i><br>{{ $t('footer.chat') }}
         </router-link>
         <router-link to="/admin/memSearch" class="nav-link text-center"  v-if="isLogin">
            <i class="bi bi-person text-primary"></i><br>{{ $t('admin.memberSearch') }}
          </router-link>
          <router-link to="/admin/memSearch" class="nav-link text-center"  v-if="isLogin">
            <i class="bi bi-bookmark-check text-primary"></i><br>{{ $t('admin.facilityApproval') }}
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
             return this.$store.state.userId !== null && this.$store.state.userId !== undefined;
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