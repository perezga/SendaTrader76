package sendaTrader76.bot.services;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import sendaTrader76.bot.dto.Candle;
import sendaTrader76.bot.dto.CandleJS;
import sendaTrader76.bot.dto.GranuarityType;
import sendaTrader76.bot.dto.Mid;
import sendaTrader76.bot.dto.Price;

public abstract class CandleChart {

	public int previousMinute = -1;

	public List<Candle> candleHistory = new ArrayList<Candle>();
	
	public SimpMessagingTemplate template;
	
	public String webSocketTopic;
	
	public CandleChart(SimpMessagingTemplate template, String webSocketTopic ){
		this.template= template;
		this.webSocketTopic = webSocketTopic;
	}

	public void setCandleHistory(List<Candle> candleHistory) {
		this.candleHistory = candleHistory;
		previousMinute = getCurrentTimeUnit(this.getLastCandle().getTime());

	}

	public void postProcess(Candle candle) {
		CandleJS candleJS = new CandleJS();
		candleJS.setGranularityType(this.getGranularityType());
		candleJS.setTime(candle.getTime().toInstant().toEpochMilli());
		candleJS.setO(candle.getMid().getO());
		candleJS.setC(candle.getMid().getC());
		candleJS.setH(candle.getMid().getH());
		candleJS.setL(candle.getMid().getL());
		template.convertAndSend(webSocketTopic, candleJS);
	}

	// TODO Create a CandleHolder for every chart (1s, 1m, etc)
	public void addCandle(Price price) {

		ZonedDateTime candleTime = price.getTime();
		BigDecimal priceMid = calculateMidPrice(price);

		int currentMinute = getCurrentTimeUnit(candleTime);

		if (previousMinute != currentMinute) {
			previousMinute = currentMinute;

			if (!candleHistory.isEmpty()) {
				Candle previousCandle = candleHistory.get(candleHistory.size() - 1);
				previousCandle.setComplete(true);
				
				postProcess(previousCandle);

			}

			Candle candle = new Candle();
			Mid mid = new Mid();

			mid.setC(priceMid);
			mid.setH(priceMid);
			mid.setL(priceMid);
			mid.setO(priceMid);
			candle.setMid(mid);
			candle.setComplete(false);
			candle.setTime(getTruncatedTime(candleTime));
			// candle.setVolume();

			// LOGGER.info("Add new candle to history " + candle);
			// log.info(price.toString());

			candleHistory.add(candle);
		} else {
			Candle lastCandle = this.getLastCandle();

			updateCandle(candleTime, priceMid, lastCandle);
		}
	}
	
	abstract public int getCurrentTimeUnit(ZonedDateTime candleTime);

	abstract ZonedDateTime getTruncatedTime(ZonedDateTime candleTime);

	/**
	 * Calculate midPric based on higher ask and lower bid
	 * 
	 * @param price
	 * @return midPrice
	 */
	private BigDecimal calculateMidPrice(Price price) {

		double maxAsk = price.getAsks()[price.getAsks().length - 1].getPrice();
		double minBid = price.getBids()[0].getPrice();

		return BigDecimal.valueOf(maxAsk).add(BigDecimal.valueOf(minBid)).divide(BigDecimal.valueOf(2));

	}

	private void updateCandle(ZonedDateTime time, BigDecimal priceMid, Candle candle) {

		if (candle.isComplete()) {
			return; // This scenario happens when first price snapshot belongs
					// to a completed candlestick
		}

		candle.getMid().setC(priceMid);
		//candle.setTime(time);
		if (candle.getMid().getH().compareTo(priceMid) < 0) {
			candle.getMid().setH(priceMid);
		}
		if (candle.getMid().getL().compareTo(priceMid) > 0) {
			candle.getMid().setL(priceMid);
		}
		// LOGGER.debug("update last candle " + candle);
	}

	public Candle getLastCandle() {
		return candleHistory.get(candleHistory.size() - 1);
	}

	public List<Candle> getCachedCandles(int count) {

		List<Candle> candleList = new ArrayList<Candle>();

		int size = candleHistory.size();

		int i = candleHistory.get(size - 1).isComplete() ? size - count : size - count - 1;

		if (i < 0) {
			return candleList;
		}

		for (int j = i; j <= size - 1; j++) {
			Candle candle = candleHistory.get(j);
			if (candle.isComplete()) {
				candleList.add(candle);
			}
		}
		return candleList;
	}
	
	abstract GranuarityType getGranularityType();

}
