package sendaTrader76.bot.indicators;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sendaTrader76.bot.dto.BollingerBands;
import sendaTrader76.bot.dto.Candle;

/**
 * Assume a 5 bar Bollinger band with 2 Deviations, and assume the last five
 * closes were 25.5, 26.75, 27.0, 26.5, and 27.25.
 * 
 * Calculate the simple moving average:
 * 
 * 25.5 + 26.75 + 27.0 + 26.5 + 27.25 = 133.0
 * 
 * 133.0 / 5 = 26.6
 * 
 * Next, for each bar, subtract 26.6 from the close and square this value:
 * 
 * 25.5 - 26.6 = -1.1 squared = 1.21 26.75 - 26.6 = 0.15 squared = 0.023 27.0 -
 * 26.6 = 0.4 squared = 0.16 26.5 - 26.6 = 0.1 squared = 0.01 27.25 - 26.6 =
 * 0.65 squared = 0.423
 * 
 * Add the above calculated values, divide by 5, and then get the square root of
 * this value to get the deviation value:
 * 
 * 1.21 + 0.023 + 0.16 + 0.01 + 0.423 = 1.826
 * 
 * 1.826 / 5 = 0.365
 * 
 * Square root of .365 = 0.604
 * 
 * The upper Bollinger band would be 26.6 + (2 * 0.604) = 27.808
 * 
 * The middle Bollinger band would be 26.6
 * 
 * The lower Bollinger band would be 26.6 - (2 * 0.604) = 25.392
 * 
 * 
 * @author jperez
 *
 */
@Component
public class BollingerIndicator {

	private static final Logger log = LoggerFactory.getLogger(BollingerIndicator.class);

	private MovingAverageIndicator indicatorService;

	@Autowired
	public BollingerIndicator(MovingAverageIndicator indicatorService) {
		this.indicatorService = indicatorService;
	}

	public BollingerBands calculate(List<Candle> candles, int deviation) {

		BigDecimal middleBand = indicatorService.calculateSMA(candles);

		BigDecimal sum = BigDecimal.ZERO;
		for (Candle candle : candles) {
			BigDecimal squared = candle.getMid().getC().subtract(middleBand)
					.multiply(candle.getMid().getC().subtract(middleBand));
			sum = sum.add(squared);

		}

		// System.out.println("num of candles to calculate bollinger " + i);
		BigDecimal deviationValue = BigDecimal.valueOf(
				Math.sqrt(sum.divide(new BigDecimal(candles.size()), 15, RoundingMode.HALF_DOWN).doubleValue()));

		BollingerBands bollinger = new BollingerBands();
		bollinger.setMiddleBand(middleBand);
		bollinger.setUpperBand(
				middleBand.add(deviationValue.multiply(BigDecimal.valueOf(deviation))).setScale(6, RoundingMode.HALF_DOWN));
		bollinger.setLowerBand(
				middleBand.subtract(deviationValue.multiply(BigDecimal.valueOf(deviation))).setScale(6, RoundingMode.HALF_DOWN));

		// log.info("UpperBand " + bollinger.getUpperBand() + " LowerBand " +
		// bollinger.getLowerBand());

		return bollinger;
	}

}
