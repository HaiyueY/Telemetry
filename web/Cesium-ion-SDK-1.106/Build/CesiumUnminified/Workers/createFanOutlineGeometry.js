define(['./Transforms-e2d4a55a', './Matrix3-41c58dde', './ComponentDatatype-cf1fa08e', './defaultValue-fe22d8c0', './Check-6ede7e26', './GeometryAttribute-13da9466', './GeometryAttributes-ad136444', './IndexDatatype-2643aa47', './VertexFormat-030f11ff', './Math-0a2ac845', './Matrix2-e1298525', './RuntimeError-ef395448', './combine-d9581036', './WebGLConstants-0b1ce7ba'], (function (Transforms, Matrix3, ComponentDatatype, defaultValue, Check, GeometryAttribute, GeometryAttributes, IndexDatatype, VertexFormat, Math$1, Matrix2, RuntimeError, combine, WebGLConstants) { 'use strict';

  let scratchCartesian = new Matrix3.Cartesian3();

  /**
   * Describes the outline of a {@link FanGeometry}.
   *
   * @alias FanOutlineGeometry
   * @ionsdk
   * @constructor
   *
   * @param {Object} options An object with the following properties:
   * @param {Spherical[]} options.directions The directions, pointing outward from the origin, that defined the fan.
   * @param {Number} [options.radius] The radius at which to draw the fan.
   * @param {Boolean} [options.perDirectionRadius] When set to true, the magnitude of each direction is used in place of a constant radius.
   * @param {Number} [options.numberOfRings=6] The number of outline rings to draw, starting from the outer edge and equidistantly spaced towards the center.
   * @param {VertexFormat} [options.vertexFormat=VertexFormat.DEFAULT] The vertex attributes to be computed.
   */
  function FanOutlineGeometry(options) {
    options = defaultValue.defaultValue(options, defaultValue.defaultValue.EMPTY_OBJECT);

    //>>includeStart('debug', pragmas.debug);
    if (!defaultValue.defined(options.directions)) {
      throw new Check.DeveloperError("options.directions is required");
    }
    if (!options.perDirectionRadius && !defaultValue.defined(options.radius)) {
      throw new Check.DeveloperError(
        "options.radius is required when options.perDirectionRadius is undefined or false."
      );
    }
    //>>includeEnd('debug');

    this._radius = options.radius;
    this._directions = options.directions;
    this._perDirectionRadius = options.perDirectionRadius;
    this._numberOfRings = defaultValue.defaultValue(options.numberOfRings, 6);
    this._vertexFormat = defaultValue.defaultValue(options.vertexFormat, VertexFormat.VertexFormat.DEFAULT);
    this._workerName = "createFanOutlineGeometry";
  }

  /**
   * Computes the geometric representation of a fan outline, including its vertices, indices, and a bounding sphere.
   *
   * @param {FanOutlineGeometry} fanGeometry A description of the fan.
   * @returns {Geometry|undefined} The computed vertices and indices.
   */
  FanOutlineGeometry.createGeometry = function (fanGeometry) {
    //>>includeStart('debug', pragmas.debug);
    if (!defaultValue.defined(fanGeometry)) {
      throw new Check.DeveloperError("fanGeometry is required");
    }
    //>>includeEnd('debug');

    const radius = fanGeometry._radius;
    const perDirectionRadius =
      defaultValue.defined(fanGeometry._perDirectionRadius) && fanGeometry._perDirectionRadius;
    const directions = fanGeometry._directions;
    const vertexFormat = fanGeometry._vertexFormat;
    const numberOfRings = fanGeometry._numberOfRings;

    let i;
    let x;
    let ring;
    let length;
    let maxRadius = 0;
    let positions;
    const directionsLength = directions.length;
    const attributes = new GeometryAttributes.GeometryAttributes();

    if (vertexFormat.position) {
      x = 0;
      length = directionsLength * 3 * numberOfRings;
      positions = new Float64Array(length);

      for (ring = 0; ring < numberOfRings; ring++) {
        for (i = 0; i < directionsLength; i++) {
          scratchCartesian = Matrix3.Cartesian3.fromSpherical(
            directions[i],
            scratchCartesian
          );
          const currentRadius = perDirectionRadius
            ? Matrix3.Cartesian3.magnitude(scratchCartesian)
            : radius;
          const ringRadius = (currentRadius / numberOfRings) * (ring + 1);
          scratchCartesian = Matrix3.Cartesian3.normalize(
            scratchCartesian,
            scratchCartesian
          );

          positions[x++] = scratchCartesian.x * ringRadius;
          positions[x++] = scratchCartesian.y * ringRadius;
          positions[x++] = scratchCartesian.z * ringRadius;
          maxRadius = Math.max(maxRadius, currentRadius);
        }
      }

      attributes.position = new GeometryAttribute.GeometryAttribute({
        componentDatatype: ComponentDatatype.ComponentDatatype.DOUBLE,
        componentsPerAttribute: 3,
        values: positions,
      });
    }

    x = 0;
    length = directionsLength * 2 * numberOfRings;
    const indices = IndexDatatype.IndexDatatype.createTypedArray(length / 3, length);

    for (ring = 0; ring < numberOfRings; ring++) {
      const offset = ring * directionsLength;
      for (i = 0; i < directionsLength - 1; i++) {
        indices[x++] = i + offset;
        indices[x++] = i + 1 + offset;
      }
      indices[x++] = i + offset;
      indices[x++] = 0 + offset;
    }

    return new GeometryAttribute.Geometry({
      attributes: attributes,
      indices: indices,
      primitiveType: GeometryAttribute.PrimitiveType.LINES,
      boundingSphere: new Transforms.BoundingSphere(Matrix3.Cartesian3.ZERO, maxRadius),
    });
  };

  var createFanOutlineGeometry = FanOutlineGeometry.createGeometry;

  return createFanOutlineGeometry;

}));
