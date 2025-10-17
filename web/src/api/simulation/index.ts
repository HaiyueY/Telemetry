import { request } from "@/utils/service"
import type * as Simulation from "./types/simulation"

/** 查场景仿真文件路径 */
export function getScenarioCzmlUrlDataApi() {
  return request<Simulation.GetCzmlUrlData>({
    url: "scenario/queryScenario",
    method: "get"
  })
}

/** 查场景态势数据 */
export function getScenarioDataApi() {
  return request<Simulation.GetScenarioData>({
    url: "scenario",
    method: "get"
  })
}

/** 更新景态势数据 */
export function updateScenarioDataApi() {
  return request<Simulation.GetScenarioData>({
    url: "scenario/update",
    method: "get"
  })
}
