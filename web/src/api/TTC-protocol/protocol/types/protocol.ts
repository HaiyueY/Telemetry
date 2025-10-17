import { CreateOrUpdateParameterRequestData } from "../../template/types/parameter"
import { GetTemplateData } from "../../template/types/template"
import { GetSatelliteData } from "@/api/resource/satellite/types/satellite"

export interface CreateOrUpdateProtocolRequestData {
  id?: string
  templateId?: string
  subsystemId?: string
  type?: string
  name?: string
  rec: string
  status: boolean
  updateTime: string
}

export interface GetProtocolRequestData {
  /** 当前页码 */
  pageNum: number
  /** 查询条数 */
  pageSize: number
  /** 查询参数：协议名称 */
  name?: string
  /** 查询参数：协议类型 */
  type?: string
}

export interface GetProtocolData {
  id: string
  templateId: string
  subsystemId: string
  name: string
  satellite: GetSatelliteData
  template: GetTemplateData
  rec: string
  status: boolean
  /** 参数列表 */
  params: CreateOrUpdateParameterRequestData[]
  updateTime: string
}

export type GetProtocolResponseData = ApiResponseData<{
  records: GetProtocolData[]
  total: number
}>
