export interface CreateOrUpdateParameterRequestData {
  id?: string
  protocolId?: string
  name: string
  type: string
  offsetByte?: number
  offsetBit?: number
  lengthByte?: number
  lengthBit?: number
  dataType: string
  base: string
  formula: string
  defaultValue: string
  scaleMax: number
  scaleMin: number
  status: boolean
  updateTime: string
}

export interface GetParameterRequestData {
  /** 当前页码 */
  pageNum: number
  /** 查询条数 */
  pageSize: number
  /** 查询参数：协议ID */
  id?: string
  /** 查询参数：查询模式 */
  mode?: string
  /** 查询参数：开始时间 */
  start?: string
  /** 查询参数：结束时间 */
  end?: string
}

export interface GetParameterData {
  id: string
  protocolId: string
  name: string
  type: string
  offsetByte: number
  offsetBit: number
  lengthByte: number
  lengthBit: number
  dataType: string
  base: string
  formula: string
  defaultValue: string
  scaleMax: number
  scaleMin: number
  status: boolean
  updateTime: string
}

export type GetParameterResponseData = ApiResponseData<{
  records: GetParameterData[]
  total: number
}>
