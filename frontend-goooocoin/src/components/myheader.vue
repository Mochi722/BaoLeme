<template>
  <div class="header">
    <div class="headerImgbox" style="float: left;width: 50px;height: 20px">
      <img style="float: left;margin-left:15px;margin-top: 6px;width: 150px;height: 60px" src="../assets/headerlogo.png">
    </div>
    <el-menu
      :default-active="activeIndex"
      class="el-menu-demo"
      mode="horizontal"
      @select="handleSelect"
      background-color="transparent"
      text-color="#fff"
      active-text-color="purple"
      style="height: 70px;float: right;margin-right: 130px;"
    >
      <el-menu-item class="el-menu-item" index="1">主页</el-menu-item>
      <el-menu-item class="el-menu-item" index="2">个人中心</el-menu-item>
    </el-menu>
    <el-dropdown
      style="position: absolute; right: 20px;top: 10px"
      @command="handleCommand"
    >
      <span class="el-dropdown-link">
              <el-avatar :size="50" :src="avatarurl" @click.native="avatarClick"></el-avatar>
      </span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item command="a">退出登录</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
export default {
  name: 'myheader',
  data () {
    return {
      circleUrl: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      circleUrl4user: JSON.parse(window.localStorage.getItem('verificationRes')).avatarAddress,
      avatarurl: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      activeIndex2: '1'
    }
  },
  props: [
    'activeIndex'
  ],
  async mounted () {
    var vcr = JSON.parse(window.localStorage.getItem('verificationRes'))
    console.log('090909909')
    console.log(vcr)
    if (window.localStorage.getItem('userID') !== -1 && vcr.avatarAddress !== '') this.avatarurl = vcr.avatarAddress
  },
  computed: {
    ...mapGetters([
      'currentUserID'
    ])
  },
  methods: {
    handleSelect (key, keyPath) {
      if (key === '1') this.$router.push('/home')
      else if (key === '2') {
        if (this.currentUserID === -1) this.$router.push('/login')
        else this.$router.push('/userpage')
      }
      console.log('handleSelect')
      console.log(this.activeIndex2)
      window.sessionStorage.setItem('currentCenter', -1)
      window.sessionStorage.setItem('entityStack', JSON.stringify([]))
      window.sessionStorage.setItem('entityInfoStack', JSON.stringify([]))
    },
    handleCommand (command) {
      window.localStorage.setItem('userID', -1)
      window.localStorage.setItem('verificationRes', null)
      window.sessionStorage.setItem('currentCenter', -1)
      window.sessionStorage.setItem('entityStack', JSON.stringify([]))
      window.sessionStorage.setItem('entityInfoStack', JSON.stringify([]))
      this.$router.push('/login')
    },
    avatarClick () {
      console.log('click')
      if (this.currentUserID === -1) this.$router.push('/login')
      else this.$router.push('/userpage')
    }
  }
}
</script>

<style scoped>
  .header{
    width: 100%;
    height: 70px;
    background-image: linear-gradient(to right, #ad6b77, #EC9390);
  }
  .headerleft{
    width: 30%;
    height: 70px;
    /*background: #7EA9D3;*/
    text-align: center;
    float: left;
  }
  .headerright{
    width: 70%;
    height: 70px;
    /*background: #C1D7E8;*/
    text-align: center;
    float: left;
  }
  .el-menu-item {
    font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
    height: 63px;
    margin-top: 7px;
    font-size: 17px;
  }
  .el-menu-item:hover {
    background-color: transparent;
  }
</style>
