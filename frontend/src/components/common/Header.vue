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
                        <router-link to="#" class="nav-link active" aria-current="page" v-text="$t('header.findSpace')" />
                    </li>
                    <li class="nav-item">
                        <router-link to="#" class="nav-link" v-text="$t('header.viewByRegion')" />
                    </li>
                    <li class="nav-item" v-if="user === null">
                        <router-link to="/login" class="nav-link" v-text="$t('member.login')" />
                    </li>
                    <li class="nav-item" v-if="user !== null">
                        <router-link to="/mypage" class="nav-link"v-text="$t('header.myPage')" />
                    </li>
                    <li class="nav-item">
                        <router-link to="/socket" class="nav-link" href="#" v-text="$t('member.socket')" />
                    </li>
                    <li class="nav-item">
                        <router-link to="/download" class="nav-link" href="#" v-text="$t('member.download')" />
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
