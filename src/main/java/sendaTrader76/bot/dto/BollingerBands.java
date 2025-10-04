package sendaTrader76.bot.dto;

import java.math.BigDecimal;

public class BollingerBands {

	private BigDecimal upperBand;
	private BigDecimal lowerBand;
	private BigDecimal middleBand;
	public BigDecimal getUpperBand() {
		return upperBand;
	}
	public void setUpperBand(BigDecimal upperBand) {
		this.upperBand = upperBand;
	}
	public BigDecimal getLowerBand() {
		return lowerBand;
	}
	public void setLowerBand(BigDecimal lowerBand) {
		this.lowerBand = lowerBand;
	}
	public BigDecimal getMiddleBand() {
		return middleBand;
	}
	public void setMiddleBand(BigDecimal middleBand) {
		this.middleBand = middleBand;
	}
	@Override
	public String toString() {
		return "Bollinger [upperBand=" + upperBand + ", lowerBand=" + lowerBand + ", middleBand=" + middleBand + "]";
	}

}
