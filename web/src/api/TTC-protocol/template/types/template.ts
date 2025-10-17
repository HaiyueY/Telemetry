import { CreateOrUpdateParameterRequestData } from "./parameter"

export interface CreateOrUpdateTemplateRequestData {
  id?: string
  name: string
  templateType: string
  headerOffset: number
  headerLength: number
  bodyOffset: number
  bodyLength: number
  validOffset: number
  validLength: number
  validType: string
  updateTime: string
}

export interface GetTemplateRequestData {
  /** 当前页码 */
  pageNum: number
  /** 查询条数 */
  pageSize: number
  /** 查询参数：模板名称 */
  name?: string
  /** 查询参数：模板类型 */
  type?: string
}

export interface GetTemplateSelectData {
  /** 查询参数：模板名称 */
  name?: string
  /** 查询参数：模板类型 */
  type?: string
}

export interface GetTemplateData {
  id: string
  name: string
  templateType: string
  headerOffset: number
  headerLength: number
  bodyOffset: number
  bodyLength: number
  validOffset: number
  validLength: number
  validType: string
  /** 参数列表 */
  params: CreateOrUpdateParameterRequestData[]
  updateTime: string
}

export type GetTemplateResponseData = ApiResponseData<{
  records: GetTemplateData[]
  total: number
}>
