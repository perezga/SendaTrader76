package sendaTrader76.bot.services;

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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sendaTrader76.bot.dto.Asks;
import sendaTrader76.bot.dto.Bids;
import sendaTrader76.bot.dto.GranuarityType;
import sendaTrader76.bot.dto.InstrumentType;
import sendaTrader76.bot.dto.Price;
import sendaTrader76.bot.strategies.StrategyExecutor;

public class PriceStreamBacktTestingService {

	private static final Log logger = LogFactory.getLog(PriceStreamBacktTestingService.class);

	private List<CandleChart> candleChartList;

	private AccountBackTestingService accountService;

	private StrategyExecutor strategyExecutor;

	public PriceStreamBacktTestingService(AccountBackTestingService accountService,
			List<CandleChart> candleChartList, StrategyExecutor strategyExecutor) {
		this.candleChartList = candleChartList;

		this.accountService = accountService;
		this.strategyExecutor = strategyExecutor;
	}

	public void startPriceService() throws IOException, URISyntaxException, InterruptedException {

		// for (int i = 6; i < 10; i++) {
		try (BufferedReader br = Files.newBufferedReader(
				Paths.get(getClass().getClassLoader().getResource("2016-12-02T18:29:03.513execution.csv").toURI()))) {
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

				candleChartList.forEach(candleChart -> candleChart.addCandle(price));
				accountService.setAskPrice(ask.getPrice(), priceDate);
				accountService.setBidPrice(bid.getPrice(), priceDate);

				strategyExecutor.PriceConsumer(price);
			}
		}
	}
}
