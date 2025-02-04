<template>
	<div class="container mt-4">
		<div class="d-flex justify-content-center">
			<SalesExport />
		</div>
	</div>
</template>

<script>
import SalesExport from '../components/admin/SalesExport.vue';

export default{
    name: "Download",
    components:{
        SalesExport
	}
}
</script>