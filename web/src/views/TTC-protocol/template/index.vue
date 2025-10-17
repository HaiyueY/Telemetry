<script lang="ts" setup>
import { nextTick, onMounted, reactive, ref, watch } from "vue"
import { type FormInstance, type FormRules, ElMessage, ElMessageBox, ElTable } from "element-plus"
import { Search, Refresh, CirclePlus, Delete, Download, RefreshRight, Plus } from "@element-plus/icons-vue"
import { usePagination } from "@/hooks/usePagination"
import { CreateOrUpdateTemplateRequestData, GetTemplateData } from "@/api/TTC-protocol/template/types/template"
import { CreateOrUpdateParameterRequestData, GetParameterData } from "@/api/TTC-protocol/template/types/parameter"
import { createTableDataApi, createParamsDataApi, deleteBatchDataApi } from "@/api/TTC-protocol/template"
import { updateTableDataApi, updateParamsDataApi } from "@/api/TTC-protocol/template"
import { deleteTableDataApi, deleteParamsDataApi } from "@/api/TTC-protocol/template"
import { getTableDataApi } from "@/api/TTC-protocol/template"
import {
  getTTCTypeDataApi,
  getValidTypeDataApi,
  getParamTypeDataApi,
  getBaseTypeDataApi,
  getSectionTypeDataApi
} from "@/api/TTC-protocol/template"
import { VueUiSparkStackbar } from "vue-data-ui"
// 获取主题
import { useTheme } from "@/hooks/useTheme"
defineOptions({
  // 命名当前组件
  name: "ttc-template"
})

const loading = ref<boolean>(false)
const { paginationData, handleCurrentChange, handleSizeChange } = usePagination()

//#region 增
const DEFAULT_FORM_DATA: CreateOrUpdateTemplateRequestData = {
  id: undefined,
  name: "",
  templateType: "",
  headerOffset: 0,
  headerLength: 0,
  bodyOffset: 0,
  bodyLength: 0,
  validOffset: 0,
  validLength: 0,
  validType: "",
  updateTime: ""
}
const DEFAULT_PARAMETER_DATA: CreateOrUpdateParameterRequestData = {
  id: undefined,
  protocolId: undefined,
  name: "",
  type: "",
  offsetByte: undefined,
  offsetBit: undefined,
  lengthByte: undefined,
  lengthBit: undefined,
  dataType: "",
  base: "",
  formula: "",
  defaultValue: "",
  status: true,
  updateTime: ""
}

const dialogVisible = ref<boolean>(false)
const dialogParameterVisible = ref<boolean>(false)

const formRef = ref<FormInstance | null>(null)
const formParameterRef = ref<FormInstance | null>(null)

const formData = ref<CreateOrUpdateTemplateRequestData>(JSON.parse(JSON.stringify(DEFAULT_FORM_DATA)))
const formParameterData = ref<CreateOrUpdateParameterRequestData>(JSON.parse(JSON.stringify(DEFAULT_PARAMETER_DATA)))

const formRules: FormRules<CreateOrUpdateTemplateRequestData> = {
  name: [{ required: true, trigger: "blur", message: "请输入模板名称" }],
  templateType: [{ required: true, trigger: "blur", message: "请选择模板类型" }],
  headerOffset: [{ required: false, trigger: "blur", message: "请输入包(主)导头起始字节" }],
  headerLength: [{ required: false, trigger: "blur", message: "请输入包(主)导头字节长度" }],
  bodyOffset: [{ required: false, trigger: "blur", message: "请输入数据区起始字节" }],
  bodyLength: [{ required: false, trigger: "blur", message: "请输入数据区字节长度" }],
  validOffset: [{ required: false, trigger: "blur", message: "请输入校验区起始字节" }],
  validLength: [{ required: false, trigger: "blur", message: "请输入校验区字节长度" }],
  validType: [{ required: true, trigger: "blur", message: "请选择校验类型" }]
}

const formParameterRules: FormRules<CreateOrUpdateParameterRequestData> = {
  name: [{ required: true, trigger: "blur", message: "请输入参数名称" }],
  type: [{ required: true, trigger: "blur", message: "请选择参数类型" }],
  offsetByte: [{ required: true, trigger: "blur", message: "请输入参数起始字节" }],
  offsetBit: [{ required: true, trigger: "blur", message: "请输入参数字节长度" }],
  lengthByte: [{ required: true, trigger: "blur", message: "请输入参数起始位" }],
  lengthBit: [{ required: true, trigger: "blur", message: "请输入参数位长度" }],
  dataType: [{ required: true, trigger: "blur", message: "请选择数据类型" }],
  base: [{ required: true, trigger: "blur", message: "请选择参数进制" }],
  formula: [{ required: false, trigger: "blur", message: "请输入公式" }],
  defaultValue: [{ required: false, trigger: "blur", message: "请输入默认值" }]
}

