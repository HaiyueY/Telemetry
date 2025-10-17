<script lang="ts" setup>
import { computed, nextTick, onMounted, reactive, Ref, ref, watch, watchEffect } from "vue"
import { type FormInstance, type FormRules, ElMessage, ElMessageBox, ElTable, TreeInstance } from "element-plus"
import { Search, Refresh, CirclePlus, Delete, Download, RefreshRight, Plus } from "@element-plus/icons-vue"
import { usePagination } from "@/hooks/usePagination"
import { CreateOrUpdateProtocolRequestData, GetProtocolData } from "@/api/TTC-protocol/protocol/types/protocol"
import { CreateOrUpdateParameterRequestData, GetParameterData } from "@/api/TTC-protocol/template/types/parameter"
import { createTableDataApi, createParamsDataApi, deleteBatchDataApi } from "@/api/TTC-protocol/protocol"
import { updateTableDataApi, updateParamsDataApi } from "@/api/TTC-protocol/protocol"
import { deleteTableDataApi, deleteParamsDataApi } from "@/api/TTC-protocol/protocol"
import { getPacketDataApi, getTableDataApi } from "@/api/ttc/telemetry"
import {
  getTTCTypeDataApi,
  getValidTypeDataApi,
  getParamTypeDataApi,
  getBaseTypeDataApi,
  getSectionTypeDataApi,
  getTemplatesByParams
} from "@/api/TTC-protocol/template"
import { VueUiSparkStackbar, VueUiBullet, VueUiSkeletonConfig, VueUiSparkline } from "vue-data-ui"
// 获取主题
import { useTheme } from "@/hooks/useTheme"
import { getSatellitesByParams } from "@/api/resource/satellite"
import { GetTemplateData } from "@/api/TTC-protocol/template/types/template"
import { CategoryTree } from "@/api/ttc/telemetry/types/Category"
import { categoryTreeApi } from "@/api/ttc/telemetry"
import LinkedeSparklines from "./components/LinkedSparkLines.vue"
import { useSocket } from "@/store/socket"
import { useUserStore } from "@/store/modules/user"

defineOptions({
  // 命名当前组件
  name: "telemetry"
})

const loading = ref<boolean>(false)
const treeLoading = ref<boolean>(false)
const selectTempLoading = ref<boolean>(false)
const selectSatLoading = ref<boolean>(false)
const categoryTreeRef = ref<TreeInstance>()
const categoryName = ref("")
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
  type: "",
  offsetByte: undefined,
  offsetBit: undefined,
  lengthByte: undefined,
  lengthBit: undefined,
  dataType: "",
  base: "",
  formula: "",
  status: true,
  updateTime: "",
  defaultValue: ""
}

const dialogVisible = ref<boolean>(false)
const dialogParameterVisible = ref<boolean>(false)

const formRef = ref<FormInstance | null>(null)
const formParameterRef = ref<FormInstance | null>(null)

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
  formula: [{ required: true, trigger: "blur", message: "请输入公式" }]
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

//#region 查
const tableData = ref<GetParameterData[]>([])
const categoryOptions = ref<CategoryTree[]>([])
const searchFormRef = ref<FormInstance | null>(null)
const searchData = reactive({
  mode: "REALTIME",
  id: "",
  start: "",
  end: ""
})

const getTableData = () => {
  console.log("查询数据")
  loading.value = true

  // TODO: 实时模式下订阅websocket
  getTableDataApi({
    pageNum: searchData.mode == "REALTIME" ? 1 : paginationData.currentPage,
    pageSize: searchData.mode == "REALTIME" ? 1000 : paginationData.pageSize,
    id: searchData.id || undefined,
    mode: searchData.mode || undefined,
    start: searchData.mode || undefined,
    end: searchData.mode || undefined
  })
    .then(({ data }) => {
      console.log("获取数据", data)
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
const getTreeData = () => {
  treeLoading.value = true

  categoryTreeApi()
    .then(({ data }) => {
      categoryOptions.value = data
    })
    .catch(() => {
      categoryOptions.value = []
    })
    .finally(() => {
      treeLoading.value = false
    })
}

const getPacketData = () => {
  getPacketDataApi({
    protocolId: searchData.id || undefined,
    mode: searchData.mode || undefined,
    start: searchData.start || undefined,
    end: searchData.end || undefined
  })
    .then(({ data }) => {
      console.log("获取数据包", data)
      tableData.value = data.records
    })
    .catch(() => {
      tableData.value = []
    })
}
const handleSearch = () => {
  paginationData.currentPage === 1 ? getTableData() : (paginationData.currentPage = 1)
}
const resetSearch = () => {
  searchFormRef.value?.resetFields()
  handleSearch()
}

onMounted(() => {
  getTreeData()
})
//#endregion

//#region 查询基础参数
type Item = { key: number; label: string }
type SelectItem = { key: number; value: string; label: string }
const ttcTypes = ref<Array<Item>>()
const validTypes = ref<Array<Item>>()
const paramTypes = ref<Array<Item>>()
const baseTypes = ref<Array<Item>>()
const sectionTypes = ref<Array<Item>>()
const modeTypes = [
  { key: 1, label: "实时接收", value: "REALTIME" },
  { key: 2, label: "历史查询", value: "HISTORY" }
]
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

const remoteSearchTemplate = (query: string) => {}

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

const expandColumn = (row: { id: any; template: GetTemplateData }, expandedRows: string | any[]) => {
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
      // config.value.theme = activeThemeName.value === "normal" ? "default" : "hack"
    },
    { immediate: true }
  )
})

