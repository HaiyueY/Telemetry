export interface CreateOrUpdateLocationRequestData {
  id?: string
  targetId?: string
  index?: number
  lon?: string
  lat?: string
  radius?: string
}

export interface GetLocationData {
  id: string
  targetId: string
  index: number
  lon: string
  lat: string
  radius: string
}

export type GetTargetResponseData = ApiResponseData<{
  records: GetLocationData[]
  total: number
}>
