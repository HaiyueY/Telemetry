<script lang="ts" setup>
import { reactive, ref, watch } from "vue"
import { type FormInstance, type FormRules, ElMessage, ElMessageBox, ElTable } from "element-plus"
import { Search, Refresh, CirclePlus, Delete, Download, RefreshRight, Plus } from "@element-plus/icons-vue"
import { usePagination } from "@/hooks/usePagination"
import { CreateOrUpdateSatelliteRequestData } from "@/api/resource/satellite/types/satellite"
import {
  CreateOrUpdateOrbitKepRequestData,
  CreateOrUpdateOrbitTleRequestData,
  GetOrbitKepData,
  GetOrbitTleData
} from "@/api/resource/satellite/types/orbit"
import { CreateOrUpdateSubsystemRequestData, GetSubsystemData } from "@/api/resource/satellite/types/subsystem"
import { GetSatelliteData } from "@/api/resource/satellite/types/satellite"
import {
  createTableDataApi,
  createOrbitDataApi,
  createSubsystemDataApi,
  getSensorShapesDataApi,
  deleteBatchDataApi
} from "@/api/resource/satellite"
import { updateTableDataApi, updateOrbitDataApi, updateSubsystemDataApi } from "@/api/resource/satellite"
import { deleteTableDataApi, deleteSubsystemDataApi } from "@/api/resource/satellite"
import { getTableDataApi } from "@/api/resource/satellite"
import { getOrbitTypesDataApi } from "@/api/resource/satellite"
import { getImagePatternsDataApi } from "@/api/resource/satellite"
import { getSensorTypesDataApi } from "@/api/resource/satellite"
defineOptions({
  // 命名当前组件
  name: "Satellite"
})

const loading = ref<boolean>(false)
const { paginationData, handleCurrentChange, handleSizeChange } = usePagination()

//#region 增
const DEFAULT_FORM_DATA: CreateOrUpdateSatelliteRequestData = {
  id: undefined,
  name: "",
  noradId: "",
  orbitType: "",
  memory: 0,
  status: true,
  subsystems: [],
  kepler: undefined,
  tle: undefined
}
const DEFAULT_KEP_DATA: CreateOrUpdateOrbitKepRequestData = {
  id: undefined,
  satelliteId: undefined,
  epoch: "",
  semiMajorAxis: "",
  eccentricity: "",
  inclination: "",
  raan: "",
  periapsis: "",
  anomaly: "",
  anomalyType: true,
  cd: "0",
  light: "0"
}
const DEFAULT_TLE_DATA: CreateOrUpdateOrbitTleRequestData = {
  id: undefined,
  satelliteId: undefined,
  line1: "",
  line2: ""
}
const DEFAULT_SENSOR_DATA: CreateOrUpdateSubsystemRequestData = {
  id: undefined,
  satelliteId: undefined,
  nid: "0x00",
  nameCN: "",
  nameEN: "",
  status: false
}

const dialogVisible = ref<boolean>(false)
const dialogOrbitVisible = ref<boolean>(false)
const dialogSensorVisible = ref<boolean>(false)

const formRef = ref<FormInstance | null>(null)
const formTleRef = ref<FormInstance | null>(null)
const formKepRef = ref<FormInstance | null>(null)
const formSensorRef = ref<FormInstance | null>(null)

const formData = ref<CreateOrUpdateSatelliteRequestData>(JSON.parse(JSON.stringify(DEFAULT_FORM_DATA)))

const formOrbitKepData = ref<CreateOrUpdateOrbitKepRequestData>(JSON.parse(JSON.stringify(DEFAULT_KEP_DATA)))
const formOrbitTleData = ref<CreateOrUpdateOrbitTleRequestData>(JSON.parse(JSON.stringify(DEFAULT_TLE_DATA)))
const formSensorData = ref<CreateOrUpdateSubsystemRequestData>(JSON.parse(JSON.stringify(DEFAULT_SENSOR_DATA)))

