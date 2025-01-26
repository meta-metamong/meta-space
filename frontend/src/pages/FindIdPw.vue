<template>
	<div class="container mt-4 box">
		<div class="d-flex flex-column gap-3 flex-md-row justify-content-center">
			<FindMember idOrPw="id" />
			<FindMember idOrPw="pw" />
		</div>
	</div>
</template>

<script>
import FindMember from '../components/member/FindMember.vue';

export default{
    name: "FindIdPw",
    components:{
        FindMember
	}
}
</script>

<style scoped>
.box {
	width: 400px;
}
</style>