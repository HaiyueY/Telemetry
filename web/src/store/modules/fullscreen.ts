import { defineStore } from "pinia"
import { ref } from "vue"
import screenfull from "screenfull"
import { ElMessage } from "element-plus"

export const useFullscreenStore = defineStore("fullscreen", () => {
  const isFullscreen = ref<boolean>(false)

  // 初始化时检查全屏状态
  if (screenfull.isEnabled) {
    isFullscreen.value = screenfull.isFullscreen
    screenfull.on("change", () => {
      isFullscreen.value = screenfull.isFullscreen
    })
  }

  // 切换全屏状态
  const toggleFullscreen = (element: string = "html") => {
    const dom = document.querySelector(element) || undefined
    if (screenfull.isEnabled) {
      screenfull.toggle(dom)
    } else {
      ElMessage.warning("您的浏览器无法工作")
    }
  }

  return { isFullscreen, toggleFullscreen }
})
