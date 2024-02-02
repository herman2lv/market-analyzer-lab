import { adjustColor, datasetsToRows, getBarPoints, getColor, getDataMaxMin, getPadding, getRectWithRoundedCorners } from './chart-util.js';
import { assignMissing } from './util.js';

/**
 * Create svg graph with X, Y axis and grid
 * @param {string} id - html chart element id
 * @param {object} display - chart display properties
 * @param {top: number, right: number, bottom: number, left: number} display.margin - chart margins
 * @param {boolean} display.horizontal - is value axis horizontal
 * @param {boolean} display.extendLabels - is label axis should be from 0 to the end or have margins
 * @param {number} display.width - chart's width
 * @param {number} display.height - chart's height
 * @param {object} data - chart data
 * @returns {{valueAxis: function, labelSubgroup: object, svg: object, width: number, labelAxis: function,
 * height: number, minVal: number, maxVal: number}} - object that contains svg graph and its properties
 */
const getXYGraph = (id, display, data) => {
  // set the dimensions and margins of the graph
  const margin = assignMissing(display.margin || {}, { top: 30, right: 30, bottom: 20, left: 60 });
  const width = display.width - margin.left - margin.right;
  const height = display.height - margin.top - margin.bottom;

  // append the svg object to the body of the page
  const svg = d3.select('#' + id)
    .append('svg')
    .attr('width', width + margin.left + margin.right)
    .attr('height', height + margin.top + margin.bottom)
    .append('g')
    .attr('transform', `translate(${margin.left},${margin.top})`);

  // Add label axis
  const labelRange = display.horizontal ? [0, height] : [0, width];
  const labelAxis = (display.extendLabels ? d3.scalePoint() : d3.scaleBand())
    .range(labelRange)
    .domain(data.labels)
    .padding(0.2);

  const labelAxisTransform = display.horizontal ? 'translate(0, 0)' : `translate(0, ${height})`;
  const labelAxisCall = display.horizontal ? d3.axisLeft(labelAxis).tickSize(0) : d3.axisBottom(labelAxis).tickSize(0);
  svg.append('g')
    .attr('transform', labelAxisTransform)
    .call(labelAxisCall)
    .select('.domain').attr('stroke', '#EBEBEB');

  // Add value axis
  const { max, min } = getDataMaxMin(data.dataSeriesList);
  let startRange = display.trim
    ? min - (max - min) * 0.1
    : Math.min(0, min - (max - min) * 0.1);
  const endRange = display.trim
    ? max + (max - min) * 0.1
    : max * 1.05;
  const valueRange = display.horizontal ? [0, width] : [height, 0];
  const valueAxis = d3.scaleLinear()
    .domain([startRange, endRange])
    .range(valueRange);

  let valueAxisCall;
  if (display.horizontal) {
    valueAxisCall = d3.axisBottom(valueAxis)
      .tickSize(display.grid ? height : 0)
      .ticks(Math.trunc(width / display.density));
  } else {
    valueAxisCall = d3.axisLeft(valueAxis)
      .tickSize(display.grid ? -width : 0)
      .ticks(Math.trunc(height / display.density));
  }

  const valueAxisTransform = display.horizontal ? `translate(0, ${display.grid ? 0 : height})` : `translate(0, 0)`;
  svg.append('g')
    .attr('transform', valueAxisTransform)
    .call(valueAxisCall)
    .select('.domain').remove();

  //subgroup scale
  const subgroups = [];
  data.dataSeriesList.forEach(set => {
    if (display.horizontal) {
      subgroups.unshift(set.label);
    } else {
      subgroups.push(set.label);
    }
  });

  const padding = getPadding(subgroups.length, labelAxis.bandwidth(), display.barMaxWidth);
  const labelSubgroupRange = display.horizontal ? [-labelAxis.bandwidth(), 0] : [0, labelAxis.bandwidth()];
  const labelSubgroup = d3.scaleBand()
    .domain(subgroups)
    .range(labelSubgroupRange)
    .padding([padding]);

  if (display.grid) {
    svg.selectAll('.tick line')
      .attr('stroke', '#EBEBEB')
      .attr('stroke-dasharray', 4)
      .attr('stroke-width', 2)
  }

  return {
    svg,
    labelSubgroup,
    labelAxis,
    valueAxis,
    width,
    height,
    minVal: startRange,
    maxVal: endRange
  };
}

