export interface CreateOrUpdatePacketRequestData {
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
  status: boolean
  updateTime: string
}

export interface GetPacketData {
  id: string
  paramId: string
  epoch: string
  name: string
  origin: string
  value: string
  status: string
  updateTime: string
}

export interface GetPacketRequestData {
  protocolId?: string
  mode?: string
  start?: string
  end?: string
}

export type GetParameterResponseData = ApiResponseData<{
  records: GetPacketData[]
  total: number
}>
