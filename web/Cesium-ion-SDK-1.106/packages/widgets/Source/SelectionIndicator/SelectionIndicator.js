import {
  defined,
  destroyObject,
  DeveloperError,
  getElement,
} from "@cesiumgs/engine";
import { knockout } from "@cesiumgs/widgets";
import SelectionIndicatorViewModel from "./SelectionIndicatorViewModel.js";

/**
 * A widget for displaying an indicator on a selected object.
 *
 * @alias SelectionIndicator
 * @constructor
 *
 * @param {Element|string} container The DOM element or ID that will contain the widget.
 * @param {Scene} scene The Scene instance to use.
 *
 * @exception {DeveloperError} Element with id "container" does not exist in the document.
 */
function SelectionIndicator(container, scene) {
  //>>includeStart('debug', pragmas.debug);
  if (!defined(container)) {
    throw new DeveloperError("container is required.");
  }
  //>>includeEnd('debug')

  container = getElement(container);

  this._container = container;

  const el = document.createElement("div");
  el.className = "cesium-selection-wrapper";
  el.setAttribute(
    "data-bind",
    '\
style: { "top" : _screenPositionY, "left" : _screenPositionX },\
css: { "cesium-selection-wrapper-visible" : isVisible }'
  );
  container.appendChild(el);
  this._element = el;

  const svgNS = "http://www.w3.org/2000/svg";
  const trianglePath = "m -10,-40 20,0 -10,40 z";
  const circlePath =
    "m 0,-35 c -19.329966,0 -35,15.67003 -35,35 0,19.32997 15.670034,35 35,35 C 19.329966,35 35,19.32997 35,0 35,-19.32997 19.329966,-35 0,-35 z m 0,5 c 16.568542,0 30,13.43146 30,30 0,16.56854 -13.431458,30 -30,30 -16.568542,0 -30,-13.43146 -30,-30 0,-16.56854 13.431458,-30 30,-30 z";

  const svg = document.createElementNS(svgNS, "svg:svg");
  svg.setAttribute("width", 160);
  svg.setAttribute("height", 160);
  svg.setAttribute("viewBox", "0 0 160 160");

  const group = document.createElementNS(svgNS, "g");
  group.setAttribute("transform", "translate(80,80)");
  svg.appendChild(group);

  const circlePathElement = document.createElementNS(svgNS, "path");
  circlePathElement.setAttribute("class", "cesium-selection-circle");
  circlePathElement.setAttribute("d", circlePath);
  group.appendChild(circlePathElement);

  for (let i = 0; i < 4; ++i) {
    const pathElement = document.createElementNS(svgNS, "path");
    pathElement.setAttribute(
      "data-bind",
      `attr: { transform: _transform${i} }`
    );
    pathElement.setAttribute("d", trianglePath);
    group.appendChild(pathElement);
  }

  el.appendChild(svg);

  const viewModel = new SelectionIndicatorViewModel(
    scene,
    this._element,
    this._container
  );
  this._viewModel = viewModel;

  knockout.applyBindings(this._viewModel, this._element);
}

Object.defineProperties(SelectionIndicator.prototype, {
  /**
   * Gets the parent container.
   * @memberof SelectionIndicator.prototype
   *
   * @type {Element}
   */
  container: {
    get: function () {
      return this._container;
    },
  },

  /**
   * Gets the view model.
   * @memberof SelectionIndicator.prototype
   *
   * @type {SelectionIndicatorViewModel}
   */
  viewModel: {
    get: function () {
      return this._viewModel;
    },
  },
});

/**
 * @returns {boolean} true if the object has been destroyed, false otherwise.
 */
SelectionIndicator.prototype.isDestroyed = function () {
  return false;
};

/**
 * Destroys the widget.  Should be called if permanently
 * removing the widget from layout.
 */
SelectionIndicator.prototype.destroy = function () {
  const container = this._container;
  knockout.cleanNode(this._element);
  container.removeChild(this._element);
  return destroyObject(this);
};
export default SelectionIndicator;
