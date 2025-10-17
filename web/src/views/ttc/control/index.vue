<script lang="ts" setup>
import { nextTick, onMounted, reactive, Ref, ref, watch } from "vue"
import { type FormInstance, type FormRules, ElMessage, ElMessageBox, ElTable } from "element-plus"
import { Search, Refresh, CirclePlus, Delete, Download, RefreshRight, Plus, Microphone } from "@element-plus/icons-vue"
import { usePagination } from "@/hooks/usePagination"
import { CreateOrUpdateProtocolRequestData, GetProtocolData } from "@/api/TTC-protocol/protocol/types/protocol"
import { CreateOrUpdateParameterRequestData, GetParameterData } from "@/api/TTC-protocol/template/types/parameter"
import { createTableDataApi, createParamsDataApi, deleteBatchDataApi } from "@/api/TTC-protocol/protocol"
import { updateTableDataApi, updateParamsDataApi } from "@/api/TTC-protocol/protocol"
import { deleteTableDataApi, deleteParamsDataApi } from "@/api/TTC-protocol/protocol"
import { getTableDataApi } from "@/api/TTC-protocol/protocol"
import {
  getTTCTypeDataApi,
  getValidTypeDataApi,
  getParamTypeDataApi,
  getBaseTypeDataApi,
  getSectionTypeDataApi,
  getTemplatesByParams
} from "@/api/TTC-protocol/template"
import { VueUiSparkStackbar, VueUiBullet } from "vue-data-ui"
// 获取主题
import { useTheme } from "@/hooks/useTheme"
import { getSatellitesByParams } from "@/api/resource/satellite"
import { GetTemplateData } from "@/api/TTC-protocol/template/types/template"
defineOptions({
  // 命名当前组件
  name: "ttc-protocol"
})

const loading = ref<boolean>(false)
const selectTempLoading = ref<boolean>(false)
const selectSatLoading = ref<boolean>(false)
const { paginationData, handleCurrentChange, handleSizeChange } = usePagination()

//#region 增
const DEFAULT_FORM_DATA: CreateOrUpdateProtocolRequestData = {
  id: undefined,
  templateId: undefined,
  subsystemId: undefined,
  name: "",
  rec: "",
  status: true,
  updateTime: ""
}
const DEFAULT_PARAMETER_DATA: CreateOrUpdateParameterRequestData = {
  id: undefined,
  protocolId: undefined,
  name: "",
  type: "数据区",
  offsetByte: undefined,
  offsetBit: undefined,
  lengthByte: undefined,
  lengthBit: undefined,
  dataType: "",
  base: "",
  formula: "",
  status: true,
  updateTime: ""
}

interface Command {
  text: string
}

const DEFAULT_COMMAND: Command = {
  text: ""
}

const dialogVisible = ref<boolean>(false)
const dialogParameterVisible = ref<boolean>(false)

const formRef = ref<FormInstance | null>(null)
const formParameterRef = ref<FormInstance | null>(null)

const command = ref<Command>(JSON.parse(JSON.stringify(DEFAULT_COMMAND)))
const formData = ref<CreateOrUpdateProtocolRequestData>(JSON.parse(JSON.stringify(DEFAULT_FORM_DATA)))
const formParameterData = ref<CreateOrUpdateParameterRequestData>(JSON.parse(JSON.stringify(DEFAULT_PARAMETER_DATA)))

const validateType = (rule: any, value: any, callback: any) => {
  if (value === "") {
    callback(new Error("请选择参数类型"))
  } else {
    if (value !== "数据区") {
      callback(new Error("暂不支持,请移步协议模板配置"))
    }
    callback()
  }
}

const formRules: FormRules<CreateOrUpdateProtocolRequestData> = {
  name: [{ required: true, trigger: "blur", message: "请输入模板名称" }],
  templateId: [{ required: true, trigger: "blur", message: "请选择协议模板" }],
  subsystemId: [{ required: true, trigger: "blur", message: "请选择所属分系统" }],
  rec: [{ required: true, trigger: "blur", message: "请输入协议标识(副类型)" }]
}

