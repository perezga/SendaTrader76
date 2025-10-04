package sendaTrader76.bot.dto;

public class StopLossOrderTransaction {
	private String id;

	private String userID;

	private String accountID;

	private String time;

	private String price;

	private String reason;

	private String clientTradeID;

	private String triggerCondition;

	private String batchID;

	private String tradeID;

	private String timeInForce;

	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getClientTradeID() {
		return clientTradeID;
	}

	public void setClientTradeID(String clientTradeID) {
		this.clientTradeID = clientTradeID;
	}

	public String getTriggerCondition() {
		return triggerCondition;
	}

	public void setTriggerCondition(String triggerCondition) {
		this.triggerCondition = triggerCondition;
	}

	public String getBatchID() {
		return batchID;
	}

	public void setBatchID(String batchID) {
		this.batchID = batchID;
	}

	public String getTradeID() {
		return tradeID;
	}

	public void setTradeID(String tradeID) {
		this.tradeID = tradeID;
	}

	public String getTimeInForce() {
		return timeInForce;
	}

	public void setTimeInForce(String timeInForce) {
		this.timeInForce = timeInForce;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ClassPojo [id = " + id + ", userID = " + userID + ", accountID = " + accountID + ", time = " + time + ", price = " + price
				+ ", reason = " + reason + ", clientTradeID = " + clientTradeID + ", triggerCondition = " + triggerCondition + ", batchID = "
				+ batchID + ", tradeID = " + tradeID + ", timeInForce = " + timeInForce + ", type = " + type + "]";
	}
}
