package sendaTrader76.bot.services;

import java.math.BigDecimal;

public class Result {

	private BigDecimal total = BigDecimal.ZERO;
	private BigDecimal partial = BigDecimal.ZERO;
	private PositionType partialType;
	private int winPositions;
	private int losePositions;
	
	public void setPartialType(PositionType partialType){
		this.partialType = partialType;
	}
	
	public PositionType getPartialType(){
		return partialType;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getPartial() {
		return partial;
	}

	public void setPartial(BigDecimal partial) {
		if (partial.compareTo(BigDecimal.ZERO) < 0) {
			this.losePositions++;
		} else {
			this.winPositions++;
		}
		this.partial = partial;
	}

	public int getWinPositions() {
		return winPositions;
	}

	public void setWinPositions(int winPositions) {
		this.winPositions = winPositions;
	}

	public int getLosePositions() {
		return losePositions;
	}

	public void setLosePositions(int losePositions) {
		this.losePositions = losePositions;
	}

}
