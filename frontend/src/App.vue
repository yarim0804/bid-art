<template>
  <div id="app">
    <!-- 모든 페이지에서 공통으로 보일 헤더 -->
    <header style="background: #f4f4f4; padding: 20px; text-align: center;">
      <h1 @click="$router.push('/')" style="cursor: pointer;">🎁 Bid-Art Auction</h1>
      <nav>
        <router-link to="/">홈(목록)</router-link>
      </nav>
    </header>

    <!-- 현재 URL에 맞는 컴포넌트(ItemList 혹은 ItemDetail)가 이 자리에 그려집니다 -->
    <main style="padding: 20px;">
      <router-view />
    </main>

    <!-- 공통 푸터 -->
    <footer style="margin-top: 50px; color: #888; text-align: center;">
      <p>© 2026 Bid-Art Project. All rights reserved.</p>
    </footer>
  </div>
</template>

<script setup>
// App.vue는 이제 라우터가 화면을 갈아 끼워주므로 
// 별도의 데이터 조회 로직이 필요 없습니다.
</script>

<style>
.container { max-width: 800px; margin: 0 auto; }
/* 공통 스타일들... */
</style>