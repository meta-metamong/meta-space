<template>
  <div class="container mt-5">
    <div class="d-flex justify-content-center">
      <FindMember idOrPw="id" class="mx-2" />
      <FindMember idOrPw="pw" class="mx-2" />
    </div>
  </div>
</template>

<script>
import FindMember from '../components/member/FindMember.vue';

export default{
  name: "FindIdPw",
  components:{
    FindMember
  }
}
</script>