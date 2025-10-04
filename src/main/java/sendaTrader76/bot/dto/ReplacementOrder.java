package sendaTrader76.bot.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class ReplacementOrder {
	private String accountID;

	private String reason;

	private String batchID;

	private String timeInForce;

	private String tradeID;

	private String type;

	private String id;

	private String replacesOrderID;

	private String userID;

	private ZonedDateTime time;

	private BigDecimal price;

	private String triggerCondition;

	private String cancellingTransactionID;

	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getBatchID() {
		return batchID;
	}

	public void setBatchID(String batchID) {
		this.batchID = batchID;
	}

	public String getTimeInForce() {
		return timeInForce;
	}

	public void setTimeInForce(String timeInForce) {
		this.timeInForce = timeInForce;
	}

	public String getTradeID() {
		return tradeID;
	}

	public void setTradeID(String tradeID) {
		this.tradeID = tradeID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReplacesOrderID() {
		return replacesOrderID;
	}

	public void setReplacesOrderID(String replacesOrderID) {
		this.replacesOrderID = replacesOrderID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public ZonedDateTime getTime() {
		return time;
	}

	public void setTime(ZonedDateTime time) {
		this.time = time;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getTriggerCondition() {
		return triggerCondition;
	}

	public void setTriggerCondition(String triggerCondition) {
		this.triggerCondition = triggerCondition;
	}

	public String getCancellingTransactionID() {
		return cancellingTransactionID;
	}

	public void setCancellingTransactionID(String cancellingTransactionID) {
		this.cancellingTransactionID = cancellingTransactionID;
	}

	@Override
	public String toString() {
		return "ClassPojo [accountID = " + accountID + ", reason = " + reason + ", batchID = " + batchID + ", timeInForce = " + timeInForce
				+ ", tradeID = " + tradeID + ", type = " + type + ", id = " + id + ", replacesOrderID = " + replacesOrderID + ", userID = " + userID
				+ ", time = " + time + ", price = " + price + ", triggerCondition = " + triggerCondition + ", cancellingTransactionID = "
				+ cancellingTransactionID + "]";
	}
}
