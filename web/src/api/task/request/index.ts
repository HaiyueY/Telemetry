import { request } from "@/utils/service"
import type * as Request from "./types/request"

/** 增 */
export function createTableDataApi(data: Request.CreateOrUpdateRequestData) {
  return request({
    url: "requestEntity",
    method: "post",
    data
  })
}

/** 删 */
export function deleteTableDataApi(id: string) {
  return request({
    url: `requestEntity/${id}`,
    method: "delete"
  })
}
export function deleteLocationDataApi(id: string) {
  return request({
    url: `requestEntity/${id}`,
    method: "delete"
  })
}
export function deleteMetaTaskDataApi(id: string) {
  return request({
    url: `meta-task-entity/${id}`,
    method: "delete"
  })
}

/** 改 */
export function updateTableDataApi(data: Request.CreateOrUpdateRequestData) {
  return request({
    url: "requestEntity/update",
    method: "post",
    data
  })
}
export function updateLocationDataApi(data: Request.CreateOrUpdateRequestData) {
  return request({
    url: "requestEntity",
    method: "post",
    data
  })
}

/** 查 */
export function getTableDataApi(params: Request.GetRequestData) {
  return request<Request.GetResponseData>({
    url: "requestEntity/page",
    method: "get",
    params
  })
}

/** 查需求类型 */
export function getRequestTypesDataApi() {
  return request<Request.GetTypesResponseData>({
    url: "requestEntity/getRequestTypes",
    method: "get"
  })
}

/** 查需求优先级 */
export function getPriorityDataApi() {
  return request<Request.GetTypesResponseData>({
    url: "requestEntity/getPriorities",
    method: "get"
  })
}

/** 查卫星分配策略 */
export function getAssignStrategyDataApi() {
  return request<Request.GetTypesResponseData>({
    url: "requestEntity/getAssignStrategies",
    method: "get"
  })
}

/** 查任务仿真文件路径 */
export function getCzmlUrlDataApi(data?: string) {
  return request<Request.GetCzmlUrlData>({
    url: "file/czml/" + data,
    method: "get"
  })
}