const formParameterRules: FormRules<CreateOrUpdateParameterRequestData> = {
  name: [{ required: true, trigger: "blur", message: "请输入参数名称" }],
  type: [{ required: true, validator: validateType, trigger: "change" }],
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

const handleCommand = () => {
  //TODO: 发送指令逻辑
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
const handleDelete = (row: GetProtocolData) => {
  ElMessageBox.confirm(`正在删除协议: ${row.name}，确认删除？`, "提示", {
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
  ElMessageBox.confirm(`正在删除协议参数：${row.name}，确认删除？`, "提示", {
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
  selectIdList.value = newSelection.map((item: GetProtocolData) => item.id)
}

const handleBatchDelete = () => {
  ElMessageBox.confirm(`确认删除已选协议: ${selectIdList.value}?`, "提示", {
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
const handleUpdate = (row: GetProtocolData) => {
  dialogVisible.value = true
  formData.value = JSON.parse(JSON.stringify(row))
}

const handleParameterUpdate = (row: GetParameterData, temp: GetProtocolData) => {
  dialogParameterVisible.value = true
  formData.value = JSON.parse(JSON.stringify(temp))
  formParameterData.value = JSON.parse(JSON.stringify(row))
}
//#endregion

//#region 查
const tableData = ref<GetProtocolData[]>([])
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
type SelectItem = { key: number; value: string; label: string }
const ttcTypes = ref<Array<Item>>()
const validTypes = ref<Array<Item>>()
const paramTypes = ref<Array<Item>>()
const baseTypes = ref<Array<Item>>()
const sectionTypes = ref<Array<Item>>()
const templateType = ref("")

const templateQueryParam = reactive({
  name: "",
  type: ""
})
const satelliteQueryParam = reactive({
  name: ""
})
const templates = ref<Array<SelectItem>>()
const satellites = ref<Array<SelectItem>>()

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

const remoteSearchTemplate = (query: string) => {
  if (query) {
    selectTempLoading.value = true
    templateQueryParam.name = query
    templateQueryParam.type = formData.value.type || ""
    getTemplatesByParams(templateQueryParam)
      .then(({ data }) => {
        templates.value = data
        selectTempLoading.value = false
      })
      .catch(() => {
        templates.value = []
        selectTempLoading.value = false
      })
  } else {
    templates.value = []
  }
}

const remoteSearchSatellite = (query: string) => {
  if (query) {
    selectSatLoading.value = true
    satelliteQueryParam.name = query
    getSatellitesByParams(satelliteQueryParam)
      .then(({ data }) => {
        satellites.value = data
        selectSatLoading.value = false
      })
      .catch(() => {
        satellites.value = []
        selectSatLoading.value = false
      })
  } else {
    satellites.value = []
  }
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

const expandColumn = (
  row: { id: any; template: GetTemplateData; protocol: GetProtocolData; params: GetParameterData[] },
  expandedRows: string | any[]
) => {
  //row 被点击当前行的数据
  //expandedRows 存放页面中被展开行的数据 对应的数组就是 expand-row-keys

  //通过expandedRows长度来判断用户点击是展开还是折叠
  if (expandedRows.length) {
    //展开
    expands.value = [] //先干掉之前展开的行
    if (row) {
      expands.value.push(row.id) //push新的行 (原理有点类似防抖)
      if (row.template.templateType === "遥控") {
        if (row.params.length === 0) {
          dataset1.value = DEFAULT_DATASET_1
        } else {
          dataset1.value = []
          ;(row.params as GetParameterData[]).forEach((item: GetParameterData) => {
            dataset1.value.push({
              name: item.name,
              value: item.lengthByte ?? 0
            })
          })
        }
      } else {
        dataset.value.segments = [
          {
            name: "包(主)导头",
            from: row.template.headerOffset,
            to: row.template.headerOffset + row.template.headerLength,
            color: "#a30a0a"
          },
          {
            name: "数据区",
            from: row.template.bodyOffset,
            to: row.template.bodyOffset + row.template.bodyLength,
            color: "#2cc3aa"
          },
          {
            name: "校验区",
            from: row.template.validOffset,
            to: row.template.validOffset + row.template.validLength,
            color: "#24b22e"
          }
        ]
        dataset.value.target = 0
        dataset.value.value = row.template.bodyOffset + row.template.bodyLength + row.template.validLength
      }
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
}

const clickParamsTable = (row: any) => {
  dataset.value.target = row.offsetByte
}
//#endregion

/** 监听分页参数的变化 */
watch([() => paginationData.currentPage, () => paginationData.pageSize], getTableData, { immediate: true })
// watch(
//   () => dialogVisible.value,
//   () => {
//     if (dialogVisible.value) {
//       remoteSearchSatellite(formData.value.subsystemId || "")
//       remoteSearchTemplate(formData.value.templateId || "")
//     }
//   }
// )

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
  userOptions: {
    show: false,
    showOnChartHover: false,
    keepStateOnChartLeave: true,
    position: "right",
    buttons: {
      tooltip: false,
      pdf: true,
      csv: false,
      img: true,
      table: false,
      labels: false,
      fullscreen: true,
      sort: false,
      stack: false,
      animation: false,
      annotator: true
    },
    buttonTitles: {
      open: "Open options",
      close: "Close options",
      pdf: "Download PDF",
      img: "Download PNG",
      fullscreen: "Toggle fullscreen",
      annotator: "Toggle annotator"
    },
    print: {
      allowTaint: false,
      backgroundColor: "#FFFFFFff",
      useCORS: false,
      onclone: null,
      scale: 2,
      logging: false
    }
  },
  style: {
    fontFamily: "inherit",
    chart: {
      backgroundColor: "#FFFFFFff",
      color: "#1A1A1Aff",
      height: "90",
      width: "579",
      padding: {
        top: 24,
        right: 24,
        bottom: 24,
        left: 12
      },
      animation: {
        show: true,
        animationFrames: 60
      },
      segments: {
        baseColor: "#9A9A9A",
        dataLabels: {
          show: true,
          color: "#1A1A1Aff",
          fontSize: 10,
          formatter: null,
          bold: false,
          prefix: "",
          suffix: "",
          rounding: 0,
          offsetY: 0
        },
        ticks: {
          show: true,
          divisions: 10,
          stroke: "#8A8A8Aff"
        }
      },
      target: {
        onTop: true,
        color: "#1A1A1Aff",
        rounded: true,
        heightRatio: "0.83",
        stroke: "#FFFFFFff",
        strokeWidth: 4,
        width: 6
      },
      valueBar: {
        color: "#3A3A3Aff",
        heightRatio: 0.33,
        stroke: "#FFFFFFff",
        strokeWidth: 1,
        label: {
          show: true,
          color: "#1A1A1Aff",
          fontSize: 14,
          bold: true,
          offsetY: 0
        }
      },
      title: {
        text: "",
        color: "#1A1A1Aff",
        fontSize: 20,
        bold: true,
        textAlign: "center",
        paddingLeft: 0,
        paddingRight: 0,
        subtitle: {
          color: "#A1A1A1ff",
          text: "",
          fontSize: 16,
          bold: false
        }
      },
      legend: {
        show: true,
        bold: false,
        backgroundColor: "#ffffffff",
        color: "#1A1A1Aff",
        fontSize: 14,
        roundingValue: 0
      }
    }
  }
})

const config1 = ref({
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

const dataset = ref({
  value: 987.1867376062175,
  target: 8,
  segments: [
    {
      name: "包(主)导头",
      from: 0,
      to: 6,
      color: "#a30a0a"
    },
    {
      name: "数据区",
      from: 6,
      to: 1022,
      color: "#2cc3aa"
    },
    {
      name: "校验区",
      from: 1022,
      to: 1024,
      color: "#24b22e"
    }
  ]
})

const DEFAULT_DATASET_1 = [
  {
    name: "有效数据",
    value: 60
  }
]

const dataset1 = ref(DEFAULT_DATASET_1)
//#endregion
</script>

<template>
  <div class="app-container">
    <el-card loading="loading" shadow="hover" class="search-wrapper">
      <el-form ref="searchFormRef" :inline="true" :model="searchData">
        <el-form-item prop="name" label="协议名称">
          <el-input v-model="searchData.name" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="type" label="协议类型">
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
          <!-- <el-button type="primary" :icon="CirclePlus" @click="dialogVisible = true">新增协议</el-button>
          <el-button type="danger" :icon="Delete" @click="handleBatchDelete">批量删除</el-button> -->
          <el-button type="primary" :icon="Microphone" @click="dialogVisible = true">手动发控</el-button>
        </div>
        <div>
          <!-- <el-tooltip content="下载">
            <el-button type="primary" :icon="Download" circle />
          </el-tooltip>
          <el-tooltip content="刷新当前页">
            <el-button type="primary" :icon="RefreshRight" circle @click="getTableData" />
          </el-tooltip> -->
        </div>
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
                <div>
                  <p m="t-0 b-2">协议id: {{ props.row.id }}, 更新时间: {{ props.row.updateTime }}</p>
                </div>
                <div style="width: 500px" v-if="props.row.template.templateType == '遥测'">
                  <VueUiBullet :config="config" :dataset="dataset" />
                </div>
                <div style="width: 500px" v-if="props.row.template.templateType == '遥控'">
                  <VueUiSparkStackbar :config="config1" :dataset="dataset1" style="border: 10px" />
                </div>
                <h3>
                  {{ props.row.template.templateType }}协议参数
                  <el-tooltip content="新增">
                    <el-button
                      size="small"
                      :icon="Plus"
                      circle
                      @click="(dialogParameterVisible = true), (formData = JSON.parse(JSON.stringify(props.row)))"
                    />
                  </el-tooltip>
                </h3>
                <el-table :data="props.row.params" style="width: 100%" @row-click="clickParamsTable" max-height="400">
                  <el-table-column type="index" align="center" />
                  <el-table-column label="协议参数名称" prop="name" align="left" />
                  <!-- <el-table-column
                    label="所属区域"
                    prop="type"
                    align="center"
                    sortalbe
                    :filters="
                      sectionTypes?.map((item) => ({
                        text: item.label,
                        value: item.label
                      }))
                    "
                    :filter-method="
                      (value: string, row: GetParameterData) => {
                        return row.type === value
                      }
                    "
                  /> -->
                  <el-table-column
                    label="参数类型"
                    prop="dataType"
                    align="center"
                    sortalbe
                    :filters="
                      paramTypes?.map((item) => ({
                        text: item.label,
                        value: item.label
                      }))
                    "
                    :filter-method="
                      (value: string, row: GetParameterData) => {
                        return row.dataType === value
                      }
                    "
                  >
                    <template #default="scope">
                      <el-tag v-if="scope.row.dataType === '数字量参数'" type="primary">{{
                        scope.row.dataType
                      }}</el-tag>
                      <el-tag v-else-if="scope.row.dataType === 'AN量参数'" type="success">{{
                        scope.row.dataType
                      }}</el-tag>
                      <el-tag v-else-if="scope.row.dataType === '状态量参数'" type="warning">{{
                        scope.row.dataType
                      }}</el-tag>
                      <el-tag v-else-if="scope.row.dataType === 'TH量参数'" type="error">{{
                        scope.row.dataType
                      }}</el-tag>
                      <el-tag v-else type="info">{{ scope.row.dataType }}</el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column label="起始字节" prop="offsetByte" align="center" />
                  <el-table-column label="起始位" prop="offsetBit" align="center" />
                  <el-table-column label="字节长度" prop="lengthByte" align="center" />
                  <el-table-column label="位长度" prop="lengthBit" align="center" />
                  <el-table-column
                    label="显示进制"
                    prop="base"
                    align="center"
                    sortalbe
                    :filters="
                      baseTypes?.map((item) => ({
                        text: item.label,
                        value: item.label
                      }))
                    "
                    :filter-method="
                      (value: string, row: GetParameterData) => {
                        return row.base === value
                      }
                    "
                  />
                  <!-- <el-table-column label="处理公式" prop="formula" align="center" /> -->
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
          <el-table-column prop="name" label="协议名称" align="center" />
          <!-- <el-table-column prop="nid" label="主类型" align="center">
            <template #default="scope">
              <span>{{ scope.row.satellite.subsystems[0].nid }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="rec" label="副类型" align="center" /> -->
          <el-table-column prop="templateType" label="协议类型" align="center">
            <template #default="scope">
              <el-tag v-if="scope.row.template.templateType == '遥控'" type="success">{{
                scope.row.template.templateType
              }}</el-tag>
              <el-tag v-else-if="scope.row.template.templateType == '遥测'" type="primary">{{
                scope.row.template.templateType
              }}</el-tag>
              <el-tag v-else type="warning">{{ scope.row.template.templateType }}</el-tag>
              <el-tag style="margin-left: 5px" type="warning">{{ scope.row.rec }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="subsystem" label="所属分系统" align="center">
            <template #default="scope">
              <span>{{ scope.row.satellite.name }} - {{ scope.row.satellite.subsystems[0].nameCN }}</span>
              <!-- <el-tag style="margin-left: 5px" type="info">{{ scope.row.satellite.subsystems[0].nid }}</el-tag> -->
            </template>
          </el-table-column>
          <el-table-column prop="subsystem" label="模板" align="center">
            <template #default="scope">
              <span>{{ scope.row.template.name }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" align="center">
            <template #default="scope">
              <el-tag v-if="scope.row.status" type="success" effect="plain">启用</el-tag>
              <el-tag v-else type="danger" effect="plain">禁用</el-tag>
            </template>
          </el-table-column>
          <!-- <el-table-column prop="updateTime" label="更新时间" align="center" /> -->
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
    <el-dialog v-model="dialogVisible" title="手动发控" @closed="resetForm" @open="handleDialogOpen" width="40%">
      <el-form ref="formRef" :model="command" label-width="150px" label-position="right">
        <el-form-item prop="text" label="指令内容">
          <el-input v-model="command.text" type="textarea" placeholder="请输入十六进制指令包" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCommand" :loading="loading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 新增/修改模板参数 -->
    <el-dialog
      v-model="dialogParameterVisible"
      :title="formParameterData.id === undefined ? '新增协议参数' : '修改协议参数'"
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
