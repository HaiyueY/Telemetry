<script setup>
import { ref, computed } from "vue"
import { VueUiSparkline } from "vue-data-ui"
import { ElButton, ElText } from "element-plus"

defineOptions({
  name: "BaseLinkedSparklineUnit"
})

const props = defineProps({
  color: {
    type: String,
    default: ""
  },
  gradient: {
    type: String,
    default: ""
  },
  lineColor: {
    type: String,
    default: ""
  },
  dataset: {
    type: Array,
    default() {
      return []
    }
  },
  selectedIndex: {
    type: Number,
    default: null
  },
  scaleMax: {
    type: Number,
    default: null
  },
  title: {
    type: String,
    default: ""
  }
})

const emit = defineEmits(["hoverIndex"])

function hoverIndex({ index }) {
  emit("hoverIndex", index)
}

const config = computed(() => {
  return {
    theme: "",
    responsive: false,
    type: "line",
    downsample: { threshold: 500 },
    style: {
      chartWidth: 290,
      animation: { show: false, animationFrames: 360 },
      fontFamily: "inherit",
      backgroundColor: "transparent",
      scaleMin: null,
      scaleMax: props.scaleMax,
      line: { color: "#3A3A3A", strokeWidth: 3, smooth: false },
      bar: { borderRadius: 3, color: props.color },
      zeroLine: { color: "#2D353C", strokeWidth: 1 },
      plot: { show: true, radius: 8, stroke: "#FFFFFF", strokeWidth: 1 },
      verticalIndicator: {
        show: true,
        strokeWidth: 1.5,
        color: "#FFFFFF",
        strokeDasharray: 0
      },
      dataLabel: {
        show: false
      },
      title: {
        show: false
      },
      area: { show: true, useGradient: true, opacity: 30, color: "#FFFFFF" }
    }
  }
})

const emptyConfig = computed(() => {
  return {
    style: {
      backgroundColor: "#1A1A1A",
      color: "#CCCCCC",
      maxHeight: 500,
      animated: true,
      flow: { color: "#5c5c5c" },
      parallelCoordinatePlot: { color: "#5c5c5c" },
      sparkline: { color: "#5c5c5c", strokeWidth: 0.7 },
      line: {
        axis: { show: true, color: "#5c5c5c", strokeWidth: 0.5 },
        path: { color: "#5c5c5c", strokeWidth: 1, showPlots: true }
      },
      donutEvolution: {
        axis: { show: true, color: "#5c5c5c", strokeWidth: 0.5 },
        donuts: { strokeWidth: 0.5, color: "#5c5c5c" }
      },
      bar: {
        axis: { show: true, color: "#5c5c5c", strokeWidth: 0.5 },
        borderRadius: 0.5,
        color: "#5c5c5c",
        barWidth: 9
      },
      chestnut: { color: "#5C5C5C" },
      donut: { color: "#5c5c5c", strokeWidth: 64 },
      onion: { color: "#5c5c5c" },
      gauge: { color: "#5c5c5c" },
      quadrant: { grid: { color: "#5c5c5c", strokeWidth: 0.5 }, plots: { radius: 1.5, color: "#5c5c5c" } },
      radar: { grid: { color: "#5c5c5c", strokeWidth: 0.5 }, shapes: { color: "#5c5c5c" } },
      waffle: { color: "#5c5c5c" },
      table: { th: { color: "#5c5c5c" }, td: { color: "#5c5c5c", strokeWidth: 0.5 } },
      rating: { useSmiley: false, color: "#5c5c5c", filled: true, strokeWidth: 1, maxWidth: 200 },
      verticalBar: { axis: { show: true, color: "#5c5c5c", strokeWidth: 0.5 }, borderRadius: 0.5, color: "#5c5c5c" },
      heatmap: { cellsX: 26, cellsY: 7, color: "#5c5c5c" },
      candlesticks: {
        axis: { show: true, color: "#5c5c5c", strokeWidth: 0.5 },
        candle: { color: "#5c5c5c", strokeWidth: 1 }
      },
      pyramid: { color: "#5c5c5c" },
      wheel: { color: "#5c5c5c" },
      rings: { color: "#5c5c5c" }
    }
  }
})
</script>

<template>
  <div
    :class="`flex flex-row pl-4 py-1 max-w-[400px] min-w-[400px] rounded`"
    :style="{
      background: `radial-gradient(at bottom left, ${color}, ${gradient})`,
      margin: '0 10px 10px 0'
    }"
  >
    <div class="w-[180px] flex flex-col place-items-start justify-center">
      <el-text
        size="mediem"
        class="text-xs text-black w-[120px]"
        style="align-self: start; font-weight: bold"
        truncated
        >{{ title }}</el-text
      >
      <span v-if="![null, undefined].includes(selectedIndex)" class="text-2xl tabular-nums text-black" truncated>
        {{ dataset[selectedIndex].label }}
      </span>
      <span v-else class="text-transparent text-2xl"> - </span>
    </div>

    <VueUiSparkline :dataset="dataset" :config="config" :selectedIndex="selectedIndex" @hoverIndex="hoverIndex" />
  </div>
</template>
