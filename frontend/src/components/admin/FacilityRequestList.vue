<template>
  <div>
    <h2 class="text-center">시설 요청 승인</h2>
    <table>
      <thead>
        <tr>
          <th>카테고리</th>
          <th>시설명</th>
          <th>단위이용시간</th>
          <th>공휴일 영업 여부</th>
          <th class="address-column">주소</th>
          <th>전화번호</th>
          <th class="request-type-column">요청종류</th>
          <th>승인/반려</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="facility in facilities" :key="facility.fctId">
          <td>{{ facility.catName }}</td>
          <td>{{ facility.fctName }}</td>
          <td>{{ facility.unitUsageTime }}</td>
          <td>{{ facility.isOpenOnHolidays }}</td>
          <td v-html="formattedAddress(facility.fullAddress)"></td>
          <td>{{ facility.fctTel }}</td>
          <td>{{ facility.fctState }}</td>
          <td>
            <button v-if="facility.fctState === '등록요청'" @click="confirmApproveFacility(facility.fctId, 'register')" class="approve-button">등록승인</button>
            <button v-if="facility.fctState === '등록요청'" @click="confirmRejectFacility(facility.fctId, 'register')" class="reject-button">등록반려</button>
            <button v-if="facility.fctState === '삭제요청'" @click="confirmApproveFacility(facility.fctId, 'delete')" class="approve-button">삭제승인</button>
            <button v-if="facility.fctState === '삭제요청'" @click="confirmRejectFacility(facility.fctId, 'delete')" class="reject-button">삭제반려</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import { get, post } from '../../apis/axios';

export default {
  data() {
    return {
      facilities: []
    };
  },
  created() {
    this.fetchRequestFacilities();
  },
  methods: {
    async fetchRequestFacilities() {
      try {
        const response = await get('/admin/getRequestFacilities');
        if (response && response.data) {
          this.facilities = response.data.content;
        }
      } catch (error) {
        console.error('Error fetching request facilities:', error);
      }
    },
    formattedAddress(address) {
      if (!address) return '';
      return address.replace(/\)/g, ')\n').replace(/\n/g, '<br>'); // )를 기준으로 줄바꿈 적용
    },
    confirmApproveFacility(facilityId, actionType) {
      let confirmMessage = actionType === 'register' ? `${facilityId} 등록 승인하시겠습니까?` : '삭제 승인하시겠습니까?';
      if (window.confirm(confirmMessage)) {
        this.approveFacility(facilityId, actionType);
      }
    },
    confirmRejectFacility(facilityId, actionType) {
      let confirmMessage = actionType === 'register' ? '등록 반려하시겠습니까?' : '삭제 반려하시겠습니까?';
      if (window.confirm(confirmMessage)) {
        this.rejectFacility(facilityId, actionType);
      }
    },
    async approveFacility(facilityId, actionType) {
      try {
        let response = await post(`/admin/facility/${facilityId}/${actionType === 'register' ? 'approval' : 'deletion/approval'}`);
        if (response && response.data) {
          alert('승인완료');
          this.fetchRequestFacilities();
        }
      } catch (error) {
        console.error('Error approving facility:', error);
      }
    },
    async rejectFacility(facilityId, actionType) {
      try {
        let response = await post(`/admin/facility/${facilityId}/${actionType === 'register' ? 'rejection' : 'deletion/rejection'}`);
        if (response && response.data) {
          alert('반려완료');
          this.fetchRequestFacilities();
        }
      } catch (error) {
        console.error('Error rejecting facility:', error);
      }
    }
  }
};
</script>

<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

th, td {
  padding: 10px;
  text-align: center;
  border: 1px solid #ddd;
  word-wrap: break-word;
}

.address-column {
  width: 150px; /* 주소 컬럼 너비 축소 */
}

.request-type-column {
  width: 130px; /* 요청 종류 컬럼 너비 확대 */
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
