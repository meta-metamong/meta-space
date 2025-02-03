<template>
	<div id="app">
        <Component :is="headerComponent" />
		<hr />
		<WebSocket v-if="isChatVisible" />
		<router-view />
		<Footer />
	</div>
</template>

<script>
import Header from "./components/common/Header.vue";
import AdminHeader from "./components/common/AdminHeader.vue";
import Footer from "./components/common/Footer.vue";
import WebSocket from './components/member/WebSocket.vue';

export default {
	name: "App",
    components: {
        Header,
		AdminHeader,
		Footer,
		WebSocket,
    },
	computed: {
		headerComponent() {
			return this.$route.path === '/admin' ? AdminHeader : Header;
		}
	},
	isChatVisible() {
      return this.$route.path === '/' || this.$route.path === '/admin';
    },
}
</script>

<style scoped>
	ul li{
		list-style: none;
	}
</style>
