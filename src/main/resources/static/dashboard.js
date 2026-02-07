var stompClient = null;
var chart = null;
var seriesCandle = null;
var seriesBuy = null;
var seriesSell = null;
var seriesStopLoss = null;

var currentOpenPosition = null;
var closedPositionsCount = 0;

$(function () {
    connect();
    initChart();
});

function connect() {
    var socket = new SockJS('/strader-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);

        // Subscribe to Candles
        stompClient.subscribe('/topic/candlestick', function (message) {
            var candle = JSON.parse(message.body);
            updateChart(candle);
        });

        // Subscribe to Positions
        stompClient.subscribe('/topic/positions', function (message) {
            var payload = JSON.parse(message.body);
            handlePositionUpdate(payload);
        });
    }, function (error) {
        console.log("Connection lost: " + error);
        setConnected(false);
        setTimeout(connect, 5000); // Retry
    });
}

function setConnected(connected) {
    if (connected) {
        $("#connection-status").html('<span class="label label-success">Connected</span>');
    } else {
        $("#connection-status").html('<span class="label label-danger">Disconnected</span>');
    }
}

function initChart() {
    chart = Highcharts.stockChart('chart-container', {
        rangeSelector: {
            selected: 1
        },
        title: {
            text: 'EUR/USD Live Chart'
        },
        series: [{
            name: 'Candles',
            type: 'candlestick',
            data: [],
            id: 'dataseries'
        }, {
            type: 'flags',
            name: 'Buy',
            data: [],
            onSeries: 'dataseries',
            shape: 'circlepin',
            width: 16,
            color: '#00cc00',
            fillColor: '#00cc00',
            style: { color: 'white' }
        }, {
            type: 'flags',
            name: 'Sell',
            data: [],
            onSeries: 'dataseries',
            shape: 'circlepin',
            width: 16,
            color: '#cc0000',
            fillColor: '#cc0000',
            style: { color: 'white' }
        }, {
            type: 'line',
            name: 'Stop Loss',
            data: [],
            color: 'orange',
            lineWidth: 2,
            dashStyle: 'ShortDash'
        }]
    });
    seriesCandle = chart.series[0];
    seriesBuy = chart.series[1];
    seriesSell = chart.series[2];
    seriesStopLoss = chart.series[3];
}

function updateChart(candle) {
    if (candle.granularityType !== "S1") return; // Filter or handle accordingly

    var time = Number(candle.time);
    var open = parseFloat(candle.o);
    var high = parseFloat(candle.h);
    var low = parseFloat(candle.l);
    var close = parseFloat(candle.c);

    // Simple append logic for Highstock
    // In a real app, you might want to update the last point if time matches
    // But here we just addPoint. 
    // Highcharts handles updating if x matches existing point if updatePoints is true? No, usually addPoint appends.
    // Let's check provided app.js logic:
    // It checks length and removes old points.

    // Check if the last point has the same time
    var data = seriesCandle.data;
    if (data.length > 0 && data[data.length - 1].x === time) {
        // Update last point
        data[data.length - 1].update([time, open, high, low, close]);
    } else {
        seriesCandle.addPoint([time, open, high, low, close], true, data.length >= 100);
    }
}

function handlePositionUpdate(payload) {
    // Determine if it is a Result or a Position
    // Result has "partial" and "total" fields. Position has "type" and "price".

    if (payload.partial !== undefined) {
        handleResult(payload);
    } else if (payload.type !== undefined) {
        handlePosition(payload);
    }
}

function handlePosition(position) {
    var type = position.type;
    var price = parseFloat(position.price);
    var time = Number(position.time);

    logEvent("Position Update: " + type + " @ " + price);

    if (type === "LONG" || type === "SHORT") {
        // New Open Position
        currentOpenPosition = {
            type: type,
            price: price,
            stopLoss: null
        };
        updateOpenPositionDisplay();

        // Add marker to chart
        if (type === "LONG") {
            seriesBuy.addPoint({
                x: time,
                title: 'B',
                text: 'Buy: ' + price
            });
        } else {
            seriesSell.addPoint({
                x: time,
                title: 'S',
                text: 'Sell: ' + price
            });
        }

    } else if (type === "STOP_LOSS" || type === "TRAILLING_STOP") {
        // Stop Loss Triggered (Closing Position)
        if (currentOpenPosition) {
            currentOpenPosition.stopLoss = price;
            updateOpenPositionDisplay();
        }

        // Plot Stop Loss line point
        seriesStopLoss.addPoint([time, price], true, false);

        if (type === "STOP_LOSS") {
            logEvent("Stop Loss Triggered @ " + price);
        } else {
            logEvent("Trailing Stop Update @ " + price);
        }
    }
}

function handleResult(result) {
    // Position Closed
    logEvent("Position Closed. P/L: " + result.partial);

    // Clear Open Position Display
    currentOpenPosition = null;
    updateOpenPositionDisplay();

    // Update Stats
    $("#total-pl").text(parseFloat(result.total).toFixed(4));
    $("#win-count").text(result.winPositions);
    $("#loss-count").text(result.losePositions);

    // Colorize Profit/Loss
    if (parseFloat(result.total) >= 0) {
        $("#total-pl").addClass("profit").removeClass("loss");
    } else {
        $("#total-pl").addClass("loss").removeClass("profit");
    }

    // Add to Closed Positions Table
    closedPositionsCount++;
    var row = "<tr>" +
        "<td>" + closedPositionsCount + "</td>" +
        "<td>" + result.partialType + "</td>" + // Assuming partialType tells us LONG/SHORT direction of the closed trade
        "<td class='" + (result.partial >= 0 ? "profit" : "loss") + "'>" + parseFloat(result.partial).toFixed(4) + "</td>" +
        "<td>" + parseFloat(result.total).toFixed(4) + "</td>" +
        "</tr>";

    $("#closed-positions-body").prepend(row);
}

function updateOpenPositionDisplay() {
    if (currentOpenPosition) {
        $("#pos-type").text(currentOpenPosition.type);
        $("#pos-price").text(currentOpenPosition.price.toFixed(5));
        $("#pos-stoploss").text(currentOpenPosition.stopLoss ? currentOpenPosition.stopLoss.toFixed(5) : "-");

        // Contextual styling
        if (currentOpenPosition.type === "LONG") {
            $(".panel-primary .panel-heading").css("background-color", "#00cc00");
        } else {
            $(".panel-primary .panel-heading").css("background-color", "#cc0000");
        }

    } else {
        $("#pos-type").text("-");
        $("#pos-price").text("-");
        $("#pos-stoploss").text("-");
        $(".panel-primary .panel-heading").css("background-color", "#337ab7"); // Reset to default primary
    }
}

function logEvent(msg) {
    var time = new Date().toLocaleTimeString();
    $("#event-log").prepend("<li class='list-group-item'><small>[" + time + "]</small> " + msg + "</li>");
}
