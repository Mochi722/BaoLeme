<template>
  <div class="QASystem">
    <div class="QA-header">
      <div class="QA-header-content" @touchstart="showInfo">
        <div v-if="!toShowMaskInfo" :class="[toShowMaskInfo ? '' : 'bounceInRight' , 'animated']">
<!--          <img src="../../../front-end/static/images/customerService.png" class="QA-header-avatar">-->
          <div class="QA-header-title-wrap" style="height: 50px">
            <div class="QA-header-title" >智能问答</div>
          </div>
          <el-button class="DialogueButton" icon="el-icon-close" id="backButton" @click="backToHome()" circle></el-button>
        </div>
      </div>
      <!-- 客服弹出的具体信息 -->
      <div v-show="toShowMaskInfo" :class="[toShowMaskInfo ? 'bounceInDown' : '' , 'QA-show-info-box', 'animated']">
        <div class="QA-logo">
<!--          <img src="../../../front-end/static/images/customerService.png">-->
          <h4>智能问答</h4>
        </div>
      </div>
    </div>

    <div class="QA-content" @touchstart="toShowMaskInfo=false" ref="xwBody">
      <div class="QA-chat-wrap">
        <ul>
          <li v-for="messageList in records" :key="messageList.type">
            <div v-if="messageList.type==1">
              <div class="xw-chat-time">{{ messageList.time }}</div>
              <div class="QA-chat-servicer">
                <div class="QA-servicer-avantar-wrap">
<!--                  <img src="/static/images/customerService.png" class="QA-servicer-avantar">-->
                </div>
                <div class="QA-chat-msg">
                  <span v-html="replaceFace(messageList.content)"></span>
                </div>
              </div>
            </div>

            <!-- 用户发送的消息模板-->
            <div v-else>
              <div class="xw-chat-time">{{ messageList.time }}</div>
              <div class="QA-chat-customer">
                <div class="QA-customer-avantar-wrap">
<!--                  <img src="/static/images/userAvatar.png" class="QA-customer-avantar">-->
                </div>
                <div class="QA-chat-msg" style="display:inline-block">
                  <span v-html="replaceFace(messageList.content)"></span>
                </div>
              </div>
            </div>
          </li>
        </ul>
      </div>
    </div>

    <div class="QA-footer-wrap" @touchstart="toShowMaskInfo=false">
      <div class="QA-footer-content">
        <div class="QA-vmodel-wrap">
          <textarea class="QA-content-textarea" placeholder="请输入您的问题" v-model="content" @focus="onFocusText" @keyup.enter="sendMsg"></textarea>
        </div>
        <el-button class="DialogueButton" icon="el-icon-s-promotion" @click="sendMsg"></el-button>
<!--        <div class="QA-chat-tool">-->
<!--          <div class="xw-chat-tool-item">-->
<!--            <transition name="fade">-->
<!--              <a href="javascript:void(0)" class="QA-send-btn-text" @click="sendMsg">发送</a>-->
<!--            </transition>-->
<!--          </div>-->
<!--        </div>-->
      </div>

    </div>
  </div>
</template>

<script>
//
import {mapActions, mapGetters, mapMutations} from 'vuex'
export default {
  data () {
    return {
      comment: {},
      showMoreOpratin: false,
      toShowMaskInfo: false,
      testContents: ['这是一个回答', '这个问题还没遇到过', '正在查询'],
      content: '',
      // 聊天记录
      records: [{
        type: 1,
        time: new Date().toLocaleTimeString(),
        content: '您好！欢迎来到智能问答，这里可以回答一些有关的问题，请问有什么能帮到您？'
      },
      {
        type: 2,
        time: new Date().toLocaleTimeString(),
        content: '在底部输入框输入问题，点击发送'
      }
      ]
    }
  },
  computed: {
    ...mapGetters([
      'answer',
      'DialogueItemShow'
    ])
  },
  methods: {
    ...mapMutations([
      'set_question',
      'set_DialogueItemShow'
    ]),
    ...mapActions([
      'getAnswer'
    ]),
    showInfo () {
      this.toShowMaskInfo = true
    },
    async sendMsg () {
      var content = this.content.trim()
      this.records.push({
        time: new Date().toLocaleTimeString(),
        content: content,
        type: 2
      })
      const question = content
      this.content = ''
      await this.getAnswer(question)
      const answer = this.answer
      console.log(answer)
      setTimeout(() => {
        this.records.push({
          time: new Date().toLocaleTimeString(),
          content: answer,
          type: 1
        })
        // this.scrollToBottom();
      }, 800)
      this.scrollToBottom()
    },
    replaceFace (con) {
      if (!con) {
        return
      }
      if (con.toString().indexOf('/:') > -1) {
        var exps = this.EXPS
        for (var i = 0; i < exps.length; i++) {
          con = con.replace(exps[i].reg, '<img src="' + exps[i].file + '"  alt="" />')
        }
      }
      this.scrollToBottom()
      return con
    },

    // 滚动到底
    scrollToBottom () {
      setTimeout(() => {
        this.$refs.xwBody.scrollTop = this.$refs.xwBody.scrollHeight
        // console.log('当前滚动条位置:' + this.$refs.xwBody.scrollTop)
        // console.log('当前可滚动区域容器的高度:' + this.$refs.xwBody.scrollHeight)
        // 清理定时器
        clearTimeout(this.messagesContainerTimer)
      }, 13)
    },
    onFocusText () {
      this.scrollToBottom()
    },
    backToHome () {
      this.set_DialogueItemShow(false)
    }
  }
}
</script>
<style>
.DialogueButton {
  color: #2c3e50;
}
.DialogueButton:active {
  color: #2c3e50;
}
.DialogueButton:hover {
  color: #2c3e50;
  border: solid #2c3e50 1px;
}
.DialogueButton:focus {
  color: #2c3e50;
  background-color: white;
}

