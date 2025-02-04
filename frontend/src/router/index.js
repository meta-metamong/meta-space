import { createRouter, createWebHistory } from 'vue-router';
import FindIdPw from '../pages/FindIdPw.vue';
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

const routes = [
	{ path: '/', component: Home },
	{ path: '/find', component: FindIdPw },
	{ path: '/login', component: Login },
	{ path: '/signup/:role', component: SignUp },
	{ path: '/select-signup-role', component: SelectSignupRole},

	{ path: '/socket', component: Socket  },
	{ path: '/download', component: Download  },

	{ path: '/mypage', component: MyPage },
	{ path: '/update', component: UpdateMember },
	{ path: '/statistics', component: UserStatistics },

	{ path: '/admin', component: Admin },
	{ path: '/admin/socket', component: ChatAdmin },

];

const router = createRouter({
	history: createWebHistory(), 
	routes
});
 
export default router;
