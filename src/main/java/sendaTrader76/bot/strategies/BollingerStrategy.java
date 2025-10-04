package sendaTrader76.bot.strategies;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sendaTrader76.bot.dto.BollingerBands;
import sendaTrader76.bot.dto.Candle;
import sendaTrader76.bot.dto.InstrumentType;
import sendaTrader76.bot.dto.OpenTrades;
import sendaTrader76.bot.dto.Order;
import sendaTrader76.bot.dto.OrderRequest;
import sendaTrader76.bot.dto.OrderResponse;
import sendaTrader76.bot.dto.Price;
import sendaTrader76.bot.dto.StopLossDetail;
import sendaTrader76.bot.dto.StopLossOnFill;
import sendaTrader76.bot.dto.StopLossOrder;
import sendaTrader76.bot.dto.StopLossOrderRequest;
import sendaTrader76.bot.dto.TradeOrderRequest;
import sendaTrader76.bot.services.AccountService;

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

		final BigDecimal currentBidPrice = BigDecimal.valueOf(price.getAsks()[price.getAsks().length - 1].getPrice());// Highest
																														// ask
		final BigDecimal currentAskPrice = BigDecimal.valueOf(price.getBids()[0].getPrice());// lowest
																								// bid
		final BigDecimal currentMidPrice = currentBidPrice.add(currentAskPrice).divide(BigDecimal.valueOf(2));

		if (isOpenPositions(accountId)) {
				setTraillingStopIfNeeded(positionType, price, accountId);
				/*if (touchedBand) {

					if ("LONG".equals(positionType) && lastCandle.getMid().getH().compareTo(bollinger.getUpperBand()) < 0) {

						sendTradeOrder(accountId, instrument, currentBidPrice, positionType);
						touchedBand = false;

					} else if ("SHORT".equals(positionType) && lastCandle.getMid().getL().compareTo(bollinger.getLowerBand()) > 0) {

						sendTradeOrder(accountId, instrument, currentAskPrice, positionType);
						touchedBand = false;

					}
				} else {
					if ("LONG".equals(positionType) && lastCandleTouchedUpperBand(lastCandle, bollinger)) {
						touchedBand = true;

						log.info("Time: " + price.getTime());
						log.info("Touched Upper Band");

					} else if ("SHORT".equals(positionType) && lastCandleTouchedLowerBand(lastCandle, bollinger)) {
						touchedBand = true;

						log.info("Time: " + price.getTime());
						log.info("Touched Lower Band");

					}
				}*/
			

		} else {
			if (waitForcandleInsideBands) {
				if (longPositionWait
						&& (lastCandle.getMid().getL().compareTo(bollinger.getLowerBand()) > 0) // candlestick
																								// is
																								// above
																								// lowerbadn
						&& (lastCandle.getMid().getO().compareTo(lastCandle.getMid().getC()) < 0)) { // Candlestick
																										// is
																										// green

					OrderResponse orderResponse = postOrderLong(accountId, instrument, currentBidPrice);

					if (orderResponse == null || orderResponse.getOrderFillTransaction() == null) {
						log.info("Order closed right before opened.");
						waitForcandleInsideBands = false;
						shortPositionWait = false;
						longPositionWait = false;
						return;
					}

					openPositionPrice = orderResponse.getOrderFillTransaction().getPrice();

					tradeId = orderResponse.getOrderCreateTransaction().getId();

					positionType = "LONG";
					waitForcandleInsideBands = false;
					longPositionWait = false;
					shortPositionWait = false;

					log.info("Time: " + price.getTime());
					log.info("Open Position (Long) " + openPositionPrice);
					log.info("Stop loss: " + stopLoss);

				} else if (shortPositionWait
						&& (lastCandle.getMid().getH().compareTo(bollinger.getUpperBand()) < 0) // candlestick
																								// is
																								// below
																								// lowerbadn
						&& (lastCandle.getMid().getC().compareTo(lastCandle.getMid().getO()) < 0)) { // Candlestick
																										// is
																										// red

					OrderResponse orderResponse = postOrderShort(accountId, instrument, currentAskPrice);

					if (orderResponse == null || orderResponse.getOrderFillTransaction() == null) {
						log.info("Order closed right before opened.");
						waitForcandleInsideBands = false;
						shortPositionWait = false;
						longPositionWait = false;
						return;
					}

					openPositionPrice = orderResponse.getOrderFillTransaction().getPrice();
					tradeId = orderResponse.getOrderCreateTransaction().getId();

					positionType = "SHORT";
					waitForcandleInsideBands = false;
					shortPositionWait = false;
					longPositionWait = false;

					log.info("Time: " + price.getTime());
					log.info("Open Position (Short) " + openPositionPrice);
					log.info("Stop loss: " + stopLoss);
				}
			} else {
				// log.info("Bollinger " + bollinger);
				// log.info("Candle " + lastCandle);

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

	private void sendTradeOrder(String accountId, InstrumentType instrument, final BigDecimal currentPrice, String positionType) {
		TradeOrderRequest tradeOrderRequest = new TradeOrderRequest();
		StopLossDetail stopLossDetail = new StopLossDetail();
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

		stopLossDetail.setPrice(stopLoss.toString());
		stopLossDetail.setTimeInForce("GTC");
		tradeOrderRequest.setStopLoss(stopLossDetail);

		log.info("Trying to update stoploss to " + stopLoss);

		accountService.postTrade(accountId, tradeOrderRequest, tradeId);
		log.info("Update Stoploss order to " + stopLoss + ", tradeId: " + tradeId);

	}

	private OrderResponse postOrderShort(String accountId, InstrumentType instrument,
			final BigDecimal currentAskPrice) {
		OrderRequest orderRequest = new OrderRequest();
		Order order = new Order();
		order.setInstrument(instrument.toString());
		order.setTimeInForce("FOK");
		order.setUnits("-10000");
		order.setType("MARKET");
		order.setPositionFill("DEFAULT");

		// TrailingStopLossOnFill trailingStopLossOnFill = new
		// TrailingStopLossOnFill();
		// trailingStopLossOnFill.setDistance("0.0005");
		// trailingStopLossOnFill.setTimeInForce("GTC");

		stopLoss = currentAskPrice.add(new BigDecimal("0.0003")).setScale(6, RoundingMode.HALF_DOWN);

		StopLossOnFill stopLossOnFill = new StopLossOnFill();
		stopLossOnFill.setTimeInForce("GTC");
		stopLossOnFill.setPrice(stopLoss.toString());

		// order.setTrailingStopLossOnFill(trailingStopLossOnFill);
		order.setStopLossOnFill(stopLossOnFill);

		orderRequest.setOrder(order);
		OrderResponse orderResponse;

		orderResponse = accountService.postOrder(accountId, orderRequest);
		return orderResponse;
	}

	private OrderResponse postOrderLong(String accountId, InstrumentType instrument, final BigDecimal currentBidPrice) {
		OrderRequest orderRequest = new OrderRequest();
		Order order = new Order();
		order.setInstrument(instrument.toString());
		order.setTimeInForce("FOK");
		order.setUnits("10000");
		order.setType("MARKET");
		order.setPositionFill("DEFAULT");
		// TrailingStopLossOnFill trailingStopLossOnFill = new
		// TrailingStopLossOnFill();
		// trailingStopLossOnFill.setDistance("0.0005");
		// trailingStopLossOnFill.setTimeInForce("GTC");

		stopLoss = currentBidPrice.subtract(new BigDecimal("0.0003")).setScale(6, RoundingMode.HALF_DOWN);

		StopLossOnFill stopLossOnFill = new StopLossOnFill();
		stopLossOnFill.setTimeInForce("GTC");
		stopLossOnFill.setPrice(stopLoss.toString());

		// order.setTrailingStopLossOnFill(trailingStopLossOnFill);
		order.setStopLossOnFill(stopLossOnFill);
		orderRequest.setOrder(order);
		OrderResponse orderResponse;

		orderResponse = accountService.postOrder(accountId, orderRequest);
		return orderResponse;
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

		if (orderTrades.getTrades().size() == 0) {
			return false;
		} else {
			tradeId = orderTrades.getTrades().get(0).getId();
			return true;
		}
	}

	private void setTraillingStopIfNeeded(String positionType, Price price, String accountId) {

		BigDecimal potentialEarning;
		TradeOrderRequest tradeOrderRequest = new TradeOrderRequest();
		StopLossDetail stopLossDetail = new StopLossDetail();
		if ("LONG".equals(positionType)) {
			BigDecimal bidPrice = BigDecimal.valueOf(price.getBids()[0].getPrice());
			potentialEarning = bidPrice.subtract(openPositionPrice);
			log.info("Potential earning " + potentialEarning);
			if (potentialEarning.compareTo(new BigDecimal("0.0005")) > 0) {
				stopLoss = bidPrice.subtract(new BigDecimal("0.0003"));
				setTraillingStop(price, accountId, tradeOrderRequest, stopLossDetail, bidPrice);
			}
		} else {
			BigDecimal askPrice = BigDecimal.valueOf(price.getAsks()[price.getAsks().length - 1].getPrice());
			potentialEarning = openPositionPrice.subtract(askPrice);
			log.info("Potential earning " + potentialEarning);
			if (potentialEarning.compareTo(new BigDecimal("0.0005")) > 0) {
				stopLoss = askPrice.add(new BigDecimal("0.0003"));
				setTraillingStop(price, accountId, tradeOrderRequest, stopLossDetail, askPrice);
			}
		}
	}

	private void setTraillingStop(Price price, String accountId, TradeOrderRequest tradeOrderRequest, StopLossDetail stopLossDetail,
			BigDecimal bidPrice) {
		log.info("Time: " + price.getTime());
		

		stopLossDetail.setPrice(stopLoss.toString());
		stopLossDetail.setTimeInForce("GTC");
		tradeOrderRequest.setStopLoss(stopLossDetail);

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
