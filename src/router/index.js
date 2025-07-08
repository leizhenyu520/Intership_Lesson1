import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Profile from '../views/Profile.vue'
import Chat from '../views/Chat.vue'

const routes = [
  {
    path: '/',
    redirect: '/profile'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/profile',
    name: 'Profile',
    component: Profile,
    meta: { requiresAuth: true }
  },
  {
    path: '/chat',
    name: 'Chat',
    component: Chat,
    meta: { requiresAuth: true }
  },
  {
    path: '/ai-painting',
    name: 'AIPainting',
    component: () => import('../views/AIPainting.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/essay-correction',
    name: 'EssayCorrection',
    component: () => import('../views/EssayCorrection.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/creative-center',
    name: 'CreativeCenter',
    component: () => import('../views/CreativeCenter.vue'),
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router