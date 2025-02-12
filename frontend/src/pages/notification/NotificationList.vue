<template>
    <div class="container mt-2">
        <h2 class="mb-3 text-center">알림 목록</h2>
        <div class="btn-group mb-3 w-100 text-center">
            <button type="button" class="btn btn-outline-secondary" :class="filterStatus === 'all' ? 'active' : ''" @click="changeFilter('all')">전체</button>
            <button type="button" class="btn btn-outline-secondary" :class="filterStatus === 'read' ? 'active' : ''" @click="changeFilter('read')">읽음</button>
            <button type="button" class="btn btn-outline-secondary" :class="filterStatus === 'unread' ? 'active' : ''" @click="changeFilter('unread')">안읽음</button>
        </div>
        <ul class="noti-list w-100 mt-1 d-flex flex-column gap-2">
            <li v-for="noti in filteredNotis" :key="noti.notiId" class="noti-item w-100 d-flex align-items-center">
                <i :class="noti.isRead ? 'bi bi-bell me-2 text-primary' : 'bi bi-bell-fill me-2 text-warning'"></i>
                <div>
                    <div>{{ noti.message }}</div>
                    <small class="text-muted">{{ noti.date }}</small>
                </div>
            </li>
        </ul>
    </div>
</template>

<script>
import { notis } from '../../components/nootification/notis';
export default{
    name: 'NotificationList',
    data(){
        return{
            notis: notis.sort((n1, n2) => new Date(n2.date) - new Date(n1.date)),
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
                this.filteredNotis = this.notis.filter(n => n.isRead);
            }else if(newStatus === 'unread'){
                this.filterStatus = 'unread'
                this.filteredNotis = this.notis.filter(n => !n.isRead);
            }
        }
    },
    mounted(){
        this.changeFilter('all');
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