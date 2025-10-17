import * as Cesium from "cesium"

export function createCredits(viewer: Cesium.Viewer): void {
  // Create a credit with a tooltip, image and link
  const hitSatCredit = new Cesium.Credit(
    `<a href="http://www.hit.edu.cn/" target="_blank" style="align-items: center; margin-top: 5px">
      <img src="img/HITSAT.png" style="margin-left: 10px; height: 30px" title="卫星技术研究所"/>
    </a>`,
    true
  )
  // const satcloudCredit = new Cesium.Credit(
  //   `<a href="http://www.hit.edu.cn" target="_blank" style="align-items: center; margin-top: 5px">
  //     <img src="img/hit_logo.png" style="margin-left: 10px; height: 30px;" title="哈尔滨工业大学"/>
  //   </a>`,
  //   true
  // )
  // viewer.creditDisplay.addStaticCredit(satcloudCredit)
  viewer.creditDisplay.addStaticCredit(hitSatCredit)
}

export function createSelectionIndicator(viewer: Cesium.Viewer): void {
  const selectionSvg = viewer.selectionIndicator.viewModel.selectionIndicatorElement.getElementsByTagName(
    "svg:svg"
  )[0] as SVGElement
  selectionSvg.innerHTML =
    '<g transform="translate(80,80)"><path data-bind="attr: { transform: _transform }" d="M -34 -34 L -34 -11.25 L -30 -15.25 L -30 -30 L -15.25 -30 L -11.25 -34 L -34 -34 z M 11.25 -34 L 15.25 -30 L 30 -30 L 30 -15.25 L 34 -11.25 L 34 -34 L 11.25 -34 z M -34 11.25 L -34 34 L -11.25 34 L -15.25 30 L -30 30 L -30 15.25 L -34 11.25 z M 34 11.25 L 30 15.25 L 30 30 L 15.25 30 L 11.25 34 L 34 34 L 34 11.25 z" transform="scale(1)"></path></g>' //修改选择器外观
  selectionSvg.style.fill = "#00FFFF"
}

export const defaultCredit: Cesium.Credit = new Cesium.Credit(
  `<a href="http://www.hrbeu.edu.cn/" target="_blank" style="align-items: center; margin-top: 5px">
    <img src="src/assets/layouts/logo-satellite.png" style="margin-left: 10px; height: 30px" title="卫星技术研究所"/>
  </a>`,
  true
)