const handleCreateOrUpdate = () => {
  loading.value = true
  const api = formData.value.id === undefined ? createTableDataApi : updateTableDataApi
  api(formData.value)
    .then(() => {
      ElMessage.success("操作成功")
      dialogVisible.value = false
      getTableData()
    })
    .finally(() => {
      loading.value = false
    })
}

const handleCreateOrUpdateParameter = () => {
  loading.value = true
  const api = formData.value.id === undefined ? createParamsDataApi : updateParamsDataApi
  // 拼接接口参数
  formParameterData.value.protocolId = formData.value.id
  api(formParameterData.value)
    .then(() => {
      ElMessage.success("操作成功")
      dialogParameterVisible.value = false
      getTableData()
    })
    .finally(() => {
      loading.value = false
    })
}

const handleBasicNext = () => {
  formRef.value?.validate((valid: boolean, fields) => {
    if (!valid) return console.error("表单校验不通过", fields)
    handleCreateOrUpdate()
  })
}
const handleParameterNext = () => {
  formParameterRef.value?.validate((valid: boolean, fields) => {
    if (!valid) return console.error("表单校验不通过", fields)
    handleCreateOrUpdateParameter()
  })
}

const resetForm = () => {
  formRef.value?.clearValidate()
  formData.value = JSON.parse(JSON.stringify(DEFAULT_FORM_DATA))
}
const resetParameterForm = () => {
  resetForm()
  formParameterRef.value?.clearValidate()
  formParameterData.value = JSON.parse(JSON.stringify(DEFAULT_PARAMETER_DATA))
}
//#endregion

//#region 删
const handleDelete = (row: GetTemplateData) => {
  ElMessageBox.confirm(`正在删除协议模板: ${row.name}，确认删除？`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    deleteTableDataApi(row.id).then(() => {
      ElMessage.success("删除成功")
      getTableData()
    })
  })
}

const handleSensorDelete = (row: GetParameterData) => {
  ElMessageBox.confirm(`正在删除协议模板参数：${row.name}，确认删除？`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    deleteParamsDataApi(row.id).then(() => {
      ElMessage.success("删除成功")
      getTableData()
    })
  })
}

const selectIdList = ref()

const handleSelectionChange = (newSelection: any[]) => {
  selectIdList.value = newSelection.map((item: GetTemplateData) => item.id)
}

const handleBatchDelete = () => {
  ElMessageBox.confirm(`确认删除已选协议模板: ${selectIdList.value}?`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    deleteBatchDataApi(selectIdList.value).then(() => {
      ElMessage.success("删除成功")
      getTableData()
    })
  })
}

//#endregion

//#region 改
const handleUpdate = (row: GetTemplateData) => {
  dialogVisible.value = true
  formData.value = JSON.parse(JSON.stringify(row))
}

const handleParameterUpdate = (row: GetParameterData, temp: GetTemplateData) => {
  dialogParameterVisible.value = true
  formData.value = JSON.parse(JSON.stringify(temp))
  formParameterData.value = JSON.parse(JSON.stringify(row))
}
//#endregion

//#region 查
const tableData = ref<GetTemplateData[]>([])
const searchFormRef = ref<FormInstance | null>(null)
const searchData = reactive({
  name: "",
  type: ""
})

const getTableData = () => {
  loading.value = true

  getTableDataApi({
    pageNum: paginationData.currentPage,
    pageSize: paginationData.pageSize,
    name: searchData.name || undefined,
    type: searchData.type || undefined
  })
    .then(({ data }) => {
      console.log(data)

      paginationData.total = data.total
      tableData.value = data.records
    })
    .catch(() => {
      tableData.value = []
    })
    .finally(() => {
      loading.value = false
    })
}
const handleSearch = () => {
  paginationData.currentPage === 1 ? getTableData() : (paginationData.currentPage = 1)
}
const resetSearch = () => {
  searchFormRef.value?.resetFields()
  handleSearch()
}
//#endregion

//#region 查询基础参数
type Item = { key: number; label: string }
const ttcTypes = ref<Array<Item>>()
const validTypes = ref<Array<Item>>()
const paramTypes = ref<Array<Item>>()
const baseTypes = ref<Array<Item>>()
const sectionTypes = ref<Array<Item>>()

