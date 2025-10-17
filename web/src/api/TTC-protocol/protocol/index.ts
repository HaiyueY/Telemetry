import { request } from "@/utils/service"
import type * as Protocol from "./types/protocol"
import type * as Parameter from "../template/types/parameter"
import type * as Satellite from "../../resource/satellite/types/satellite"

/** 增 */
export function createTableDataApi(data: Protocol.CreateOrUpdateProtocolRequestData) {
  return request({
    url: "protocol-entity",
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
    url: `protocol-entity/${id}`,
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
    url: `protocol-entity/del/batch`,
    method: "post",
    data
  })
}

/** 改 */
export function updateTableDataApi(data: Protocol.CreateOrUpdateProtocolRequestData) {
  return request({
    url: "protocol-entity",
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
export function getTableDataApi(params: Protocol.GetProtocolRequestData) {
  return request<Protocol.GetProtocolResponseData>({
    url: "protocol-entity/page",
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
