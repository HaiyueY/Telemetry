<script lang="ts" setup>
import { ref, computed, watch } from "vue"
import { ElMessage } from "element-plus"
import { Bell } from "@element-plus/icons-vue"
import NotifyList from "./NotifyList.vue"
import { type ListItem, notifyData, messageData, todoData } from "./data"
import { useSocket } from "@/store/socket"
import { ElNotification } from "element-plus"
import { useUserStore } from "@/store/modules/user"

const { username } = useUserStore()

const socket = useSocket()

const topic = "notify"

// 初始化ws
socket.wsInit("ws://127.0.0.1:9095/websocket/" + username + "/" + topic, topic)

type TabName = "通知" | "消息" | "待办"

interface DataItem {
  name: TabName
  type: "primary" | "success" | "warning" | "danger" | "info"
  list: ListItem[]
}

/** 角标当前值 */
const badgeValue = computed(() => {
  return data.value.reduce((sum, item) => sum + item.list.length, 0)
})
/** 角标最大值 */
const badgeMax = 99
/** 面板宽度 */
const popoverWidth = 350
/** 当前 Tab */
const activeName = ref<TabName>("通知")
/** 所有数据 */
const data = ref<DataItem[]>([
  // 通知数据
  {
    name: "通知",
    type: "primary",
    list: notifyData
  },
  // 消息数据
  {
    name: "消息",
    type: "danger",
    list: messageData
  },
  // 待办数据
  {
    name: "待办",
    type: "warning",
    list: todoData
  }
])

// const handleHistory = () => {
//   data.value[0].list.push({
//     avatar: "https://gw.alipayobjects.com/zos/rmsportal/OKJXDXrmkNshAMvwtvhu.png",
//     title: "成功",
//     datetime: new Date().toLocaleString(),
//     description: "卫星运控原型系统用户登录成功"
//   })
//   ElMessage.success(`跳转到${activeName.value}历史页面`)
// }

const notification = (title: string, message: string, type: string) => {
  ElNotification({
    title: title,
    message: message,
    type: type,
    position: "bottom-right",
    duration: 0.5 * 60 * 1000
  })
}

// 获取服务器发给客户端的数据
watch(
  () => socket.socketData,
  (msg) => {
    if (msg !== null && msg !== "null") {
      // 推送至通知列表
      if (data.value[0].list.length == 0) {
        data.value[0].list.push(JSON.parse(msg))
      } else {
        data.value[0].list.unshift(JSON.parse(msg))
      }

      // 并弹出提示
      const note = JSON.parse(msg)

      if (note.status === "primary") {
        notification(note.title, note.description, "success")
      } else if (note.status === "danger") {
        notification(note.title, note.description, "error")
      } else {
        // notification("实时推送", "收到遥测解析数据", "normal")
      }
    }
  },
  {
    immediate: true,
    deep: true
  }
)
</script>

<template>
  <div class="notify">
    <el-popover placement="bottom" :width="popoverWidth" trigger="click">
      <template #reference>
        <el-badge :value="badgeValue" :max="badgeMax" :hidden="badgeValue === 0">
          <el-tooltip effect="dark" content="后台推送消息" placement="bottom">
            <el-icon :size="20">
              <Bell />
            </el-icon>
          </el-tooltip>
        </el-badge>
      </template>
      <template #default>
        <el-tabs v-model="activeName" class="demo-tabs" stretch>
          <el-tab-pane v-for="(item, index) in data" :name="item.name" :key="index">
            <template #label>
              {{ item.name }}
              <el-badge :value="item.list.length" :max="badgeMax" :type="item.type" />
            </template>
            <el-scrollbar height="400px">
              <NotifyList :list="item.list" />
            </el-scrollbar>
          </el-tab-pane>
        </el-tabs>
        <!-- <div class="notify-history">
          <el-button link @click="handleHistory">查看{{ activeName }}历史</el-button>
        </div> -->
      </template>
    </el-popover>
  </div>
</template>

<style lang="scss" scoped>
.notify {
  margin-right: 10px;
  color: var(--el-text-color-regular);
}
.notify-history {
  text-align: center;
  padding-top: 12px;
  border-top: 1px solid var(--el-border-color);
}
</style>