const getBasicData = () => {
  getTTCTypeDataApi()
    .then(({ data }) => {
      // 将Map转化为Array
      const array: Item[] = []
      data = new Map<number, string>(Object.entries(data).map(([key, value]) => [Number(key), value]))
      data.forEach((value, key) => {
        array.push({ key: key, label: value })
      })
      ttcTypes.value = array
    })
    .catch(() => {
      ElMessage.error("获取测控类型失败")
      ttcTypes.value = []
    })

  getValidTypeDataApi()
    .then(({ data }) => {
      // 将Map转化为Array
      const array: Item[] = []
      data = new Map<number, string>(Object.entries(data).map(([key, value]) => [Number(key), value]))
      data.forEach((value, key) => {
        array.push({ key: key, label: value })
      })
      validTypes.value = array
    })
    .catch(() => {
      ElMessage.error("获取校验类型失败")
      validTypes.value = []
    })

  getParamTypeDataApi()
    .then(({ data }) => {
      // 将Map转化为Array
      const array: Item[] = []
      data = new Map<number, string>(Object.entries(data).map(([key, value]) => [Number(key), value]))
      data.forEach((value, key) => {
        array.push({ key: key, label: value })
      })
      paramTypes.value = array
    })
    .catch(() => {
      ElMessage.error("获取参数类型失败")
      paramTypes.value = []
    })

  getBaseTypeDataApi()
    .then(({ data }) => {
      // 将Map转化为Array
      const array: Item[] = []
      data = new Map<number, string>(Object.entries(data).map(([key, value]) => [Number(key), value]))
      data.forEach((value, key) => {
        array.push({ key: key, label: value })
      })
      baseTypes.value = array
    })
    .catch(() => {
      ElMessage.error("获取参数类型失败")
      baseTypes.value = []
    })

  getSectionTypeDataApi()
    .then(({ data }) => {
      // 将Map转化为Array
      const array: Item[] = []
      data = new Map<number, string>(Object.entries(data).map(([key, value]) => [Number(key), value]))
      data.forEach((value, key) => {
        array.push({ key: key, label: value })
      })
      sectionTypes.value = array
    })
    .catch(() => {
      ElMessage.error("获取参数类型失败")
      sectionTypes.value = []
    })
}
//#endregion

//#region 新增或修改弹窗
const handleDialogOpen = () => {
  // dialogVisible.value = true
  getBasicData()
}

//#endregion

//#region 展开行逻辑
const expands = ref<any>([])

const expandColumn = (row: { id: any }, expandedRows: string | any[]) => {
  //row 被点击当前行的数据
  //expandedRows 存放页面中被展开行的数据 对应的数组就是 expand-row-keys

  //通过expandedRows长度来判断用户点击是展开还是折叠
  if (expandedRows.length) {
    //展开
    expands.value = [] //先干掉之前展开的行
    if (row) {
      expands.value.push(row.id) //push新的行 (原理有点类似防抖)
    }
  } else {
    expands.value = [] //折叠 就清空expand-row-keys对应的数组
  }
}

const getRowKeys = (row: { id: any }) => {
  //row是当前行的数据
  //给每行绑定唯一的标识
  return row.id
}
const tableRef = ref<InstanceType<typeof ElTable>>() //表标识
const clickTable = (row: any) => {
  tableRef.value?.toggleRowExpansion(row)
  dataset.value = [
    {
      name: "包(主)导头",
      value: row.headerLength
    },
    {
      name: "数据区",
      value: row.bodyLength
    },
    {
      name: "校验区",
      value: row.validLength
    }
  ]
}
//#endregion

/** 监听分页参数的变化 */
watch([() => paginationData.currentPage, () => paginationData.pageSize], getTableData, { immediate: true })

onMounted(() => {
  getBasicData()
})

const { activeThemeName } = useTheme()
// 监听主题变化
nextTick(() => {
  watch(
    activeThemeName,
    () => {
      config.value.theme = activeThemeName.value === "normal" ? "default" : "hack"
    },
    { immediate: true }
  )
})

