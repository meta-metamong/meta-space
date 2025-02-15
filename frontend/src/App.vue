<template>
	<div id="app">
	  <!-- Header 컴포넌트 (관리자가 아닐 때만 표시) -->
	  <Component v-if="!isAdmin" :is="headerComponent" />
  
	  <!-- Main content area with sidebar -->
	  <div class="main-content">
		<!-- Left Sidebar (관리자일 때만 보이도록 설정) -->
		<div class="sidebar" v-if="isAdmin">
		  <AdminSidebar />
		</div>
  
		<!-- Main content area (router-view) -->
		<div class="routed" :class="{ 'full-width scroll': !isAdmin }">
		  <router-view />
		</div>
	  </div>
  
	  <!-- Footer 컴포넌트 (관리자가 아닐 때만 보이도록 설정) -->
	  <Component v-if="!isAdmin" :is="footerComponent" />
	</div>
  </template>
  
  <script>
  import Header from "./components/common/Header.vue";
  import Footer from "./components/common/Footer.vue";
  import AdminFooter from "./components/common/AdminFooter.vue";
  import AdminSidebar from "./components/common/AdminSidebar.vue"; // 사이드바 컴포넌트 추가
  
  export default {
	name: "App",
	components: {
	  Header,
	  Footer,
	  AdminFooter,
	  AdminSidebar, // 사이드바 컴포넌트 등록
	},
	computed: {
	  headerComponent() {
		// 관리자가 아닐 때만 Header를 표시하고, 관리자는 아예 헤더를 숨긴다
		return !this.isAdmin ? Header : null;
	  },
	  footerComponent() {
		if (this.$store.state.userId) {
		  return /^\/admin/.test(this.$route.path) ? AdminFooter : Footer;
		} else {
		  return Footer;
		}
	  },
	  isAdmin() {
		// 경로가 '/admin'으로 시작하면 관리자
		return /^\/admin/.test(this.$route.path);
	  },
	},
  };
  </script>
  
  <style scoped>
  #app {
	display: flex;
	flex-direction: column;
	height: 100vh;
	/* 화면 전체 높이를 사용 */
  }
  
  .main-content {
	display: flex;
	flex: 1;
	/* 메인 컨텐츠 영역이 화면을 꽉 채우게 설정 */
  }
  
  .sidebar {
	width: 250px;
	/* 사이드바 너비 설정 */
	background-color: #f4f4f4;
	/* 배경색 추가 */
	padding: 20px;
	box-sizing: border-box;
  }
  
  .routed {
	flex: 1;
	/* 나머지 공간을 차지 */
	overflow-y: auto;
	/* 컨텐츠가 넘치면 스크롤 가능 */
	margin-top: 50px;
  }
  
  .scroll {
	max-height: 780px;
	overflow: scroll;
  }
  
  /* 관리자가 아니면 사이드바를 없애고 routed 영역이 전체 너비를 차지하도록 설정 */
  .routed.full-width {
	width: 100%;
	/* 사이드바가 없을 경우 전체 너비 차지 */
  }
  
  footer {
	/* margin-top: fixed; */
	/* footer는 맨 아래로 고정 */
	width: 100%;
	/* footer를 화면 전체 너비로 설정 */
  }
  </style>
  