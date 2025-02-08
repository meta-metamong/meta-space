<template>
  <div>
    <h2>시설 요청 승인</h2>
    <table>
      <thead>
        <tr>
          <th>시설명</th>
          <th>Zone명</th>
          <th>요청종류</th>
          <th>승인/반려</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="facility in facilities" :key="facility.fctName">
          <td>{{ facility.fctName }}</td>
          <td>{{ facility.zoneName }}</td>
          <td>{{ facility.fctState }}</td>
          <td>
            <!-- 등록 승인 버튼 -->
            <button v-if="facility.fctState === '등록요청'" @click="confirmApproveFacility(facility.id, 'register')" class="approve-button">등록승인</button>
            <button v-if="facility.fctState === '등록요청'" @click="confirmRejectFacility(facility.id, 'register')" class="reject-button">등록반려</button>
            
            <!-- 삭제 승인 버튼 -->
            <button v-if="facility.fctState === '삭제요청'" @click="confirmApproveFacility(facility.id, 'delete')" class="approve-button">삭제승인</button>
            <button v-if="facility.fctState === '삭제요청'" @click="confirmRejectFacility(facility.id, 'delete')" class="reject-button">삭제반려</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import { get } from '../../apis/axios'; // axios 경로를 수정하세요.

export default {
  data() {
    return {
      facilities: [] // 요청받은 시설 리스트
    };
  },
  created() {
    this.fetchRequestFacilities(); // 컴포넌트가 생성되면 데이터 조회
  },
  methods: {
    // 시설 요청 리스트를 가져오는 메소드
    async fetchRequestFacilities() {
      try {
        const response = await get('/admin/getRequestFacilities'); // API 경로에 맞춰서 호출
        if (response && response.data) {
          this.facilities = response.data.content; // 응답 데이터에서 시설 리스트를 받는다.
        }
      } catch (error) {
        console.error('Error fetching request facilities:', error);
      }
    },
    // 등록승인 또는 삭제승인 버튼 클릭시 확인을 요청하는 메소드
    confirmApproveFacility(facilityId, actionType) {
      let confirmMessage = '';
      if (actionType === 'register') {
        confirmMessage = '등록 승인하시겠습니까?';
      } else if (actionType === 'delete') {
        confirmMessage = '삭제 승인하시겠습니까?';
      }

      const confirmed = window.confirm(confirmMessage);
      if (confirmed) {
        this.approveFacility(facilityId, actionType); // 승인 진행
      }
    },
    // 등록반려 또는 삭제반려 버튼 클릭시 확인을 요청하는 메소드
    confirmRejectFacility(facilityId, actionType) {
      let confirmMessage = '';
      if (actionType === 'register') {
        confirmMessage = '등록 반려하시겠습니까?';
      } else if (actionType === 'delete') {
        confirmMessage = '삭제 반려하시겠습니까?';
      }

      const confirmed = window.confirm(confirmMessage);
      if (confirmed) {
        this.rejectFacility(facilityId, actionType); // 반려 진행
      }
    },
    // 등록 승인 또는 삭제 승인 처리 메소드
    async approveFacility(facilityId, actionType) {
      try {
        let response;
        if (actionType === 'register') {
          response = await get(`/approveFacility/register/${facilityId}`); // 등록 승인 처리 API 호출
        } else if (actionType === 'delete') {
          response = await get(`/approveFacility/delete/${facilityId}`); // 삭제 승인 처리 API 호출
        }

        if (response && response.data) {
          this.$toast.success('시설이 승인되었습니다!');
          this.fetchRequestFacilities(); // 승인 후 리스트 갱신
        }
      } catch (error) {
        console.error('Error approving facility:', error);
        this.$toast.error('시설 승인 중 오류가 발생했습니다.');
      }
    },
    // 등록 반려 또는 삭제 반려 처리 메소드
    async rejectFacility(facilityId, actionType) {
      try {
        let response;
        if (actionType === 'register') {
          response = await get(`/rejectFacility/register/${facilityId}`); // 등록 반려 처리 API 호출
        } else if (actionType === 'delete') {
          response = await get(`/rejectFacility/delete/${facilityId}`); // 삭제 반려 처리 API 호출
        }

        if (response && response.data) {
          this.$toast.success('시설이 반려되었습니다!');
          this.fetchRequestFacilities(); // 반려 후 리스트 갱신
        }
      } catch (error) {
        console.error('Error rejecting facility:', error);
        this.$toast.error('시설 반려 중 오류가 발생했습니다.');
      }
    }
  }
};
</script>

<style scoped>
/* 스타일 추가 */
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}
th, td {
  padding: 10px;
  text-align: left;
  border: 1px solid #ddd;
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
