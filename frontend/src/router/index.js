import { createRouter, createWebHistory } from 'vue-router';
import Home from '../pages/Home.vue';
import Login from '../pages/Login.vue';
import SignUp from '../pages/SignUp.vue';
import MyPage from '../pages/MyPage.vue';
import UpdateMember from '../components/member/UpdateMember.vue';
import Reservation from '../pages/Reservation.vue';


const routes = [
	{ path: '/', component: Home },
	{ path: '/login', component: Login },
	{ path: '/signup', component: SignUp},
	{ path: '/mypage', component: MyPage},
	{ path: '/update', component: UpdateMember},

	{ path: '/reservation', component: Reservation},
];

const router = createRouter({
	history: createWebHistory(), 
	routes
});
 
export default router;
