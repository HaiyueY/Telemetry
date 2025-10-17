export interface CreateOrUpdateStatusRequestData {
  id?: string
  targetId?: string
  index?: number
  lon?: string
  lat?: string
  radius?: string
}

export interface GetStatusData {
  id: string
  requestId: string
  status: string
  details: string
  updateTime: string
}

export type GetStatusResponseData = ApiResponseData<{
  records: GetStatusData[]
  total: number
}>
