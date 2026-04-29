<template>
  <div class="container">
    <h1>🎁 경매 물품 리스트</h1>
    <hr />

    <div style="margin-bottom: 20px; padding: 15px; border: 1px solid #ddd; border-radius: 5px;">
      <input v-model="newItem.name" placeholder="물품명" />
      <input v-model.number="newItem.price" type="number" placeholder="가격" />
      <button @click="addItem">물품 등록</button>
    </div>

    <div v-if="loading">데이터를 불러오는 중...</div>
    
    <div v-else>
      <table border="1" style="width: 100%; text-align: center; border-collapse: collapse;">
        <thead>
          <tr style="background-color: #f4f4f4;">
            <th>ID</th>
            <th>물품명</th>
            <th>현재가</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in itemList" :key="item.id">
            <td>{{ item.id }}</td>
            <td>{{ item.name }}</td>
            <td>{{ item.price.toLocaleString() }}원</td>
          </tr>
        </tbody>
      </table>
      
      <p v-if="itemList.length === 0">등록된 물품이 없습니다.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const itemList = ref([])
const loading = ref(true)

// 새 물품 데이터를 담을 객체
const newItem = ref({ name: '', price: 0 })

const fetchItems = async () => {
  try {
    // 백엔드 API 호출 (ApiResponse 구조에 맞춰 response.data.data 접근)
    const response = await axios.get('http://localhost:8080/api/items')

    // axios의 데이터(response.data) 안의 우리가 정의한 data(response.data.data)
    console.log("전체 응답:", response.data);
    
    if (response.data.status === 'success') {
      itemList.value = response.data.data
    } else {
      alert('데이터 로드 실패: ' + response.data.message)
    }
  } catch (error) {
    console.error('API 호출 에러:', error)
    alert('백엔드 서버와 통신할 수 없습니다.')
  } finally {
    loading.value = false
  }
}

// 데이터 추가 함수
const addItem = async () => {
  if (!newItem.value.name || newItem.value.price <= 0) {
    alert('물품명과 가격을 확인해주세요.');
    return;
  }

  try {
    const response = await axios.post('http://localhost:8080/api/items', newItem.value);
    if (response.data.status === 'success') {
      alert('등록 성공!');
      newItem.value = { name: '', price: 0 }; // 입력창 초기화
      await fetchItems(); // 목록 새로고침
    }
  } catch (error) {
    console.error('등록 실패:', error);
    alert('등록 중 오류가 발생했습니다.');
  }
}

onMounted(() => {
  fetchItems()
})
</script>

<style scoped>
.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: sans-serif;
}
table th, table td {
  padding: 10px;
}
</style>