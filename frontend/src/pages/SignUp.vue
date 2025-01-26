<template>
	<div class="container mt-4">
		<div class="d-flex">
			<SignUpMember />
		</div>
	</div>
</template>

<script>
import SignUpMember from '../components/member/SignUpMember.vue';

export default{
    name: "SignUp",
    components:{
        SignUpMember
	}
}
</script>