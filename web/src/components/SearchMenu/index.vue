<script lang="ts" setup>
import { ref, watch, onMounted, onUnmounted } from "vue"
import SearchModal from "./SearchModal.vue"
import { useTheme } from "@/hooks/useTheme"
import { useMagicKeys } from "@vueuse/core"

/** 控制 modal 显隐 */
const modalVisible = ref<boolean>(false)
/** 打开 modal */
const handleOpen = () => {
  modalVisible.value = true
}
/** 主题 */
const { activeThemeName } = useTheme()
const keys = useMagicKeys()
const CtrlK = keys["Ctrl+K"]
const CommandK = keys["Cmd+K"]

watch(CtrlK, () => {
  handleOpen()
})

watch(CommandK, () => {
  handleOpen()
})

// 监听 Ctrl+K
const handleKeydown = (event: any) => {
  if (event.ctrlKey && event.key === "k") {
    event.preventDefault() // 阻止默认行为
  }
}

onMounted(() => {
  window.addEventListener("keydown", handleKeydown)
})

onUnmounted(() => {
  window.removeEventListener("keydown", handleKeydown)
})
</script>

<template>
  <div>
    <!-- <SvgIcon name="search" @click="handleOpen" /> -->
    <el-button class="search-button" round @click="handleOpen">
      <SvgIcon name="search" />
      <span style="font-family: Arial, Helvetica, sans-serif; padding: 0 12px 0 6px">搜索</span>

      <kbd :class="activeThemeName == 'normal' ? 'DocSearch-Commands-Key-light' : 'DocSearch-Commands-Key'"
        ><SvgIcon name="ctrl" />
      </kbd>
      <span
        ><kbd :class="activeThemeName == 'normal' ? 'DocSearch-Commands-Key-light' : 'DocSearch-Commands-Key'"
          >K</kbd
        ></span
      >
    </el-button>
    <SearchModal v-model="modalVisible" />
  </div>
</template>

<style lang="scss" scoped>
.svg-icon {
  font-size: 20px;
  &:focus {
    outline: none;
  }
}
.search-button {
  border: 0;
  background-color: var(--el-bg-color);
}
.el-button:focus,
.el-button:hover,
.el-button:active {
  background: var(--el-fill-color);
  box-shadow: inset 0 0 0 2px var(--el-color-primary);
  color: var(--el-text-color-regular);
  outline: none;
}
.DocSearch-Commands-Key-light {
  font-family: var(--code-font-family);
  align-items: center;
  background: var(--el-fill-color);
  border-radius: 2px;
  box-shadow:
    inset 0 -2px 0 0 #cdcde6,
    inset 0 0 1px 1px #fff,
    0 1px 2px 1px rgba(30, 35, 90, 0.4);
  display: flex;
  height: 18px;
  justify-content: center;
  margin-right: 0.4em;
  padding: 0 0 1px;
  color: var(--el-text-color-regular);
  border: 0;
  width: 20px;
  .svg-icon {
    padding: 2px;
    font-size: 20px;
  }
}
.DocSearch-Commands-Key {
  font-family: var(--code-font-family);
  align-items: center;
  background: var(--el-fill-color);
  border-radius: 2px;
  display: flex;
  height: 18px;
  justify-content: center;
  margin-right: 0.4em;
  padding: 0 0 1px;
  color: var(--el-text-color-regular);
  border: 0;
  width: 20px;
  .svg-icon {
    padding: 2px;
    font-size: 20px;
  }
}
</style>
