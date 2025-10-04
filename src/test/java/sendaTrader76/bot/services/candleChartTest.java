package sendaTrader76.bot.services;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;

import sendaTrader76.bot.dto.Candle;
import sendaTrader76.bot.dto.Mid;

public class candleChartTest {

	private CandleS1Chart candleChart;

	@Before
	public void setup() {
		candleChart = new CandleS1Chart(null, null);

		Candle candle0 = createCandle(new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), true);
		Candle candle1 = createCandle(new BigDecimal("1"), new BigDecimal("1"), new BigDecimal("1"), new BigDecimal("1"), true);
		Candle candle2 = createCandle(new BigDecimal("2"), new BigDecimal("2"), new BigDecimal("2"), new BigDecimal("2"), true);
		Candle candle3 = createCandle(new BigDecimal("3"), new BigDecimal("3"), new BigDecimal("3"), new BigDecimal("3"), true);
		Candle candle4 = createCandle(new BigDecimal("4"), new BigDecimal("4"), new BigDecimal("4"), new BigDecimal("4"), false);

		List<Candle> candleHistory = Arrays.asList(candle0, candle1, candle2, candle3, candle4);

		candleChart.setCandleHistory(candleHistory);

		Assert.assertEquals(8, candleChart.getLastCandle().getTime().getMinute());
	}

	private Candle createCandle(BigDecimal o, BigDecimal c, BigDecimal h, BigDecimal l, boolean isComplete) {
		Candle candle = new Candle();
		Mid mid = new Mid();
		mid.setC(c);
		mid.setO(o);
		mid.setH(h);
		mid.setL(l);
		candle.setMid(mid);
		candle.setComplete(isComplete);
		candle.setTime(ZonedDateTime.of(2016, 11, 5, 17, 8, 0, 0, ZoneId.of("GMT")));

		return candle;
	}

	@Test
	public void getCachedCandlesTest() {
		List<Candle> candle = candleChart.getCachedCandles(3);

		Assert.assertEquals(3, candle.size());

		Assert.assertEquals(new BigDecimal("1"), candle.get(0).getMid().getO());
		Assert.assertEquals(new BigDecimal("2"), candle.get(1).getMid().getO());
		Assert.assertEquals(new BigDecimal("3"), candle.get(2).getMid().getO());
	}

}
