import Vue from 'vue'
import Router from 'vue-router'
import presentation from '../view/presentation'
import login from '../view/login'
import signup from '../view/signup'
import homepage from '../view/homepage'
import detailPage from '../view/detailPage'
import userpage from '../view/userpage'
import adminpage from '../view/adminpage'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/presentation',
      name: 'presentation',
      component: presentation
    },
    {
      path: '/',
      name: 'intro',
      component: homepage
    },
    {
      path: '/login',
      name: 'login',
      component: login
    },
    {
      path: '/signup',
      name: 'signup',
      component: signup
    },
    {
      path: '/home',
      name: 'homepage',
      component: homepage
    },
    {
      path: '/home/detailPage',
      name: 'detailPage',
      component: detailPage
    },
    {
      path: '/userpage',
      name: 'userpage',
      component: userpage
    },
    {
      path: '/adminpage',
      name: 'adminpage',
      component: adminpage
    }
  ],
  base: '/gooodcoinFront/'
})
