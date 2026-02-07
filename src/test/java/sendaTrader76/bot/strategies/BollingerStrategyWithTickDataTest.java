package sendaTrader76.bot.strategies;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import sendaTrader76.bot.config.AppConfigTest;
import sendaTrader76.bot.dto.Asks;
import sendaTrader76.bot.dto.Bids;
import sendaTrader76.bot.dto.BollingerBands;
import sendaTrader76.bot.dto.Candle;
import sendaTrader76.bot.dto.CandleFormat;
import sendaTrader76.bot.dto.GranuarityType;
import sendaTrader76.bot.dto.InstrumentType;
import sendaTrader76.bot.dto.Price;
import sendaTrader76.bot.indicators.BollingerIndicator;
import sendaTrader76.bot.services.CandleS1Chart;
import sendaTrader76.bot.services.CandleService;
import sendaTrader76.bot.stubs.AccountServiceStub;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AppConfigTest.class })
public class BollingerStrategyWithTickDataTest {

	private static final String ACCOUN_NUMBER = "1";

	private BollingerStrategy bollingerStrategy;

	@Autowired
	private BollingerIndicator bollingerIndicator;

	private AccountServiceStub accountService;

	private CandleS1Chart candleChart;

	@BeforeEach
	public void setup() {
		SimpMessageSendingOperations template = Mockito.mock(SimpMessageSendingOperations.class);
		candleChart = new CandleS1Chart(template, null);
		accountService = new AccountServiceStub();
		bollingerStrategy = new BollingerStrategy(accountService);
	}

	@Test
	public void BollingerStrategyTest() {

		// candleService.setCandleHistory(candleService.getCandles(InstrumentType.EUR_USD,
		// 25, GranuarityType.M1, CandleFormat.M, "2016-11-07T17:22:00.000000000Z"));

		// for (int i = 6; i < 10; i++) {
		try (BufferedReader br = Files.newBufferedReader(
				Paths.get(getClass().getClassLoader().getResource("DAT_ASCII_EURUSD_T_201610.csv").toURI()))) {
			String line;

			while ((line = br.readLine()) != null) {
				// E.x. 20161003 204031840,1.120760,1.120810,0
				String[] lineSplit = StringUtils.split(line, ",");

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmmssSSS")
						.withZone(ZoneId.of("GMT-5"));

				ZonedDateTime priceDate = ZonedDateTime.parse(lineSplit[0], formatter);

				Price price = new Price();
				price.setTime(priceDate);

				Bids bid = new Bids();
				bid.setPrice(new BigDecimal(lineSplit[1]).doubleValue());
				Bids[] bids = { bid };
				price.setBids(bids);

				Asks ask = new Asks();
				ask.setPrice(new BigDecimal(lineSplit[2]).doubleValue());
				Asks[] asks = { ask };
				price.setAsks(asks);

				int volume = Integer.parseInt(lineSplit[3]);

				candleChart.addCandle(price);

				List<Candle> candlesHistory = candleChart.getCachedCandles(20);

				if (!candlesHistory.isEmpty()) {

					accountService.setAskPrice(ask.getPrice());
					accountService.setBidPrice(bid.getPrice());

					final List<Candle> candles = candleChart.getCachedCandles(20);

					final BollingerBands bollinger = bollingerIndicator.calculate(candles, 2);

					final Candle lastCandle = candles.get(candles.size() - 1);

					bollingerStrategy.executeTask(price, ACCOUN_NUMBER, InstrumentType.EUR_USD, bollinger, lastCandle);

				}
			}

			// expected
			// StrategyResults [startDate=null, endDate=null, balance=-0.001840,
			// totalLoss=-0.002780, totalWin=0.00094, totalPositions=13, numWinPosition=3,
			// numLosePositions=10]

			// actual
			// StrategyResults [startDate=null, endDate=null, balance=-0.001370,
			// totalLoss=-0.001670, totalWin=0.00030, totalPositions=11, numWinPosition=2,
			// numLosePositions=9]

		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		// }
	}
}
