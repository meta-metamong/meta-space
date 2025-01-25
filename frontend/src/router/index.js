import { createRouter, createWebHistory } from 'vue-router';
import FindIdPw from '../pages/FindIdPw.vue';
import Home from '../pages/Home.vue';


const routes = [
	{ path: '/', component: Home },
	{ path: '/find', component: FindIdPw }
];

const router = createRouter({
	history: createWebHistory(), 
	routes
});
 
 export default router;