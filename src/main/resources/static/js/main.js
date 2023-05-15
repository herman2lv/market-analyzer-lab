import {getChartBuilder} from "./chart-bulder.js";

$.get("/api/v1/categories", processCategories);

const $toggleButton = $('.toggle-button')
const $chartButton = $('.display-chart-button')
const $inputContainer = $('.input-container')
const $inputDateStart = $('.input-date-start')
const $inputDateEnd = $('.input-date-end')
const $inputInflationType = $('.inflation-rate-type')
const $inputAveragePriceType = $('.average-price-type')
const $inputGroupCategories = $('.input-group-categories')
const $inputInflationRound = $('.input-inflation-round')
const $inputInflationGrid = $('.input-inflation-grid')
const $inputAveragePriceRound = $('.input-average-price-round')
const $inputAveragePriceTrim = $('.input-average-price-trim')
const $inputAveragePriceGrid = $('.input-average-price-grid')
const $chartAverage = $('#chart-average');
const $chartInflation = $('#chart-inflation');

const dateValue = new Date()
$inputDateEnd.val(dateValue.toISOString().substring(0,10))
dateValue.setFullYear(dateValue.getFullYear() - 1)
$inputDateStart.val(dateValue.toISOString().substring(0,10))

function processCategories(categories) {
  for (const category of categories) {
    const $elm = $(`<label>${category.display}:<input type="checkbox" name="categories" value="${category.key}"></label>`);
    $inputContainer.append($elm)
  }
}

let toggle = true
$toggleButton.on('click', (e) => {
  e.preventDefault()
  $inputContainer.find('input').prop('checked', toggle)
  toggle = !toggle
  $toggleButton.text(toggle ? 'Select All' : 'Unselect All')
})

$chartButton.on('click', (e) => {
  e.preventDefault()
  $('.chart').addClass('hidden')
  $chartAverage.empty()
  $chartInflation.empty()
  const chartProperties = createChartProperties()
  $.ajax({
    type: 'POST',
    url: '/api/v1/charts',
    data: JSON.stringify(chartProperties),
    dataType: 'json',
    contentType: 'application/json',
    success: processChartData
  })
})

function createChartProperties() {
  const props = {categories: []}
  $inputContainer.find('input').each(function () {
    const $elm = $(this)
    if ($elm.prop('checked')) {
      props.categories.push($elm.val())
    }
  })
  props.start = $inputDateStart.val()
  props.end = $inputDateEnd.val()
  return props
}


function processChartData(dataset, status, $XHR) {
  if ($XHR.status >= 200 && $XHR.status < 300) {
    console.log(dataset)
    const chartBuilderInflation = getChartBuilder($inputInflationType.val())
    const displayInflation = {
      height: 330,
      width: 1200,
      grid: $inputInflationGrid.prop('checked'),
      density: 50,
      colors: ['#027fcc'],
      tooltip: false,
      dots: true,
      dotRadius: 5,
      trim: false,
      lineWidth: 2,
      curveType: $inputInflationRound.prop('checked') ? 'rounded' : 'linear',
    }
    const datasetInflation = {labels: dataset.labels, dataSeriesList: [dataset.dataSeriesList[0]] }
    const datasetAverage= {labels: dataset.labels, dataSeriesList: [dataset.dataSeriesList[1]] }
    $('.chart').removeClass('hidden')
    chartBuilderInflation.render('chart-inflation', displayInflation, datasetInflation);
    $chartInflation.append($('<div class="chart-title">Inflation Rate</div>'))
    const displayAveragePrice = {
      height: 330,
      width: 1200,
      grid: $inputAveragePriceGrid.prop('checked'),
      density: 50,
      colors: ['#2ea42e'],
      tooltip: false,
      dots: true,
      dotRadius: 5,
      trim: $inputAveragePriceTrim.prop('checked'),
      lineWidth: 2,
      curveType: $inputAveragePriceRound.prop('checked') ? 'rounded' : 'linear',
    }
    const chartBuilderAverage = getChartBuilder($inputAveragePriceType.val())
    chartBuilderAverage.render('chart-average', displayAveragePrice, datasetAverage);
    $chartAverage.append($('<div class="chart-title">Average Price</div>'))
  } else {
    console.log('Unexpected status:', $XHR.status)
  }

}
