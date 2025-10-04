package sendaTrader76.bot.strategies;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class StrategyResults {

	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private BigDecimal totalLoss = BigDecimal.ZERO;
	private BigDecimal totalWin = BigDecimal.ZERO;
	private int numWinPosition;
	private int numLosePositions;

	public BigDecimal addPartialWin(BigDecimal positionwin) {
		totalWin = totalWin.add(positionwin);
		return totalWin;
	}

	public BigDecimal addPartialLoss(BigDecimal positionLoss) {
		totalLoss = totalLoss.add(positionLoss);
		return totalLoss;
	}

	public BigDecimal getBalance() {
		return totalWin.add(totalLoss);
	}

	public int increaseNumWinPosition() {
		return ++numWinPosition;
	}

	public int increaseNumLosePosition() {
		return ++numLosePositions;
	}

	public int getTotalNumPositions() {
		return numWinPosition + numLosePositions;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StrategyResults [startDate=").append(startDate).append(", endDate=").append(endDate).append(", balance=").append(this.getBalance()).append(", totalLoss=").append(totalLoss)
				.append(", totalWin=").append(totalWin).append(", totalPositions=").append(this.getTotalNumPositions()).append(", numWinPosition=").append(numWinPosition).append(", numLosePositions=")
				.append(numLosePositions).append("]");
		return builder.toString();
	}

	

}
