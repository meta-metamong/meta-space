import { createRouter, createWebHistory } from 'vue-router';
import Home from '../pages/Home.vue';
import Login from '../pages/member/Login.vue';
import SignUp from '../pages/member/SignUp.vue';
import Profile from '../pages/member/Profile.vue';
import UpdateMember from '../pages/member/UpdateMember.vue';
import Download from '../pages/Download.vue';
import Admin from '../pages/AdminHome.vue';
import ChatList  from '../pages/ChatList.vue';
import Reservation from '../pages/Reservation.vue';
import DetailReservation from '../components/reservation/DetailReservation.vue';
import ConfirmPassword from '../pages/member/ConfirmPassword.vue';
import ChangePassword from '../pages/member/ChangePassword.vue';
import FindPassword from '../pages/member/FindPassword.vue';
import AdminChat from '../components/admin/OnlineMemberList.vue';
import MemberChat from '../components/admin/ChattingMember.vue';

const routes = [
	{ path: '/', component: Home },
	{ path: '/login', component: Login },
	{ path: '/profile', component: Profile },
	{ path: '/update', component: UpdateMember },
	{ path: '/signup', component: SignUp},
	{ path: '/confirm-pw/:type', component: ConfirmPassword, props: true },
	{ path: '/change-pw', component: ChangePassword },
	{ path: '/find-pw', component: FindPassword },

	//{ path: '/socket', component: Socket  },
	{ path: '/chat', component: MemberChat },
	{ path: '/admin/chat', component: AdminChat },
	{ path: '/download', component: Download  },

	{ path: '/admin', component: Admin },
	{ path: '/admin/chatlist', component: ChatList },

	{ path: '/reservation', component: Reservation },
	{ path: '/reservation/:id', component: DetailReservation, props: true },
];

const router = createRouter({
	history: createWebHistory(), 
	routes
});
 
export default router;
