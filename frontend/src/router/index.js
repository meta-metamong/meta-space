import { createRouter, createWebHistory } from 'vue-router';
import Home from '../pages/Home.vue';
import Login from '../pages/member/Login.vue';
import SignUp from '../pages/member/SignUp.vue';
import Profile from '../pages/member/Profile.vue';
import UpdateMember from '../pages/member/UpdateMember.vue';
import Download from '../pages/Download.vue';
import Admin from '../pages/AdminHome.vue';
import ChatList  from '../pages/ChatList.vue';
import DetailReservation from '../pages/reservation/DetailReservation.vue';
import Reservation from '../pages/reservation/Reservation.vue';
import ReservationList from '../pages/reservation/ReservationList.vue';
import AdminChat from '../components/admin/OnlineMemberList.vue';
import MemberChat from '../components/admin/ChattingMember.vue';
import ConfirmPassword from '../pages/member/ConfirmPassword.vue';
import ChangePassword from '../pages/member/ChangePassword.vue';
import FindPassword from '../pages/member/FindPassword.vue';
import AdminMemberSearch from '../pages/admin/MemberSearch.vue';
import FacilityApproval from '../pages/admin/FacilityApproval.vue';
import AdminMemberBen from '../components/admin/AdminMemberBen.vue';
import AdminSalesExport from '../components/admin/AdminSalesExport.vue';
import Map from '../pages/Map.vue';
import FacilityRegistration from "../pages/facility/FacilityRegistration.vue";
import MyFacilityList from "../pages/facility/MyFacilityList.vue";
import FacilityEdit from "../pages/facility/FacilityEdit.vue";
import PaymentList from '../pages/payment/PaymentList.vue';
import DetailPayment from '../pages/payment/DetailPayment.vue';
import FacilityDetail from "../components/facility/FacilityDetail.vue";
import Search from '../pages/Search.vue';
import SearchedFacilityList from '../pages/facility/SearchedFacilityList.vue';
import PaymentResult from '../pages/payment/PaymentResult.vue';

const routes = [
	{ path: '/', component: Home },
	{ path: '/search', component: Search },

	{ path: '/login', component: Login },
	{ path: '/profile', component: Profile },
	{ path: '/update', component: UpdateMember },
	{ path: '/signup', component: SignUp},
	{ path: '/chat', component: MemberChat },
	{ path: '/admin/chat', component: AdminChat },
	{ path: '/confirm-pw/:type', component: ConfirmPassword, props: true },
	{ path: '/change-pw', component: ChangePassword },
	{ path: '/find-pw', component: FindPassword },
	{ path: '/download', component: Download  },

	{ path: '/facilities/register', component: FacilityRegistration },
	{ path: '/facilities/my-facility-list', component: MyFacilityList },
	{ path: '/facilities/:fctId', component: FacilityDetail, props: true },
	{ path: '/facilities/:fctId/edit', component: FacilityEdit, props: true },
	{ path: '/facilities', component: SearchedFacilityList },

	{ path: '/admin', component: Admin },
	{ path: '/admin/chatlist', component: ChatList },
	{ path: '/admin/memSearch', component: AdminMemberSearch },
	{ path: '/admin/facilityApproval', component: FacilityApproval },
	{ path: '/admin/memBen', component: AdminMemberBen },
	{ path: '/admin/salesExport', component: AdminSalesExport },


	{ path: '/reserve/:fctId', component: Reservation },
	{ path: '/reservation/list', component: ReservationList },
	{ path: '/reservation/:id', component: DetailReservation, props: true },
	{ path: '/map', component: Map },

	{ path: '/payment/list', component: PaymentList },
	{ path: '/payment/:rvtId', component: DetailPayment, props: true },
	{ path: '/payment/result', component: PaymentResult }

];

const router = createRouter({
	history: createWebHistory(), 
	routes
});
 
export default router;
