export interface ListItem {
  avatar?: string
  title: string
  datetime?: string
  description?: string
  status?: "primary" | "success" | "info" | "warning" | "danger"
  extra?: string
}

export const notifyData: ListItem[] = [
  // {
  //   avatar: "https://gw.alipayobjects.com/zos/rmsportal/OKJXDXrmkNshAMvwtvhu.png",
  //   title: "登录成功",
  //   datetime: new Date().toLocaleString(),
  //   description: "卫星运控原型系统用户登录成功"
  // }
]

export const messageData: ListItem[] = []

export const todoData: ListItem[] = []
