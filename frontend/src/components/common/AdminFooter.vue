<!-- AdminSidebar.vue -->
<template>
  <div class="admin-sidebar">
    <div class="navbar-nav flex-column">
      <router-link to="/admin/chat" class="nav-link text-center" :class="{ 'disabled-link': !isLogin }">
        <i class="bi bi-chat text-primary"></i><br>{{ $t('footer.chat') }}
      </router-link>
      <router-link to="/admin/memSearch" class="nav-link text-center" v-if="isLogin">
        <i class="bi bi-person text-primary"></i><br>{{ $t('admin.memberSearch') }}
      </router-link>
      <router-link to="/admin" class="nav-link text-center" v-if="isLogin">
        <i class="bi bi-house text-primary"></i><br>{{ $t('admin.dashboard') }}
      </router-link>
      <router-link to="/admin/facilityApproval" class="nav-link text-center" v-if="isLogin">
        <i class="bi bi-bookmark-check text-primary"></i><br>{{ $t('admin.facilityApproval') }}
      </router-link>
      <router-link to="/admin/memBen" class="nav-link text-center" v-if="isLogin">
        <i class="bi bi-slash-circle text-primary"></i><br>{{ $t('admin.memberBen') }}
      </router-link>
      <router-link to="/admin/salesExport" class="nav-link text-center" v-if="isLogin">
        <i class="bi bi-coin text-primary"></i><br>{{ $t('admin.salesExport') }}
      </router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: "AdminSidebar",
  computed: {
    isLogin() {
      return this.$store.state.userId !== null && this.$store.state.userId !== undefined;
    }
  }
};
</script>

<style scoped>
.admin-sidebar {
  position: fixed;
  top: 0;
  left: 0;
  width: 250px;
  height: 100%;
  background-color: #f8f9fa;
  border-right: 1px solid #ddd;
  padding-top: 20px;
  display: flex;
  justify-content: center; /* 수평 중앙 정렬 */
  align-items: center; /* 수직 중앙 정렬 */
}

.navbar-nav {
  display: flex;
  flex-direction: column;
  justify-content: center; /* 수직 중앙 정렬 */
  align-items: center; /* 수평 중앙 정렬 */
}

.nav-link {
  font-size: 1rem;
  margin: 10px 0;
}

.nav-link i {
  display: block;
  font-size: 2rem;
}

.disabled-link {
  pointer-events: none;
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
