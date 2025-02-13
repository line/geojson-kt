# geojson-kt

`geojson-kt` is a native Kotlin library for working with GeoJSON data that aims to fully support [RFC 7946](https://datatracker.ietf.org/doc/html/rfc7946).

In addition, it includes functions for some common spatial calculations, e.g., calculating the centroid of a polygon.

## Features

- Almost full RFC 7946 support (see table below)
  + All geometry types have been implemented.
  + Validation is incomplete in some cases (e.g., when geometry crosses the antimeridian)
- Serialization of all GeoJSON types via [Jackson](https://github.com/FasterXML/jackson).
- Common spatial calculations:
  + Polygon centroid calculation
  + PIP (point-in-polygon) check
  + Determining if two polygons intersect


### RFC 7946 Support Matrix

| RFC Section | Type / Feature                                            | Supported? | Notes                                                                                   |
|-------------|-----------------------------------------------------------|------------|-----------------------------------------------------------------------------------------|
| 3.1         | Geometry Object                                           | ✅         | Abstract Geometry class is provided.                                                    |
| 3.1.1       | Position                                                  | ✅         |                                                                                         |
| 3.1.2       | Point                                                     | ✅         |                                                                                         |
| 3.1.3       | MultiPoint                                                | ✅         |                                                                                         |
| 3.1.4       | LineString                                                | ✅         |                                                                                         |
| 3.1.5       | MultiLineString                                           | ✅         |                                                                                         |
| 3.1.6       | Polygon                                                   | ✅         |                                                                                         |
| 3.1.7       | MultiPolygon                                              | ✅         |                                                                                         |
| 3.1.8       | GeometryCollection                                        | ✅         |                                                                                         |
| 3.1.9       | Antimeridian Cutting                                      | ❌         | Support should be added for all geometry types (not required).                          |
| 3.1.10      | Uncertainty and Precision                                 | ✅         | No assumptions about the certainty of coordinate positions are made based on precision. |
| 3.2         | Feature Object                                            | ✅         |                                                                                         |
| 3.3         | Feature Collection                                        | ✅         |                                                                                         |
| 4           | Coordinate Reference System                               | ✅         | All coordinates are assumed to be in WGS 84 format.                                     |
| 5           | Bounding Box                                              | ❗️         | Can be automatically calculated from geometry. Point-order validation is missing.       |
| 5.1         | The Connecting Lines                                      | ✅         |                                                                                         |
| 5.2         | The Antimeridian                                          | ❌         | Validation needs to be added.                                                           |
| 5.3         | The Poles                                                 | ❌         | Validation needs to be added.                                                           |
| 5           | Bounding Box                                              | ✅         | Can be automatically calculated from geometry.                                          |
| 6           | Extending GeoJSON                                         | ✅         |                                                                                         |
| 6.1         | Extending GeoJSON                                         | ✅         | Additional properties are supported for Features only.                                  |
| 7           | GeoJSON Types Are Not Extensible                          | ✅         |                                                                                         |
| 7.1         | Semantics of GeoJSON Members and Types Are Not Changeable | ✅         |                                                                                         |

## Contributing

To get started, please take a look at [CONTRIBUTING.md](./CONTRIBUTING.md).

We ask that all contributors review and agree to adhere to our code of conduct prior to contributing.

- Please see [CODE_OF_CONDUCT.md](./CODE_OF_CONDUCT.md) for details.

## License

```
Copyright 2024 LY Corporation

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
