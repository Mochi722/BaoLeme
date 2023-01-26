<template>
  <div class="all">
    <myheader :activeIndex="activeIndex" v-if="mhrefresh"></myheader>
    <el-container style="height: 85%">
      <background-canvas style="position: absolute;width: 100%;height: 85%;pointer-events: none;z-index: 0"></background-canvas>
      <el-main>
        <div class="main">
          <edit-password-dialog></edit-password-dialog>
          <div class="infoBox" v-if="formrefresh">
            <el-page-header v-if="editing" class="pheader" @back="goBack" content="编辑资料">
            </el-page-header>
            <p style="font-size: 18px;color: #303133;text-align: left;margin-left: 20px;margin-bottom: -3px" v-if="!editing">账号资料</p>
            <div style="height: 100%; width: 100%; padding-top: 50px">
              <div style="height: 50px; width: 50px; margin-left: 20px">
                <el-avatar :size="110" :src="imgPath"></el-avatar>
                <avatar @myheadrefresh="myheadrefresh" :isShowDialog='isShowDialog' :upload-data="UploadData" :upload-url="UploadUrl" @getAvatarUrl="getAvatarUrl" @uploadVfRes="uploadVfRes"></avatar>
              </div>
            </div>
            <div class="form">
              <el-form ref="form" :model="form" label-width="80px">
                <el-form-item label="电话号码:" style="margin-top: 130px">
                  <el-input v-if="editing" v-model="userPhoneNumber" :placeholder="userPhoneNumber" size="medium"></el-input>
                  <span class="formtext" v-else>{{userPhoneNumber}}</span>
                </el-form-item>
                <el-form-item label="用户名:" style="margin-top: -10px">
                  <el-input v-if="editing" v-model="userName" :placeholder="userName" size="medium"></el-input>
                  <span class="formtext" v-else>{{userName}}</span>
                </el-form-item>
                <el-form-item label="用户id:" style="margin-top: -10px">
                  <span class="formtext">{{userId}}</span>
                </el-form-item>
              </el-form>
            </div>
            <div class="editbtns" style="margin-top: 20px;margin-left: 20px;width: 100%">
              <el-button v-if="!editing" icon="el-icon-key" round @click="editPassword" style="float:right;margin-right: 50px" size="small">修改密码</el-button>
              <el-button type="success" v-if="editing" icon="el-icon-check" round @click="finishEdit" style="margin-top: 10px;margin-left: 5px;margin-bottom: 20px" size="small">保存更改</el-button>
              <el-button type="primary"  v-if="!editing" icon="el-icon-edit" round @click="startEdit" style="float:right;margin-bottom: 10px;margin-right: 10px" size="small">修改资料</el-button>
            </div>
            </div>
          <div class="collectBox">
            <p style="font-size: 18px;color: #303133;text-align: left;margin-left: 20px;margin-bottom: -3px">收藏夹</p>
            <div class="collaspeBox">
              <el-scrollbar style="height: 100%;" class="el-scrollbar">
                <el-collapse :style="customStyle"><!--span>v-model="activeNames" @change="handleChange"</span-->
                  <el-collapse-item v-for="item in this.collectionList" class="eci" :key="item.id">
                  <span slot="title" class="a" style="width: 90%">
                    <span class="b" style="float:left;"><el-avatar :size="size" :src="item.labels[0] === 'Recipe' ? circleUrl4recipe : circleUrl4ingredients" style="margin: 18px 0 0 10px"></el-avatar></span>
                    <span class="c" style="float:left;margin-left: 10px; margin-top: 13px">{{item.properties.name}}</span>
                    <el-popconfirm
                      title="确定取消收藏？"
                      @confirm="unCollectEntity(item)"
                    >
                      <el-link slot="reference" type="warning" style="float:right;margin-top: 13px">取消收藏</el-link>
                    </el-popconfirm>
                    <el-link type="primary" icon="el-icon-share" style="float:right;margin-right: 20px;margin-top: 13px" @click="enterGraph(item)">进入图谱</el-link>
                  </span>
                    <div v-if="item.labels[0] === 'Recipe'"  style="text-align: left;margin-left: 20px;padding-top: 10px;width: 33%"><span style="color: #488cd1">口味：</span>{{item.properties.口味}}</div>
                    <div v-if="item.labels[0] === 'Recipe'" style="text-align: left;margin-left: 20px;padding-top: 10px;width: 33%"><span style="color: #488cd1">工艺：</span>{{item.properties.工艺}}</div>
                    <div v-if="item.labels[0] === 'Recipe'" style="text-align: left;margin-left: 20px;padding-top: 10px;width: 33%"><span style="color: #488cd1">类型：</span>{{item.properties.类型}}</div>
                    <div v-if="item.labels[0] === 'Recipe'" style="text-align: left;margin-left: 20px;padding-top: 10px;width: 33%"><span style="color: #488cd1">耗时：</span>{{item.properties.耗时}}</div>
                    <div v-if="item.labels[0] === 'Recipe'" style="text-align: left;margin-left: 20px;padding-top: 10px;width: 100%"><span style="color: #488cd1">做法：</span>{{item.properties.做法}}</div>
                    <div v-if="item.labels[0] === 'Ingredients'"  style="text-align: left;margin-left: 20px;padding-top: 10px;width: 33%"><span style="color: #488cd1">适用人群：</span>{{item.properties.适用人群}}</div>
                    <div v-if="item.labels[0] === 'Ingredients'" style="text-align: left;margin-left: 20px;padding-top: 10px;width: 33%"><span style="color: #488cd1">禁忌人群：</span>{{item.properties.禁忌人群}}</div>
                    <div v-if="item.labels[0] === 'Ingredients'" style="text-align: left;margin-left: 20px;padding-top: 10px;width: 100%"><span style="color: #488cd1">营养价值：</span>{{item.properties.营养价值}}</div>
                    <div v-if="item.labels[0] === 'Ingredients'" style="text-align: left;margin-left: 20px;padding-top: 10px;width: 100%"><span style="color: #488cd1">选购技巧：</span>{{item.properties.选购技巧}}</div>
                  </el-collapse-item>
                </el-collapse>
              </el-scrollbar>
            </div>
          </div>
          <div class="preferenceBox" v-if="pfrRefresh">
            <p style="font-size: 18px;color: #303133;text-align: left;margin-left: 20px;">偏好设置</p>
            <el-button type="primary" round icon="el-icon-check" style="float: right;margin-top: -40px;margin-right: 20px" @click="savePreference" v-if="unsaved">保存设置</el-button>
            <div class="setBox">
              <p style="font-size: 15px;color: #303133;text-align: left;margin-left: 20px;">我的口味</p>
              <div class="tagBox">
                <el-scrollbar style="height: 100%;" class="el-scrollbar">
                  <el-tag
                    v-for="tag in tasteTags"
                    :key="tag"
                    closable
                    style="float:left;"
                    @close="handleClose(tag)"
                  >
                    {{tag}}
                  </el-tag>
                  <el-autocomplete
                    class="input-new-tag"
                    v-if="inputVisible"
                    v-model="inputValue"
                    ref="saveTagInput"
                    size="small"
                    @keyup.enter.native="handleInputConfirm"
                    :fetch-suggestions="querySearchAsync"
                    placeholder="请输入内容"
                    @select="handleSelect"
                  ></el-autocomplete>
