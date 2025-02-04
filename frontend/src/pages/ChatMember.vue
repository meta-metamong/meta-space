<template>
	<div class="container mt-4">
		<div class="d-flex justify-content-center">
			<WebSocket />
		</div>
	</div>
</template>

<script>
import WebSocket from '../components/member/ChattingMember.vue';

export default{
    name: "Answer",
    components:{
        WebSocket
	}
}
</script>