const formRules: FormRules<CreateOrUpdateSatelliteRequestData> = {
  name: [{ required: true, trigger: "blur", message: "请输入卫星名称" }],
  noradId: [{ required: false, trigger: "blur", message: "请输入Norad编号" }],
  orbitType: [{ required: true, trigger: "blur", message: "请选择轨道类型" }]
}
const formOrbitKepRules: FormRules<CreateOrUpdateOrbitKepRequestData> = {
  epoch: [{ required: true, trigger: "blur", message: "请输入轨道时刻" }],
  semiMajorAxis: [{ required: true, trigger: "blur", message: "请输入轨道参数" }],
  eccentricity: [{ required: true, trigger: "blur", message: "请输入轨道参数" }],
  inclination: [{ required: true, trigger: "blur", message: "请输入轨道参数" }],
  raan: [{ required: true, trigger: "blur", message: "请输入轨道参数" }],
  periapsis: [{ required: true, trigger: "blur", message: "请输入轨道参数" }],
  anomaly: [{ required: true, trigger: "blur", message: "请输入轨道参数" }],
  anomalyType: [{ required: false, trigger: "blur", message: "请输入轨道参数" }],
  cd: [{ required: true, trigger: "blur", message: "请输入轨道参数" }],
  light: [{ required: true, trigger: "blur", message: "请输入轨道参数" }]
}
const formOrbitTleRules: FormRules<CreateOrUpdateOrbitTleRequestData> = {
  line1: [{ required: true, trigger: "blur", message: "请输入line1" }],
  line2: [{ required: true, trigger: "blur", message: "请输入line2" }]
}

