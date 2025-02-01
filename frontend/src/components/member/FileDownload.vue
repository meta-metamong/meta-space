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
        // URL 구문 오류 수정: 템플릿 문자열 제대로 작성
        const response = await get(`/download/${filename}`, {
          responseType: 'blob',  // 파일 다운로드를 위한 응답 형식
        });

        console.log("다운로드 응답:", response);

        // Blob을 사용하여 URL 객체를 생성하고, 다운로드 링크 생성
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', filename);  // 다운로드 파일 이름 설정
        document.body.appendChild(link);
        link.click();  // 다운로드 시작

        document.body.removeChild(link);  // 다운로드 후 링크 제거
      } catch (error) {
        console.error("파일 다운로드 실패:", error);
      }
    },
  },
};
</script>
