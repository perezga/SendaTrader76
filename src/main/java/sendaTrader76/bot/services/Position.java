package sendaTrader76.bot.services;

import java.math.BigDecimal;

public class Position {

	private PositionType type;
	private long time;
	private BigDecimal price;

	public PositionType getType() {
		return type;
	}

	public void setType(PositionType type) {
		this.type = type;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
