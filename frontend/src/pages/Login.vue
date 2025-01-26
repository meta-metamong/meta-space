<template>
	<div class="container mt-4">
		<div class="d-flex justify-content-center">
			<LoginMember />
		</div>
	</div>
</template>

<script>
import LoginMember from '../components/member/LoginMember.vue';

export default{
    name: "Login",
    components:{
        LoginMember
	}
}
</script>