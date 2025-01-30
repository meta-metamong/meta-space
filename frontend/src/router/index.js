import { createRouter, createWebHistory } from 'vue-router';
import FindIdPw from '../pages/FindIdPw.vue';
import Home from '../pages/Home.vue';
import Login from '../pages/Login.vue';
import SignUp from '../pages/SignUp.vue';
import UserStatistics from '../pages/UserStatistics.vue';

const routes = [
	{ path: '/', component: Home },
	{ path: '/find', component: FindIdPw },
	{ path: '/login', component: Login },
	{ path: '/signup', component: SignUp },
	{ path: '/statistics', component: UserStatistics },
];

const router = createRouter({
	history: createWebHistory(), 
	routes
});
 
export default router;
