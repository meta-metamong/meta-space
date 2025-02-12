<template>
	<div class="container mt-4">
		<div class="d-flex justify-content-center">
			<AdminSalesExport />
		</div>
	</div>
</template>

<script>
import AdminSalesExport from '../../components/admin/AdminSalesExport.vue';

export default{
    name: "Answer",
    components:{
        AdminSalesExport
	}
}
</script>