<template>
	<div class="container mt-4">
		<div class="d-flex justify-content-center">
			<AdminMemberSearch />
		</div>
	</div>
</template>

<script>
import AdminMemberSearch from '../../components/admin/AdminMemberSearch.vue';

export default{
    name: "Answer",
    components:{
        AdminMemberSearch
	}
}
</script>