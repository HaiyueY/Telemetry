export interface CreateOrUpdateOrbitKepRequestData {
  satelliteId?: string
  id?: string
  epoch: string
  semiMajorAxis: string
  eccentricity: string
  inclination: string
  raan: string
  periapsis: string
  anomaly: string
  anomalyType: boolean
  cd: string
  light: string
}

export interface CreateOrUpdateOrbitTleRequestData {
  satelliteId?: string
  id?: string
  line1: string
  line2: string
}

export interface GetOrbitTleData {
  satelliteId: string
  id: string
  line1: string
  line2: string
}

export interface GetOrbitKepData {
  satelliteId: string
  id: string
  epoch: string
  semiMajorAxis: string
  eccentricity: string
  inclination: string
  raan: string
  periapsis: string
  anomaly: string
  anomalyType: boolean
  cd: string
  light: string
}

export type GetOrbitResponseData = ApiResponseData<{
  records: Object[]
  total: number
}>
