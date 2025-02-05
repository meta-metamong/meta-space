<template>
	<div id="app">
        <Component :is="headerComponent" />
		<hr />
		<router-view />
		<Footer />
	</div>
</template>

<script>
import Header from "./components/common/Header.vue";
import AdminHeader from "./components/common/AdminHeader.vue";
import Footer from "./components/common/Footer.vue"

export default {
	name: "App",
    components: {
        Header,
		AdminHeader,
		Footer,
    },
	computed: {
		headerComponent() {
			return /^\/admin/.test(this.$route.path) ? AdminHeader : Header;
		}
	}
}
</script>

<style scoped>
</style>
