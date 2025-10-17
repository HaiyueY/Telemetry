# 环境安装

## 前端环境

- IDE：vs code
  - https://code.visualstudio.com/Download
- 环境：Node.js
  - https://nodejs.org/en/download
  - 版本：v22.16.0

## 后端环境

- IDE: Idea
  - 需破解
  - https://www.jetbrains.com/idea/download/other.html
  - 版本：2024及以前
- 环境：JDK 17
  - IDE内安装

## 数据库环境

- IDE: Navicat
  - 需破解
- 环境：MySQL
  - 版本：社区版即可

# 系统启动

## 前端启动

1. 进入前端文件目录 

   ```
   cd {项目路径}/web/
   ```

2. 安装包管理器 

   ```
   npm install -g pnpm
   ```

3. 安装依赖 

   ```
   cd ./Cesium-ion-SDK-1.106/
   npm i
   #等待安装完成后
   cd ../
   pnpm i
   ```

4. 项目启动 

   ```
   pnpm dev
   ```

## 后端启动

1. 进入后端目录 

   ```
   cd {项目路径}/service/
   ```

2. 用Idea打开该目录

3. maven安装依赖

   1. Idea会自动识别maven项目，只需在编辑器中操作进行依赖安装即可

4. 启动项目

   1. 在编辑器中启动，无需配置启动参数

## 数据库导入

1. 在navicat中连接mySQL
2. 新建数据库，库名：sat_com_db
3. 选择该数据库，右键，选择运行sql文件