// 监听搜索条件变化
nextTick(() => {
  watch(
    () => searchData,
    () => {
      getTableData()
      if (searchData.mode === "REALTIME") {
        // 实时模式下开启TCP客户端
        getPacketData()
      }
    },
    { immediate: true, deep: true }
  )
})

//#region socket通信

const socket = useSocket()
const { username } = useUserStore()

const DEFAULT_TOPIC = "notify"
const topic = ref(DEFAULT_TOPIC)

nextTick(() => {
  watch(
    () => topic,
    () => {
      socket.destroyScoket()
      socket.wsInit("ws://127.0.0.1:9095/websocket/" + username + "/" + topic.value, topic.value)
    },
    { immediate: true, deep: true }
  )
})
//#endregion

//#region 分类树

/** 节点单击事件 */
const handleNodeClick = (data: CategoryTree) => {
  if (data.categoryName === "测控协议") {
    searchData.id = data.id
    getTableData()
    // 异步执行
    nextTick(() => {
      topic.value = data.id
    })
  } else {
    topic.value = DEFAULT_TOPIC
  }
}
/** 通过条件过滤节点  */
const filterNode = (value: string, data: any) => {
  if (!value) return true
  return data.label.includes(value)
}

/** 根据名称筛选部门树 */
watchEffect(
  () => {
    categoryTreeRef.value!.filter(categoryName.value)
  },
  {
    flush: "post" // watchEffect会在DOM挂载或者更新之前就会触发，此属性控制在DOM元素更新后运行
  }
)

//#endregion
</script>

<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :lg="4" :xs="24" style="">
        <el-card shadow="hover">
          <el-input v-model="categoryName" placeholder="请输入遥测分类名" prefix-icon="Search" clearable />
          <el-tree
            ref="categoryTreeRef"
            class="mt-2"
            default-expand-all
            node-key="id"
            :data="categoryOptions"
            :filter-node-method="filterNode"
            expand-on-click-node="true"
            @node-click="handleNodeClick"
            highlight-current
            :props="{ label: 'label', children: 'children' }"
          />
        </el-card>
      </el-col>
      <el-col :lg="20" :xs="24">
        <el-card loading="loading" shadow="hover" class="search-wrapper">
          <el-form ref="searchFormRef" :inline="true" :model="searchData">
            <el-form-item prop="mode">
              <el-select v-model="searchData.mode" style="width: 100px">
                <el-option v-for="item in modeTypes" :key="item.key" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
            <el-form-item prop="type" label="查询时间">
              <!-- <el-input v-model="searchData.type" placeholder="请输入" /> -->
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
              <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
        <el-card loading="loading" shadow="hover" v-if="searchData.mode === 'HISTORY'">
          <div class="toolbar-wrapper">
            <div style="width: 100%" />
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
              @expand-change="expandColumn"
              :row-key="getRowKeys"
              :expand-row-keys="expands"
              style="width: 100%"
              ref="tableRef"
              @row-click="clickTable"
            >
              <el-table-column type="selection" :reserve-selection="true" width="50" align="center" />
              <el-table-column type="expand" />
              <el-table-column label="参数名称" prop="name" align="center" />
              <!-- <el-table-column label="所属区域" prop="type" align="center" /> -->
              <el-table-column label="参数类型" prop="dataType" align="center" />
              <!-- <el-table-column label="参数起始字节" prop="offsetByte" align="center" />
              <el-table-column label="参数起始位" prop="offsetBit" align="center" />
              <el-table-column label="参数字节长度" prop="lengthByte" align="center" />
              <el-table-column label="参数位长度" prop="offsetBit" align="center" /> -->
              <el-table-column label="显示进制" prop="base" align="center" />
              <!-- <el-table-column label="处理公式" prop="formula" align="center" /> -->
              <el-table-column prop="status" label="状态" align="center">
                <template #default="scope">
                  <el-tag v-if="scope.row.status" type="success" effect="plain">启用</el-tag>
                  <el-tag v-else type="danger" effect="plain">禁用</el-tag>
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
        <el-card loading="loading" shadow="hover" v-else>
          <LinkedeSparklines :data="tableData" />
          <!-- <div v-for="item in tableData" :key="item.id" style="width: 250px; display: inline-block">
            <el-card style="margin-right: 5px">
              <LinkedeSparklines />
            </el-card>
          </div> -->
        </el-card>
      </el-col>
    </el-row>
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