//#region 图表
const config = ref({
  theme: "",
  customPalette: [],
  style: {
    fontFamily: "inherit",
    backgroundColor: "#ffffff00",
    animation: {
      show: true,
      animationFrames: 60
    },
    bar: {
      gradient: {
        show: true,
        intensity: 40,
        underlayerColor: "rgba(255, 255, 255, 0)"
      }
    },
    legend: {
      show: true,
      textAlign: "left",
      fontSize: 12,
      margin: "6px 0 0 0",
      name: {
        color: "#1A1A1Aff",
        bold: false
      },
      value: {
        show: true,
        bold: false,
        color: "#1A1A1Aff",
        prefix: "",
        suffix: "",
        rounding: 0,
        formatter: null
      },
      percentage: {
        show: true,
        bold: true,
        color: "#1A1A1Aff",
        rounding: 1
      }
    },
    title: {
      text: "",
      color: "#1A1A1Aff",
      fontSize: 16,
      bold: true,
      textAlign: "left",
      paddingLeft: 0,
      paddingRight: 0,
      subtitle: {
        color: "#A1A1A1ff",
        text: "",
        fontSize: 12,
        bold: false
      },
      margin: "0 0 6px 0"
    },
    tooltip: {
      show: true,
      color: "#1A1A1Aff",
      backgroundColor: "#ffffffff",
      fontSize: 14,
      customFormat: null,
      borderRadius: 7,
      borderColor: "#e1e5e8",
      borderWidth: 1,
      backgroundOpacity: 30,
      position: "right",
      offsetY: 24
    }
  }
})

const dataset = ref([
  {
    name: "包(主)导头",
    value: 60
  },
  {
    name: "数据区",
    value: 100
  },
  {
    name: "校验区",
    value: 50
  }
])
//#endregion
</script>

