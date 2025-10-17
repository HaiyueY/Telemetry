/**
 * @license
 * Cesium Analytics SDK
 * Version 1.106
 *
 * Copyright 2012-2020 Cesium GS, Inc.
 * All rights reserved.
 *
 * Patents US9153063B2 US9865085B1 US9449424B2 US10592242
 * Patents pending US15/829,786 US16/850,266 US16/851,958
 *
 * Portions licensed separately.
 * See https://github.com/CesiumGS/cesium/blob/main/LICENSE.md for open-source Cesium license.
 */

typeof self>"u"&&(self={}),self.onmessage=function(a){const e=a.data.array,s=self.webkitPostMessage||self.postMessage;try{s({array:e},[e.buffer])}catch{s({})}};
