var stompClientPosition = null;
var stompClientCandlestickChart = null;

var chart = null;
var seriesS1Data = null;
var seriesPositionsLong = null;
var seriesPositionsShort = null;
var seriesPositionsClose = null;
var seriesPositionsTrailling = null;

var maxNumCandlesToDisplay = 100;

function connectandlestickChartWS() {
    var socket = new SockJS('/strader-websocket');
    stompClientCandlestickChart = Stomp.over(socket);
    stompClientCandlestickChart.connect({}, function (frame) {
        stompClientCandlestickChart.subscribe('/topic/candlestick', function (candlestick) {
        	var candle = JSON.parse(candlestick.body);
        	updateChart(candle);
        });
    });
}

function updateChart(candle){
	if (candle.granularityType == "S1"){
		if ( seriesS1Data.data.length > maxNumCandlesToDisplay ) {
			seriesS1Data.addPoint([+candle.time, parseFloat(candle.o), parseFloat(candle.h), parseFloat(candle.l), parseFloat(candle.c)], true, true);
			if (seriesS1Data.xData[0] > seriesPositionsLong.xData[0]){
				seriesPositionsLong.removePoint(0);
			} else if (seriesS1Data.xData[0] > seriesPositionsShort.xData[0]){
				seriesPositionsShort.removePoint(0);
			} else if (seriesS1Data.xData[0] > seriesPositionsClose.xData[0]){
				seriesPositionsClose.removePoint(0);
			} else if (seriesS1Data.xData[0] > seriesPositionsTrailling.xData[0]){
				seriesPositionsTrailling.removePoint(0);
			}
		} else {
			seriesS1Data.addPoint([+candle.time, parseFloat(candle.o), parseFloat(candle.h), parseFloat(candle.l), parseFloat(candle.c)], true, false);
		}
	} else if (candle.granularityType == "M1"){
		console.log( "Candle M1 update!" );
	}
}


function connectPositionsWS() {
    var socket = new SockJS('/strader-websocket');
    stompClientPosition = Stomp.over(socket);
    stompClientPosition.connect({}, function (frame) {
        stompClientPosition.subscribe('/topic/positions', function (position) {
        	showNewPosition(JSON.parse(position.body));
        });
    });
}

function sendName() {
	stompClientPosition.send("/ws/position/example", {}, $("#name").val());
}

function showNewPosition(position) {
	if ( position.type == "LONG") {
		if ( seriesPositionsLong.data.length > maxNumCandlesToDisplay ) {
			seriesPositionsLong.addPoint([+position.time,parseFloat(position.price)], true, true);
    	} else {
    		seriesPositionsLong.addPoint([+position.time,parseFloat(position.price)], true, false);
    	}
		// $("#positions").prepend("<tr><td>OPEN LONG " + position.price +
		// "</td></tr>");
		
	} else if ( position.type == "SHORT"){	
		if ( seriesPositionsShort.data.length > maxNumCandlesToDisplay ) {
			seriesPositionsShort.addPoint([+position.time,parseFloat(position.price)], true, true);
    	} else {
    		seriesPositionsShort.addPoint([+position.time,parseFloat(position.price)], true, false);
    	}
		// $("#positions").prepend("<tr><td>OPEN SHORT " + position.price +
		// "</td></tr>");
		
    } else if ( position.type == "STOP_LOSS"){
    	if ( seriesPositionsClose.data.length > maxNumCandlesToDisplay ) {
    		seriesPositionsClose.addPoint([+position.time,parseFloat(position.price)], true, true);
    	} else {
    		seriesPositionsClose.addPoint([+position.time,parseFloat(position.price)], true, false);
    	}

		// $("#positions").prepend("<tr><td>STOP LOSS " + position.price +
		// "</td></tr>");
		
    } else if ( position.type == "TRAILLING_STOP"){
    	if ( seriesPositionsTrailling.data.length > maxNumCandlesToDisplay ) {
    		seriesPositionsTrailling.addPoint([+position.time,parseFloat(position.price)], true, true);
    	} else {
    		seriesPositionsTrailling.addPoint([+position.time,parseFloat(position.price)], true, false);
    	}
		// $("#positions").prepend("<tr><td>Trailling Stop " + position.price +
		// "</td></tr>");
    	
    } else if ( position.partial < 0 ){
    	$("#positions").prepend("<tr><td>" + position.partialType + "</td><td><span style=\"color: red;\">" + parseFloat(position.partial) + "</span></td><td>" + parseFloat(position.total) + "</td><td>" + +position.winPositions + "</td><td>" + +position.losePositions + "</td></tr>");
    } else if ( position.partial > 0 ){
    	$("#positions").prepend("<tr><td>" + position.partialType + "</td><td><span style=\"color: green;\">" + parseFloat(position.partial) + "</span></td><td>" + parseFloat(position.total) + "</td><td>" + +position.winPositions + "</td><td>" + +position.losePositions + "</td></tr>");
    }
	
}