/**
 * Create bar chart
 * @param {string} id - html element id
 *
 * @param {object} display - chart display properties
 * @param {number} display.height - chart total height, px
 * @param {number} display.width - chart total width, px
 * @param {number} display.barMaxWidth - max allowed width for single bar, px
 * @param {boolean} display.grid - option whether display grid or not
 * @param {number} display.density - grid line will be drawn each specified value of px
 * @param {string[]} display.colors - list of colors to be used for chart datasets
 * @param {boolean} display.tooltip - option whether display tooltip or not
 *
 * @param {object} data - chart data
 * @param {string[]} data.labels - list of labels for X asis
 * @param {Array.<{label: string, values: number[]}>} data.dataSeriesList - list of datasets
 */
const bar = (id, display, data) => {
  display.horizontal = false;
  display.extendLabels = false;
  const graph = getXYGraph(id, display, data);
  const displayBar = (value, i) => {
    const initX = graph.labelSubgroup(data.dataSeriesList[i].label);
    const initY = graph.valueAxis(value.value);
    const barWidth = graph.labelSubgroup.bandwidth();
    const barHeight = graph.height - graph.valueAxis(value.value);
    const points = getBarPoints(initX, initY, barHeight, barWidth);
    return getRectWithRoundedCorners(points, true, true, false, false);
  };
  displayBars(id, graph, display, data, false, displayBar);
};

/**
 * Create horizontal bar chart
 * @param {string} id - html element id
 *
 * @param {object} display - chart display properties
 * @param {number} display.height - chart total height, px
 * @param {number} display.width - chart total width, px
 * @param {number} display.barMaxWidth - max allowed width for single bar, px
 * @param {boolean} display.grid - option whether display grid or not
 * @param {number} display.density - grid line will be drawn each specified value of px
 * @param {string[]} display.colors - list of colors to be used for chart datasets
 * @param {boolean} display.tooltip - option whether display tooltip or not
 *
 * @param {object} data - chart data
 * @param {string[]} data.labels - list of labels for X asis
 * @param {Array.<{label: string, values: number[]}>} data.dataSeriesList - list of datasets
 */
const horizontalBar = (id, display, data) => {
  display.horizontal = true;
  display.extendLabels = false;
  const graph = getXYGraph(id, display, data);
  const displayBar = (value, i) => {
    const initX = graph.valueAxis(0);
    const initY = -graph.labelSubgroup(data.dataSeriesList[i].label) - graph.labelSubgroup.bandwidth();
    const barWidth = graph.valueAxis(value.value) - graph.valueAxis(0);
    const barHeight = graph.labelSubgroup.bandwidth();
    const points = getBarPoints(initX, initY, barHeight, barWidth);
    return getRectWithRoundedCorners(points, false, true, true, false);
  };
  displayBars(id, graph, display, data, true, displayBar);
};

/**
 * Display bars based on dataset over an existing axis graph
 * @param {string} id - html chart element id
 * @param {object} graph - axis graph
 * @param {object} display - chart display properties
 * @param {object} data - chart data
 * @param {boolean} horizontal - value axis should be horizontal or not
 * @param {function} displayBar - function that display single bar
 */
const displayBars = (id, graph, display, data, horizontal, displayBar) => {
  const rows = datasetsToRows(data);
  const rowTransform = horizontal
    ? row => `translate(0, ${graph.labelAxis(row.label)})`
    : row => `translate(${graph.labelAxis(row.label)}, 0)`;

  graph.svg.selectAll('mybar')
    .data(rows)
    .join('g')
    .attr('transform', rowTransform)
    .attr('class', 'item-group')
    .attr('data-metaChart', (row) => JSON.stringify({ label: row.label }))
    .selectAll('rect')
    .data((row) => row.value.map(value => {
      return { value, label: row.label };
    }))
    .join('path')
    .attr('d', displayBar)
    .attr('fill', (value, i) => getColor(i, display.colors))
    .attr('class', 'item')
    .attr('data-metaChart', (value, i) => {
      return JSON.stringify({
        label: value.label,
        name: data.dataSeriesList[i].label,
        value: value.value,
        color: getColor(i, display.colors)
      });
    });

  if (display.tooltip) {
    addTooltips($('#' + id));
  }
};

