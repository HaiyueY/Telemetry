import {
  CreateOrUpdateOrbitKepRequestData,
  CreateOrUpdateOrbitTleRequestData,
  GetOrbitKepData,
  GetOrbitTleData
} from "./orbit"
import { CreateOrUpdateSubsystemRequestData, GetSubsystemData } from "./subsystem"

export interface CreateOrUpdateSatelliteRequestData {
  id?: string
  name: string
  noradId?: string
  orbitType: string
  memory: number
  subsystems: CreateOrUpdateSubsystemRequestData[]
  tle?: CreateOrUpdateOrbitTleRequestData
  kepler?: CreateOrUpdateOrbitKepRequestData
  status: boolean
}

export interface CreateConstellationRequestData {
  name: string
  satelliteTotal: number
  planeTotal: number
  interPlaneSpacing: number
  a: number
  i: number
  epoch: string
}

export interface GetSatelliteRequestData {
  /** 当前页码 */
  pageNum: number
  /** 查询条数 */
  pageSize: number
  /** 查询参数：卫星名称 */
  name?: string
  /** 查询参数：卫星编号 */
  noradId?: string
}

export interface GetSatelliteSelectData {
  /** 查询参数：模糊查询名称 */
  name?: string
}

export interface GetSatelliteData {
  updateTime: string
  noradId: string
  id: string
  orbitType: string
  sensorTypes: Array<string>
  subSystems: GetSubsystemData[]
  tle: GetOrbitTleData
  kepler: GetOrbitKepData
  status: boolean
  name: string
}

export interface GetSelectData {
  key: number
  value: string
  label: string
}

export type GetSatelliteSelectResponseData = ApiResponseData<GetSelectData[]>

export type GetSatelliteResponseData = ApiResponseData<{
  records: GetSatelliteData[]
  total: number
}>

export type GetTypesResponseData = ApiResponseData<Map<number, string>>

export type GetPatternResponseData = ApiResponseData<[string]>
