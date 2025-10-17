# @cesiumgs/ion-sdk-widgets

![Cesium](https://github.com/CesiumGS/cesium/wiki/logos/Cesium_Logo_Color.jpg)

Extend your CesiumJS-based apps with additional widgets for 3D visualization and analysis.

[CesiumJS](https://github.com/CesiumGS/cesium) is a JavaScript library for creating 3D globes and 2D maps in a web browser without a plugin. It uses WebGL for hardware-accelerated graphics, and is cross-platform, cross-browser, and tuned for dynamic-data visualization.

---

[**Docs**](https://cesium.com/learn/ion-sdk/ref-doc/) :earth_americas: [**Website**](https://cesium.com/platform/cesiumjs/ion-sdk/) :earth_africa: [**Forum**](https://community.cesium.com/) :earth_asia: [**User Stories**](https://cesium.com/user-stories/)

---

## Install

`@cesiumgs/cesium-analytics` is published as ES modules with full typing support.

If you’re building your application using a module bundler such as Webpack, Parcel, or Rollup, you can install via the `@cesiumgs/cesium-analytics` GitHub package.

```sh
npm install @cesiumgs/cesium-analytics --save
```

## Usage

Import individual modules to benefit from tree shaking optimizations through most build tools:

```js
import { Viewer, viewerMeasureMixin } from "@cesiumgs/cesium-analytics";
import "@cesiumgs/cesium-analytics/Source/Widgets/widgets.css";

// Create a new CesiumJS viewer
const viewer = new Viewer("cesiumContainer");

// Extend the viewer with the Cesium ion SDK measurement widget
viewer.extend(Cesium.viewerMeasureMixin);
```

See our [Quickstart Guide](https://cesium.com/learn/cesiumjs-learn/cesiumjs-quickstart/) for more information on getting a CesiumJS app up and running.

## Community

Have questions? Ask them on the [community forum](https://community.cesium.com/).

Interested in contributing? See [CONTRIBUTING.md](https://github.com/CesiumGS/cesium/blob/main/CONTRIBUTING.md). :heart:

## License

See [sla-cesium-ion-components.pdf]("./sla-cesium-ion-components.pdf").

## Development

### Scripts

#### Build

```sh
npm run build --workspace @cesiumgs/ion-sdk-widgets
```

#### Generate TypeScript definitions

```sh
npm run build-ts --workspace @cesiumgs/ion-sdk-widgets
```

#### Run tests

```sh
npm run test --workspace @cesiumgs/ion-sdk-widgets
```

#### Run coverage report

```sh
npm run coverage --workspace @cesiumgs/ion-sdk-widgets
```
