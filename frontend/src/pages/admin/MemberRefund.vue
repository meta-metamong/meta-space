<template>
	<div class="container mt-4">
		<div class="d-flex justify-content-center">
			<AdminMemberRefund />
		</div>
	</div>
</template>

<script>
import AdminMemberRefund from '../../components/admin/AdminMemberRefund.vue';

export default{
    name: "Answer",
    components:{
        AdminMemberRefund
	}
}
</script>