<script setup lang="ts">
import { ref, computed, watch, nextTick } from "vue"
import BaseLinkedSparklineUnit from "./BaseLinkedSparklineUnit.vue"
import { GetParameterData } from "@/api/TTC-protocol/template/types/parameter"
import { useSocket } from "@/store/socket"
import { v } from "vxe-table"
import { l } from "node_modules/vite/dist/node/types.d-jgA8ss1A"

defineOptions({
  name: "LinkedSparkLines"
})

const socket = useSocket()

const props = defineProps({
  waffle: {
    type: Boolean,
    default: false
  },
  donut: {
    type: Boolean,
    default: false
  },
  data: {
    type: Array as () => GetParameterData[],
    default() {
      return []
    }
  }
})

function makeSparklineRandomDataset(scale = 1000, n = 24, periodName = "Period") {
  const arr = []

  for (let i = 0; i < n; i += 1) {
    arr.push({
      // period: `2026/06/05T16:03:${i + 1 < 10 ? "0" : ""}${i + 1}Z`,
      period: new Date().toISOString(),
      /**保留两位小数 */
      // value: Math.round(Math.random() * scale * 1000) / 1000
      value: 0,
      label: "--"
    })
  }

  return arr
}

const sparklines = ref([
  {
    lineColor: "#5f8aee",
    color: "#5f8aee",
    gradient: "#abc2f6",
    dataset: makeSparklineRandomDataset(10),
    title: "暂无数据",
    id: "0",
    scaleMax: 250,
    scaleMin: 0
  }
])

nextTick(() => {
  watch(
    () => props.data,
    (newData) => {
      if (newData && newData.length > 0) {
        console.log(newData.length)
        sparklines.value = []

        // 默认数据
        newData.forEach((item: GetParameterData) => {
          sparklines.value.push({
            lineColor: "#5f8aee",
            color: "#5f8aee",
            gradient: "#abc2f6",
            dataset: makeSparklineRandomDataset(10),
            title: item.name,
            id: item.id,
            scaleMax: item.scaleMax || 255,
            scaleMin: item.scaleMin || 0
          })
        })
      }
    },
    { immediate: true, deep: true }
  )
})

interface DataItem {
  paramId: string
  name: string
  origin: string
  value: number
  status: string
  updateTime: string
}

nextTick(() => {
  watch(
    () => socket.socketData,
    (msg) => {
      if (msg !== null && msg !== "null") {
        msg = msg.replace(/"paramId":\s*(\d+)/g, '"paramId":"$1"')
        console.log(msg)
        // 解析数据
        const data: DataItem[] = JSON.parse(msg, (key, value) => {
          if (key === "updateTime") {
            return new Date(value).toISOString()
          }
          if (key === "value") {
            return new Number(value).toFixed(2)
          }
          return value
        })
        console.log(data)
        // 更新sparklines数据
        sparklines.value.forEach((sparkline) => {
          data.forEach((aData: any) => {
            if (aData.paramId === sparkline.id) {
              sparkline.dataset.push({
                period: new Date().toISOString(),
                value: aData.value,
                label: aData.label
              })
              // 保持数据长度不超过24
              if (sparkline.dataset.length > 24) {
                sparkline.dataset.shift()
              }
            }
          })
        })
      }
    },
    { immediate: true, deep: true }
  )
})

const scaleMax = computed(() => {
  return Math.max(...sparklines.value.flatMap((sparkline) => Math.max(...sparkline.dataset.map((ds) => ds.value))))
})

const lastIndex = ref(sparklines.value[0].dataset.length - 1)

const selectedIndex = ref(lastIndex.value)

function hoverIndex(index: any) {
  if ([null, undefined].includes(index)) {
    selectedIndex.value = lastIndex.value
  } else {
    selectedIndex.value = index
  }
}

const selectedPeriod = computed(() => {
  if (selectedIndex.value === null || selectedIndex.value === undefined) return ""
  return sparklines.value[0].dataset[selectedIndex.value].period
})
</script>

<template>
  <span class="tabular-nums text-gray-500"> 测控时刻: {{ selectedPeriod }} </span>
  <div style="margin-top: 5px">
    <div v-for="sparkline in sparklines" :key="sparkline.title" style="display: inline-block">
      <BaseLinkedSparklineUnit
        :dataset="sparkline.dataset"
        :color="sparkline.color"
        :gradient="sparkline.gradient"
        :lineColor="sparkline.lineColor"
        :title="sparkline.title"
        :selectedIndex="selectedIndex"
        :scaleMax="sparkline.scaleMax"
        :scaleMin="sparkline.scaleMin"
        @hoverIndex="hoverIndex"
      />
    </div>
  </div>
</template>
