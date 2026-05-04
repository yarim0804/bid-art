<template>
  <div v-if="item" class="container">
    <h2>🔍 물품 상세 정보</h2>
    <hr />
    <div class="detail-card" style="border: 1px solid #ddd; padding: 20px; border-radius: 8px;">
      <p><strong>번호:</strong> {{ item.id }}</p>
      <p><strong>품명:</strong> {{ item.name }}</p>
      <p><strong>현재가:</strong> {{ item.price?.toLocaleString() }}원</p>
    </div>
    <br />
    <button @click="$router.push('/')">목록으로 돌아가기</button>
  </div>
  <div v-else>
    <p>데이터를 불러오는 중입니다...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router' // URL 파라미터를 읽기 위해 필요합니다
import axios from 'axios'

const route = useRoute()
const item = ref(null)

onMounted(async () => {
  const id = route.params.id // URL의 :id 값을 가져옵니다
  try {
    const response = await axios.get(`http://localhost:8080/api/items/${id}`)
    item.value = response.data.data
  } catch (error) {
    console.error('상세 정보 로드 실패:', error)
    alert('정보를 불러오지 못했습니다.')
  }
})
</script>

<style scoped>
.detail-card {
  background-color: #fafafa;
  margin-top: 20px;
}
button {
  padding: 10px 20px;
  cursor: pointer;
}
</style>