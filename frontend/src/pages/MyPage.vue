<template>
	<div class="container mt-4 box">
		<div class="d-flex flex-column gap-3 flex-md-row justify-content-center">
			<MyPageView />
		</div>
	</div>
</template>

<script>
import MyPageView from '../components/member/MyPageView.vue';

export default{
    name: "MyPage",
    components:{
		MyPageView,
	}
}
</script>

<style scoped>
</style>