<!--                  <el-input-->
<!--                    class="input-new-tag"-->
<!--                    v-if="inputVisible"-->
<!--                    v-model="inputValue"-->
<!--                    ref="saveTagInput"-->
<!--                    size="small"-->
<!--                    @keyup.enter.native="handleInputConfirm"-->
<!--                    @blur="handleInputConfirm"-->
<!--                  >-->
<!--                  </el-input>-->
                  <el-button v-else class="button-new-tag" size="small" @click="showInput" style="float:left;">+</el-button>
                  <el-button v-if="inputVisible" icon="el-icon-check" circle type="success" size="small" style="float: left" @click="handleInputConfirm"></el-button>
                </el-scrollbar>
                </div>
            </div>
            <div class="setBox">
              <p style="font-size: 15px;color: #303133;text-align: left;margin-left: 20px;margin-top: -5px;"><br/>烹饪工艺</p>
              <div class="tagBox">
                <el-scrollbar style="height: 100%;" class="el-scrollbar">
                  <el-tag
                    v-for="tag in techTags"
                    :key="tag"
                    closable
                    style="float:left;"
                    @close="handleClose2(tag)"
                  >
                    {{tag}}
                  </el-tag>
                  <el-autocomplete
                    class="input-new-tag"
                    v-if="inputVisible2"
                    v-model="inputValue2"
                    ref="saveTagInput"
                    size="small"
                    @keyup.enter.native="handleInputConfirm2"
                    :fetch-suggestions="querySearchAsync2"
                    placeholder="请输入内容"
                    @select="handleSelect2"
                  ></el-autocomplete>
                  <el-button v-if="inputVisible2" icon="el-icon-check" circle type="success" size="small" style="float: left" @click="handleInputConfirm2"></el-button>
                  <el-button v-else class="button-new-tag" size="small" @click="showInput2" style="float:left;">+</el-button>
                </el-scrollbar>
              </div>
            </div>
            <div class="setBox">
              <p style="font-size: 15px;color: #303133;text-align: left;margin-left: 20px;margin-top: -10px">菜肴类型</p>
              <div class="tagBox">
                <el-scrollbar style="height: 100%;" class="el-scrollbar">
                  <el-tag
                    v-for="tag in typeTags"
                    :key="tag"
                    closable
                    style="float:left;"
                    @close="handleClose3(tag)"
                  >
                    {{tag}}
                  </el-tag>
                  <el-autocomplete
                    class="input-new-tag"
                    v-if="inputVisible3"
                    v-model="inputValue3"
                    ref="saveTagInput"
                    size="small"
                    @keyup.enter.native="handleInputConfirm3"
                    :fetch-suggestions="querySearchAsync3"
                    placeholder="请输入内容"
                    @select="handleSelect"
                  ></el-autocomplete>
                  <el-button v-if="inputVisible3" icon="el-icon-check" circle type="success" size="small" style="float: left" @click="handleInputConfirm3"></el-button>
                  <el-button v-else class="button-new-tag" size="small" @click="showInput3" style="float:left;">+</el-button>
                </el-scrollbar>
              </div>
            </div>
          </div>
        </div>
      </el-main>
    </el-container>

  </div>
