export interface CreateOrUpdateSubsystemRequestData {
  satelliteId?: string
  id?: string
  nid: string
  nameCN: string
  nameEN: string
  status: boolean
}

export interface GetSubsystemData {
  satelliteId?: string
  id: string
  nid: string
  nameCN: string
  nameEN: string
  telemetryProtocols: string[]
  commandProtocols: string[]
  status: boolean
  updateTime: string
}

export type GetSubsystemResponseData = ApiResponseData<{
  records: GetSubsystemData[]
  total: number
}>
