<script lang="ts" setup>
import { useDevice } from "@/hooks/useDevice"
import { useTheme } from "@/hooks/useTheme"

interface Props {
  total: number
}

/** 主题 */
const { activeThemeName } = useTheme()

const props = defineProps<Props>()

const { isMobile } = useDevice()
</script>

<template>
  <div class="search-footer">
    <template v-if="!isMobile">
      <span class="search-footer-item">
        <kbd :class="activeThemeName == 'normal' ? 'DocSearch-Commands-Key-light' : 'DocSearch-Commands-Key'">
          <SvgIcon name="keyboard-enter" />
        </kbd>
        <span>确认</span>
      </span>
      <span class="search-footer-item">
        <kbd :class="activeThemeName == 'normal' ? 'DocSearch-Commands-Key-light' : 'DocSearch-Commands-Key'">
          <SvgIcon name="keyboard-up" />
        </kbd>
        <kbd :class="activeThemeName == 'normal' ? 'DocSearch-Commands-Key-light' : 'DocSearch-Commands-Key'">
          <SvgIcon name="keyboard-down" />
        </kbd>
        <span>切换</span>
      </span>
      <span class="search-footer-item">
        <kbd :class="activeThemeName == 'normal' ? 'DocSearch-Commands-Key-light' : 'DocSearch-Commands-Key'">
          <SvgIcon name="keyboard-esc" />
        </kbd>
        <span>关闭</span>
      </span>
    </template>
    <span class="search-footer-total">共 {{ props.total }} 项</span>
  </div>
</template>

<style lang="scss" scoped>
.search-footer {
  display: flex;
  color: var(--el-text-color-secondary);
  font-size: 14px;
  &-item {
    display: flex;
    align-items: center;
    margin-right: 12px;
  }
  &-total {
    margin: 0 0 0 auto;
  }
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
