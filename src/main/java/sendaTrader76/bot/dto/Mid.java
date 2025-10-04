package sendaTrader76.bot.dto;

import java.math.BigDecimal;

public class Mid {
	private BigDecimal c;

	private BigDecimal o;

	private BigDecimal l;

	private BigDecimal h;

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

	@Override
	public String toString() {
		return "c = " + c + ", o = " + o + ", l = " + l + ", h = " + h;
	}
}
