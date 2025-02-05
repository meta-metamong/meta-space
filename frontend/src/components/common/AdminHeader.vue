<template>
    <nav class="navbar navbar-expand-lg bg-light">
        <div class="container-fluid">
            <router-link to="/" class="navbar-brand">Meta Space</router-link>
            <button class="navbar-toggler"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target="#collapsed-container"
                    aria-controls="collapsed-container"
                    aria-expanded="false"
                    aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="collapsed-container">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <router-link to="/admin/socket" class="nav-link" href="#" v-text="$t('admin.socket')" />
                    </li>
                    <li class="nav-item">
                        <router-link to="/admin/salesList" class="nav-link" href="#" v-text="$t('admin.salesList')" />
                    </li>
                    <li class="nav-item">
                        <router-link to="/admin/chatlist" class="nav-link" href="#" v-text="$t('admin.chatlist')" />
                    </li>
                    <li class="nav-item" v-if="user !== null">
                        <button class="nav-link locale-btn" v-text="$t('member.logout')" @click="$store.dispatch('logoutRequest')"/>
                    </li>
                    <li class="nav-item">
                        <button class="nav-link locale-btn" v-text="locale" @click="changeLocale"></button>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</template>

<script>
import { toRaw } from 'vue';
export default {
    name: "Header",
    data(){
    },
    computed:{
        locale(){
            return this.$i18n.locale === 'ko' ? '영어로 변경' : 'Change to Korean';
        },
        user(){
            return toRaw(this.$store.state.user);
        }
    },
    methods:{
        changeLocale(){
            this.$i18n.locale = this.$i18n.locale === 'ko' ? 'en' : 'ko';
        },
        test(){
            console.log(toRaw(this.$store.state.user));
        }
    }
}
</script>

<style scoped>
.locale-btn{
    width: 100%;
    text-align: center;
}
</style>
