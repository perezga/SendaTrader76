package sendaTrader76.bot.strategies;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.Subscribe;

import sendaTrader76.bot.dto.BollingerBands;
import sendaTrader76.bot.dto.Candle;
import sendaTrader76.bot.dto.InstrumentType;
import sendaTrader76.bot.dto.Price;
import sendaTrader76.bot.indicators.BollingerIndicator;
import sendaTrader76.bot.services.AccountService;
import sendaTrader76.bot.services.CandleChart;

public class StrategyExecutor {

	private static final Logger log = LoggerFactory.getLogger(BollingerStrategy.class);

	private int previousMinute = -1;

	private static final int DEVIATION_2 = 2;

	private String accountId;

	private BollingerIndicator bollingerIndicator;

	public BollingerStrategy bollingerStrategy;

	private CandleChart candleChart;

	private boolean isProcessing = false;

	@Autowired
	public StrategyExecutor(AccountService accountService,
			CandleChart candleChart,BollingerIndicator bollingerIndicator, String accountId) {
		this.bollingerStrategy = new BollingerStrategy(accountService);
		this.candleChart = candleChart;
		this.bollingerIndicator= bollingerIndicator;
		this.accountId = accountId;

	}

	@Subscribe
	public void PriceConsumer(Price price) {
		int currentMinute = candleChart.getCurrentTimeUnit(price.getTime());
		// log.info("previousMinute " + previousMinute + " current " +
		// currentMinute);
		if (!isProcessing && previousMinute != currentMinute) {

			isProcessing = true;

			previousMinute = currentMinute;

			final List<Candle> candles = candleChart.getCachedCandles(20);

			if (!candles.isEmpty()) {

				final BollingerBands bollinger = bollingerIndicator.calculate(candles, DEVIATION_2);

				final Candle lastCandle = candles.get(candles.size() - 1);

				bollingerStrategy.executeTask(price, accountId, InstrumentType.EUR_USD, bollinger, lastCandle);
			}

			isProcessing = false;
		}
	}
}
