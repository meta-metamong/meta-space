<template>
    <div class="container root-container">
        <h2 class="text-center mb-2" style="margin-top: 15px" v-text="$t('myFacility.myFacilityList')"></h2>
        <div class="w-100 mb-4" id="register-facility-button-container">
            <router-link
                class="btn btn-outline-primary mx-auto"
                to="/facilities/register"
            ><i class="bi bi-plus"></i> {{ $t("myFacility.registerNewFaciltiy") }}</router-link>
        </div>
        <div class="list-item" v-for="facility in facilities">
            <div class="col-md-6 col-lg-4 mb-2">
                <div class="card":class="setBorder(facility.fctState)">
                    <div class="card-body p-4">
                        <div class="d-flex justify-content-between align-items-center">
                            <h5 class="card-title mb-2">{{ facility.fctName }}</h5>
                            <div class="btn-group dropstart mb-2">
                                <button type="button" class="btn dropdown-btn" data-bs-toggle="dropdown" aria-expanded="false">
                                    <i class="bi bi-three-dots-vertical"></i>
                                </button>
                                <ul class="dropdown-menu dropdown-menu-end">
                                    <!-- Dropdown menu links -->
                                    <li><router-link class="dropdown-item" :to="`/facilities/${facility.fctId}`">{{ $t("facility.detail") }}</router-link></li>
                                    <li><router-link class="dropdown-item" :to="`/facilities/my-facility-list/${facility.fctId}`">{{ $t("myFacility.facilityReservationList") }}</router-link></li>
                                    <li><router-link class="dropdown-item" :to="`/facilities/${facility.fctId}/edit`">{{ $t("myFacility.edit") }}</router-link></li>
                                    <li><router-link class="dropdown-item" :to="`/confirm-pw/delete_fct_${facility.fctId}`">{{ $t("myFacility.delete") }}</router-link></li> <!-- To delete -->
                                </ul>
                            </div>
                        </div>
                        <p class="card-text text-muted">{{ facility.address }}</p>
                    </div>

                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { toRaw } from "vue";
import { get } from "../../apis/axios";

export default {
    data() {
        return {
            facilities: []
        }
    },
    methods:{
        setBorder(state){
            switch(state){
                case 'REG_REQUESTED':
                    return 'border border-warning';
                case 'REG_APPROVED':
                    return 'border border-success';
                default:
                    return 'border border-danger';
            }
        }
    },
    computed: {
        userId() {
            return this.$store.state.userId;
            // return 21;
        }
    },
    mounted() {
        get(`/members/${this.userId}/facilities`).then((response) => {
            const responseBody = response.data;
            const content = responseBody.content;
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

.dropdown-btn {
    --bs-btn-padding-x: 0px !important;
}

.btn-check:checked+.btn, .btn.active, .btn.show, .btn:first-child:active, :not(.btn-check)+.btn:active {
    border: none;
}
</style>