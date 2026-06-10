<template>
  <div v-if="item" class="container">
    <h2>🔍 물품 상세 / 실시간 경매</h2>
    <hr />

    <div class="detail-card">
      <p><strong>번호:</strong> {{ item.id }}</p>
      <p><strong>품명:</strong> {{ item.name }}</p>
      <p class="current-price">
        <strong>현재가:</strong>
        <span>{{ item.price?.toLocaleString() }}원</span>
        <span class="live" :class="{ connected }">{{ connected ? '● LIVE' : '○ 연결 중...' }}</span>
      </p>
    </div>

    <!-- 입찰 폼 -->
    <div class="bid-form">
      <h3>💰 입찰하기</h3>
      <div class="bid-inputs">
        <input v-model="bidderName" placeholder="입찰자 이름" />
        <input v-model.number="bidAmount" type="number" placeholder="입찰 금액" />
        <button @click="placeBid" :disabled="submitting">
          {{ submitting ? '처리 중...' : '입찰' }}
        </button>
      </div>
      <p class="hint">현재가보다 높은 금액을 입력하세요.</p>
    </div>

    <!-- 입찰 이력 (실시간 갱신) -->
    <div class="bid-history">
      <h3>📜 입찰 내역 ({{ bids.length }})</h3>
      <table v-if="bids.length > 0">
        <thead>
          <tr>
            <th>입찰자</th>
            <th>금액</th>
            <th>시각</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="bid in bids" :key="bid.id" :class="{ 'latest': bid.id === bids[0].id }">
            <td>{{ bid.bidderName }}</td>
            <td>{{ bid.amount.toLocaleString() }}원</td>
            <td>{{ formatTime(bid.createdAt) }}</td>
          </tr>
        </tbody>
      </table>
      <p v-else>아직 입찰 내역이 없습니다. 첫 입찰의 주인공이 되어보세요!</p>
    </div>

    <br />
    <button @click="$router.push('/')">목록으로 돌아가기</button>
  </div>
  <div v-else>
    <p>데이터를 불러오는 중입니다...</p>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'
import { Client } from '@stomp/stompjs'

const route = useRoute()
const itemId = route.params.id

const item = ref(null)
const bids = ref([])
const bidderName = ref('')
const bidAmount = ref(null)
const submitting = ref(false)
const connected = ref(false)

let stompClient = null

// 초기 데이터 로드 (물품 정보 + 입찰 이력)
const fetchItem = async () => {
  const response = await axios.get(`http://localhost:8080/api/items/${itemId}`)
  item.value = response.data.data
}

const fetchBids = async () => {
  const response = await axios.get(`http://localhost:8080/api/items/${itemId}/bids`)
  bids.value = response.data.data
}

// 입찰 요청
const placeBid = async () => {
  if (!bidderName.value.trim()) {
    alert('입찰자 이름을 입력해주세요.')
    return
  }
  if (!bidAmount.value || bidAmount.value <= 0) {
    alert('입찰 금액을 입력해주세요.')
    return
  }

  submitting.value = true
  try {
    await axios.post(`http://localhost:8080/api/items/${itemId}/bids`, {
      bidderName: bidderName.value,
      amount: bidAmount.value
    })
    // 성공 시 화면 갱신은 WebSocket 브로드캐스트가 처리하므로 입력값만 초기화
    bidAmount.value = null
  } catch (error) {
    if (error.response && error.response.status === 400) {
      alert(error.response.data.message || '입력값이 올바르지 않습니다.')
    } else {
      alert('입찰 중 오류가 발생했습니다.')
    }
  } finally {
    submitting.value = false
  }
}

// 실시간 구독 연결
const connectWebSocket = () => {
  stompClient = new Client({
    brokerURL: 'ws://localhost:8080/ws',
    reconnectDelay: 5000,
    onConnect: () => {
      connected.value = true
      // 이 물품의 입찰 토픽 구독
      stompClient.subscribe(`/topic/items/${itemId}`, (message) => {
        const newBid = JSON.parse(message.body)
        // 중복 방지 후 최신 입찰을 맨 위에 추가하고 현재가 갱신
        if (!bids.value.some((b) => b.id === newBid.id)) {
          bids.value.unshift(newBid)
        }
        if (item.value) {
          item.value.price = newBid.amount
        }
      })
    },
    onDisconnect: () => {
      connected.value = false
    },
    onWebSocketClose: () => {
      connected.value = false
    }
  })
  stompClient.activate()
}

const formatTime = (isoString) => {
  if (!isoString) return ''
  const d = new Date(isoString)
  return d.toLocaleString('ko-KR')
}

onMounted(async () => {
  try {
    await fetchItem()
    await fetchBids()
    connectWebSocket()
  } catch (error) {
    console.error('상세 정보 로드 실패:', error)
    alert('정보를 불러오지 못했습니다.')
  }
})

onUnmounted(() => {
  // 화면을 벗어나면 WebSocket 연결 해제
  if (stompClient) {
    stompClient.deactivate()
  }
})
</script>

<style scoped>
.detail-card {
  border: 1px solid #ddd;
  padding: 20px;
  border-radius: 8px;
  background-color: #fafafa;
  margin-top: 20px;
}
.current-price span:nth-child(2) {
  font-size: 1.4em;
  font-weight: bold;
  color: #d6336c;
  margin: 0 10px;
}
.live {
  font-size: 0.8em;
  color: #aaa;
}
.live.connected {
  color: #2f9e44;
}
.bid-form,
.bid-history {
  margin-top: 25px;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
}
.bid-inputs {
  display: flex;
  gap: 10px;
}
.bid-inputs input {
  flex: 1;
  padding: 8px;
}
.hint {
  color: #888;
  font-size: 0.85em;
  margin-top: 8px;
}
table {
  width: 100%;
  border-collapse: collapse;
  text-align: center;
}
table th,
table td {
  padding: 10px;
  border-bottom: 1px solid #eee;
}
table th {
  background-color: #f4f4f4;
}
tr.latest {
  background-color: #fff3bf;
  animation: flash 1s ease-out;
}
@keyframes flash {
  from {
    background-color: #ffd43b;
  }
  to {
    background-color: #fff3bf;
  }
}
button {
  padding: 10px 20px;
  cursor: pointer;
}
button:disabled {
  cursor: not-allowed;
  opacity: 0.6;
}
</style>
