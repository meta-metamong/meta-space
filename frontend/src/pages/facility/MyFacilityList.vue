<template>
    <div class="container root-container">
        <h2 class="text-center mb-2" v-text="$t('myFacility.myFacilityList')"></h2>
        <div class="w-100 mb-4" id="register-facility-button-container">
            <router-link
                class="btn btn-outline-primary mx-auto"
                to="/facilities/register"
            ><i class="bi bi-plus"></i> {{ $t("myFacility.registerNewFaciltiy") }}</router-link>
        </div>
        <div class="list-item" v-for="facility in facilities">
            <div class="col-md-6 col-lg-4 mb-2">
                <div class="card">
                    <div class="card-body p-4">
                        <h5 class="card-title">{{ facility.fctName }}</h5>
                        <p class="card-text text-muted">{{ facility.address }}</p>
                        <div class="d-flex justify-content-end gap-2">
                            <router-link class="btn btn-sm edit-button" :to="`/facilities/${facility.fctId}/edit`">{{ $t("myFacility.edit") }}</router-link>
                            <button class="btn btn-sm delete-button">{{ $t("myFacility.delete") }}</button> <!-- To delete -->
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { get } from "../../apis/axios";

export default {
    data() {
        return {
            facilities: []
        }
    },
    computed: {
        userId() {
            // return this.$store.state.userId;
            return 21;
        }
    },
    mounted() {
        get(`/members/${this.userId}/facilities`).then((response) => {
            const responseBody = response.data;
            const content = responseBody.content;
            console.log(content);
            this.facilities = content;
        });
    }
}
</script>

<style scoped>
.header {
    font-size: 24px;
    font-weight: bold;
    text-align: center;
}

#register-facility-button-container {
    display: flex;
    justify-self: center;
    padding-left: auto;
    padding-right: auto;
}

.list-item {
    width: 85%;
}

.list-item h2 {
    font-size: 20px;
    font-weight: 600;
}

.list-item p {
    font-size: 16px;
    color: #717171;
}

.root-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 18px;
}

.list-item-button-container {
    display: flex;
    gap: 12px;
    justify-content: space-evenly;
}

.list-item-button-container .button {
    font-size: 18px;
    padding: 6px 24px;
    border-radius: 100px;
}

.edit-button {
    background-color: #4a66e6;
    color: #fff;
    text-decoration: none;
}

.delete-button {
    background-color: #ec7461;
    color: #fff;
}
</style>