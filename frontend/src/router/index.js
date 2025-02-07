import { createRouter, createWebHistory } from 'vue-router';
import Home from '../pages/Home.vue';
import Login from '../pages/member/Login.vue';
import SignUp from '../pages/member/SignUp.vue';
import Profile from '../pages/member/Profile.vue';
import UpdateMember from '../pages/member/UpdateMember.vue';
import Download from '../pages/Download.vue';
import Admin from '../pages/AdminHome.vue';
import ChatList  from '../pages/ChatList.vue';
import DetailReservation from '../components/reservation/DetailReservation.vue';
import Reservation from '../pages/reservation/Reservation.vue';
import ReservationList from '../pages/reservation/ReservationList.vue';

const routes = [
	{ path: '/', component: Home },
	{ path: '/login', component: Login },
	{ path: '/profile', component: Profile },
	{ path: '/update', component: UpdateMember },
	{ path: '/signup', component: SignUp},
	//{ path: '/socket', component: Socket  },
	{ path: '/download', component: Download  },

	{ path: '/admin', component: Admin },
	{ path: '/admin/chatlist', component: ChatList },

	{ path: '/reservation', component: Reservation },
	{ path: '/reservation/list', component: ReservationList },
	{ path: '/reservation/:id', component: DetailReservation, props: true },
];

const router = createRouter({
	history: createWebHistory(), 
	routes
});
 
export default router;
