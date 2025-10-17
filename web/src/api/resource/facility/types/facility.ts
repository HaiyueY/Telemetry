import { CreateOrUpdateAntennaRequestData, GetAntennaData } from "./antenna"

export interface CreateOrUpdateTableRequestData {
  id?: string
  name: string
  lon: string
  lat: string
  antennas: CreateOrUpdateAntennaRequestData[]
  status: boolean
}

export interface GetTableRequestData {
  /** 当前页码 */
  pageNum: number
  /** 查询条数 */
  pageSize: number
  /** 查询参数：地面站名称 */
  name?: string
}

export interface GetTableData {
  updateTime: string
  id: string
  name: string
  lon: string
  lat: string
  facilityTypes: string[]
  antennas: GetAntennaData[]
  status: boolean
}

export type GetTableResponseData = ApiResponseData<{
  records: GetTableData[]
  total: number
}>

export type GetTypesResponseData = ApiResponseData<Map<number, string>>
