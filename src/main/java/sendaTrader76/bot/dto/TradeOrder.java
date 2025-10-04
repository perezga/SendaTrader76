package sendaTrader76.bot.dto;

public class TradeOrder {
	private String lastTransactionID;

	private StopLossOrderTransaction stopLossOrderTransaction;

	private String[] relatedTransactionIDs;

	public String getLastTransactionID() {
		return lastTransactionID;
	}

	public void setLastTransactionID(String lastTransactionID) {
		this.lastTransactionID = lastTransactionID;
	}

	public StopLossOrderTransaction getStopLossOrderTransaction() {
		return stopLossOrderTransaction;
	}

	public void setStopLossOrderTransaction(StopLossOrderTransaction stopLossOrderTransaction) {
		this.stopLossOrderTransaction = stopLossOrderTransaction;
	}

	public String[] getRelatedTransactionIDs() {
		return relatedTransactionIDs;
	}

	public void setRelatedTransactionIDs(String[] relatedTransactionIDs) {
		this.relatedTransactionIDs = relatedTransactionIDs;
	}

	@Override
	public String toString() {
		return "ClassPojo [lastTransactionID = " + lastTransactionID + ", stopLossOrderTransaction = " + stopLossOrderTransaction
				+ ", relatedTransactionIDs = " + relatedTransactionIDs + "]";
	}
}
