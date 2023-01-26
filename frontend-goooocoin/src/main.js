import Vue from 'vue'
import ElementUI, { Message } from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import App from './App'
import router from './router'
import store from './store/store'
import HeaderGroup from './components/HeaderGroup'
import OpDialog from './components/OpDialog'
import SideBar from './components/SideBar'
import Antd from 'ant-design-vue'
import backgroundCanvas from './components/backgroundCanvas'
import DialogueItem from './components/DialogueItem'
import VueCropper from 'vue-cropper'
import axios from 'axios'
import 'jquery'

Vue.config.productionTip = false
Vue.use(ElementUI)
Vue.use(Antd)
Vue.use(HeaderGroup)
Vue.use(OpDialog)
Vue.use(SideBar)
Vue.use(backgroundCanvas)
Vue.use(DialogueItem)
Vue.component(Message)
Vue.use(VueCropper)
Vue.prototype.$message = Message
Vue.prototype.$axios = axios

// eslint-disable-next-line no-unused-vars
const options = {
  namespace: 'pro__',
  name: 'ls',
  storage: 'local'
}
Vue.directive('title', {
  inserted: function (el, binding) {
    document.title = el.dataset.title
  }
})

// eslint-disable-next-line no-unused-vars
const app = new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App),
  template: '<App/>',
  components: { App }
}).$mount('#app')
