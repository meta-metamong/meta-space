<template>
  <div class="container">
    <h2>환불 요청 승인</h2>
    <table>
      <thead>
        <tr>
          <th>이름</th>
          <th>결제금액</th>
          <th>취소사유</th>
          <th>환불계좌</th>
          <th>승인/반려</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="refund in refunds" :key="refund.rvtId">
          <td>{{ refund.memName }}</td>
          <td>{{ refund.payPrice }}</td>
          <td>{{ refund.rvtCancelationReason }}</td>
          <td>{{ refund.refundInfo }}</td>
          <td>
            <!-- 등록 승인 버튼 -->
            <button @click="confirmApproverefund(refund.rvtId, 'approval')" class="approve-button">환불승인</button>
            <button @click="confirmRejectrefund(refund.rvtId, 'rejection')" class="reject-button">환불반려</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>


<script>
import { get, post } from '../../apis/axios'; // axios 경로를 수정하세요.

export default {
data() {
  return {
    refunds: [] // 환불 요청 리스트
  };
},
created() {
  this.fetchRequestRefunds(); // 컴포넌트가 생성되면 데이터 조회
},
methods: {
  // 환불 요청 리스트를 가져오는 메소드
  async fetchRequestRefunds() {
    try {
      const response = await get('/admin/getRefundMembers'); // API 경로에 맞춰서 호출
      if (response && response.data) {
        console.log("환불 요청 리스트 (content):", response.data.content);
        this.refunds = response.data.content; // 응답 데이터에서 환불 요청 리스트를 받는다.
      }
    } catch (error) {
      console.error('Error fetching request Refunds:', error);
    }
  },
  // 환불 승인 또는 반려 버튼 클릭 시 확인을 요청하는 메소드
  confirmApproverefund(refundId, actionType) {
    console.log("refundId:", refundId);
    let confirmMessage = '';
    if (actionType === 'approval') {
      confirmMessage = '환불 승인하시겠습니까?';
    } else if (actionType === 'rejection') {
      confirmMessage = '환불 반려하시겠습니까?';
    }

    const confirmed = window.confirm(confirmMessage);
    if (confirmed) {
      this.approverefund(refundId, actionType); // 승인 진행
    }
  },
  // 환불 반려 버튼 클릭 시 확인을 요청하는 메소드
  confirmRejectrefund(refundId, actionType) {
    let confirmMessage = '';
    if (actionType === 'approval') {
      confirmMessage = '환불 승인을 반려하시겠습니까?';
    } else if (actionType === 'rejection') {
      confirmMessage = '환불 반려하시겠습니까?';
    }

    const confirmed = window.confirm(confirmMessage);
    if (confirmed) {
      this.rejectrefund(refundId, actionType); // 반려 진행
    }
  },
  // 환불 승인 처리 메소드
  async approverefund(refundId, actionType) {
    try {
      let response;
      if (actionType === 'approval') {
        console.log("호출" + refundId);
        response = await post(`/admin/refund/${refundId}/approval`); // 환불 승인 처리 API 호출
      } else if (actionType === 'rejection') {
        response = await post(`/admin/refund/${refundId}/rejection`); // 환불 반려 처리 API 호출
      }

      if (response && response.data) {
        alert('처리가 완료되었습니다!');
        this.fetchRequestRefunds(); // 승인/반려 후 리스트 갱신
      }
    } catch (error) {
      console.error('Error processing refund:', error);
      this.$toast.error('환불 처리 중 오류가 발생했습니다.');
    }
  },
  // 환불 반려 처리 메소드
  async rejectrefund(refundId, actionType) {
    try {
      let response;
      if (actionType === 'rejection') {
        console.log("반려 호출" + refundId);
        response = await post(`/admin/refund/${refundId}/rejection`); // 환불 반려 처리 API 호출
      }

      if (response && response.data) {
        this.fetchRequestRefunds(); // 반려 후 리스트 갱신
      }
    } catch (error) {
      console.error('Error rejecting refund:', error);
      this.$toast.error('환불 반려 중 오류가 발생했습니다.');
    }
  }
}
};
</script>


<style scoped>
/* 스타일 추가 */
.container {
display: flex;
flex-direction: column;
justify-content: center;
align-items: center;
text-align: center;
margin-top: 20px;
}

table {
width: 80%; /* 테이블의 너비를 90%로 설정하여 화면에 맞게 조정 */
border-collapse: collapse;
margin-top: 20px;
}

th, td {
padding: 10px; /* 열의 패딩을 늘려서 너비를 더 확장 */
text-align: center; /* 가운데 정렬 */
border: 1px solid #ddd;
/* min-width: 120px; 최소 너비 설정 */
}

th {
background-color: #f4f4f4;
text-align: center; /* 열 이름을 가운데 정렬 */
}

button.approve-button {
background-color: #4CAF50;
color: white;
padding: 8px 16px;
border: none;
cursor: pointer;
border-radius: 4px;
}
button.approve-button:hover {
background-color: #45a049;
}

button.reject-button {
background-color: #f44336;
color: white;
padding: 8px 16px;
border: none;
cursor: pointer;
border-radius: 4px;
}
button.reject-button:hover {
background-color: #e53935;
}
</style>
