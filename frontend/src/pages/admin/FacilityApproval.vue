<template>
	<div class="container mt-4">
		<div class="d-flex justify-content-center">
			<FacilityRequestList />
		</div>
	</div>
</template>

<script>
import FacilityRequestList from '../../components/admin/FacilityRequestList.vue';

export default{
    name: "Answer",
    components:{
        FacilityRequestList
	}
}
</script>