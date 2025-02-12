import { createApp } from 'vue'
import App from './App.vue'
import 'v-calendar/style.css';
import './style.css'
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import "bootstrap-icons/font/bootstrap-icons.css";
import router from './router'
import i18n from './i18n';
import store from './store';
import '@fortawesome/fontawesome-free/css/all.min.css';
import { setupCalendar, Calendar, DatePicker } from 'v-calendar';

console.warn = console.error = () => {};

const app = createApp(App);
app.use(router);
app.use(i18n);
app.use(store);
app.use(setupCalendar, {})

app.component('VCalendar', Calendar)
app.component('VDatePicker', DatePicker)
app.mount('#app');