</template>

<script>
import backgroundCanvas from '../components/backgroundCanvas'
import editPasswordDialog from '../components/editPasswordDialog'
import avatar from '../components/avatar'
import {mapActions, mapGetters, mapMutations} from 'vuex'
import Myheader from '../components/myheader'
export default {
  name: 'userpage',
  components: {Myheader, backgroundCanvas, editPasswordDialog, avatar},
  data () {
    return {
      activeNO: '1',
      selectNO: '1',
      userName: '',
      userId: '',
      userPassword: '',
      userPhoneNumber: '',
      editing: false,
      inputVisible: false,
      inputVisible2: false,
      inputVisible3: false,
      unsaved: false,
      pfrRefresh: true,
      inputValue: '',
      inputValue2: '',
      inputValue3: '',
      // 上传地址
      UploadUrl: 'http://150.158.23.207:8080/goooocoin/user/modifyAvatar',
      // 上传时，附带的参数
      UploadData: {
        userId: -1
      },
      imgPath: JSON.parse(window.localStorage.getItem('verificationRes')).avatarAddress, // 裁剪好的头像
      form: {
        name: ''
      },
      customStyle:
        'background: #f7f7f7;border-radius: 4px;margin-bottom: 12px;border: 0;text-align:left;text-size:35px',
      circleUrl: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      // circleUrl: 'https://gitee.com/MochiQ/req_imgs/raw/master/image-20210618214537701.png',
      circleUrl4recipe: 'https://gitee.com/MochiQ/req_imgs/raw/master/image-20210618215803360.png',
      circleUrl4ingredients: 'https://gitee.com/MochiQ/req_imgs/raw/master/image-20210618215813118.png',
      tasteTags: [],
      techTags: [],
      typeTags: [],
      defaultAvatar: '../assets/COINlogo.jpg',
      activeIndex: '2',
      mhrefresh: true,
      formrefresh: true
    }
  },
  computed: {
    ...mapGetters([
      'verificationRes',
      'currentUserID',
      'editpswdDialogVisible',
      'flavorList',
      'typeList',
      'methodList',
      'ingreTypeList',
      'collectionList'
    ])
  },
  async mounted () {
    if (this.imgPath === '') this.imgPath = this.circleUrl
    var userID = window.localStorage.getItem('userID')
    this.set_currentUserID(userID)
    var verificationRes = window.localStorage.getItem('verificationRes')
    this.set_verificationRes(JSON.parse(verificationRes))
    if (this.currentUserID === -1 || this.currentUserID === null) this.$router.push('/login')
    else {
      this.UploadData.userId = this.verificationRes.id
      this.tasteTags = this.verificationRes.flavor
      this.techTags = this.verificationRes.method
      this.typeTags = this.verificationRes.type
      this.userPhoneNumber = this.verificationRes.phoneNumber
      this.userName = this.verificationRes.username
      this.userId = this.verificationRes.id
      this.userPassword = this.verificationRes.password
      await this.getDistinctPropsValue({label: 'Recipe', propName: '口味'})
      await this.getDistinctPropsValue({label: 'Recipe', propName: '工艺'})
      await this.getDistinctPropsValue({label: 'Recipe', propName: '类型'})
      console.log('****@@@@@@@@@**')
      console.log(this.flavorList)
      console.log(this.imgPath)
      await this.getCollection(this.currentUserID)
      console.log(this.collectionList)
    }
    window.sessionStorage.setItem('currentCenter', -1)
    window.sessionStorage.setItem('entityStack', JSON.stringify([]))
    window.sessionStorage.setItem('entityInfoStack', JSON.stringify([]))
    console.log('~~~~~~~~`')
    console.log(window.sessionStorage.getItem('entityInfoStack'))
  },
  methods: {
    ...mapMutations([
      'set_editpswdDialogVisible',
      'set_currentUserID',
      'set_verificationRes',
      'set_currentEntityInfo',
      'set_currentCenter',
      'set_currentCenterInfo'
    ]),
    ...mapActions([
      'verifyInformation',
      'modifyFavor',
      'getDistinctPropsValue',
      'getCollection',
      'uncollect',
      'getRelativeRelation',
      'modifyUserInfo'
    ]),
    // handleSelect (key) {
    //   this.selectNO = key
    // },
    async enterGraph (item) {
      console.log('enterGraph')
      console.log(item)
      await this.set_currentEntityInfo(item)
      await this.set_currentCenter(item.id)
      await this.set_currentCenterInfo(item)
      await this.getRelativeRelation(item.id)
      window.sessionStorage.setItem('currentCenter', item.id)
      window.sessionStorage.setItem('currentCenterInfo', item)
      this.$router.push('/home/detailPage')
    },
    myheadrefresh () {
      this.mhrefresh = false
      this.$nextTick(() => {
        this.mhrefresh = true
      })
    },
    handleCommand (command) {
      window.localStorage.setItem('userID', -1)
      window.localStorage.setItem('verificationRes', null)
      window.sessionStorage.setItem('currentCenter', null)
      this.$router.push('/login')
    },
    async unCollectEntity (item) {
      console.log(item)
      console.log({id: this.currentUserID, entity: item.id})
      await this.uncollect({id: this.currentUserID, entity: item.id})
    },
    goBack () {
      console.log('go back')
      this.editing = false
    },
    startEdit () {
      this.editing = true
    },
    async finishEdit () {
      console.log('............')
      console.log({
        id: this.currentUserID,
        username: this.userName,
        phoneNumber: this.userPhoneNumber
      })
      var res = await this.modifyUserInfo({
        id: this.currentUserID,
        username: this.userName,
        phoneNumber: this.userPhoneNumber
      })
      console.log(res)
      if (res.success) {
        this.$message.success(res.message)
        // alert('修改成功')
      } else this.$message.error(res.message)
      this.editing = false
      this.formrefresh = false
      this.$nextTick(() => {
        this.formrefresh = true
      })
    },
    showInput () {
      this.inputVisible = true
      this.$nextTick(_ => {
        document.getElementById('select1').focus()
        // console.log(document.getElementsByClassName('input-new-tag'))
        // this.$refs.select1.focus()
        console.log('*************')
        console.log(document.getElementById('select1'))
        this.$nextTick(_ => {
          this.$refs.saveTagInput.$refs.input.focus()
        })
      })
    },
    showInput2 () {
      this.inputVisible2 = true
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus()
      })
    },
    showInput3 () {
      this.inputVisible3 = true
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus()
      })
    },
    handleClose (tag) {
      this.unsaved = true
      this.tasteTags.splice(this.tasteTags.indexOf(tag), 1)
    },
    handleClose2 (tag) {
      this.unsaved = true
      this.techTags.splice(this.techTags.indexOf(tag), 1)
    },
    handleClose3 (tag) {
      this.unsaved = true
      this.typeTags.splice(this.typeTags.indexOf(tag), 1)
    },
    handleInputConfirm () {
      let inputValue = this.inputValue
      if (inputValue) {
        if (!this.listInclude(this.flavorList, inputValue)) this.$message.warning('没有这种口味哦~')
        else if (!this.tasteTags.includes(inputValue)) this.tasteTags.push(inputValue)
        else {
          this.$message.warning('已经添加过这个标签了哦~')
        }
      }
      this.inputVisible = false
      this.inputValue = ''
      this.unsaved = true
    },
    handleInputConfirm2 () {
      let inputValue = this.inputValue2
      console.log('===========****===========')
      console.log(this.methodList)
      if (inputValue) {
        if (!this.listInclude(this.methodList, inputValue)) this.$message.warning('没有这种烹饪工艺哦~')
        else if (!this.techTags.includes(inputValue)) this.techTags.push(inputValue)
        else {
          this.$message.warning('已经添加过这个标签了哦~')
        }
      }
      this.inputVisible2 = false
      this.inputValue2 = ''
      this.unsaved = true
    },
    handleInputConfirm3 () {
      let inputValue = this.inputValue3
      if (inputValue) {
        if (!this.listInclude(this.typeList, inputValue)) this.$message.warning('没有这种菜肴类型哦~')
        else if (!this.typeTags.includes(inputValue)) this.typeTags.push(inputValue)
        else {
          this.$message.warning('已经添加过这个标签了哦~')
        }
      }
      this.inputVisible3 = false
      this.inputValue3 = ''
      this.unsaved = true
    },
    listInclude (list, tag) {
      for (var i = 0; i < list.length; i++) {
        if (list[i].name === tag) return true
      }
      return false
    },
    savePreference () {
      console.log(this.tasteTags)
      console.log(this.typeTags)
      console.log(this.techTags)
      console.log(this.verificationRes)
      window.localStorage.setItem('verificationRes', JSON.stringify(this.verificationRes))
      var params = {id: this.currentUserID, flavor: this.tasteTags, method: this.techTags, type: this.typeTags}
      this.modifyFavor(params)
      this.$message({
        message: '更改偏好设置成功！',
        type: 'success'
      })
      this.pfrRefresh = false
      this.$nextTick(() => {
        this.pfrRefresh = true
        this.unsaved = false
      })
    },
    querySearchAsync (queryString, cb) {
      var tasteList = this.listProcessor(this.flavorList)
      var results = queryString ? tasteList.filter(this.createStateFilter(queryString)) : tasteList

      clearTimeout(this.timeout)
      this.timeout = setTimeout(() => {
        cb(results)
      }, 0 * Math.random())
    },
    querySearchAsync2 (queryString, cb) {
      var techList = this.listProcessor(this.methodList)
      var results = queryString ? techList.filter(this.createStateFilter(queryString)) : techList

      clearTimeout(this.timeout)
      this.timeout = setTimeout(() => {
        cb(results)
      }, 0 * Math.random())
    },
    querySearchAsync3 (queryString, cb) {
      var typeList = this.listProcessor(this.typeList)
      var results = queryString ? typeList.filter(this.createStateFilter(queryString)) : typeList

      clearTimeout(this.timeout)
      this.timeout = setTimeout(() => {
        cb(results)
      }, 0 * Math.random())
    },
    createStateFilter (queryString) {
      return (state) => {
        return (state.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0)
      }
    },
    editPassword () {
      this.set_editpswdDialogVisible(true)
    },
    listProcessor (list) {
      var res = []
      for (var i = 0; i < list.length; i++) {
        res.push({value: list[i].name})
      }
      return res
    },
    handleSelect (key, keyPath) {
      if (key === '1') this.$router.push('/home')
      else if (key === '2') {
        if (this.currentUserID === -1) this.$router.push('/')
        else this.$router.push('/userpage')
      }
    },
    getAvatarUrl (path) {
      this.imgPath = path
      console.log('qqqqqqq')
    },
    uploadVfRes () {
      var vfres = this.verificationRes
      vfres.avatarAddress = this.imgPath
      this.set_verificationRes(vfres)
      window.localStorage.setItem('verificationRes', JSON.stringify(vfres))
    }
  }
}
</script>

