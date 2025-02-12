<template>
    <div class="admin-sidebar">
      <div class="navbar-nav flex-column">
        <!-- <img src="../../resource/image/logo.png" alt="MetaSpace Logo" class="navbar-logo"> -->
        <img src="../../resource/image/logo.png" alt="MetaSpace Logo" class="navbar-logo">

        <!-- 대시보드 항목 -->
        <router-link to="/admin" class="nav-link text-center" v-if="isLogin">
          <i class="bi bi-house menu-icon" :style="{ color: linkColor }"></i><span>{{ $t('admin.dashboard') }}</span>
        </router-link>
  
        <!-- 회원 관리 메뉴 -->
        <div class="nav-link" @click="toggleMemberManagement">
          <i class="bi bi-person menu-icon" :style="{ color: linkColor }"></i><span>{{ $t('admin.memberManagement') }}</span>
          <i class="bi bi-caret-down ml-3 "></i> <!-- 화살표 아이콘을 좀 더 오른쪽으로 이동 -->
        </div>
        <div v-if="isMemberManagementVisible" class="sub-menu">
          <router-link to="/admin/memSearch" class="nav-link text-center">
            <span>{{ $t('admin.memberSearch') }}</span>
          </router-link>
          <router-link to="/admin/memBen" class="nav-link text-center">
            <span>{{ $t('admin.memberBen') }}</span>
          </router-link>
        </div>
  
        <!-- 시설 관리 메뉴 -->
        <div class="nav-link" @click="toggleFacilityManagement">
          <i class="bi bi-building menu-icon" :style="{ color: linkColor }"></i><span>{{ $t('admin.facilityManagement') }}</span>
          <i class="bi bi-chevron-right text-white ml-auto"></i> <!-- 화살표 아이콘 추가 -->
        </div>
        <div v-if="isFacilityManagementVisible" class="sub-menu">
          <router-link to="/admin/facilityApproval" class="nav-link text-center">
            <span>{{ $t('admin.facilityApproval') }}</span>
          </router-link>
        </div>
  
        <!-- 매출 관리 메뉴 -->
        <div class="nav-link" @click="toggleSalesManagement">
          <i class="bi bi-coin" :style="{ color: linkColor }"></i><span>{{ $t('admin.salesManagement') }}</span>
          <i class="bi bi-caret-down ml-3 "></i> <!-- 화살표 아이콘 추가 -->
        </div>
        <div v-if="isSalesManagementVisible" class="sub-menu">
          <router-link to="/admin/salesExport" class="nav-link text-center">
            <span>{{ $t('admin.salesExport') }}</span>
          </router-link>
        </div>
  
        <!-- 채팅 메뉴 -->
        <router-link to="/admin/chat" class="nav-link text-center" :class="{ 'disabled-link': !isLogin }">
          <i class="bi bi-chat" :style="{ color: linkColor }"></i><span>{{ $t('footer.chat') }}</span>
        </router-link>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: "AdminSidebar",
    data() {
      return {
        isMemberManagementVisible: false,
        isFacilityManagementVisible: false,
        isSalesManagementVisible: false,
      };
    },
    computed: {
      isLogin() {
        return this.$store.state.userId !== null && this.$store.state.userId !== undefined;
      },
      linkColor() {
        return '#003366';  // 메뉴 텍스트와 아이콘의 색상을 동일하게 설정
      }
    },
    methods: {
      toggleMemberManagement() {
        this.isMemberManagementVisible = !this.isMemberManagementVisible;
        this.isFacilityManagementVisible = false; // 다른 메뉴는 닫기
        this.isSalesManagementVisible = false;
      },
      toggleFacilityManagement() {
        this.isFacilityManagementVisible = !this.isFacilityManagementVisible;
        this.isMemberManagementVisible = false; // 다른 메뉴는 닫기
        this.isSalesManagementVisible = false;
      },
      toggleSalesManagement() {
        this.isSalesManagementVisible = !this.isSalesManagementVisible;
        this.isMemberManagementVisible = false; // 다른 메뉴는 닫기
        this.isFacilityManagementVisible = false;
      }
    }
  };
  </script>
  
  <style scoped>
  .admin-sidebar {
    position: fixed;
    top: 0;
    left: 0;
    width: 15%;
    height: 100%;
    background-color: #f8f9fa;
    border-right: 1px solid #ddd;
    padding-top: 20px;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: flex-start;
  }
  
  .navbar-nav {
    display: flex;
    flex-direction: column;
    width: 100%;
  }
  
  .nav-link {
    display: flex;
    align-items: center;
    font-size: 1.3rem;
    padding: 10px 15px;
    text-decoration: none;
    color: #003366;
    background-color: transparent;
    border-bottom: 1px solid #ddd;
    width: 100%;
    cursor: pointer;
  }
  
  .nav-link:hover {
    background-color: #f0f0f0;
    border-bottom: 1px solid #003366;
  }
  
  .nav-link:active, .nav-link:focus {
    background-color: #cfe7fc;
    border-bottom: 1px solid #003366;
  }
  
  .sub-menu {
    padding-left: 20px;
    background-color: #c9ddf9;
  }
  
  .sub-menu .nav-link {
    background-color: transparent;
    margin: 5px 0;
    border-bottom: none;
    font-size: 1rem;
  }
  
  .sub-menu .nav-link:hover {
    background-color: #cce7ff;
  }
  
  .sub-menu .nav-link:active, .sub-menu .nav-link:focus {
    background-color: #99c2ff;
  }
  
  .ml-auto {
    margin-left: auto;
  }
  
  .ml-3 {
    margin-left: 4rem;
  }
  
  .disabled-link {
    pointer-events: none;
    opacity: 0.5;
    cursor: not-allowed;
  }

  .menu-icon {
    margin-left: 10px;
  margin-right: 25px;
}
.navbar-logo {
  width: 200px; /* 원하는 이미지 크기 */
  height: 200px; /* 동일한 높이 설정 */
  border-radius: 50%; /* 동그랗게 만들기 */
  display: block; /* 이미지를 블록 요소로 만들고 */
  margin: 0 auto; /* 가로로 가운데 정렬 */
}


  </style>
  