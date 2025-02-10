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
import FacilityRegistration from "../pages/facility/FacilityRegistration.vue";

const routes = [
	{ path: '/', component: Home },
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

	{ path: '/admin', component: Admin },
	{ path: '/admin/chatlist', component: ChatList },
	{ path: '/admin/memSearch', component: AdminMemberSearch },
	{ path: '/admin/facilityApproval', component: FacilityApproval },
	{ path: '/admin/memBen', component: AdminMemberBen },
	{ path: '/admin/salesExport', component: AdminSalesExport },


	{ path: '/reservation', component: Reservation },
	{ path: '/reservation/list', component: ReservationList },
	{ path: '/reservation/:id', component: DetailReservation, props: true },

];

const router = createRouter({
	history: createWebHistory(), 
	routes
});
 
export default router;
