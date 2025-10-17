<script lang="ts" setup>
import { reactive, ref } from "vue"
import { useRouter } from "vue-router"
import { useUserStore } from "@/store/modules/user"
import { type FormInstance, type FormRules } from "element-plus"
import { User, Lock } from "@element-plus/icons-vue"
// import { getLoginCodeApi } from "@/api/login"
import { type LoginRequestData } from "@/api/login/types/login"
import ThemeSwitch from "@/components/ThemeSwitch/index.vue"
// import Owl from "./components/Owl.vue"
import { useFocus } from "./hooks/useFocus"

const router = useRouter()
const { handleBlur, handleFocus } = useFocus()
const VITE_APP_TITLE = import.meta.env.VITE_APP_TITLE

/** 登录表单元素的引用 */
const loginFormRef = ref<FormInstance | null>(null)

/** 登录按钮 Loading */
const loading = ref(false)
/** 验证码图片 URL */
// const codeUrl = ref("")
/** 登录表单数据 */
const loginFormData: LoginRequestData = reactive({
  username: "admin",
  password: "123456"
  // code: ""
})
/** 登录表单校验规则 */
const loginFormRules: FormRules = {
  username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, max: 16, message: "长度在 6 到 16 个字符", trigger: "blur" }
  ]
  // code: [{ required: true, message: "请输入验证码", trigger: "blur" }]
}
/** 登录逻辑 */
const handleLogin = () => {
  loginFormRef.value?.validate((valid: boolean, fields) => {
    if (valid) {
      loading.value = true
      useUserStore()
        .login(loginFormData)
        .then(() => {
          router.push({ path: "/" })
        })
        .catch(() => {
          createCode()
          loginFormData.password = ""
        })
        .finally(() => {
          loading.value = false
        })
    } else {
      console.error("表单校验不通过", fields)
    }
  })
}
/** 创建验证码 */
const createCode = () => {
  // // 先清空验证码的输入
  // loginFormData.code = ""
  // // 获取验证码
  // codeUrl.value = ""
  // getLoginCodeApi().then((res) => {
  //   codeUrl.value = res.data
  // })
}

/** 初始化验证码 */
createCode()
</script>

<template>
  <div class="login-container">
    <div />
    <img
      src="/img/find-satellite-desktop-1.png"
      alt=""
      style="z-index: 1; position: absolute; left: 0; height: 100%; opacity: 0.8"
    />
    <!-- <img
      src="/img/MAR-5321_footerv2-bg.inline_revised_fill.svg"
      alt=""
      style="z-index: 0; position: absolute; right: 0; bottom: 0; width: 100%"
    /> -->
    <ThemeSwitch class="theme-switch" />
    <!-- <Owl :close-eyes="isFocus" /> -->
    <div class="login-card">
      <!-- <img
        src="/img/find-satellite-desktop-1.png"
        alt=""
        style="z-index: 1; position: relative; right: 0; height: 10vh"
      /> -->
      <div class="title">
        {{ VITE_APP_TITLE }}
        <!-- <img src="@/assets/layouts/logo-text-2.png" /> -->
      </div>
      <div class="content">
        <el-form ref="loginFormRef" :model="loginFormData" :rules="loginFormRules" @keyup.enter="handleLogin">
          <el-form-item prop="username">
            <el-input
              v-model.trim="loginFormData.username"
              placeholder="用户名"
              type="text"
              tabindex="1"
              :prefix-icon="User"
              size="large"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model.trim="loginFormData.password"
              placeholder="密码"
              type="password"
              tabindex="2"
              :prefix-icon="Lock"
              size="large"
              show-password
              @blur="handleBlur"
              @focus="handleFocus"
            />
          </el-form-item>
          <!-- <el-form-item prop="code">
            <el-input
              v-model.trim="loginFormData.code"
              placeholder="验证码"
              type="text"
              tabindex="3"
              :prefix-icon="Key"
              maxlength="7"
              size="large"
            >
              <template #append>
                <el-image :src="codeUrl" @click="createCode" draggable="false">
                  <template #placeholder>
                    <el-icon>
                      <Picture />
                    </el-icon>
                  </template>
                  <template #error>
                    <el-icon>
                      <Loading />
                    </el-icon>
                  </template>
                </el-image>
              </template>
            </el-input>
          </el-form-item> -->
          <el-button :loading="loading" type="primary" size="large" @click.prevent="handleLogin">登 录</el-button>
        </el-form>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.login-container {
  // background-image: url("/img/MAR-5321_footerv2-bg.inline_revised_fill.svg");
  // background-size: cover;
  // background-repeat: no-repeat;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: end;
  width: 100%;
  min-height: 100%;
  .theme-switch {
    position: fixed;
    top: 5%;
    right: 5%;
    cursor: pointer;
  }
  .login-card {
    // background-image: url("/img/background.png");
    // background-size: 100% 100%;
    // background-repeat: no-repeat;
    margin-right: calc(25% - 240px);
    z-index: 2;
    width: 480px;
    border-radius: 20px;
    box-shadow: 0 0 10px #59c3ec;
    overflow: hidden;
    .title {
      z-index: 4;
      font-size: 32px;
      display: flex;
      justify-content: center;
      align-items: center;
      height: 150px;
      img {
        height: 100%;
      }
    }
    .content {
      padding: 20px 50px 50px 50px;
      :deep(.el-input-group__append) {
        padding: 0;
        overflow: hidden;
        .el-image {
          width: 100px;
          height: 40px;
          border-left: 0px;
          user-select: none;
          cursor: pointer;
          text-align: center;
        }
      }
      .el-button {
        width: 100%;
        margin-top: 10px;
      }
    }
  }
}
</style>
