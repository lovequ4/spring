import { createRouter, createWebHistory } from 'vue-router'
import SignUp from '../views/Signup.vue'
import SignIn from '../views/Signin.vue'
import Index from '../views/Index.vue'
import Navbar from '../components/Navbar.vue'
import Test from '../views/Test.vue'

const routes = [
    {
      path: '/',
      redirect: '/signin',
    },
    {
      path: '/signup',
      name: 'signup',
      component: SignUp
    },
    {
      path: '/signin',
      name: 'signin',
      component: SignIn
    },
    {
      path: '/index',
      
      component: Navbar, 
      children: [
        {
          path: '',
          name: 'index',
          component: Index,
        },
        {
          path:'test',
          name:'test',
          component: Test,
        }
      ],
    },
  ]
  
  const router = createRouter({
    history: createWebHistory(),
    routes
  })
  
  
  export default router