<style scoped>
  p{
    all: revert;
  }
  .all{
    position: absolute;
    width: 100%;
    height: 100%;
    background: #f7f2e7;
  }
  .setBox{
    float: top;
  }
  .sidemenu{
    height: 90.6%;
  }
  .main{
    background: rgba(193, 215, 232, 0.0);
    width: 82%;
    height: 110%;
    float: left;
    margin-left: 9%;
    margin-top: 15px;
    z-index: 2;
  }
  .aside{
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
    margin-left: 14%;
    margin-top: 50px;
  }
  .pheader{
    margin-top: 20px;
    margin-left: 20px;
  }
  .infoBox{
    margin: 20px 0 0 40px;
    width: 30%;
    background-color: rgba(211,227,240, 0.7);
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    float: left;
    border-radius: 15px;
  }
  .formtext{
    text-align: left;
    float:left;
  }
  .block{
    float: left;
    margin-left: 30px;
    margin-top: 32px;
  }
  .form{
    width: 55%;
    margin-left: 40%;
    margin-top: -50%;
    float: left;
  }
  .collectBox{
    width: 60%;
    float: left;
    margin-left: 40px;
    background-color: rgba(249,208,216, 0.4);
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    margin-top: 20px;
    border-radius: 15px;
  }
  .collaspeBox {
    width: 97%;
    height: 615px;
    margin: 30px 0 0 10px;
    /*overflow: scroll;*/
    /*overflow-x: hidden;*/
  }
  .eci >>>.el-collapse-item__header{
    background-color: rgba(249,208,216, 0.5);
  }
  .eci >>>.el-collapse-item__content{
    background-color: rgba(249,208,216, 0.4);
  }
  .eci >>>.el-icon-arrow-right{
    margin-right: 25px;
  }
  .preferenceBox{
    margin: -370px 0 0 40px;
    width: 30%;
    height: 405px;
    /*background-color: rgba(193, 215, 232, 0.7);*/
    background-color: rgba(233,232,247, 1);
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    float: left;
    border-radius: 15px;
  }
  .tagBox{
    margin-left: 20px;
    height: 70px;
    padding-bottom: 10px;
    width: 90%;
    /*overflow: scroll;*/
    /*overflow-x: hidden;*/
  }
  .el-scrollbar >>>.el-scrollbar__wrap {
    overflow-x: hidden;
  }
  .input-new-tag {
    float: left
  }
</style>
