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
                        <!-- TODO: 현재 페이지에 해당하는 navbar만 active 클래스, 그리고 aria-current="page" -->
                        <router-link to="#" class="nav-link active" aria-current="page" href="#" v-text="$t('header.findSpace')" />
                    </li>
                    <li class="nav-item">
                        <router-link to="#" class="nav-link" href="#" v-text="$t('header.viewByRegion')" />
                    </li>
                    <!-- <li class="nav-item">
                        <router-link to="#" class="nav-link" href="#" v-text="$t('header.reservation')" />
                    </li> -->
                    <li class="nav-item">
                        <router-link to="/login" class="nav-link" href="#" v-text="$t('member.login')" />
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
export default {
    name: "Header",
    computed:{
        locale(){
            return this.$i18n.locale === 'ko' ? '영어로 변경' : 'Change to Korean';
        }
    },
    methods:{
        changeLocale(){
            this.$i18n.locale = this.$i18n.locale === 'ko' ? 'en' : 'ko';
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
