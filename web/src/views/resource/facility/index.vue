<script lang="ts" setup>
import { reactive, ref, watch } from "vue"
import {
  createTableDataApi,
  createAntennaDataApi,
  deleteTableDataApi,
  deleteAntennaDataApi,
  updateTableDataApi,
  updateAntennaDataApi,
  getTableDataApi,
  getFacilityTypesDataApi
} from "@/api/resource/facility"
import { type CreateOrUpdateTableRequestData, type GetTableData } from "@/api/resource/facility/types/facility"
import { type CreateOrUpdateAntennaRequestData, type GetAntennaData } from "@/api/resource/facility/types/antenna"
import { type FormInstance, type FormRules, ElMessage, ElMessageBox } from "element-plus"
import { Search, Refresh, CirclePlus, Delete, Download, RefreshRight, Plus } from "@element-plus/icons-vue"
import { usePagination } from "@/hooks/usePagination"

defineOptions({
  // 命名当前组件
  name: "Facility"
})

const loading = ref<boolean>(false)
const { paginationData, handleCurrentChange, handleSizeChange } = usePagination()

//#region 增
const DEFAULT_FORM_DATA: CreateOrUpdateTableRequestData = {
  id: undefined,
  name: "",
  lon: "",
  lat: "",
  antennas: [],
  status: true
}
const DEFAULT_FORM_ANTENNA_DATA: CreateOrUpdateAntennaRequestData = {
  id: undefined,
  name: "",
  facilityId: undefined,
  antennaTypes: [],
  altitudeMin: "",
  status: true
}
const dialogVisible = ref<boolean>(false)
const dialogAntennaVisible = ref<boolean>(false)
const formRef = ref<FormInstance | null>(null)
const formAntennaRef = ref<FormInstance | null>(null)
const formData = ref<CreateOrUpdateTableRequestData>(JSON.parse(JSON.stringify(DEFAULT_FORM_DATA)))
const formAntennaData = ref<CreateOrUpdateAntennaRequestData>(JSON.parse(JSON.stringify(DEFAULT_FORM_ANTENNA_DATA)))
const formRules: FormRules<CreateOrUpdateTableRequestData> = {
  name: [{ required: true, trigger: "blur", message: "请输入地面站名称" }],
  lon: [{ required: true, trigger: "blur", message: "请输入经度" }],
  lat: [{ required: true, trigger: "blur", message: "请输入纬度" }]
}
const formAntennaRules: FormRules<CreateOrUpdateAntennaRequestData> = {
  name: [{ required: true, trigger: "blur", message: "请输入天线名称" }],
  altitudeMin: [{ required: true, trigger: "blur", message: "请输入最小仰角" }]
}
const handleCreateOrUpdate = () => {
  formRef.value?.validate((valid: boolean, fields) => {
    if (!valid) return console.error("表单校验不通过", fields)
    loading.value = true
    console.log(formData.value)

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
  })
}
const handleAntennaCreateOrUpdate = () => {
  formAntennaRef.value?.validate((valid: boolean, fields) => {
    if (!valid) return console.error("表单校验不通过", fields)
    loading.value = true
    formData.value.antennas = [formAntennaData.value]
    const api = formAntennaData.value.id === undefined ? createAntennaDataApi : updateAntennaDataApi
    api(formData.value)
      .then(() => {
        ElMessage.success("操作成功")
        dialogAntennaVisible.value = false
        getTableData()
      })
      .finally(() => {
        loading.value = false
      })
  })
}
const resetForm = () => {
  formRef.value?.clearValidate()
  formData.value = JSON.parse(JSON.stringify(DEFAULT_FORM_DATA))
}
const resetAntennaForm = () => {
  formAntennaRef.value?.clearValidate()
  formAntennaData.value = JSON.parse(JSON.stringify(DEFAULT_FORM_ANTENNA_DATA))
}
//#endregion

