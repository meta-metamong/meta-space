<template>
	<div id="app">
		<ul>
			<li>
				<router-link to="/">홈화면</router-link>
			</li>
			<li>
				<router-link to="/login">로그인</router-link>
			</li>
			<li>
				<router-link to="/signup">회원가입</router-link>
			</li>
			<li>
				<router-link to="/find">아이디/비밀번호 찾기</router-link>
			</li>
		</ul>
		<router-view />
	</div>
</template>

<script>

export default{
	name: 'App'
}
</script>

<style scoped>
	ul li{
		list-style: none;
	}
</style>