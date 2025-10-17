import { CreateOrUpdateLocationRequestData } from "./location"

export interface CreateOrUpdateTargetRequestData {
  id?: string
  name: string
  locations: CreateOrUpdateLocationRequestData[]
  targetType: string
  targetPattern: string
}

export interface GetTargetRequestData {
  /** 当前页码 */
  pageNum: number
  /** 查询条数 */
  pageSize: number
  /** 查询参数：目标名称 */
  name?: string
}

export interface GetTargetSelectData {
  /** 查询参数：目标名称 */
  name?: string
  /** 查询参数：目标类型 */
  targetType?: string
}

export interface GetTargetData {
  id: string
  name: string
  locations: CreateOrUpdateLocationRequestData[]
  targetType: string
  targetPattern: string
}

export interface GetSelectData {
  key: number
  value: string
  label: string
}

export type GetTargetResponseData = ApiResponseData<{
  records: GetTargetData[]
  total: number
}>

export type GetTargetSelectResponseData = ApiResponseData<GetSelectData[]>

export type GetTypesResponseData = ApiResponseData<Map<number, string>>
