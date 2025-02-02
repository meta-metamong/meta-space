<template>
	<div class="container mt-2">
		<div class="card p-3 mx-3 w-50 mx-auto text-center" style="max-width: 1000px;">
            <h2>어떤 회원이세요?</h2>
			<div class="d-flex mt-3 flex-md-row flex-column gap-3 justify-content-center mx-auto">
				<router-link to="/signup/user" class="btn btn-primary btn-lg py-4 w-75 text-center" v-text="$t('member.signupUser')" />
				<router-link to="/signup/owner" class="btn btn-primary btn-lg py-4 w-75 text-center" v-text="$t('member.signupOwner')" />
			</div>
		</div>
	</div>
</template>


<script>
export default{
    name: 'SelectSignupRole'
}
</script>

<style>
@media (max-width: 360px) {
    .btn {
        width: 100% !important;
    }
}
</style>