.QASystem {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
}

.QA-content {
  flex: 1;
  position: relative;
  overflow: auto;
  transition: height .8s;
}

.animated {
  -webkit-animation-duration: 1s;
  animation-duration: 1s;
  -webkit-animation-fill-mode: both;
  animation-fill-mode: both;
}

.animated.bounceIn,
.animated.bounceOut {
  -webkit-animation-duration: .75s;
  animation-duration: .75s;
}

@-webkit-keyframes bounceInDown {
  from, 60%, 75%, 90%, to {
    -webkit-animation-timing-function: cubic-bezier(0.215, 0.610, 0.355, 1.000);
    animation-timing-function: cubic-bezier(0.215, 0.610, 0.355, 1.000);
  }

  0% {
    opacity: 0;
    -webkit-transform: translate3d(0, -3000px, 0);
    transform: translate3d(0, -3000px, 0);
  }

  60% {
    opacity: 1;
    -webkit-transform: translate3d(0, 25px, 0);
    transform: translate3d(0, 25px, 0);
  }

  75% {
    -webkit-transform: translate3d(0, -10px, 0);
    transform: translate3d(0, -10px, 0);
  }

  90% {
    -webkit-transform: translate3d(0, 5px, 0);
    transform: translate3d(0, 5px, 0);
  }

  to {
    -webkit-transform: none;
    transform: none;
  }
}

@keyframes bounceInLeft {
  from, 60%, 75%, 90%, to {
    -webkit-animation-timing-function: cubic-bezier(0.215, 0.610, 0.355, 1.000);
    animation-timing-function: cubic-bezier(0.215, 0.610, 0.355, 1.000);
  }

  0% {
    opacity: 0;
    -webkit-transform: translate3d(-3000px, 0, 0);
    transform: translate3d(-3000px, 0, 0);
  }

  60% {
    opacity: 1;
    -webkit-transform: translate3d(25px, 0, 0);
    transform: translate3d(25px, 0, 0);
  }

  75% {
    -webkit-transform: translate3d(-10px, 0, 0);
    transform: translate3d(-10px, 0, 0);
  }

  90% {
    -webkit-transform: translate3d(5px, 0, 0);
    transform: translate3d(5px, 0, 0);
  }

  to {
    -webkit-transform: none;
    transform: none;
  }
}

.bounceInLeft {
  -webkit-animation-name: bounceInLeft;
  animation-name: bounceInLeft;
}

#backButton {
  background-color: Transparent;
  border-style: none;
  outline: none;
  float:right;
  margin-top: 8px;
  font-size: 20px;
  color: #fff;
}

@-webkit-keyframes bounceInRight {
  from, 60%, 75%, 90%, to {
    -webkit-animation-timing-function: cubic-bezier(0.215, 0.610, 0.355, 1.000);
    animation-timing-function: cubic-bezier(0.215, 0.610, 0.355, 1.000);
  }

  from {
    opacity: 0;
    -webkit-transform: translate3d(3000px, 0, 0);
    transform: translate3d(3000px, 0, 0);
  }

  60% {
    opacity: 1;
    -webkit-transform: translate3d(-25px, 0, 0);
    transform: translate3d(-25px, 0, 0);
  }

  75% {
    -webkit-transform: translate3d(10px, 0, 0);
    transform: translate3d(10px, 0, 0);
  }

  90% {
    -webkit-transform: translate3d(-5px, 0, 0);
    transform: translate3d(-5px, 0, 0);
  }

  to {
    -webkit-transform: none;
    transform: none;
  }
}

