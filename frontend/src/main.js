import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import "bootstrap-icons/font/bootstrap-icons.css";
import router from './router'
import i18n from './i18n';
import store from './store';

console.warn = console.error = () => {};

const app = createApp(App);
app.use(router);
app.use(i18n);
app.use(store);
app.mount('#app');
