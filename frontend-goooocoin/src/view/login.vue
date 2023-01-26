<template>
  <div class="all">
    <div class="box">
      <div class="head">
        <h1>登录&nbsp;|&nbsp;Login</h1>
      </div>
      <div class="content">
        <div class="loginbox">
          <input type="text" id="userName" v-model="userPhoneNumber" name="userName" placeholder="请输入手机号"><br>
          <input type="password" id="password" v-model="password" name="password" placeholder="请输入密码"><br>

          <div class="input-interval" id="verify">
            <input type="text" placeholder="请输入验证码" id="inputCode" size="29" @keyup.enter="CheckValidation()"/>

            <div id="verificationCode" class="verificationCode">
              <canvas width="100" height="40" id="verifyCanvas"></canvas>
              <img id="code_img" @click="resetCode()">
            </div>
          </div>
          <input type="button" @click="CheckValidation()" value="登录">
        </div>
      </div>
      <div class="signin_a">
        <a @click="toSignup()"><!--img src="../assets/注册.png"-->注册账号</a>
      </div>
    </div>
    <div class="divider" style="float:left;margin-left:670px;margin-top: 60px;height: 700px; width: 3px; background-color: rgba(255,255,255,0.66);"></div>
    <div class="picBox" style="float:left;margin-left: 50px;margin-top: 140px; background-color: rgba(255, 255, 255, 0.5); width: 850px; height: 500px;box-shadow: 0 20px 40px rgba(255, 255, 255, .6), 0 0 6px rgba(255, 255, 255, .4);border-radius: 15px">
      <div style="height: 40px"></div>
      <img src="../assets/headerlogowtbg.png" style="width: 350px; height: 140px">
<!--      <div class="divider2" style="width: 800px; height: 2px; background-color: white; margin-top: 0px; margin-left: 25px"></div>-->
      <div style="width: 600px; height: 300px; text-align: center; margin-left: 125px;margin-top: 30px">
        <span style="font-size: 45px; font-weight: bold;color: #7EA9D3;text-align: right;float:top;">——基于知识图谱的美食菜谱</span>
        <div style="height: 20px"></div>
        <span style="font-size: 28px; font-weight: bold;color: #7EA9D3;text-align: right;float:top;">属于你的美食小天地<br></span>
        <div style="height: 20px"></div>
        <span style="font-size: 35px; font-weight: bold;color: #7EA9D3;text-align: right;float:top;">没有人比 <span style="color:#EF9FAF"> 饱了吗</span> 更懂你</span>
      </div>
    </div>
  </div>
