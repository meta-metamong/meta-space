<template>
  <div>
    <button @click="downloadFile('token.zip')">Download File</button>
  </div>
</template>

<script>
import { get } from "../../apis/axios";

export default {
  methods: {
    async downloadFile(filename) {
      try {
        const response = await get(`/download?filename=${filename}`, {
          responseType: 'blob', 
        });

        console.log("다운로드 응답:", response);  

        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', filename); 
        document.body.appendChild(link);
        link.click(); 

        document.body.removeChild(link);
      } catch (error) {
        console.error("파일 다운로드 실패:", error);
      }
    },
  },
};
</script>

<style scoped>

</style>
