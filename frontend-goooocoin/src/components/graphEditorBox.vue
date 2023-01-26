<template>
  <div class="box">
    <div class="boxleft">
      <transition class="inner-container" name="slide" mode="out-in">
        <p class="text2" style="text-align: left;margin-left: 10px; margin-top: 10px;color: #6c3e48;font-size: 16px" :key="newsText.id">{{newsText.val}}</p>
      </transition>
    </div>
    <div class="boxright">
      <el-tooltip class="item" effect="dark" content="还原" placement="top-start">
        <el-button type="danger" icon="el-icon-refresh-right" circle style="float:right; margin-right: 14px;" @click="zoominithere"></el-button>
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="缩小" placement="top-start">
        <el-button type="danger" icon="el-icon-minus" circle style="float:right; margin-right: 12px;" @click="zoomout"></el-button>
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="放大" placement="top-start">
        <el-button type="danger" icon="el-icon-plus" circle style="float:right; margin-right: 6px;" @click="zoomin"></el-button>
      </el-tooltip>
    </div>
  </div>
</template>

<script>
export default {
  name: 'graphEditorBox',
  data () {
    return {
      news: [
        { id: '1', value: '点击节点，查看详情﻿ε≡٩(๑>₃<)۶' },
        { id: '2', value: '图谱太小看不清？试试滚轮缩放吧٩( \'ω\' )و ' },
        { id: '3', value: '节点跑出框了？试试拖拽画布看到更多内容٩꒰▽ ꒱۶⁼³₌₃' },
        // eslint-disable-next-line standard/object-curly-even-spacing
        { id: '4', value: '双击节点，探索更多(σﾟ∀ﾟ)σ..:*☆'}
      ],
      number: 0
    }
  },
  mounted () {
    this.number = 0
    clearInterval(this.timer)
    this.timer = setInterval(() => {
      if (this.number < 3) {
        this.number += 1
      } else {
        this.number = 0
      }
    }, 4000)
  },
  computed: {
    newsText () {
      return {
        id: this.number,
        val: this.news[this.number].value
      }
    }
  },
  methods: {
    async zoominithere () {
      await this.$emit('kgrefresh')
    },
    zoomin () {
      this.$emit('zoomin')
    },
    zoomout () {
      this.$emit('zoomout')
    }
  }
}
</script>

<style scoped>
.box {
  width: 1000px;
  height: 45px;
  float: left;
  margin-left: 30px;
  margin-top: 15px;
  border-radius: 0 0 10px 0;
  background-image: linear-gradient(to right, #ecbdc1, #efcfb9)
}
.boxleft {
  width: 400px;
  height: 40px;
  float: left;
  overflow-y: hidden;
}
.boxright {
  width: 600px;
  height: 40px;
  margin-top: 2.5px;
  float: left;
}

.scroll-content {
  height: 20px;
  overflow: hidden;
}
.slide-enter-active, .slide-leave-active {
  transition: all 0.5s linear;
}
.slide-leave-to {
  transform: translateY(-20px);
}
.slide-enter {
  transform: translateY(20px);
}
  .inner-container {
    height: 40px;
  }
  el-button {
    width: 30px;
    height: 30px;
  }
</style>
