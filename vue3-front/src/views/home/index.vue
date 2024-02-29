<template>
  <el-card>
    <div class="box">
      <img :src="formatImage(userStore.avatar)" alt="" class="avatar" />
      <div class="bottom">
        <h3 class="title">{{ getTime() }}好，{{ userStore.realName }}</h3>
        <p class="subtitle">框架平台</p>
      </div>
    </div>
  </el-card>
  <div class="bottoms">
    <svg-icon name="welcome" width="600px" height="300px"></svg-icon>
  </div>
</template>

<script setup lang="ts">
import { getTime } from '@/utils/time'
import { onMounted } from 'vue'
//引入用户相关的小仓库
import useUserStore from '@/store/modules/user'
import { formatImage } from '@/utils/common'
let userStore = useUserStore()
// ***** 以上为通用部分 *****

onMounted(() => {})
</script>

<style scoped lang="scss">
.box {
  display: flex;

  .avatar {
    width: 100px;
    height: 100px;
    border-radius: 50%;
  }

  .bottom {
    margin-left: 20px;

    .title {
      font-size: 30px;
      font-weight: 900;
      margin-bottom: 30px;
    }

    .subtitle {
      font-style: italic;
      color: skyblue;
    }
  }
}
.bottoms {
  margin-top: 60px;
  display: flex;
  justify-content: center;
}
</style>