//#region 删
const handleDelete = (row: GetTableData) => {
  ElMessageBox.confirm(`正在删除地面站：${row.name}，确认删除？`, "提示", {
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
const handleAntennaDelete = (row: GetAntennaData) => {
  ElMessageBox.confirm(`正在删除天线：${row.name}，确认删除？`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    deleteAntennaDataApi(row.id).then(() => {
      ElMessage.success("删除成功")
      getTableData()
    })
  })
}
//#endregion

//#region 改
const handleUpdate = (row: GetTableData) => {
  dialogVisible.value = true
  formData.value = JSON.parse(JSON.stringify(row))
}
const handleAntennaUpdate = (row: GetAntennaData, sat: GetTableData) => {
  dialogAntennaVisible.value = true
  formData.value = JSON.parse(JSON.stringify(sat))
  formAntennaData.value = JSON.parse(JSON.stringify(row))
}
//#endregion

//#region 查
const tableData = ref<GetTableData[]>([])
const searchFormRef = ref<FormInstance | null>(null)
const searchData = reactive({
  name: ""
})
const getTableData = () => {
  loading.value = true
  getTableDataApi({
    pageNum: paginationData.currentPage,
    pageSize: paginationData.pageSize,
    name: searchData.name || undefined
  })
    .then(({ data }) => {
      paginationData.total = data.total
      tableData.value = data.records
      console.log(tableData.value)
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
const facilityTypes = ref<Array<Item>>()

const getBasicData = () => {
  getFacilityTypesDataApi()
    .then(({ data }) => {
      // 将Map转化为Array
      const array: Item[] = []
      data = new Map<number, string>(Object.entries(data).map(([key, value]) => [Number(key), value]))
      data.forEach((value, key) => {
        array.push({ key: key, label: value })
      })
      facilityTypes.value = array
      // ElMessage.success("获取地面站类型成功")
    })
    .catch(() => {
      ElMessage.error("获取地面站类型失败")
      facilityTypes.value = []
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
//#endregion
/** 监听分页参数的变化 */
watch([() => paginationData.currentPage, () => paginationData.pageSize], getTableData, { immediate: true })
</script>

<template>
  <div class="app-container">
    <el-card v-loading="loading" shadow="never" class="search-wrapper">
      <el-form ref="searchFormRef" :inline="true" :model="searchData">
        <el-form-item prop="username" label="地面站名称">
          <el-input v-model="searchData.name" placeholder="请输入" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card v-loading="loading" shadow="never">
      <div class="toolbar-wrapper">
        <div>
          <el-button type="primary" :icon="CirclePlus" @click="dialogVisible = true">新增地面站</el-button>
          <el-button type="danger" :icon="Delete">批量删除</el-button>
        </div>
        <div>
          <el-tooltip content="下载">
            <el-button type="primary" :icon="Download" circle />
          </el-tooltip>
          <el-tooltip content="刷新当前页">
            <el-button type="primary" :icon="RefreshRight" circle @click="getTableData" />
          </el-tooltip>
        </div>
      </div>
      <div class="table-wrapper">
        <el-table :data="tableData" @expand-change="expandColumn" :row-key="getRowKeys" :expand-row-keys="expands">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column type="expand">
            <template #default="props">
              <div m="4">
                <h3>
                  地面站天线
                  <el-tooltip content="新增">
                    <el-button
                      size="small"
                      :icon="Plus"
                      circle
                      @click="(dialogAntennaVisible = true), (formData = JSON.parse(JSON.stringify(props.row)))"
                    />
                  </el-tooltip>
                </h3>
                <div>
                  <p m="t-0 b-2">地面站id: {{ props.row.id }}, 更新时间: {{ props.row.updateTime }}</p>
                </div>
                <el-table :data="props.row.antennas">
                  <el-table-column label="天线名称" prop="name" align="center" />
                  <el-table-column label="天线类型" prop="antennaTypes" align="center" />
                  <el-table-column label="最小仰角" prop="altitudeMin" align="center" />
                  <el-table-column prop="status" label="状态" align="center">
                    <template #default="scope">
                      <el-tag v-if="scope.row.status" type="success" effect="plain">启用</el-tag>
                      <el-tag v-else type="danger" effect="plain">禁用</el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="150" align="center">
                    <template #default="scope">
                      <el-button type="primary" text bg size="small" @click="handleAntennaUpdate(scope.row, props.row)"
                        >修改</el-button
                      >
                      <el-button type="danger" text bg size="small" @click="handleAntennaDelete(scope.row)"
                        >删除</el-button
                      >
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="地面站名称" align="center" />
          <el-table-column prop="facilityTypes" label="类型" align="center" />
          <el-table-column prop="status" label="状态" align="center">
            <template #default="scope">
              <el-tag v-if="scope.row.status" type="success" effect="plain">启用</el-tag>
              <el-tag v-else type="danger" effect="plain">禁用</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150" align="center">
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
    <!-- 新增/修改 -->
    <el-dialog
      v-model="dialogVisible"
      :title="formData.id === undefined ? '新增地面站资源' : '修改地面站属性'"
      @closed="resetForm"
      width="30%"
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px" label-position="right">
        <el-form-item prop="name" label="地面站名称">
          <el-input v-model="formData.name" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="lon" label="经度">
          <el-input-number
            :max="180"
            :min="-180"
            :step="10"
            :precision="2"
            step-strictly
            v-model="formData.lon"
            placeholder="请输入"
          />
        </el-form-item>
        <el-form-item prop="lat" label="纬度">
          <el-input-number
            :max="90"
            :min="-90"
            :step="10"
            :precision="2"
            step-strictly
            v-model="formData.lat"
            placeholder="请输入"
          />
        </el-form-item>
        <el-form-item prop="status" label="状态">
          <el-switch v-model="formData.status" active-text="启用" inactive-text="禁用" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreateOrUpdate" :loading="loading">确认</el-button>
      </template>
    </el-dialog>

    <!-- 新增/修改天线 -->
    <el-dialog
      v-model="dialogAntennaVisible"
      :title="formData.id === undefined ? '新增测站天线' : '修改测站天线'"
      @closed="resetAntennaForm"
      @open="handleDialogOpen"
      width="30%"
    >
      <el-form
        ref="formAntennaRef"
        :model="formAntennaData"
        :rules="formAntennaRules"
        label-width="100px"
        label-position="right"
      >
        <el-form-item prop="name" label="地面站名称">
          <el-input v-model="formAntennaData.name" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="antennaTypes" label="天线类型">
          <el-checkbox-group v-model="formAntennaData.antennaTypes">
            <el-checkbox v-for="item in facilityTypes" :key="item.key" :label="item.label" :value="item.label" />
          </el-checkbox-group>
        </el-form-item>
        <el-form-item prop="altitudeMin" label="最小仰角">
          <el-input-number
            :max="90"
            :min="0"
            :step="1"
            :precision="1"
            v-model="formAntennaData.altitudeMin"
            placeholder="请输入"
          />
        </el-form-item>
        <el-form-item prop="status" label="状态">
          <el-switch v-model="formData.status" active-text="启用" inactive-text="禁用" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogAntennaVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAntennaCreateOrUpdate" :loading="loading">确认</el-button>
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
</style>
