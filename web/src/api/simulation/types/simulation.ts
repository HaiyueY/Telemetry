export type GetCzmlUrlData = ApiResponseData<string>

export type GetScenarioId = ApiResponseData<string>

export type GetScenarioData = ApiResponseData<ScenarioData>

export interface ScenarioData {
  satelliteCount: number
  activeSatelliteCount: number
  facilityCount: number
  activeFacilityCount: number
  taskScheduledCount: number
  taskSchedulingCount: number
  newNormalRequestCount: number
  newNormalRequestDOD: string
  newEmgRequestCount: number
  newEmgRequestDOD: string
}
