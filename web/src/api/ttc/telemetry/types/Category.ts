export interface CategoryTree {
  /**
   * 协议ID
   */
  id: string

  /**
   * 父级id
   */
  parentId: string | number | null

  /**
   * 协议分类名称
   */
  label: string

  /**
   * 级别名称
   */
  categoryName: string

  /**
   * 显示顺序
   */
  // orderNum: number

  /**
   * 子对象
   */
  children?: CategoryTree[]
}

export interface CategoryQuery {
  /**
   * 流程分类名称
   */
  categoryName?: string
}

export interface GetCategoryTreeRequestData {
  type?: string
}

export type GetCategoryTreeResponseData = ApiResponseData<CategoryTree[]>