</template>
<script>
import {mapActions, mapGetters, mapMutations} from 'vuex'
export default {
  name: 'login',
  data () {
    return {
      res: '',
      nums: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
      ],
      str: '',
      userPhoneNumber: '',
      password: ''
    }
  },
  computed: {
    ...mapGetters([
      'verificationRes'
    ])
  },
  mounted () {
    this.drawCode('')
  },
  methods: {
    ...mapMutations([
    ]),
    ...mapActions([
      'verifyInformation'
    ]),
    toSignup () {
      this.$router.push('/signup')
    },
    drawCode (str) {
      console.log('verifyCode')
      var canvas = document.getElementById('verifyCanvas') // 获取HTML端画布
      var context = canvas.getContext('2d') // 获取画布2D上下文
      context.fillStyle = 'rgba(72,140,209,0.5)' // 画布填充色
      context.fillRect(0, 0, canvas.width, canvas.height) // 清空画布
      context.fillStyle = 'white' // 设置字体颜色
      context.font = '25px Arial' // 设置字体
      // eslint-disable-next-line no-array-constructor
      var rand = new Array()
      // eslint-disable-next-line no-array-constructor
      var x = new Array()
      // eslint-disable-next-line no-array-constructor
      var y = new Array()
      for (var i = 0; i < 4; i++) {
        rand.push(rand[i])
        rand[i] = this.nums[Math.floor(Math.random() * this.nums.length)]
        this.res += rand[i]
        x[i] = i * 20 + 10
        y[i] = Math.random() * 20 + 20
        context.fillText(rand[i], x[i], y[i])
      }
      this.res = this.res.substr(this.res.length - 4)
      str = rand.join('').toUpperCase()
      // 画3条随机线
      for (var j = 0; j < 3; j++) {
        this.drawline(canvas, context)
      }

      // 画30个随机点
      for (var k = 0; k < 50; k++) {
        this.drawDot(canvas, context)
      }
      this.convertCanvasToImage(canvas)
      return str
    },
    drawline (canvas, context) {
      context.moveTo(Math.floor(Math.random() * canvas.width), Math.floor(Math.random() * canvas.height)) // 随机线的起点x坐标是画布x坐标0位置，y坐标是画布高度的随机数
      context.lineTo(Math.floor(Math.random() * canvas.width), Math.floor(Math.random() * canvas.height)) // 随机线的终点x坐标是画布宽度，y坐标是画布高度的随机数
      context.lineWidth = 0.5 // 随机线宽
      context.strokeStyle = 'rgba(50,50,50,0.3)' // 随机线描边属性
      context.stroke() // 描边，即起点描到终点
    },
    drawDot (canvas, context) {
      var px = Math.floor(Math.random() * canvas.width)
      var py = Math.floor(Math.random() * canvas.height)
      context.moveTo(px, py)
      context.lineTo(px + 1, py + 1)
      context.lineWidth = 0.2
      context.stroke()
    },
    convertCanvasToImage (canvas) {
      document.getElementById('verifyCanvas').style.display = 'none'
      var image = document.getElementById('code_img')
      image.src = canvas.toDataURL('image/png')
      return image
    },
    resetCode () {
      $('#verifyCanvas').remove()
      $('#code_img').before('<canvas width="100" height="40" id="verifyCanvas"></canvas>')
      this.drawCode()
    },
    async CheckValidation () {
      // this.$router.push('/')
      // console.log('lalala')
      // var inputCode = document.getElementById('inputCode').value
      // if (inputCode.length <= 0) {
      //   alert('请输入验证码')
      //   $('.err-message').css('visibility', 'visible')
      //   return false
      // } else if (inputCode.toUpperCase() !== this.res.toUpperCase()) {
      //   alert('验证码错误，请重新输入')
      //   $('.err-message').css('visibility', 'visible')
      //   $('#inputCode').val('')
      //   $('#userpwd').val('')
      //   this.drawCode()
      //   return false
      // } else {
      //   this.$router.push('/')
      //   return true
      // }

      // 1. 验证验证码
      var inputCode = document.getElementById('inputCode').value
      if (inputCode.length <= 0) {
        this.$message({
          message: '请输入验证码！',
          type: 'warning'
        })
        return false
      } else if (inputCode.toUpperCase() !== this.res.toUpperCase()) {
        this.$message({
          message: '验证码错误，请重新输入！',
          type: 'warning'
        })
        $('#inputCode').val('')
        $('#password').val('')
        this.drawCode()
        return false
      }
      // 2. 验证用户名密码
      // var username = document.getElementById('userName')
      // var passwd = document.getElementById('password')
      console.log({phoneNumber: this.userPhoneNumber, password: this.password})
      await this.verifyInformation({phoneNumber: this.userPhoneNumber, password: this.password})
      if (this.verificationRes.userType === 1) {
        this.$router.push('/presentation')
      } else if (this.verificationRes.userType === 0) {
        this.$router.push('/home')
      } else {
        this.$message({
          message: '用户名或密码错误，请重新输入！',
          type: 'warning'
        })
        $('#inputCode').val('')
        $('#password').val('')
      }
    }
  }
}
</script>

<style scoped>
  body{
    all: revert;
  }
  .all {
    position: absolute;
    /*z-index: -1;*/
    width: 100%;
    height: 100%;
    background: url(../assets/yellowpinkbg2.png) no-repeat;
    background-size: 100% 100%;
  }

  .box {
    width: 400px;
    height: 490px;
    background-color: rgba(255,255,255,0.8);
    position: absolute;
    float: left;
    top: 18%;
    left: 12%;
    right: 0;
    bottom: 0;
    border-radius: 10px;
    box-shadow: 0 20px 40px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
  }

  .head {
    color: #488cd1;
    text-align: left;
    margin-top: 10%;
    margin-left: 5%;
  }

  .content {
    all: revert;
    width: 90%;
    margin-top: 8%;
    margin-left: 5%;
    margin-bottom: 0;
    border-radius: 5px;
    color: white;
  }

  input[id=userName], select {
    width: 100%;
    padding: 12px 20px;
    margin: 8px 0;
    display: inline-block;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-sizing: border-box;
  }

  input[id=inputCode], select {
    /*all: revert;*/
    width: 70%;
    float: left;
    padding: 12px 20px;
    margin: 8px 0;
    display: inline-block;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-sizing: border-box;
  }

  input[type=password], select {
    width: 100%;
    padding: 12px 20px;
    margin: 8px 0;
    display: inline-block;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-sizing: border-box;
  }

  input[type=button] {
    width: 100%;
    background-color: #488cd1;
    color: white;
    padding: 14px 20px;
    margin: 30px 0;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }

  input[type=button   ]:hover {
    background-color: rgba(72, 140, 209,0.5);
  }

  .verificationCode {
    float: right;
    margin-top: 8px;
  }

  .signin_a {
    width: 88%;
    margin-left: 5%;
    text-align: left;
    padding-bottom: 10px;
  }

  a {
    margin-right: 0;
    text-decoration: none;
    color: #488cd1;
  }

  a:hover {
    color: #00008B;
  }

  .signin_a > img {
    width: 8%;
  }
</style>
