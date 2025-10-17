import { request } from "@/utils/service"
import type * as Template from "./types/template"
import type * as Parameter from "./types/parameter"
import type * as Satellite from "../../resource/satellite/types/satellite"

/** 增 */
export function createTableDataApi(data: Template.CreateOrUpdateTemplateRequestData) {
  return request({
    url: "protocol-template-entity",
    method: "post",
    data
  })
}
export function createParamsDataApi(data: Parameter.CreateOrUpdateParameterRequestData) {
  return request({
    url: "protocol-param-entity",
    method: "post",
    data
  })
}

/** 删 */
export function deleteTableDataApi(id: string) {
  return request({
    url: `protocol-template-entity/${id}`,
    method: "delete"
  })
}
export function deleteParamsDataApi(id: string) {
  return request({
    url: `protocol-param-entity/${id}`,
    method: "delete"
  })
}
export function deleteBatchDataApi(data: string[]) {
  return request({
    url: `protocol-template-entity/del/batch`,
    method: "post",
    data
  })
}

/** 改 */
export function updateTableDataApi(data: Template.CreateOrUpdateTemplateRequestData) {
  return request({
    url: "protocol-template-entity",
    method: "post",
    data
  })
}
export function updateParamsDataApi(data: Parameter.CreateOrUpdateParameterRequestData) {
  return request({
    url: "protocol-param-entity",
    method: "post",
    data
  })
}

/** 查 */
export function getTableDataApi(params: Template.GetTemplateRequestData) {
  return request<Template.GetTemplateResponseData>({
    url: "protocol-template-entity/page",
    method: "get",
    params
  })
}

/** 根据名称类型查缩略 */
export function getTemplatesByParams(params: Template.GetTemplateSelectData) {
  return request<Satellite.GetSatelliteSelectResponseData>({
    url: "protocol-template-entity/queryByParams",
    method: "get",
    params
  })
}

/** 查测控类型 */
export function getTTCTypeDataApi() {
  return request<Satellite.GetTypesResponseData>({
    url: "protocol-template-entity/getTTCType",
    method: "get"
  })
}

/** 查校验类型 */
export function getValidTypeDataApi() {
  return request<Satellite.GetTypesResponseData>({
    url: "protocol-template-entity/getValidType",
    method: "get"
  })
}

/** 查参数类型 */
export function getParamTypeDataApi() {
  return request<Satellite.GetTypesResponseData>({
    url: "protocol-param-entity/getParamType",
    method: "get"
  })
}

/** 查参数类型 */
export function getSectionTypeDataApi() {
  return request<Satellite.GetTypesResponseData>({
    url: "protocol-param-entity/getSectionType",
    method: "get"
  })
}

/** 查参数类型 */
export function getBaseTypeDataApi() {
  return request<Satellite.GetTypesResponseData>({
    url: "protocol-param-entity/getBaseType",
    method: "get"
  })
}