/**
 * Create line chart
 * @param {string} id - html element id
 *
 * @param {object} display - chart display properties
 * @param {number} display.height - chart total height, px
 * @param {number} display.width - chart total width, px
 * @param {boolean} display.grid - option whether display grid or not
 * @param {number} display.density - grid line will be drawn each specified value of px
 * @param {string[]} display.colors - list of colors to be used for chart datasets
 * @param {boolean} display.tooltip - option whether display tooltip or not
 * @param {boolean} display.dots - option whether display dots at line steps or not
 * @param {number} display.dotRadius - radius of dots at line steps
 * @param {boolean} display.area - option whether fill area under the line or not
 * @param {number} display.lineWidth - line width, px
 * @param {string} display.curveType - type of curve line ('linear', 'rounded', 'step')
 *
 * @param {object} data - chart data
 * @param {string[]} data.labels - list of labels for X asis
 * @param {Array.<{name: string, values: number[]}>} data.dataSeriesList - list of datasets
 */
const lineInternal = (id, display, data) => {
  display.horizontal = false;
  display.extendLabels = true;
  const graph = getXYGraph(id, display, data);
  const rows = datasetsToRows(data);
  const curveTypes = {
    linear: d3.curveLinear,
    rounded: d3.curveMonotoneX,
    step: d3.curveStepAfter
  }
  let curveType = curveTypes[display.curveType];
  curveType || (curveType = d3.curveLinear);

  let lineWidth = display.lineWidth;
  if (lineWidth === void 0 || lineWidth === null) {
    lineWidth = 1.5
  }

  let getAdjustment = (label) => {
    if (data.labels.length === 1) {
      return 0;
    }
    let offset = graph.labelAxis(data.labels[0]);
    let totalAdjustment = offset * 2;
    let k = data.labels.indexOf(label) / (data.labels.length - 1);
    return totalAdjustment * k - offset;
  }

  if (display.area) {
    const drawArea = (i) => d3.area()
      .curve(curveType)
      .x(d => graph.labelAxis(d.label) + getAdjustment(d.label))
      .y0(graph.valueAxis(graph.minVal))
      .y1(d => graph.valueAxis(d.value[i]));

    for (let i = 0; i < data.dataSeriesList.length; i++) {
      graph.svg
        .append('path')
        .datum(rows)
        .attr('fill', lineWidth ? adjustColor(getColor(i, display.colors), 100) : getColor(i, display.colors))
        .attr('fill-opacity', 0.5)
        .attr('d', drawArea(i));
    }
  }

  const drawLine = (i) => d3.line()
    .curve(curveType)
    .x(d => graph.labelAxis(d.label) + getAdjustment(d.label))
    .y(d => graph.valueAxis(d.value[i]));

  for (let i = 0; i < data.dataSeriesList.length; i++) {
    graph.svg
      .append('path')
      .datum(rows)
      .attr('fill', 'none')
      .attr('stroke', getColor(i, display.colors))
      .attr('stroke-width', lineWidth)
      .attr('d', drawLine(i));

    if (display.dots) {
      graph.svg
        .append('g')
        .selectAll('dot')
        .data(rows)
        .join('circle')
        .attr('cx', d => graph.labelAxis(d.label) + getAdjustment(d.label))
        .attr('cy', d => graph.valueAxis(d.value[i]))
        .attr('r', display.dotRadius ? display.dotRadius : 5)
        .attr('fill', getColor(i, display.colors));
    }
  }
}

const line = (id, display, data) => {
  display.area = false;
  lineInternal(id, display, data);
}

const area = (id, display, data) => {
  display.area = true;
  lineInternal(id, display, data);
}

const types = {
  bar,
  horizontalBar,
  line,
  area,
}

export const getChartBuilder = (type) => {
  let chart = types[type];
  if (!chart) {
    chart = types.bar;
  }
  return { render: chart };
}
