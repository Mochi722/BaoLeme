<template>
  <div class="all">
    <div class="header">
      <div class="headerleft">吃了么</div>
      <div class="headerright">导航栏</div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'adminpage'
}
</script>

<style scoped>
  .all{
    position: absolute;
    width: 100%;
    height: 100%;
    background: #C1D7E8;
    margin-top: -60px;
    margin-left: -8px;
  }
  .search{
    width: 600px;
    position: relative;
    margin: 80px auto 0px;
    height: 80px;
    background: #D3D3D3;
  }
  .header{
    width: 100%;
    height: 70px;
    background: #2c3e50;
    background-image: linear-gradient(to right, #7EA9D3, #FFC0CB);
  }
  .headerleft{
    width: 30%;
    height: 70px;
    text-align: center;
    float: left;
  }
  .headerright{
    width: 70%;
    height: 70px;
    text-align: center;
    float: left;
  }
</style>
