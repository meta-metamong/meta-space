<template>
	<div class="container mt-4">
		<div class="d-flex justify-content-center">
			<FileDownload />
		</div>
	</div>
</template>

<script>
import FileDownload from '../components/member/FileDownload.vue';

export default{
    name: "Download",
    components:{
        FileDownload
	}
}
</script>