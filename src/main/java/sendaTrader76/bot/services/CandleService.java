package sendaTrader76.bot.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import sendaTrader76.bot.dto.Candle;
import sendaTrader76.bot.dto.CandleFormat;
import sendaTrader76.bot.dto.CandleResponse;
import sendaTrader76.bot.dto.GranuarityType;
import sendaTrader76.bot.dto.InstrumentType;

@Component
public class CandleService {

	@Autowired
	private RestTemplate restTemplate;

	private static final int INVALID_MINUTE = 100;// 100 is a magic number to
													// indicate first
	// candle not yet processed.

	private static final Logger LOGGER = LoggerFactory.getLogger(CandleService.class);

	public List<Candle> getCandles(InstrumentType instrument, Integer count, GranuarityType granularity,
			CandleFormat candleFormat) {
		CandleResponse candleResponse = restTemplate.getForObject(
				"/v3/instruments/{instrument}/candles?count=" + (count + 1) +
						"&price=" + candleFormat + "&granularity=" + granularity,
				CandleResponse.class, instrument);

		List<Candle> candleList = new ArrayList<Candle>();

		int i = 0;
		if (candleResponse.getCandles().get(count).isComplete()) {
			i++;
		}

		for (int j = i; j <= count; j++) {
			Candle candle = candleResponse.getCandles().get(j);
			if (candle.isComplete()) {
				candleList.add(candle);
			}
		}

		return candleList;
	}

	public List<Candle> getCandles(InstrumentType instrument, Integer count, GranuarityType granularity,
			CandleFormat candleFormat, String fromDate) {

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer cc5204f202711b0051d643e7b99c02b4-ed7c02158d87620ddce9f7e4a11ce73c");
		headers.set("Content-Type", "application/json");

		HttpEntity r = new HttpEntity(headers);

		CandleResponse candleResponse = new RestTemplate()
				.exchange("https://api-fxpractice.oanda.com/v3/instruments/{instrument}/candles?count=" + (count + 1) +
						"&price=" + candleFormat + "&granularity=" + granularity + "&from=" + fromDate, HttpMethod.GET, r,
						CandleResponse.class, instrument)
				.getBody();

		List<Candle> candleList = new ArrayList<Candle>();

		int i = 0;
		if (candleResponse.getCandles().get(count).isComplete()) {
			i++;
		}

		for (int j = i; j <= count; j++) {
			Candle candle = candleResponse.getCandles().get(j);
			if (candle.isComplete()) {
				candleList.add(candle);
			}
		}

		return candleList;
	}
}
