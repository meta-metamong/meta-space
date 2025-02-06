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

const routes = [
	{ path: '/', component: Home },
	{ path: '/login', component: Login },
	
	{ path: '/mypage', component: MyPage },
	{ path: '/update', component: UpdateMember },
	{ path: '/signup', component: SignUp},
	{ path: '/download', component: Download  },
	
	//{ path: '/socket', component: Socket  },
	//{ path: '/admin', component: Admin },
	//{ path: '/admin/socket', component: ChatAdmin },
	//{ path: '/admin/chatlist', component: ChatList },

	{ path: '/reservation', component: Reservation},
];

const router = createRouter({
	history: createWebHistory(), 
	routes
});
 
export default router;
