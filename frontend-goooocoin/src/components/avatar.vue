<template>
  <div>
    <el-button @click="isShowDialog = true" style="margin-top: 10px;margin-left: 16px" size="small" round>编辑头像</el-button>
    <el-dialog title="设置头像" :visible.sync="isShowDialog" width="1000px" @close="closeDialog">
      <input type="file" style="display:none" id="input-file" accept="image/png, image/jpeg, image/jpg, image/bmp" @change="uploadImg($event)" />
      <el-row>
        <el-col :span="16" style="height: 400px;">
          <div class="container-left">
            <div v-if="isShowFileButton" class="btn-box">
              <el-popover width="200" trigger="hover" :content="'图片不可大于 ' + imageMaxSize + ' MB'">
                <div class="choose-file-btn" @click="changeFile" slot="reference">选 择 图 片</div>
              </el-popover>
            </div>
            <div v-else class="cropper-box">
              <vueCropper
                style="width:550px;height:400px;margin-left: 40px"
                ref="cropper"
                :img="options.img"
                :auto-crop="options.autoCrop"
                :fixed-box="options.fixedBox"
                :can-move-box="options.canMoveBox"
                :auto-crop-width="options.autoCropWidth"
                :auto-crop-height="options.autoCropHeight"
                :center-box="options.centerBox"
                @realTime="realTime"
              >
              </vueCropper>
            </div>
          </div>
        </el-col>
        <el-col :span="8" style="height: 400px;">
          <h2 style="text-align: center; line-height: 50px">头像预览</h2>
          <div class="show-preview">
            <div style="height: 200px; width: 200px" class="preview">
              <img :src="previews.url" :style="previews.img" alt="" />
            </div>
          </div>
          <div v-show="!isShowFileButton" class="preview-footer">
            <el-button @click="changeFile">更 换 图 片</el-button>
          </div>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button @click="isShowDialog = false">取 消</el-button>
        <el-button type="primary" @click="commitCut" v-loading.fullscreen.lock="fullscreenLoading" :disabled="Object.keys(previews).length === 0">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'avatar',
  props: [
    'mhrefresh',
    'UploadUrl', // 上传地址
    'UploadData' // 上传文件的附带参数数据
  ],
  data () {
    return {
      // isShowDialog: false, // 弹窗状态
      isShowFileButton: true, // 选择文件的按钮状态
      options: { // vue-cropper的参数
        img: '', // 裁剪的图片blob地址
        autoCrop: true, // 默认生成截图框
        fixedBox: true, // 固定截图框大小
        canMoveBox: false, // 截图框不能拖动
        autoCropWidth: 200, // 截图框宽度
        autoCropHeight: 200, // 截图框高度
        centerBox: true // 截图框被限制在图片里面
      },
      previews: {}, // 预览图对象
      fullscreenLoading: false, // 全屏加载遮罩状态
      imageUrl: '', // 上传后的图片地址
      isShowDialog: false,
      // 图片的大小限制，默认 10 MB
      imageMaxSize: 10
    }
  },
  methods: {
    // 关闭截图对话框弹窗
    closeDialog () {
      this.isShowDialog = false
      this.previews = {}
      this.isShowFileButton = true
    },
    // 点击选择图片或更换图片按钮，变更裁剪图片
    changeFile () {
      document.getElementById('input-file').click()
    },
    // 变更裁剪图片
    uploadImg (e) {
      let file = e.target.files[0]
      // 判断文件格式是否符合要求规范
      if (!/\.(jpg|jpeg|png|bmp|JPG|PNG)$/.test(e.target.value)) {
        this.$message.error('图片类型必须是,jpeg,jpg,png,bmp中的一种')
        return false
      }
      // 判断文件是否超出大小限制
      if (file.size / 1024 / 1024 > this.imageMaxSize) {
        this.$message.error('图片不可大于 ' + this.imageMaxSize + ' MB')
        return false
      }
      // fileReader 接口，用于异步读取文件数据
      let reader = new FileReader()
      reader.readAsDataURL(file) // 重要 以dataURL形式读取文件
      reader.onload = e => {
        // this.options.img = window.URL.createObjectURL(new Blob([e.target.result])) 转化为blob格式
        this.options.img = e.target.result
        // 转化为base64
        // reader.readAsDataURL(file)
        // 转化为blob
      }
      this.isShowFileButton = false
    },
    // 同步实时预览的数据
    realTime (data) {
      this.previews = data
    },
    // 确认截图,上传到后台
    commitCut () {
      this.fullscreenLoading = true
      let formData = new FormData()
      this.$refs.cropper.getCropBlob(res => {
        // res是裁剪后图片的blob对象,遍历组件上传数据的对象 => 添加到formData对象中
        formData.append('file', res)
        for (let key in this.UploadData) {
          formData.append(key, this.UploadData[key])
        }
        this.$axios.post(this.UploadUrl, formData).then(async res => {
          res = res.data
          console.log(res)
          if (res.success) {
            console.log(res.message)
            this.imageUrl = res.content.path
            this.closeDialog()
            this.fullscreenLoading = false
            this.SetUrl()
            await this.$emit('uploadVfRes')
            this.$emit('myheadrefresh')
            console.log('-+-+-+-+-+-+')
            console.log(window.localStorage.getItem('verificationRes'))
            var info = JSON.parse(window.localStorage.getItem('verificationRes'))
            console.log(info)
            info.avatarAddress = res.content.path
            console.log(info)
            window.localStorage.setItem('verificationRes', JSON.stringify(info))
            console.log(window.localStorage.getItem('verificationRes'))
            console.log('-+-+-+-+-+-+')
          } else {
            console.log('上传失败', 'color:#a00')
            this.fullscreenLoading = false
          }
        })
      })
    },
    // 将裁剪后的图片传出的父组件
    SetUrl () {
      this.$emit('getAvatarUrl', this.imageUrl)
    }
  }
}
</script>

<style lang="scss" scoped>
  .container-left {
    width: 100%;
    height: 100%;
    .btn-box {
      width: 100%;
      height: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
    }
    .cropper-box {
      width: 100%;
      height: 100%;
    }
  }

  .choose-file-btn {
    width: 160px;
    height: 80px;
    font-size: 22px;
    font-weight: 600;
    line-height: 80px;
    text-align: center;
    color: #333;
    border: #aaa 1px solid;
    cursor: pointer;
    transition: 0.25s;
    box-shadow: 5px 5px 10px rgba(0, 0, 0, 0.3);
  }
  .choose-file-btn:hover {
    color: #fff;
    background: #00d7c6;
    border: #00d7c6 1px solid;
    transform: scale(1.15);
  }

  .show-preview {
    display: flex;
    justify-content: center;
  }

  .preview {
    border-radius: 50%;
    overflow: hidden;
    border: 1px solid #ccc;
    background: #ccc;
  }

  .preview-footer {
    width: 100%;
    display: flex;
    justify-content: center;
    padding-top: 15px;
  }
</style>