const formSensorRules: FormRules<CreateOrUpdateSubsystemRequestData> = {
  nameCN: [{ required: true, trigger: "blur", message: "请输入分系统名称" }],
  nameEN: [{ required: true, trigger: "blur", message: "请输入分系统名称" }],
  nid: [{ required: true, trigger: "blur", message: "请输入分系统标识（主标识）" }]
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

const handleCreateOrUpdateOrbit = () => {
  loading.value = true
  const api = formData.value.id === undefined ? createOrbitDataApi : updateOrbitDataApi
  // 拼接接口参数
  formData.value.kepler = formOrbitKepData.value
  formData.value.tle = formOrbitTleData.value
  api(formData.value)
    .then(() => {
      ElMessage.success("操作成功")
      dialogOrbitVisible.value = false
      getTableData()
    })
    .finally(() => {
      loading.value = false
    })
}

const handleCreateOrUpdateSubsystem = () => {
  loading.value = true
  const api = formData.value.id === undefined ? createSubsystemDataApi : updateSubsystemDataApi
  // 拼接接口参数
  formData.value.subsystems = [formSensorData.value]

  console.log(formData.value)
  api(formData.value)
    .then(() => {
      ElMessage.success("操作成功")
      dialogSensorVisible.value = false
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
const handleTleNext = () => {
  formTleRef.value?.validate((valid: boolean, fields) => {
    if (!valid) return console.error("表单校验不通过", fields)
    handleCreateOrUpdateOrbit()
  })
}
const handleKepNext = () => {
  formKepRef.value?.validate((valid: boolean, fields) => {
    if (!valid) return console.error("表单校验不通过", fields)
    handleCreateOrUpdateOrbit()
  })
}
const handleSubsystemNext = () => {
  formSensorRef.value?.validate((valid: boolean, fields) => {
    if (!valid) return console.error("表单校验不通过", fields)
    handleCreateOrUpdateSubsystem()
  })
}

const resetForm = () => {
  formRef.value?.clearValidate()
  formData.value = JSON.parse(JSON.stringify(DEFAULT_FORM_DATA))
}
const resetOrbitTleForm = () => {
  resetForm()
  formTleRef.value?.clearValidate()
  formOrbitTleData.value = JSON.parse(JSON.stringify(DEFAULT_TLE_DATA))
}
const resetOrbitKepForm = () => {
  resetForm()
  formKepRef.value?.clearValidate()
  formOrbitKepData.value = JSON.parse(JSON.stringify(DEFAULT_KEP_DATA))
}
const resetSensorForm = () => {
  resetForm()
  formSensorRef.value?.clearValidate()
  formSensorData.value = JSON.parse(JSON.stringify(DEFAULT_SENSOR_DATA))
}
//#endregion

//#region 删
const handleDelete = (row: GetSatelliteData) => {
  ElMessageBox.confirm(`正在删除卫星：${row.name}，确认删除？`, "提示", {
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

const handleSensorDelete = (row: GetSubsystemData) => {
  ElMessageBox.confirm(`正在删除卫星分系统：${row.nameCN}，确认删除？`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    deleteSubsystemDataApi(row.id).then(() => {
      ElMessage.success("删除成功")
      getTableData()
    })
  })
}

const selectIdList = ref()

const handleSelectionChange = (newSelection: any[]) => {
  selectIdList.value = newSelection.map((item: GetSatelliteData) => item.id)
}

const handleBatchDelete = () => {
  ElMessageBox.confirm(`确认删除已选卫星：${selectIdList.value}？`, "提示", {
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
const handleUpdate = (row: GetSatelliteData) => {
  dialogVisible.value = true
  formData.value = JSON.parse(JSON.stringify(row))
}

const handleSensorUpdate = (row: GetSubsystemData, sat: GetSatelliteData) => {
  dialogSensorVisible.value = true
  formData.value = JSON.parse(JSON.stringify(sat))
  formSensorData.value = JSON.parse(JSON.stringify(row))
}

const handleTleUpdate = (row: GetOrbitTleData) => {
  dialogOrbitVisible.value = true
  formData.value = JSON.parse(JSON.stringify(row))
  console.log(formData.value)

  formOrbitTleData.value = formData.value.tle ? formData.value.tle : DEFAULT_TLE_DATA
}

const handleKeplerUpdate = (row: GetOrbitKepData) => {
  dialogOrbitVisible.value = true
  formData.value = JSON.parse(JSON.stringify(row))
  formOrbitKepData.value = formData.value.kepler ? formData.value.kepler : DEFAULT_KEP_DATA
}
//#endregion

//#region 查
const tableData = ref<GetSatelliteData[]>([])
const searchFormRef = ref<FormInstance | null>(null)
const searchData = reactive({
  satName: "",
  noradId: ""
})

const getTableData = () => {
  loading.value = true

  getTableDataApi({
    pageNum: paginationData.currentPage,
    pageSize: paginationData.pageSize,
    name: searchData.satName || undefined,
    noradId: searchData.noradId || undefined
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
const orbitTypes = ref<Array<Item>>()
const sensorTypes = ref<Array<Item>>()
const imagePatterns = ref<Array<Item>>()
const sensorShapes = ref<Array<Item>>()

const getBasicData = () => {
  getOrbitTypesDataApi()
    .then(({ data }) => {
      // 将Map转化为Array
      const array: Item[] = []
      data = new Map<number, string>(Object.entries(data).map(([key, value]) => [Number(key), value]))
      data.forEach((value, key) => {
        array.push({ key: key, label: value })
      })
      orbitTypes.value = array
      // ElMessage.success("获取轨道类型成功")
    })
    .catch(() => {
      ElMessage.error("获取轨道类型失败")
      orbitTypes.value = []
    })

  getSensorTypesDataApi()
    .then(({ data }) => {
      // 将Map转化为Array
      const array: Item[] = []
      data = new Map<number, string>(Object.entries(data).map(([key, value]) => [Number(key), value]))
      data.forEach((value, key) => {
        array.push({ key: key, label: value })
      })
      sensorTypes.value = array
      // ElMessage.success("获取分系统类型成功")
    })
    .catch(() => {
      ElMessage.error("获取分系统类型失败")
      sensorTypes.value = []
    })

  getImagePatternsDataApi()
    .then(({ data }) => {
      // 将Map转化为Array
      const array: Item[] = []
      data = new Map<number, string>(Object.entries(data).map(([key, value]) => [Number(key), value]))
      data.forEach((value, key) => {
        array.push({ key: key, label: value })
      })
      imagePatterns.value = array
      // ElMessage.success("获取成像模式成功")
    })
    .catch(() => {
      ElMessage.error("获取成像模式失败")
      imagePatterns.value = []
    })

  getSensorShapesDataApi()
    .then(({ data }) => {
      // 将Map转化为Array
      const array: Item[] = []
      data = new Map<number, string>(Object.entries(data).map(([key, value]) => [Number(key), value]))
      data.forEach((value, key) => {
        array.push({ key: key, label: value })
      })
      sensorShapes.value = array
      // ElMessage.success("获取视场形状成功")
    })
    .catch(() => {
      ElMessage.error("获取视场形状失败")
      sensorShapes.value = []
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
}
//#endregion

/** 监听分页参数的变化 */
watch([() => paginationData.currentPage, () => paginationData.pageSize], getTableData, { immediate: true })
</script>

<template>
  <div class="app-container">
    <el-card loading="loading" shadow="hover" class="search-wrapper">
      <el-form ref="searchFormRef" :inline="true" :model="searchData">
        <el-form-item prop="satName" label="卫星名称">
          <el-input v-model="searchData.satName" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="noradId" label="卫星编号">
          <el-input v-model="searchData.noradId" placeholder="请输入" />
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
          <el-button type="primary" :icon="CirclePlus" @click="dialogVisible = true">新增卫星</el-button>
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
                  <el-tooltip content="轨道配置">
                    <el-button
                      style="margin-left: 5px"
                      circle
                      bg
                      size="small"
                      @click="
                        props.row.orbitType === '轨道根数'
                          ? props.row.kepler === null
                            ? ((dialogOrbitVisible = true), (formData = JSON.parse(JSON.stringify(props.row))))
                            : handleKeplerUpdate(props.row)
                          : props.row.tle === null
                            ? ((dialogOrbitVisible = true), (formData = JSON.parse(JSON.stringify(props.row))))
                            : handleTleUpdate(props.row)
                      "
                      ><svg-icon name="Orbit_mechanics_icon" font-size="30px"
                    /></el-button>
                  </el-tooltip>
                </h3>
                <div>
                  <p m="t-0 b-2">卫星id: {{ props.row.id }}, 更新时间: {{ props.row.updateTime }}</p>
                </div>
                <h3>
                  分系统
                  <el-tooltip content="新增">
                    <el-button
                      size="small"
                      :icon="Plus"
                      circle
                      @click="(dialogSensorVisible = true), (formData = JSON.parse(JSON.stringify(props.row)))"
                    />
                  </el-tooltip>
                </h3>
                <el-table :data="props.row.subsystems">
                  <el-table-column label="分系统名称" prop="nameCN" align="center">
                    <template #default="scope">
                      {{ scope.row.nameCN }}
                      <el-tag type="info" effect="plain">{{ scope.row.nameEN }}</el-tag>
                      <!-- <el-tag type="info">{{ scope.row.nid }}</el-tag> -->
                    </template>
                  </el-table-column>
                  <el-table-column label="分系统标识（主类别）" prop="nid" align="center" />
                  <el-table-column label="遥测协议版本" prop="telemetryProtocols" align="center">
                    <template #default="scope">
                      <el-tag v-if="scope.row.telemetryProtocols.length === 0" type="info" effect="plain"
                        >未配置</el-tag
                      >
                      <el-tag
                        v-for="(item, index) in scope.row.telemetryProtocols"
                        :key="index"
                        type="primary"
                        effect="plain"
                        >{{ item }}</el-tag
                      >
                    </template>
                  </el-table-column>
                  <el-table-column label="指令协议版本" prop="commandProtocols" align="center">
                    <template #default="{ row }">
                      <el-tag v-if="row.commandProtocols.length === 0" type="info" effect="plain">未配置</el-tag>
                      <!-- 按照下划线区分，并只取最后一个 -->
                      <el-tag
                        v-for="(item, index) in row.commandProtocols"
                        :key="index"
                        type="warning"
                        effect="plain"
                        >{{ item.split("_").pop() }}</el-tag
                      >
                    </template>
                  </el-table-column>
                  <el-table-column prop="status" label="状态" align="center" width="100">
                    <template #default="scope">
                      <el-tag v-if="scope.row.status" type="success" effect="plain">启用</el-tag>
                      <el-tag v-else type="danger" effect="plain">禁用</el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="150" align="center">
                    <template #default="scope">
                      <el-button type="primary" text bg size="small" @click="handleSensorUpdate(scope.row, props.row)"
                        >修改</el-button
                      >
                      <el-button type="danger" text bg size="small" @click="handleSensorDelete(scope.row)"
                        >删除</el-button
                      >
                    </template>
                  </el-table-column>
                </el-table>

                <!-- <div v-if="props.row.maintenanceList.length != 0">
                  <h3>当日轨道维持计划</h3>
                  <el-table :data="props.row.maintenanceList">
                    <el-table-column label="最佳机动时刻" prop="epoch" align="center" />

                    <el-table-column label="脉冲方向" align="center">
                      <template #default="scope">
                        [{{ scope.row.deltaX }}, {{ scope.row.deltaY }}, {{ scope.row.deltaZ }}]
                      </template>
                    </el-table-column>
                    <el-table-column label="脉冲冲量" prop="impulse" align="center" />
                    <el-table-column label="推进剂总消耗" prop="fuelCost" align="center" />
                  </el-table>
                </div> -->
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="卫星名称" align="center" />
          <el-table-column prop="noradId" label="Norad编号" align="center" />
          <el-table-column prop="orbitType" label="轨道加载状态" align="center">
            <template #default="scope">
              <el-tag
                v-if="scope.row.orbitType === '轨道根数' ? scope.row.kepler !== null : scope.row.tle !== null"
                type="success"
                effect="plain"
                >已配置</el-tag
              >
              <el-tag v-else type="danger" effect="plain">未配置</el-tag>
              <el-tag v-if="scope.row.orbitType == '轨道根数'" type="primary">COE</el-tag>
              <el-tag v-else type="warning">TLE</el-tag>
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
    <el-dialog
      v-model="dialogVisible"
      :title="formData.id === undefined ? '新增卫星资源' : '修改卫星属性'"
      @closed="resetForm"
      @open="handleDialogOpen"
      width="30%"
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px" label-position="right">
        <el-form-item prop="name" label="卫星名称">
          <el-input v-model="formData.name" placeholder="请输入卫星名称" />
        </el-form-item>
        <el-form-item prop="noradId" label="卫星编号">
          <el-input v-model="formData.noradId" placeholder="请输入卫星Norad编号" />
        </el-form-item>
        <el-form-item prop="orbitType" label="加载方式">
          <el-radio-group v-model="formData.orbitType">
            <el-radio v-for="item in orbitTypes" :key="item.label" :label="item.label">{{ item.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="status" label="状态">
          <el-switch v-model="formData.status" active-text="启用" inactive-text="禁用" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleBasicNext" :loading="loading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 新增/修改轨道 -->
    <el-dialog
      v-model="dialogOrbitVisible"
      :title="formData.id === undefined ? '新增卫星轨道' : '修改卫星轨道'"
      @closed="formData.orbitType === '轨道根数' ? resetOrbitKepForm() : resetOrbitTleForm()"
      width="30%"
    >
      <el-form
        v-if="formData.orbitType === '两行根数'"
        ref="formTleRef"
        :model="formOrbitTleData"
        :rules="formOrbitTleRules"
        label-width="100px"
        label-position="top"
      >
        <el-form-item prop="line1" label="line1">
          <el-input v-model="formOrbitTleData.line1" type="textarea" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="line2" label="line2">
          <el-input v-model="formOrbitTleData.line2" type="textarea" placeholder="请输入" />
        </el-form-item>
      </el-form>

      <el-form
        v-else-if="formData.orbitType === '轨道根数'"
        ref="formKepRef"
        :model="formOrbitKepData"
        :rules="formOrbitKepRules"
        label-width="100px"
        label-position="right"
      >
        <el-form-item prop="epoch" label="轨道时刻">
          <el-date-picker v-model="formOrbitKepData.epoch" type="datetime" placeholder="请选择轨道时刻" />
        </el-form-item>
        <el-form-item prop="semiMajorAxis" label="半长轴">
          <el-input v-model="formOrbitKepData.semiMajorAxis" placeholder="单位: km" />
        </el-form-item>
        <el-form-item prop="eccentricity" label="偏心率">
          <el-input v-model="formOrbitKepData.eccentricity" placeholder="" />
        </el-form-item>
        <el-form-item prop="inclination" label="轨道倾角">
          <el-input v-model="formOrbitKepData.inclination" placeholder="单位: deg" />
        </el-form-item>
        <el-form-item prop="raan" label="升交点赤经">
          <el-input v-model="formOrbitKepData.raan" placeholder="单位: deg" />
        </el-form-item>
        <el-form-item prop="periapsis" label="近地点幅角">
          <el-input v-model="formOrbitKepData.periapsis" placeholder="单位: deg" />
        </el-form-item>
        <el-form-item prop="anomaly" label="近点角">
          <el-switch v-model="formOrbitKepData.anomalyType" active-text="真" inactive-text="平" />
          <el-input v-model="formOrbitKepData.anomaly" placeholder="单位: deg" />
        </el-form-item>
      </el-form>
      <el-empty v-else description="该卫星没有设置轨道配置方式" />
      <template #footer>
        <el-button @click="dialogOrbitVisible = false">取消</el-button>
        <el-button
          type="primary"
          @click="formData.orbitType === '轨道根数' ? handleKepNext() : handleTleNext()"
          :loading="loading"
          >确定</el-button
        >
      </template>
    </el-dialog>

    <!-- 新增/修改分系统 -->
    <el-dialog
      v-model="dialogSensorVisible"
      :title="formSensorData.id === undefined ? '新增分系统' : '修改分系统'"
      @open="handleDialogOpen"
      @closed="resetSensorForm"
      width="40%"
    >
      <el-form
        ref="formSensorRef"
        :model="formSensorData"
        :rules="formSensorRules"
        label-width="100px"
        label-position="right"
        :inline="true"
      >
        <el-form-item prop="nameCN" label="中文名称" style="width: 40%">
          <el-input v-model="formSensorData.nameCN" placeholder="请输入分系统名称" />
        </el-form-item>
        <el-form-item prop="nameEN" label="英文名称" style="width: 40%">
          <el-input v-model="formSensorData.nameEN" placeholder="请输入分系统名称" />
        </el-form-item>
        <el-form-item prop="nid" label="分系统标识" style="width: 40%">
          <el-input v-model="formSensorData.nid" placeholder="请输入分系统标识（主类别）" />
        </el-form-item>
        <el-form-item prop="status" label="状态">
          <el-switch v-model="formSensorData.status" active-text="启用" inactive-text="禁用" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogSensorVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubsystemNext" :loading="loading">确定</el-button>
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
