package sendaTrader76.bot.strategies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sendaTrader76.bot.dto.*;
import sendaTrader76.bot.services.AccountService;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BollingerStrategy {

    private static final Logger log = LoggerFactory.getLogger(BollingerStrategy.class);

    private AccountService accountService;
    private BigDecimal openPositionPrice;
    private String positionType;

    private boolean shortPositionWait = false;
    private boolean longPositionWait = false;
    private BigDecimal totalEarnings = BigDecimal.ZERO;
    private boolean waitForcandleInsideBands = false;
    private BigDecimal stopLoss;
    private String tradeId;
    private boolean touchedBand = false;
    private StrategyResults strategyResults;

    public BollingerStrategy(AccountService accountService) {
        this.accountService = accountService;
        this.strategyResults = new StrategyResults();
    }

    public void executeTask(Price price, String accountId, InstrumentType instrument, BollingerBands bollinger, Candle lastCandle) {

        final BigDecimal currentBidPrice = price.getCloseoutAsk();
        final BigDecimal currentAskPrice = price.getCloseoutBid();
        final BigDecimal currentMidPrice = currentBidPrice.add(currentAskPrice).divide(BigDecimal.valueOf(2));

        if (isOpenPositions(accountId)) {
            setTraillingStopIfNeeded(positionType, price, accountId);
        } else {
            if (waitForcandleInsideBands) {
                if (longPositionWait
                        && (lastCandle.getMid().getL().compareTo(bollinger.getLowerBand()) > 0)
                        && (lastCandle.getMid().getO().compareTo(lastCandle.getMid().getC()) < 0)) {

                    OrderResponse orderResponse = postOrderLong(accountId, instrument, currentBidPrice);

                    if (orderResponse == null || orderResponse.getOrderFillTransaction() == null) {
                        log.info("Order closed right before opened.");
                        waitForcandleInsideBands = false;
                        shortPositionWait = false;
                        longPositionWait = false;
                        return;
                    }

                    openPositionPrice = orderResponse.getOrderFillTransaction().getPrice().bigDecimalValue();
                    tradeId = orderResponse.getOrderFillTransaction().getTradeOpened().getTradeID().toString();
                    positionType = "LONG";
                    waitForcandleInsideBands = false;
                    longPositionWait = false;
                    shortPositionWait = false;

                    log.info("Time: " + price.getTime());
                    log.info("Open Position (Long) " + openPositionPrice);
                    log.info("Stop loss: " + stopLoss);

                } else if (shortPositionWait
                        && (lastCandle.getMid().getH().compareTo(bollinger.getUpperBand()) < 0)
                        && (lastCandle.getMid().getC().compareTo(lastCandle.getMid().getO()) < 0)) {

                    OrderResponse orderResponse = postOrderShort(accountId, instrument, currentAskPrice);

                    if (orderResponse == null || orderResponse.getOrderFillTransaction() == null) {
                        log.info("Order closed right before opened.");
                        waitForcandleInsideBands = false;
                        shortPositionWait = false;
                        longPositionWait = false;
                        return;
                    }

                    openPositionPrice = orderResponse.getOrderFillTransaction().getPrice().bigDecimalValue();
                    tradeId = orderResponse.getOrderFillTransaction().getTradeOpened().getTradeID().toString();
                    positionType = "SHORT";
                    waitForcandleInsideBands = false;
                    shortPositionWait = false;
                    longPositionWait = false;

                    log.info("Time: " + price.getTime());
                    log.info("Open Position (Short) " + openPositionPrice);
                    log.info("Stop loss: " + stopLoss);
                }
            } else {
                if (lastCandleTouchedLowerBand(lastCandle, bollinger) && lastCandleTouchedUpperBand(lastCandle, bollinger)) {
                    log.info("Last candle touched upper and lower bands so ignoring.");
                } else if (lastCandleTouchedLowerBand(lastCandle, bollinger)) {
                    waitForcandleInsideBands = true;
                    longPositionWait = true;
                    shortPositionWait = false;

                    log.info("Time: " + price.getTime());
                    log.info("Waiting for candle to be inside bands before opening position. current price "
                            + currentMidPrice + "  lower band " + bollinger.getLowerBand());
                } else if (lastCandleTouchedUpperBand(lastCandle, bollinger)) {
                    waitForcandleInsideBands = true;
                    shortPositionWait = true;
                    longPositionWait = false;

                    log.info("Time: " + price.getTime());
                    log.info("Waiting for candle to be inside bands before opening position. current price "
                            + currentMidPrice + "  higuer band " + bollinger.getUpperBand());
                }
            }
        }
    }

    private void sendTradeOrder(String accountId, final BigDecimal currentPrice, String positionType) {
        TradeOrderRequest tradeOrderRequest = new TradeOrderRequest();
        StopLossOnFill stopLossOnFill = new StopLossOnFill();
        if ("LONG".equals(positionType)) {
            BigDecimal potentialStopLoss = currentPrice.subtract(new BigDecimal("0.0001"));
            if (stopLoss.compareTo(potentialStopLoss) < 0) {
                stopLoss = potentialStopLoss;
            }
        } else {
            BigDecimal potentialStopLoss = currentPrice.add(new BigDecimal("0.0001"));
            if (stopLoss.compareTo(potentialStopLoss) > 0) {
                stopLoss = potentialStopLoss;
            }
        }

        stopLossOnFill.setPrice(stopLoss.toString());
        stopLossOnFill.setTimeInForce("GTC");
        tradeOrderRequest.setStopLossOnFill(stopLossOnFill);

        log.info("Trying to update stoploss to " + stopLoss);

        accountService.postTrade(accountId, tradeOrderRequest, tradeId);
        log.info("Update Stoploss order to " + stopLoss + ", tradeId: " + tradeId);
    }

    private OrderResponse postOrderShort(String accountId, InstrumentType instrument, final BigDecimal currentAskPrice) {
        OrderRequest orderRequest = new OrderRequest();
        Order order = new Order();
        order.setInstrument(instrument.toString());
        order.setTimeInForce("FOK");
        order.setUnits("-10000");
        order.setType("MARKET");
        order.setPositionFill("DEFAULT");

        stopLoss = currentAskPrice.add(new BigDecimal("0.0003")).setScale(6, RoundingMode.HALF_DOWN);

        StopLossOnFill stopLossOnFill = new StopLossOnFill();
        stopLossOnFill.setTimeInForce("GTC");
        stopLossOnFill.setPrice(stopLoss.toString());

        order.setStopLossOnFill(stopLossOnFill);
        orderRequest.setOrder(order);
        return accountService.postOrder(accountId, orderRequest);
    }

    private OrderResponse postOrderLong(String accountId, InstrumentType instrument, final BigDecimal currentBidPrice) {
        OrderRequest orderRequest = new OrderRequest();
        Order order = new Order();
        order.setInstrument(instrument.toString());
        order.setTimeInForce("FOK");
        order.setUnits("10000");
        order.setType("MARKET");
        order.setPositionFill("DEFAULT");

        stopLoss = currentBidPrice.subtract(new BigDecimal("0.0003")).setScale(6, RoundingMode.HALF_DOWN);

        StopLossOnFill stopLossOnFill = new StopLossOnFill();
        stopLossOnFill.setTimeInForce("GTC");
        stopLossOnFill.setPrice(stopLoss.toString());

        order.setStopLossOnFill(stopLossOnFill);
        orderRequest.setOrder(order);
        return accountService.postOrder(accountId, orderRequest);
    }

    private void calculateStrategyResults() {
        log.info("StopLoss " + stopLoss + " reached");
        if ("SHORT".equals(positionType)) {
            BigDecimal winnings = openPositionPrice.subtract(stopLoss);
            if (winnings.compareTo(BigDecimal.ZERO) > 0) {
                strategyResults.increaseNumWinPosition();
                strategyResults.addPartialWin(winnings);
            } else {
                strategyResults.increaseNumLosePosition();
                strategyResults.addPartialLoss(winnings);
            }
        } else {
            BigDecimal winnings = stopLoss.subtract(openPositionPrice);
            totalEarnings = totalEarnings.add(winnings);

            if (winnings.compareTo(BigDecimal.ZERO) > 0) {
                strategyResults.increaseNumWinPosition();
                strategyResults.addPartialWin(winnings);
            } else {
                strategyResults.increaseNumLosePosition();
                strategyResults.addPartialLoss(winnings);
            }
        }
        log.info(strategyResults.toString());
    }

    private boolean isOpenPositions(String accountId) {
        OpenTrades orderTrades = accountService.getOpenTrades(accountId);
        if (orderTrades.getTrades().isEmpty()) {
            return false;
        } else {
            tradeId = orderTrades.getTrades().get(0).getId().toString();
            return true;
        }
    }

    private void setTraillingStopIfNeeded(String positionType, Price price, String accountId) {
        BigDecimal potentialEarning;
        if ("LONG".equals(positionType)) {
            BigDecimal bidPrice = price.getCloseoutBid();
            potentialEarning = bidPrice.subtract(openPositionPrice);
            log.info("Potential earning " + potentialEarning);
            if (potentialEarning.compareTo(new BigDecimal("0.0005")) > 0) {
                stopLoss = bidPrice.subtract(new BigDecimal("0.0003"));
                setTraillingStop(price, accountId, bidPrice);
            }
        } else {
            BigDecimal askPrice = price.getCloseoutAsk();
            potentialEarning = openPositionPrice.subtract(askPrice);
            log.info("Potential earning " + potentialEarning);
            if (potentialEarning.compareTo(new BigDecimal("0.0005")) > 0) {
                stopLoss = askPrice.add(new BigDecimal("0.0003"));
                setTraillingStop(price, accountId, askPrice);
            }
        }
    }

    private void setTraillingStop(Price price, String accountId, BigDecimal bidPrice) {
        log.info("Time: " + price.getTime());

        TradeOrderRequest tradeOrderRequest = new TradeOrderRequest();
        StopLossOnFill stopLossOnFill = new StopLossOnFill();
        stopLossOnFill.setPrice(stopLoss.toString());
        stopLossOnFill.setTimeInForce("GTC");
        tradeOrderRequest.setStopLossOnFill(stopLossOnFill);

        log.info("Trying to update stoploss to " + stopLoss);

        accountService.postTrade(accountId, tradeOrderRequest, tradeId);
        log.info("Set Stoploss order to " + stopLoss + ", tradeId:" + tradeId);

        openPositionPrice = bidPrice;
    }

    private boolean lastCandleTouchedLowerBand(Candle candle, BollingerBands bollinger) {
        return candle.getMid().getL().compareTo(bollinger.getLowerBand()) < 0;
    }

    private boolean lastCandleTouchedUpperBand(Candle candle, BollingerBands bollinger) {
        return candle.getMid().getH().compareTo(bollinger.getUpperBand()) > 0;
    }
}