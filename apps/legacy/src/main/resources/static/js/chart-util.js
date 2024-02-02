/**
 * Returns min and max values from all datasets
 * @param {object} dataSeriesList
 * @returns {{min: number, max: number}}
 */
export const getDataMaxMin = (dataSeriesList) => {
  let max = +dataSeriesList[0].values[0];
  let min = max;
  dataSeriesList.forEach(set => {
    set.values.forEach(val => {
      if (val > max) {
        max = +val;
      }
      if (val < min) {
        min = +val;
      }
    })
  });
  return { min, max };
}
/**
 * Calculates padding for X asis with condition that bar should not exceed max width
 * @param datasetsNumber - number of datasets to be displayed in one group
 * @param bandwidth - width of the area for one group at X axis
 * @param barMaxWidth - max acceptable width of the bar
 * @returns {number} calculated padding
 */
export const getPadding = (datasetsNumber, bandwidth, barMaxWidth) => {
  let padding = datasetsNumber > 1 ? 0.05 : 0;
  const barWidth = bandwidth * (1 - padding) / (datasetsNumber + padding);
  if (barWidth > barMaxWidth) {
    padding = (bandwidth - datasetsNumber * barMaxWidth) / (barMaxWidth + bandwidth);
  }
  return padding;
}
/**
 * Reorganize data from datasets to x asis oriented rows
 * @param {object} data - chart data
 * @param {string[]} data.labels - list of labels for X asis
 * @param {Array.<{name: string, values: number[]}>} data.dataSeriesList - list of datasets
 * @returns {{label: string, value: number}[]}
 */
export const datasetsToRows = (data) => {
  const rows = [];
  for (let i = 0; i < data.labels.length; i++) {
    const value = [];
    data.dataSeriesList.forEach(set => value.push(set.values[i]));
    rows.push({
      label: data.labels[i],
      value
    });
  }
  return rows;
};
/**
 * Calculate coordinates of the all 4 points for rectangular
 * @param {number} topLeftX top left rect corner X asis coordinates
 * @param {number} topLeftY top left rect corner Y asis coordinates
 * @param {number} barHeight height of the rectangular
 * @param {number} barWidth width of the rectangular
 * @returns {{bottomLeft: {x: number, y: number}, bottomRight: {x: number, y: number}, topLeft: {x: number, y: number}, topRight: {x: number, y: number}}}
 */
export const getBarPoints = (topLeftX, topLeftY, barHeight, barWidth) => {
  return {
    topLeft: {
      x: topLeftX,
      y: topLeftY,
    },
    bottomLeft: {
      x: topLeftX,
      y: topLeftY + barHeight,
    },
    bottomRight: {
      x: topLeftX + barWidth,
      y: topLeftY + barHeight,
    },
    topRight: {
      x: topLeftX + barWidth,
      y: topLeftY,
    }
  }
};
/**
 * Creates SVG Path of rectangular shape with defined rounded corners
 * @param {{bottomLeft: {x: number, y: number}, bottomRight: {x: number, y: number}, topLeft: {x: number, y: number}, topRight: {x: number, y: number}}} points
 * @param {boolean} [topLeft=true] - should be the corner rounded or not
 * @param {boolean} [topRight=true] - should be the corner rounded or not
 * @param {boolean} [bottomRight=true] - should be the corner rounded or not
 * @param {boolean} [bottomLeft=true] - should be the corner rounded or not
 * @returns {object} - SVG path representing desired shape
 */
export const getRectWithRoundedCorners = (points, topLeft = true, topRight = true, bottomRight = true, bottomLeft = true) => {
  const path = d3.path()
  path.moveTo(points.bottomLeft.x, points.bottomLeft.y)
  if (topLeft) {
    path.arcTo(points.topLeft.x, points.topLeft.y, points.topRight.x, points.topRight.y, 5)
  } else {
    path.lineTo(points.topLeft.x, points.topLeft.y);
  }
  if (topRight) {
    path.arcTo(points.topRight.x, points.topRight.y, points.bottomRight.x, points.bottomRight.y, 5)
  } else {
    path.lineTo(points.topRight.x, points.topRight.y);
  }
  if (bottomRight) {
    path.arcTo(points.bottomRight.x, points.bottomRight.y, points.bottomLeft.x, points.bottomLeft.y, 5);
  } else {
    path.lineTo(points.bottomRight.x, points.bottomRight.y);
  }
  if (bottomLeft) {
    path.arcTo(points.bottomLeft.x, points.bottomLeft.y, points.topLeft.x, points.topLeft.y, 5);
  }
  path.closePath();
  return path;
};
/**
 * Creates SVG Path of rectangular shape with rounded top left and top right corners
 * @param {{bottomLeft: {x: number, y: number}, bottomRight: {x: number, y: number}, topLeft: {x: number, y: number}, topRight: {x: number, y: number}}} points
 */
export const getRect = (points) => {
  return getRectWithRoundedCorners(points, false, false, false, false);
}
/**
 * Adjust color on a linear-based algorithm
 * @param {string} color - HEX string color representation
 * @param {number} amount - adjustment value
 * @returns {string} - HEX string color representation
 */
export const adjustColor = (color, amount) => {
  return '#' + color.replace(/^#/, '')
    .replace(/../g, color => {
      let str = Math.min(255, Math.max(0, parseInt(color, 16) + amount)).toString(16);
      return str.substring(str.length - 2);
    });
}
/**
 * Get color from list based on dataset
 * @param {number} datasetIndex - zero-based dataset number
 * @param {string[]} colors - list of available colors
 * @returns {string} - selected color
 */
export const getColor = (datasetIndex, colors) => {
  const i = datasetIndex % colors.length;
  let color = colors[i];
  if (datasetIndex >= colors.length) {
    let above = datasetIndex - colors.length + 1;
    color = adjustColor(color, above * 50);
  }
  return color;
}