.bounceInRight {
  -webkit-animation-name: bounceInRight;
  animation-name: bounceInRight;
}

@keyframes bounceInUp {
  from, 60%, 75%, 90%, to {
    -webkit-animation-timing-function: cubic-bezier(0.215, 0.610, 0.355, 1.000);
    animation-timing-function: cubic-bezier(0.215, 0.610, 0.355, 1.000);
  }

  from {
    opacity: 0;
    -webkit-transform: translate3d(0, 3000px, 0);
    transform: translate3d(0, 3000px, 0);
  }

  60% {
    opacity: 1;
    -webkit-transform: translate3d(0, -20px, 0);
    transform: translate3d(0, -20px, 0);
  }

  75% {
    -webkit-transform: translate3d(0, 10px, 0);
    transform: translate3d(0, 10px, 0);
  }

  90% {
    -webkit-transform: translate3d(0, -5px, 0);
    transform: translate3d(0, -5px, 0);
  }

  to {
    -webkit-transform: translate3d(0, 0, 0);
    transform: translate3d(0, 0, 0);
  }
}

 html,body,a,button,input,textarea,img,ul,li,p,dl,dd,h1,h2,h3,h4,h5,h6{
   box-sizing: border-box;
   margin: 0;
   padding: 0;
 }
a,button,input,textarea,img{
  -webkit-tap-highlight-color: transparent;
  -webkit-touch-callout: none;
  -webkit-user-select: none;
  outline: none;
}
body {
  height: 100%;
  width: 100vw;
  overflow: hidden;
  font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, 微软雅黑, Arial, sans-serif;
  font-size: 14px;
  line-height: 1.43;
  color: #333;
  word-wrap: break-word;
  word-break: normal;
  hyphens: auto;
  transition:.5s;
}

a {
  text-decoration: none;
  color: #4078c0;
}

img {
  border-style: none;
}

ul{
  list-style: none;
}

::-webkit-scrollbar-track {
  border-radius: 0;
  padding: 0;
  background-color: transparent;
}

::-webkit-scrollbar-thumb {
  border-radius: 10px;
  padding: 0;
  background-color: rgba(0, 0, 0, .2);
}

::-webkit-scrollbar {
  width: 7px;
  height: 7px;
  background-color: transparent;
}

.QA-header {
  z-index: 2;
  max-height: 360px;
  background: #2c3e50;
  box-shadow: 0 1px 3px rgba(0, 0, 0, .2);
}

.QA-header-content {
  position: relative;
  overflow: hidden;
  padding: 0px 16px;
  min-height: 50px;
}

.QA-chat-servicer:after,
.QA-chat-servicer:before,
.QA-chat-customer:after,
.QA-chat-customer:before,
.QA-chat-wrap:after,
.QA-chat-wrap:before,
.QA-chat-tool:after,
.QA-chat-tool:before,
.QA-chat-ul-box:after,
.QA-chat-ul-box:before {
  display: table;
  content: "";
}

.QA-header-avatar {
  float: left;
  height: 40px;
  width: 40px;
  border-radius: 100%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .1);
  margin-right: 8px;
  margin-top: 5px;
}

.QA-header-title-wrap {
  padding: 3px 0;
  float: left;
  width: 200px;
}

.QA-header-title {
  font-size: 25px;
  color: #fff;
  margin-top: 8px;
}

.QA-chat-wrap {
  overflow: auto;
  padding-left: 16px;
  padding-bottom: 20px;
}

.QA-chat-wrap .xw-chat-time,
.QA-chat-wrap time {
  line-height: 1;
  text-align: center;
  display: block;
  margin-bottom: 8px;
  margin-top: 8px;
  font-size: 12px;
  color: #999;
  text-shadow: 1px 1px 1px hsla(0, 0%, 100%, .6);
}

.QA-chat-servicer,
.QA-chat-customer {
  position: relative;
  margin-bottom: 16px;
  padding: 0 10px 0 45px;
}

.QA-servicer-avantar-wrap {
  position: absolute;
  bottom: 0;
  line-height: 0;
}
.QA-send-btn-text{
  height: 40px;
  width: 40px;
  line-height: 40px;
  text-align: center;
  color: #2c3e50;
  border-radius: 5px;
  font-size: 15px;
}
.QA-customer-avantar-wrap {
  position: absolute;
  bottom: 0;
  right: 8px;
  line-height: 0;
}

.QA-chat-servicer .QA-servicer-avantar-wrap,.QA-customer-avantar-wrap{
  left: 0;
}

.QA-servicer-avantar {
  border-radius: 100%;
}

.QA-servicer-avantar-wrap img,.QA-customer-avantar-wrap img {
  height: 34px;
  width: 34px;
}

