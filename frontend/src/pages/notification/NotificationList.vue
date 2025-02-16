<template>
    <div class="container mt-2">
        <h2 class="mb-3 text-center">알림 목록</h2>
        <div class="btn-group mb-3 w-100 text-center">
            <button type="button" class="btn btn-outline-secondary" :class="filterStatus === 'all' ? 'active' : ''" @click="changeFilter('all')">전체</button>
            <button type="button" class="btn btn-outline-secondary" :class="filterStatus === 'read' ? 'active' : ''" @click="changeFilter('read')">읽음</button>
            <button type="button" class="btn btn-outline-secondary" :class="filterStatus === 'unread' ? 'active' : ''" @click="changeFilter('unread')">안읽음</button>
        </div>
        <ul class="noti-list w-100 mt-1 d-flex flex-column gap-2">
            <li v-for="(noti, idx) in filteredNotis" :key="noti.notiId" class="noti-item w-100 d-flex align-items-center">
                <i :class="noti.read ? 'bi bi-bell me-2 text-primary' : 'bi bi-bell-fill me-2 text-warning'" @click="() => readMessage(noti.notiId, idx)"></i>
                <div>
                    <div>{{ noti.notiMsg }}</div>
                    <small class="text-muted">{{ noti.createdAt }}</small>
                </div>
            </li>
        </ul>
    </div>
</template>

<script>
import apiClient, { get } from "../../apis/axios";
import { getUserIdInLocal } from "../../store";
export default{
    name: 'NotificationList',
    data(){
        return{
            notis: [],
            filteredNotis: [],
            filterStatus: ''
        }
    },
    methods: {
        changeFilter(newStatus){
            if(newStatus === 'all'){
                this.filterStatus = 'all';
                this.filteredNotis = this.notis;
            }else if(newStatus === 'read'){
                this.filterStatus = 'read'
                this.filteredNotis = this.notis.filter(n => n.read);
            }else if(newStatus === 'unread'){
                this.filterStatus = 'unread'
                this.filteredNotis = this.notis.filter(n => !n.read);
            }
        },
        readMessage(notiId, idx) {
            if (!this.notis[idx].read) {
                console.log("read noti, id=" + notiId);
                apiClient.patch(`/notifications/${notiId}/read`);
                this.notis[idx].read = true;
                this.$store.commit("decrementNotificationCount");
            }
        }
    },
    mounted(){
        this.changeFilter('all');
        const memId = getUserIdInLocal();
        get(`/members/${memId}/notifications`).then((response) => {
            const responseBody = response.data;
            const content = responseBody.content;
            console.log(content);
            for (const item of content) {
                this.notis.push(item);
            }
        });

        const socketClient = this.$store.state.socketClient;
        socketClient.onmessage = (msg) => {
            // console.log(JSON.parse(msg.data));
            this.notis.unshift(JSON.parse(msg.data));
            this.$store.commit("incrementNotificationCount");
        };
    },
    unmounted() {
        const socketClient = this.$store.state.socketClient;

        socketClient.onmessage = (msg) => {};
    }
}
</script>

<style scoped>
.noti-list{
    padding: 0;
    margin: 0;
}
.noti-item{
    padding: 5px 20px;
    border: 1px solid #999;
    border-radius: 15px;
}
</style>