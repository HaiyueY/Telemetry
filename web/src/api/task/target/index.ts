import { request } from "@/utils/service"
import type * as Target from "./types/target"

/** 增 */
export function createTableDataApi(data: Target.CreateOrUpdateTargetRequestData) {
  return request({
    url: "targetEntity",
    method: "post",
    data
  })
}
export function createLocationDataApi(data: Target.CreateOrUpdateTargetRequestData) {
  return request({
    url: "targetEntity",
    method: "post",
    data
  })
}

/** 删 */
export function deleteTableDataApi(id: string) {
  return request({
    url: `targetEntity/${id}`,
    method: "delete"
  })
}
export function deleteLocationDataApi(id: string) {
  return request({
    url: `targetEntity/${id}`,
    method: "delete"
  })
}

/** 改 */
export function updateTableDataApi(data: Target.CreateOrUpdateTargetRequestData) {
  return request({
    url: "targetEntity",
    method: "post",
    data
  })
}
export function updateLocationDataApi(data: Target.CreateOrUpdateTargetRequestData) {
  return request({
    url: "targetEntity",
    method: "post",
    data
  })
}

/** 查 */
export function getTableDataApi(params: Target.GetTargetRequestData) {
  return request<Target.GetTargetResponseData>({
    url: "targetEntity/page",
    method: "get",
    params
  })
}

/** 根据名称类型查缩略 */
export function getTargetByName(params: Target.GetTargetSelectData) {
  return request<Target.GetTargetSelectResponseData>({
    url: "targetEntity/queryByName",
    method: "get",
    params
  })
}

/** 查目标类型 */
export function getTargetTypesDataApi() {
  return request<Target.GetTypesResponseData>({
    url: "targetEntity/getTargetTypes",
    method: "get"
  })
}
/** 查加载模式 */
export function getTargetPatternsDataApi() {
  return request<Target.GetTypesResponseData>({
    url: "targetEntity/getTargetPatterns",
    method: "get"
  })
}