.QA-chat-msg {
  -webkit-font-smoothing: antialiased;
  hyphens: auto;
  word-wrap: break-word;
  word-break: normal;
  position: relative;
  clear: both;
  padding: 8px 16px;
  border: 1px solid transparent;
  max-width: 100%;
  min-width: 50px;
  min-height: 22px;
  line-height: 1.6em;
  max-width: 70%;
}

.QA-chat-servicer .QA-chat-msg {
  background: #fff;
  border-color: #efefef;
  float: left;
}

.QA-chat-msg :last-child {
  margin-bottom: 0;
}

.QA-chat-msg span{
  white-space: pre-line;
}
.QA-chat-servicer,
.QA-chat-customer {
  position: relative;
  margin-bottom: 16px;
  padding: 0 10px 0 45px;
}

.QA-chat-customer {
  text-align: right;
}

.QA-chat-customer .QA-chat-msg {
  float: right;
  text-align: left;
  background: #2c3e50;
  border-color: #2c3e50;
  color: #fff;
  margin-right: 45px;
}
.QA-customer-avantar{
  border-radius: 50%;
}
.QA-chat-msg :last-child {
  margin-bottom: 0;
}

.QA-chat-wrap .xw-system-info-time,
.QA-chat-wrap time {
  line-height: 1;
  text-align: center;
  display: block;
  margin-bottom: 8px;
  font-size: 12px;
  color: #999;
  text-shadow: 1px 1px 1px hsla(0, 0%, 100%, .6);
}

.QA-chat-wrap>:last-child {
  margin-bottom: 16px;
}

.QA-system-info span {
  display: inline-block;
  background: rgba(0, 0, 0, .08);
  padding: 4px 8px;
  line-height: 1;
  color: #7c7c7c;
  border-radius: 2px;
  text-shadow: 1px 1px 1px hsla(0, 0%, 100%, .6);
}

.QA-footer-wrap {
  justify-content:flex-end;
  width: 100%;
  box-shadow: 0 -1px 4px rgba(0, 0, 0, .05);
  background: #fff;
  z-index: 2;
}

.QA-footer-content {
  position: relative;
  height: 40px;
  display: flex;
  box-shadow: rgba(0, 0, 0, 0.05) 0px -1px 4px;
}
.QA-vmodel-wrap{
  flex:1;
}
.QA-content-textarea{
  /*box-sizing: border-box;*/
  z-index: 1;
  overflow-y: auto;
  font-size: 15px;
  line-height: 40px;
  text-align: center;
  border: none;
  resize: none;
  position: absolute;
  left: 0px;
  width: 94%;
  height: 40px;
  font-family: inherit;
  white-space: pre;
  white-space: pre-wrap;
  word-wrap: break-word;
}

.QA-chat-ul-box a {
  display: block;
  height: 40px;
  width: 40px;
}

.QA-chat-servicer:after,
.QA-chat-customer:after,
.QA-chat-wrap:after,
.QA-chat-tool:after,
.QA-chat-ul-box:after{
  clear: both;
}

.QA-chat-msg:last-child:before {
  bottom: -1px;
}

.QA-chat-msg:last-child:after,
.QA-chat-msg:last-child:before {
  content: "";
  position: absolute;
  height: 0;
  width: 0;
  display: block;
}

.QA-chat-customer .QA-chat-msg:last-child:before {
  display: inline-block;
  height: 0;
  width: 0;
  border: 10px solid transparent;
  border-bottom: 10px solid #2c3e50;
  right: -10px;
  border-left-width: 9px;
}

.QA-chat-servicer .QA-chat-msg:last-child:before {
  display: inline-block;
  height: 0;
  width: 0;
  border: 10px solid transparent;
  border-bottom: 10px solid #efefef;
  left: -10px;
  border-left-width: 9px;
}

.QA-chat-servicer .QA-chat-msg:last-child:after {
  display: inline-block;
  height: 0;
  width: 0;
  border: 10px solid transparent;
  border-bottom: 10px solid #fff;
  left: -8px;
  border-left-width: 9px;
  bottom: 0px;
}

.QA-show-info-box{
  height: 200px;
  margin-top: -50px;
  z-index: 1000;
  background: #2c3e50;
  color:#fff;
}

.QA-logo{
  text-align: center;
}

.QA-logo img{
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-top: 20px;
}

.QA-header-info p{
  height: 110px;
  padding:0 16px;
  line-height: 1.6rem;
  word-wrap: break-word;
  text-align: left;
  overflow: auto;
}

.QA-chat-msg span img{
  vertical-align: middle;
}
.mint-swipe-items-wrap > div{
  text-align: center;
}
.scroll-ul{
  min-height: 340px;
}
.mint-swipe-indicator.is-active {
  background: #2c3e50;
  opacity: 1;
}
</style>
