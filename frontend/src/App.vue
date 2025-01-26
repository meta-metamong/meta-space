<template>
	<div id="app">
        <Header />
		<hr />
		<router-view />
	</div>
</template>

<script>
import Header from "./components/common/Header.vue";

export default {
	name: "App",
    components: {
        Header
    }
}
</script>

<style scoped>
	ul li{
		list-style: none;
	}
</style>
