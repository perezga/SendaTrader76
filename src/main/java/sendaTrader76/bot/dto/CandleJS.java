package sendaTrader76.bot.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class CandleJS {
	private GranuarityType granularityType;
	private long time;
	private BigDecimal c;

	private BigDecimal o;

	private BigDecimal l;

	private BigDecimal h;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public BigDecimal getC() {
		return c;
	}

	public void setC(BigDecimal c) {
		this.c = c;
	}

	public BigDecimal getO() {
		return o;
	}

	public void setO(BigDecimal o) {
		this.o = o;
	}

	public BigDecimal getL() {
		return l;
	}

	public void setL(BigDecimal l) {
		this.l = l;
	}

	public BigDecimal getH() {
		return h;
	}

	public void setH(BigDecimal h) {
		this.h = h;
	}

	public GranuarityType getGranularityType() {
		return granularityType;
	}

	public void setGranularityType(GranuarityType granularityType) {
		this.granularityType = granularityType;
	}
}