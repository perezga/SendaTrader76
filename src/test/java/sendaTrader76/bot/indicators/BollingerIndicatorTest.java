package sendaTrader76.bot.indicators;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import sendaTrader76.bot.dto.BollingerBands;
import sendaTrader76.bot.dto.Candle;
import sendaTrader76.bot.dto.Mid;

public class BollingerIndicatorTest {

	@Test
	public void test() {
		BollingerIndicator b = new BollingerIndicator(new MovingAverageIndicator());
		// 25.5 + + + +
		Candle candle1 = new Candle();
		candle1.setMid(new Mid());
		candle1.getMid().setC(new BigDecimal("25.5"));
		Candle candle2 = new Candle();
		candle2.setMid(new Mid());

		candle2.getMid().setC(new BigDecimal("26.75"));
		Candle candle3 = new Candle();
		candle3.setMid(new Mid());

		candle3.getMid().setC(new BigDecimal("27.0"));
		Candle candle4 = new Candle();
		candle4.setMid(new Mid());

		candle4.getMid().setC(new BigDecimal("26.5"));
		Candle candle5 = new Candle();
		candle5.setMid(new Mid());

		candle5.getMid().setC(new BigDecimal("27.25"));

		List<Candle> candles = new ArrayList<Candle>();
		candles.add(candle1);
		candles.add(candle2);
		candles.add(candle3);
		candles.add(candle4);
		candles.add(candle5);

		BollingerBands boll = b.calculate(candles, 2);
		
		Assert.assertEquals(new BigDecimal("27.808305"), boll.getUpperBand());
		Assert.assertEquals(new BigDecimal("26.60"), boll.getMiddleBand());
		Assert.assertEquals(new BigDecimal("25.391695"), boll.getLowerBand());
		
	}
}
