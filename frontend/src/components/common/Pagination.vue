<!--
Pagination을 위한 공통 컴포넌트.
페이지 목록을 나타낼 때 사용할 수 있다.

사용하려면 5가지 props를 전달해야 하는데, 그 중 4가지는 필수이다.

{
    currentPage: {
        description: "현재 페이지",
        type: number
    },
    start-page-no: {
        description: "display될 페이지 목록의 시작 페이지 번호",
        type: number
    },
    end-page-no: {
        description: "display될 페이지 목록의 마지막 페이지 번호",
        type: number
    },
    isPreviousAvailable: {
        description: "이전 페이지 목록으로 이동할 수 있는지. 만약 현재 페이지 목록이 1~10까지 display된다면 0번 페이지는 없으니까 isPreviousAvailable은 false",
        type: boolean
    },
    isNextAvailable: {
        description: "다음 페이지 목록으로 이동할 수 있는지. 만약 현재 페이지 목록에 마지막 페이지가 포함돼 있다면 isNextAvailable은 false",
        type: boolean
    },
    onSelectPage: {
        description: "페이지 버튼을 클릭했을 때 콜백 함수"
        type: (page: number) => {}
    },
    onPreviosuClick: {
        description: "이전 버튼을 클릭했을 때 콜백 함수"
        type: () => {}
    },
    onNextClick: {
        description: "다음 버튼을 클릭했을 때 콜백 함수",
        type: () => {}
    }
}
-->

<template>
    <div class="pagination-box">
        <button :disabled="!isPreviousAvailable"
                class="btn-other-pageset"
                @click="$emit('clickPrevious')">이전</button>
        <ul class="page-box">
            <li :class="selectedPageNo === page ? 'selected-page' : ''" v-for="page in pageSet()" :key="page">
                <button class="btn-page" @click="onPageClick(page)">
                    {{ page }}
                </button>
            </li>
        </ul>
        <button :disabled="!isNextAvailable"
                class="btn-other-pageset"
                @click="$emit('clickNext')">다음</button>
    </div>
</template>

<script>
export default {
    data() {
        return {
            selectedPageNo: this.currentPage
        }
    },
    props: {
        currentPage: {
            type: Number,
            required: true
        },
        startPageNo: {
            type: Number,
            required: true
        },
        endPageNo: {
            type: Number,
            required: true
        },
        isPreviousAvailable: {
            type: Boolean,
            required: true
        },
        isNextAvailable: {
            type: Boolean,
            required: true
        }
    },
    methods: {
        pageSet() {
            const l = [];
            for (let i = this.startPageNo; i <= this.endPageNo; i++) {
                l.push(i);
            }
            return l;
        },
        onPageClick(page) {
            this.selectedPageNo = page;
            this.$emit("clickPageItem", page);
        }
    }
}
</script>

<style scoped>
ul {
    padding: 0;
    margin: 0;
}

li {
    list-style-type: none;
}

.pagination-box {
    display: flex;
    align-items: center;
}

.page-box {
    display: flex;
    align-items: center;
    
}

.btn-other-pageset {

}

.selected-page * {
    background-color: blue;
}
</style>