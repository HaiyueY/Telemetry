export interface CreateOrUpdateAntennaRequestData {
  id?: string
  name: string
  facilityId?: string
  altitudeMin: string
  antennaTypes: string[]
  status: boolean
}

export interface GetAntennaData {
  updateTime: string
  id: string
  name: string
  facilityId?: string
  altitudeMin: string
  antennaTypes: string[]
  status: boolean
}

export type GetAntennaResponseData = ApiResponseData<{
  list: GetAntennaData[]
  total: number
}>
