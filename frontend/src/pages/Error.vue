<template>
    <div class="container-fluid py-5">
        <div class="container py-5 text-center">
            <div class="row justify-content-center">
                <div class="col-lg-6">
                    <i class="bi bi-exclamation-triangle display-1 text-secondary"></i>
                    <!-- <h1 class="display-1">{{ error }}</h1> -->
                    <h1 class="mb-4 mt-2">{{ errorTitle }}</h1>
                    <p class="mb-4">{{ errorMessage }}</p>
                    <router-link to="/" class="btn border-secondary rounded-pill py-3 px-5">{{ $t('error.home') }}</router-link>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: "Error",
    data() {
        return {
        }
    },
    computed: {
        error() {
            return sessionStorage.getItem('error');
        },
        errorTitle() {
            return this.$t(`error.${this.error}.title`);
        },
        errorMessage() {
            return this.$t(`error.${this.error}.message`);
        },
    },
    mounted() {
        sessionStorage.removeItem('error');
    }
}
</script>

<style scoped></style>