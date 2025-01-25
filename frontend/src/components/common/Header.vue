<template>
    <nav class="navbar navbar-expand-lg bg-light">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">MetaSpace</a>
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
                        <a class="nav-link active" aria-current="page" href="#">
                            공간찾기
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#">예약</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#">지역별</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#">로그인</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#">회원가입</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</template>

<script>
export default {
    name: "Header",
}
</script>

<style scoped>
</style>
