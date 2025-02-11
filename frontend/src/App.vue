<template>
	<div id="app">
	  <Component :is="headerComponent" />
  
	  <!-- Main content area with sidebar -->
	  <div class="main-content">
		<!-- Left Sidebar (only show when isAdmin is true) -->
		<div class="sidebar" v-if="isAdmin">
		  <AdminSidebar />
		</div>
  
		<!-- Main content area (router-view) -->
		<div class="routed" :class="{ 'full-width': !isAdmin }">
		  <router-view />
		</div>
	  </div>
  
	  <Component :is="footerComponent" />
	</div>
  </template>
  
  <script>
  import Header from "./components/common/Header.vue";
  import AdminHeader from "./components/common/AdminHeader.vue";
  import Footer from "./components/common/Footer.vue";
  import AdminFooter from "./components/common/AdminFooter.vue";
  import AdminSidebar from "./components/common/AdminSidebar.vue"; // 사이드바 컴포넌트 추가
  
  export default {
	name: "App",
	components: {
	  Header,
	  AdminHeader,
	  Footer,
	  AdminFooter,
	  AdminSidebar, // 사이드바 컴포넌트 등록
	},
	computed: {
	  headerComponent() {
		if (this.$store.state.userId) {
		  return /^\/admin/.test(this.$route.path) ? AdminHeader : Header;
		} else {
		  return Header;
		}
	  },
	  footerComponent() {
		if (this.$store.state.userId) {
		  return /^\/admin/.test(this.$route.path) ? AdminFooter : Footer;
		} else {
		  return Footer;
		}
	  },
	  isAdmin() {
		return /^\/admin/.test(this.$route.path); // 경로가 '/admin'으로 시작하면 admin인 경우
	  }
	}
  };
  </script>
  
  <style scoped>
  #app {
	display: flex;
	flex-direction: column;
  }
  
  .main-content {
	display: flex;
	height: 100vh; /* 화면 전체 높이를 사용 */
  }
  
  .sidebar {
	width: 250px; /* 사이드바 너비 설정 */
	background-color: #f4f4f4; /* 배경색 추가 */
	padding: 20px;
	box-sizing: border-box;
  }
  
  .routed {
	flex: 1; /* 나머지 공간을 차지 */
	padding: 20px;
	overflow-y: auto; /* 컨텐츠가 넘치면 스크롤 가능 */
  }
  
  /* If not admin, make routed area full width (remove sidebar) */
  .routed.full-width {
	width: 100%; /* 사이드바가 없을 경우 전체 너비 차지 */
  }
  
  footer {
	margin-top: auto; /* footer는 맨 아래로 고정 */
  }
  </style>
  