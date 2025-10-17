import { request } from "@/utils/service"
import type * as Satellite from "./types/satellite"

/** 增 */
export function createTableDataApi(data: Satellite.CreateOrUpdateSatelliteRequestData) {
  return request({
    url: "satelliteEntity",
    method: "post",
    data
  })
}
export function createOrbitDataApi(data: Satellite.CreateOrUpdateSatelliteRequestData) {
  return request({
    url: "orbitEntity",
    method: "post",
    data
  })
}
export function createSubsystemDataApi(data: Satellite.CreateOrUpdateSatelliteRequestData) {
  return request({
    url: "subsystemEntity",
    method: "post",
    data
  })
}

/** 删 */
export function deleteTableDataApi(id: string) {
  return request({
    url: `satelliteEntity/${id}`,
    method: "delete"
  })
}
export function deleteSubsystemDataApi(id: string) {
  return request({
    url: `subsystemEntity/${id}`,
    method: "delete"
  })
}
export function deleteBatchDataApi(data: string[]) {
  return request({
    url: `satelliteEntity/del/batch`,
    method: "post",
    data
  })
}

/** 改 */
export function updateTableDataApi(data: Satellite.CreateOrUpdateSatelliteRequestData) {
  return request({
    url: "satelliteEntity",
    method: "post",
    data
  })
}
export function updateOrbitDataApi(data: Satellite.CreateOrUpdateSatelliteRequestData) {
  return request({
    url: "orbitEntity",
    method: "post",
    data
  })
}
export function updateSubsystemDataApi(data: Satellite.CreateOrUpdateSatelliteRequestData) {
  return request({
    url: "subsystemEntity",
    method: "post",
    data
  })
}

/** 查 */
export function getTableDataApi(params: Satellite.GetSatelliteRequestData) {
  return request<Satellite.GetSatelliteResponseData>({
    url: "satelliteEntity/page",
    method: "get",
    params
  })
}

/** 根据名称类型查缩略 */
export function getSatellitesByParams(params: Satellite.GetSatelliteSelectData) {
  return request<Satellite.GetSatelliteSelectResponseData>({
    url: "satelliteEntity/queryByParams",
    method: "get",
    params
  })
}

/** 查轨道类型 */
export function getOrbitTypesDataApi() {
  return request<Satellite.GetTypesResponseData>({
    url: "satelliteEntity/getOrbitTypes",
    method: "get"
  })
}

/** 查载荷类型 */
export function getSensorTypesDataApi() {
  return request<Satellite.GetTypesResponseData>({
    url: "satelliteEntity/getSensorTypes",
    method: "get"
  })
}

/** 查成像模式 */
export function getImagePatternsDataApi() {
  return request<Satellite.GetTypesResponseData>({
    url: "satelliteEntity/getImagePatterns",
    method: "get"
  })
}

/** 查视场形状 */
export function getSensorShapesDataApi() {
  return request<Satellite.GetTypesResponseData>({
    url: "satelliteEntity/getSensorShapes",
    method: "get"
  })
}
