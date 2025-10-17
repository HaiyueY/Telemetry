import { GetSatelliteData } from "@/api/resource/satellite/types/satellite"
import { GetStatusData } from "./status"
import { GetTargetData } from "../../target/types/target"
import { GetLocationData } from "../../target/types/location"

export interface CreateOrUpdateRequestData {
  id?: string
  name: string
  target: string
  requestType: string
  requestTime?: string[]
  priority: string
  requestSensor: string
  imagePattern: string
  requestResolution?: string
  solarElevationMin?: string
  imageElevationMin?: string
  stereoDuration?: string
  /**
   * 卫星选择策略
   */
  assignStrategy: string
  /**
   * 执行卫星
   */
  satellites?: string[]
}

export interface GetRequestData {
  /** 当前页码 */
  pageNum: number
  /** 查询条数 */
  pageSize: number
  /** 查询参数：需求名称 */
  name?: string
}

export interface GetObservRequestData {
  id: string
  name: string
  target: string
  targetVO: GetTargetData
  requestType: string
  requestTime: string[]
  priority: string
  requestSensor: string
  imagePattern: string
  requestResolution: string
  solarElevationMin: string
  imageElevationMin: string
  stereoDuration?: string
  /** 卫星选择策略 */
  assignStrategy: string
  /** 执行卫星 */
  satellites?: GetSatelliteData[]
  /** 元任务列表 */
  metaTaskList: MetaTask[]
  status: string
  statusChain: GetStatusData[]
}

export interface MetaTask {
  id: string
  name: string
  requestId: string
  targetId: string
  satelliteId?: string
  sensorId?: string
  type: string
  targetType: string
  duration: number
  locations: GetLocationData[]
  observationStart?: string
  observationEnd?: string
  maneuverStart?: string
  maneuverEnd?: string
  pitch?: string
  roll?: string
  yaw?: string
  isPinned?: string
}

export type GetResponseData = ApiResponseData<{
  records: GetObservRequestData[]
  total: number
}>

export type GetTypesResponseData = ApiResponseData<Map<number, string>>

export type GetCzmlUrlData = ApiResponseData<string>

export type MetaTaskList = MetaTask[]
