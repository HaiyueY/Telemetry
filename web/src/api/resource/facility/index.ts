import { request } from "@/utils/service"
import type * as Facility from "./types/facility"

/** 增 */
export function createTableDataApi(data: Facility.CreateOrUpdateTableRequestData) {
  return request({
    url: "facilityEntity",
    method: "post",
    data
  })
}
export function createAntennaDataApi(data: Facility.CreateOrUpdateTableRequestData) {
  return request({
    url: "antennaEntity",
    method: "post",
    data
  })
}

/** 删 */
export function deleteTableDataApi(id: string) {
  return request({
    url: `facilityEntity/${id}`,
    method: "delete"
  })
}

export function deleteAntennaDataApi(id: string) {
  return request({
    url: `antennaEntity/${id}`,
    method: "delete"
  })
}

/** 改 */
export function updateTableDataApi(data: Facility.CreateOrUpdateTableRequestData) {
  return request({
    url: "facilityEntity",
    method: "post",
    data
  })
}
export function updateAntennaDataApi(data: Facility.CreateOrUpdateTableRequestData) {
  return request({
    url: "antennaEntity",
    method: "post",
    data
  })
}

/** 查 */
export function getTableDataApi(params: Facility.GetTableRequestData) {
  return request<Facility.GetTableResponseData>({
    url: "facilityEntity/page",
    method: "get",
    params
  })
}

export function getFacilityTypesDataApi() {
  return request<Facility.GetTypesResponseData>({
    url: "facilityEntity/getFacilityTypes",
    method: "get"
  })
}