function showCandlestick() {
	
	// var ADBE = [
	// [1317888000000,372.5101,375,372.2,372.52],
	// [1317888000000,372.5101,375,372.2,372.52]];

	// var S1Data = [
	// [1479340810000,1.07055,1.07055,1.07055,1.07055],
	// [1479340670000,1.070645,1.070645,1.070645,1.070645]
	// ];
	
	// create the chart
	chart = Highcharts.stockChart('container', {


        title: {
            text: 'AAPL stock price by minute'
        },

        rangeSelector: {
            buttons: [{
                type: 'hour',
                count: 1,
                text: '1h'
            }, {
                type: 'day',
                count: 1,
                text: '1D'
            }, {
                type: 'all',
                count: 1,
                text: 'All'
            }],
            selected: 1
            // ,
            // inputEnabled: false
        },

        series: [{
            name: 'S1',
            type: 'candlestick',
            // data: S1Data
        },
        {
            name: 'PositionLong',
            // type: 'line',
            lineWidth: 0,
            // data: PositionsData,
            marker: {
                enabled: true,
                symbol: 'triangle',
                radius: 10
            }
        },
        {
            name: 'PositionShort',
            // type: 'line',
            lineWidth: 0,
            // data: PositionsData,
            marker: {
                enabled: true,
                symbol: 'triangle-down',
                radius: 10
            }
        },
        {
            name: 'PositionClose',
            // type: 'line',
            lineWidth: 0,
            // data: PositionsData,
            marker: {
                enabled: true,
                symbol: 'square',
                radius: 10
            }
        },
        {
            name: 'TraillingStop',
            // type: 'line',
            lineWidth: 0,
            // data: PositionsData,
            marker: {
                enabled: true,
                symbol: 'circle',
                radius: 5
            }
        }]
    });

    // Actualizar chart con nuevos valores o una nueva serie i.. bollinger.
    // chart.addSeries({
    // name: 'ADBE',
    // data: ADBE
    // });
    
    seriesS1Data = chart.series[0];
    seriesPositionsLong = chart.series[1];
    seriesPositionsShort = chart.series[2];
    seriesPositionsClose = chart.series[3];
    seriesPositionsTrailling = chart.series[4];
    // var seriesADBE = this.series[1];
    
    // seriesS1Data.addPoint([1479340670000,1.070645,1.070645,1.070645,1.070645],
	// true, false);
    // seriesPositions.addPoint([1479340670000,1.0705], true, false);
    // seriesseriesADBE.addPoint([x, open, high, low, close], true, true);
    // [
    // [1317888000000,372.5101,375,372.2,372.52],
    // [1317888060000,372.4,373,372.01,372.16],
    // [1317888120000,372.16,372.4,371.39,371.62]]
    
    // var chart = Highcharts.charts[0];
    // chart.series[0].setData(data);

}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    
    $( "#send" ).click(function() { sendName(); });   
    
    showCandlestick();
    
    connectPositionsWS();
    connectandlestickChartWS();
    
});