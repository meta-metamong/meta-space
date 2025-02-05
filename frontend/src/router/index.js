import { createRouter, createWebHistory } from 'vue-router';
import Home from '../pages/Home.vue';
import Login from '../pages/Login.vue';
import SignUp from '../pages/SignUp.vue';

import Socket  from '../pages/ChatMember.vue';
import ChatAdmin  from '../pages/ChatAdmin.vue';

import UserStatistics from '../pages/UserStatistics.vue';
import MyPage from '../pages/MyPage.vue';
import SelectSignupRole from "../pages/SelectSignupRole.vue";
import UpdateMember from '../components/member/UpdateMember.vue';
import Download from '../pages/Download.vue';
import Admin from '../pages/AdminHome.vue';
import Error from '../pages/Error.vue';
import Map from '../pages/Map.vue';

const routes = [
	{ path: '/', component: Home },
	{ path: '/login', component: Login },
	{ path: '/signup', component: SignUp}
];

const router = createRouter({
	history: createWebHistory(), 
	routes
});
 
export default router;
