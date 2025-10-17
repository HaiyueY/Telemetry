<template>
  <div>
    <EarthHeatMap style="width: 100%" />
  </div>
</template>

<style lang="scss" scoped>
.center {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
<script setup lang="ts">
import EarthHeatMap from "../../dashboard/components/EarthHeatMap.vue"
</script>
