package sendaTrader76.bot.indicators;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import sendaTrader76.bot.dto.Candle;

@Component
public class MovingAverageIndicator {

	/*
	 * If there are N candle sticks then Mth candle stick will have weight M/(N
	 * * (N+1)/2). Therefore the divisor D for each candle is (N * (N+1)/2)
	 */
	public BigDecimal calculateWMA(List<Candle> candles) {
		BigDecimal divisor = BigDecimal.valueOf((candles.size() * (candles.size() + 1)) / 2);
		int count = 0;
		BigDecimal sumwma = BigDecimal.ZERO;
		for (Candle candle : candles) {
			count++;
			sumwma = sumwma.add((BigDecimal.valueOf(count).multiply(candle.getMid().getC())).divide(divisor));
		}
		return sumwma;
	}

	/*
	 * Simple average calculation of close price of candle stick
	 */
	public BigDecimal calculateSMA(List<Candle> candles) {
		BigDecimal sumsma = BigDecimal.ZERO;
		for (Candle candle : candles) {
			sumsma = sumsma.add(candle.getMid().getC());
		}
		return sumsma.divide(BigDecimal.valueOf(candles.size()));
	}

	// Exponential Moving Average EMA
	// SMA: 10 period sum / 10
	// Multiplier: (2 / (Time periods + 1) ) = (2 / (10 + 1) ) = 0.1818 (18.18%)
	// EMA: {Close - EMA(previous day)} x multiplier + EMA(previous day).

}