<template>
  <div class="app-container">
    <el-card loading="loading" shadow="hover" class="search-wrapper">
      <el-form ref="searchFormRef" :inline="true" :model="searchData">
        <el-form-item prop="name" label="模板名称">
          <el-input v-model="searchData.name" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="type" label="模板类型">
          <el-input v-model="searchData.type" placeholder="请输入" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card loading="loading" shadow="hover">
      <div class="toolbar-wrapper">
        <div>
          <el-button type="primary" :icon="CirclePlus" @click="dialogVisible = true">新增模板</el-button>
          <el-button type="danger" :icon="Delete" @click="handleBatchDelete">批量删除</el-button>
        </div>
        <!-- <div>
          <el-tooltip content="下载">
            <el-button type="primary" :icon="Download" circle />
          </el-tooltip>
          <el-tooltip content="刷新当前页">
            <el-button type="primary" :icon="RefreshRight" circle @click="getTableData" />
          </el-tooltip>
        </div> -->
      </div>
      <div class="table-wrapper">
        <el-table
          :data="tableData"
          @selection-change="handleSelectionChange"
          @expand-change="expandColumn"
          :row-key="getRowKeys"
          :expand-row-keys="expands"
          style="width: 100%"
          ref="tableRef"
          @row-click="clickTable"
        >
          <el-table-column type="selection" :reserve-selection="true" width="50" align="center" />
          <el-table-column type="expand">
            <template #default="props">
              <div m="4">
                <h3>
                  详情
                  <!-- <el-tag v-if="props.row.maintenanceList.length != 0" effect="plain" style="margin-left: 5px"
                    >本日需进行构型保持机动</el-tag
                  >
                  <el-tag v-else effect="plain" style="margin-left: 5px" type="info">本日暂无构型保持计划</el-tag> -->
                </h3>
                <div style="width: 40%">
                  <p m="t-0 b-2">模板id: {{ props.row.id }}, 更新时间: {{ props.row.updateTime }}</p>
                  <VueUiSparkStackbar :config="config" :dataset="dataset" style="border: 10px" />
                </div>
                <h3>
                  模板参数
                  <el-tooltip content="新增">
                    <el-button
                      size="small"
                      :icon="Plus"
                      circle
                      @click="(dialogParameterVisible = true), (formData = JSON.parse(JSON.stringify(props.row)))"
                    />
                  </el-tooltip>
                </h3>
                <el-table :data="props.row.params" style="width: 100%">
                  <el-table-column type="index" align="center" />
                  <el-table-column label="模板参数名称" prop="name" align="center" />
                  <el-table-column label="所属区域" prop="type" align="center" />
                  <el-table-column label="参数类型" prop="dataType" align="center" />
                  <el-table-column label="参数起始字节" prop="offsetByte" align="center" />
                  <el-table-column label="参数起始位" prop="offsetBit" align="center" />
                  <el-table-column label="参数字节长度" prop="lengthByte" align="center" />
                  <el-table-column label="参数位长度" prop="lengthBit" align="center" />
                  <el-table-column label="显示进制" prop="base" align="center" />
                  <!-- <el-table-column label="处理公式" prop="formula" align="center" />
                  <el-table-column label="默认值" prop="defaultValue" align="center" /> -->
                  <el-table-column prop="status" label="状态" align="center">
                    <template #default="scope">
                      <el-tag v-if="scope.row.status" type="success" effect="plain">启用</el-tag>
                      <el-tag v-else type="danger" effect="plain">禁用</el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="150" align="center">
                    <template #default="scope">
                      <el-button
                        type="primary"
                        text
                        bg
                        size="small"
                        @click="handleParameterUpdate(scope.row, props.row)"
                        >修改</el-button
                      >
                      <el-button type="danger" text bg size="small" @click="handleSensorDelete(scope.row)"
                        >删除</el-button
                      >
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="模板名称" align="center" />
          <el-table-column prop="templateType" label="模板类型" align="center">
            <template #default="scope">
              <el-tag v-if="scope.row.templateType == '遥控'" type="success">{{ scope.row.templateType }}</el-tag>
              <el-tag v-else-if="scope.row.templateType == '遥测'" type="primary">{{ scope.row.templateType }}</el-tag>
              <el-tag v-else type="warning">{{ scope.row.templateType }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="validType" label="校验方式" align="center">
            <template #default="scope">
              <el-tag v-if="scope.row.validType == 'CRC校验'" type="primary">{{ scope.row.validType }}</el-tag>
              <el-tag v-else type="warning">{{ scope.row.validType }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="190" align="center">
            <template #default="scope">
              <el-button type="primary" text bg size="small" @click="handleUpdate(scope.row)">修改</el-button>
              <el-button type="danger" text bg size="small" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="pager-wrapper">
        <el-pagination
          background
          :layout="paginationData.layout"
          :page-sizes="paginationData.pageSizes"
          :total="paginationData.total"
          :page-size="paginationData.pageSize"
          :currentPage="paginationData.currentPage"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
    <!-- 新增/修改基本信息 -->
    <el-dialog
      v-model="dialogVisible"
      :title="formData.id === undefined ? '新增协议模板' : '修改协议模板'"
      @closed="resetForm"
      @open="handleDialogOpen"
      width="40%"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
        label-position="right"
        :inline="true"
      >
        <el-form-item prop="name" label="模板名称" style="width: 40%">
          <el-input v-model="formData.name" placeholder="请输入模板名称" />
        </el-form-item>
        <el-form-item prop="templateType" label="模板类型" style="width: 40%">
          <el-select v-model="formData.templateType" placeholder="请选择模板类型">
            <el-option v-for="item in ttcTypes" :key="item.key" :label="item.label" :value="item.label"
          /></el-select>
        </el-form-item>
        <el-form-item prop="headerOffset" label="(主)导头起始" style="width: 40%">
          <el-input-number
            :min="0"
            :step="1"
            :precision="0"
            step-strictly
            v-model="formData.headerOffset"
            placeholder="单位: Byte"
          />
        </el-form-item>
        <el-form-item prop="headerLength" label="(主)导头长度" style="width: 40%">
          <el-input-number
            :min="0"
            :step="1"
            :precision="0"
            step-strictly
            v-model="formData.headerLength"
            placeholder="单位: Byte"
          />
        </el-form-item>
        <el-form-item prop="bodyOffset" label="数据区起始" style="width: 40%">
          <el-input-number
            :min="0"
            :step="1"
            :precision="0"
            step-strictly
            v-model="formData.bodyOffset"
            placeholder="单位: Byte"
          />
        </el-form-item>
        <el-form-item prop="bodyLength" label="数据区长度" style="width: 40%">
          <el-input-number
            :min="0"
            :step="1"
            :precision="0"
            step-strictly
            v-model="formData.bodyLength"
            placeholder="单位: Byte"
          />
        </el-form-item>
        <el-form-item prop="validOffset" label="校验区起始" style="width: 40%">
          <el-input-number
            :min="0"
            :step="1"
            :precision="0"
            step-strictly
            v-model="formData.validOffset"
            placeholder="单位: Byte"
          />
        </el-form-item>
        <el-form-item prop="validLength" label="校验区长度" style="width: 40%">
          <el-input-number
            :min="0"
            :step="1"
            :precision="0"
            step-strictly
            v-model="formData.validLength"
            placeholder="单位: Byte"
          />
        </el-form-item>
        <el-form-item prop="validType" label="校验方式">
          <el-radio-group v-model="formData.validType">
            <el-radio v-for="item in validTypes" :key="item.label" :label="item.label">{{ item.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleBasicNext" :loading="loading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 新增/修改模板参数 -->
    <el-dialog
      v-model="dialogParameterVisible"
      :title="formParameterData.id === undefined ? '新增模板参数' : '修改模板参数'"
      @open="handleDialogOpen"
      @closed="resetParameterForm"
      width="40%"
    >
      <el-form
        ref="formParameterRef"
        :model="formParameterData"
        :rules="formParameterRules"
        label-width="100px"
        label-position="right"
        :inline="true"
      >
        <el-form-item prop="name" label="参数名称" style="width: 40%">
          <el-input v-model="formParameterData.name" placeholder="请输入模板参数名称" />
        </el-form-item>
        <el-form-item prop="type" label="区域类型" style="width: 40%">
          <el-select v-model="formParameterData.type" placeholder="请选择区域类型">
            <el-option v-for="item in sectionTypes" :key="item.key" :label="item.label" :value="item.label"
          /></el-select>
        </el-form-item>
        <el-form-item prop="offsetByte" label="起始字节" style="width: 40%">
          <el-input-number
            :min="0"
            :step="1"
            :precision="0"
            step-strictly
            v-model="formParameterData.offsetByte"
            placeholder="单位: Byte"
          />
        </el-form-item>
        <el-form-item prop="offsetBit" label="起始位" style="width: 40%">
          <el-input-number
            :min="0"
            :step="1"
            :precision="0"
            step-strictly
            v-model="formParameterData.offsetBit"
            placeholder="单位: bit"
          />
        </el-form-item>
        <el-form-item prop="lengthByte" label="字节长度" style="width: 40%">
          <el-input-number
            :min="0"
            :step="1"
            :precision="0"
            step-strictly
            v-model="formParameterData.lengthByte"
            placeholder="单位: Byte"
          />
        </el-form-item>
        <el-form-item prop="lengthBit" label="位长度" style="width: 40%">
          <el-input-number
            :min="0"
            :step="1"
            :precision="0"
            step-strictly
            v-model="formParameterData.lengthBit"
            placeholder="单位: bit"
          />
        </el-form-item>
        <el-form-item prop="dataType" label="参数类型" style="width: 40%">
          <el-select v-model="formParameterData.dataType" placeholder="请选择参数类型">
            <el-option v-for="item in paramTypes" :key="item.key" :label="item.label" :value="item.label"
          /></el-select>
        </el-form-item>
        <el-form-item prop="base" label="显示进制" style="width: 40%">
          <el-select v-model="formParameterData.base" placeholder="请选择显示进制">
            <el-option v-for="item in baseTypes" :key="item.key" :label="item.label" :value="item.label"
          /></el-select>
        </el-form-item>
        <el-form-item prop="formula" label="处理公式" style="width: 40%">
          <el-input v-model="formParameterData.formula" placeholder="请输入参数处理公式" />
        </el-form-item>
        <el-form-item prop="defaultValue" label="默认值" style="width: 40%">
          <el-input v-model="formParameterData.defaultValue" placeholder="请输入参数默认/固定值" />
        </el-form-item>
        <el-form-item prop="scaleMax" label="上限" style="width: 40%">
          <el-input-number
            :min="0"
            :step="1"
            :precision="0"
            step-strictly
            v-model="formParameterData.scaleMax"
            placeholder="请输入参数上限值"
          />
        </el-form-item>
        <el-form-item prop="scaleMin" label="下限" style="width: 40%">
          <el-input-number
            :min="0"
            :step="1"
            :precision="0"
            step-strictly
            v-model="formParameterData.scaleMin"
            placeholder="请输入参数下限值"
          />
        </el-form-item>
        <el-form-item prop="status" label="状态">
          <el-switch v-model="formParameterData.status" active-text="启用" inactive-text="禁用" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogParameterVisible = false">取消</el-button>
        <el-button type="primary" @click="handleParameterNext" :loading="loading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.search-wrapper {
  margin-bottom: 20px;
  :deep(.el-card__body) {
    padding-bottom: 2px;
  }
}

.toolbar-wrapper {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.table-wrapper {
  margin-bottom: 20px;
  background-image: url("/img/MAR-5321_footerv2-bg.inline_revised_fill.svg");
  background-size: 100%;
  background-repeat: repeat;
}

.pager-wrapper {
  display: flex;
  justify-content: flex-end;
}

.sensor-form-inline .el-input {
  --el-input-width: 40%;
}
</style>
