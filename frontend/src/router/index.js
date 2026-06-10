import { createRouter, createWebHistory } from 'vue-router'
import ItemList from '../components/ItemList.vue'
import ItemDetail from '../components/ItemDetail.vue'

const routes = [
  { path: '/', component: ItemList },
  { path: '/items/:id', component: ItemDetail, props: true }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router