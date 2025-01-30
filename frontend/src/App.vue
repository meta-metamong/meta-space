<template>
	<div id="app">
        <Header />
		<hr />
		<router-view />
		<Footer />
	</div>
</template>

<script>
import Header from "./components/common/Header.vue";
import Footer from "./components/common/Footer.vue";

export default {
	name: "App",
    components: {
        Header,
		Footer
    }
}
</script>

<style scoped>
	ul li{
		list-style: none;
	}
</style>
