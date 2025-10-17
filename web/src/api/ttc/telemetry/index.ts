import { request } from "@/utils/service"
import * as Protocol from "../../TTC-protocol/protocol/types/protocol"
import type * as Parameter from "../../TTC-protocol/template/types/parameter"
import type * as Category from "./types/Category"
import type * as Packet from "./types/Packet"

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
export function getTableDataApi(params: Parameter.GetParameterRequestData) {
  return request<Parameter.GetParameterResponseData>({
    url: "protocol-param-entity/page",
    method: "get",
    params
  })
}

export function getPacketDataApi(params: Packet.GetPacketRequestData) {
  return request<Parameter.GetParameterResponseData>({
    url: "packet-entity/page",
    method: "get",
    params
  })
}

/** 查询树 */
export function categoryTreeApi(params: Category.GetCategoryTreeRequestData) {
  return request<Category.GetCategoryTreeResponseData[]>({
    url: "protocol-entity/tree",
    method: "get",
    params
  })
}
