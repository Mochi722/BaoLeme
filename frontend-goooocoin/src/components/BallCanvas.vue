<template>
  <div style="margin-top: 90px">
    <div id="tagbox" v-show="!len">
      <a class="link" v-for="item in resList"
         :key="item.id"
         :value="item.properties.name"
         type="text" href="javascript:void(0)"
         @click="handleClick(item)">
        {{item.properties.name.slice(0,7)}}{{item.properties.name.length > 7 ? '...':''}}
      </a>
    </div>
    <div v-if="len && sequance===2" style="padding-top: 215px">你的收藏夹还没有任何东西哦~</div>
    <div v-if="illegalSearch && sequance===3" style="padding-top: 215px">什么都没有搜到哦T.T</div>
  </div>
</template>

<script src="../utils/script.js">
</script>

<style scoped>
  #tagbox{
    position:relative;
    margin:0px auto 0px;
    width:600px;
    height:500px;
    background:rgba(255,255,255,0);
  }
  #tagbox a{
    position:absolute;
    padding:3px 6px;
    font-family:Microsoft YaHei;
    color:#ac6e87;
    TOP:0px;
    font-weight:bold;
    text-decoration:none;
    left:0px
  }
  #tagbox a:hover{
    /*border:#eee 1px solid;*/
    color: #2c3e50;
    /*background:#C1D7E8;*/
  }
  #tagbox .blue{color:blue}
  #tagbox .red{color:red}
  #tagbox .yellow{color:yellow}
</style>
