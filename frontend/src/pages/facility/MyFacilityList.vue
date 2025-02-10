<template>
    <div class="container root-container">
        <h1 class="header">{{ $t("myFacility.myFacilityList") }}</h1>
        <div class="w-100" id="register-facility-button-container">
            <router-link
                class="btn btn-outline-primary mx-auto"
                to="/facilities/register"
            ><i class="bi bi-plus"></i> {{ $t("myFacility.registerNewFaciltiy") }}</router-link>
        </div>
        <div class="list-item">
            <h2><!-- TODO -->광장동실내배드민턴장</h2>
            <p>서울시 OO구</p>
            <div class="list-item-button-container">
                <button class="edit-button">{{ $t("myFacility.edit") }}</button>
                <button class="delete-button">{{ $t("myFacility.delete") }}</button>
            </div>
        </div>
        <div class="list-item">
            <h2><!-- TODO -->광장동실내배드민턴장</h2>
            <p>서울시 OO구</p>
            <div class="list-item-button-container">
                <button class="edit-button">{{ $t("myFacility.edit") }}</button>
                <button class="delete-button">{{ $t("myFacility.delete") }}</button>
            </div>
        </div>
    </div>
</template>

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
    padding: 16px 20px;
    border: 1px solid #999999;
    border-radius: 16px;
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

.list-item-button-container button {
    font-size: 18px;
    padding: 6px 24px;
    border-radius: 100px;
}

.edit-button {
    background-color: #4a66e6;
}

.delete-button {
    background-color: #ec7461;
}
</style>