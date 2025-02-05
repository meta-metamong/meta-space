<template>
	<div class="container mt-4">
		<div class="d-flex justify-content-center">
			<OnlineMemberList />
		</div>
	</div>
</template>

<script>
import OnlineMemberList from '../components/admin/OnlineMemberList.vue';

export default{
    name: "Answer",
    components:{
        OnlineMemberList
	}
}
</script>