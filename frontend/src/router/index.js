import { createRouter, createWebHistory } from 'vue-router';
import Home from '../pages/Home.vue';
import Login from '../pages/Login.vue';
import SignUp from '../pages/SignUp.vue';
import MyPage from '../pages/MyPage.vue';
import UpdateMember from '../components/member/UpdateMember.vue';
import Download from '../pages/Download.vue';
import Admin from '../pages/AdminHome.vue';
import ChatList  from '../pages/ChatList.vue';
import Reservation from '../pages/Reservation.vue';
import DetailReservation from '../components/reservation/DetailReservation.vue';
import AdminChat from '../components/admin/OnlineMemberList.vue';
import MemberChat from '../components/admin/ChattingMember.vue';

const routes = [
	{ path: '/', component: Home },
	{ path: '/login', component: Login },
	{ path: '/mypage', component: MyPage },
	{ path: '/update', component: UpdateMember },
	{ path: '/signup', component: SignUp},
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
