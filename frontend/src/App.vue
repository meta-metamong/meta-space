<template>
	<div id="app">
	  <Component :is="headerComponent" />
	  <hr />
	  <div class="routed">
		<router-view />
	  </div>
	  <Component :is="footerComponent" />
	</div>
  </template>
  
  <script>
  import Header from "./components/common/Header.vue";
  import AdminHeader from "./components/common/AdminHeader.vue";
  import Footer from "./components/common/Footer.vue";
  import AdminFooter from "./components/common/AdminFooter.vue";
  
  export default {
	name: "App",
	components: {
	  Header,
	  AdminHeader,
	  Footer,
	  AdminFooter
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
	  }
	}
  };
  </script>
  
  <style scoped>
  </style>
  