package sendaTrader76.bot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {
	private String marginCloseoutNAV;

	private String marginUsed;

	private String pendingOrderCount;

	private String marginCloseoutPercent;

	private String currency;

	private String id;

	private String balance;

	private String marginCloseoutPositionValue;

	private String unrealizedPL;

	private Trades[] trades;

	private String NAV;

	private String marginCloseoutMarginUsed;

	private String marginRate;

	private String positionValue;

	private String withdrawalLimit;

	private String hedgingEnabled;

	private String createdTime;

	private String alias;

	private String marginAvailable;

	private String openTradeCount;

	private Positions[] positions;

	private String pl;

	private String resettablePL;

	private String createdByUserID;

	private String openPositionCount;

	private String[] orders;

	private String marginCloseoutUnrealizedPL;

	public String getMarginCloseoutNAV() {
		return marginCloseoutNAV;
	}

	public void setMarginCloseoutNAV(String marginCloseoutNAV) {
		this.marginCloseoutNAV = marginCloseoutNAV;
	}

	public String getMarginUsed() {
		return marginUsed;
	}

	public void setMarginUsed(String marginUsed) {
		this.marginUsed = marginUsed;
	}

	public String getPendingOrderCount() {
		return pendingOrderCount;
	}

	public void setPendingOrderCount(String pendingOrderCount) {
		this.pendingOrderCount = pendingOrderCount;
	}

	public String getMarginCloseoutPercent() {
		return marginCloseoutPercent;
	}

	public void setMarginCloseoutPercent(String marginCloseoutPercent) {
		this.marginCloseoutPercent = marginCloseoutPercent;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getMarginCloseoutPositionValue() {
		return marginCloseoutPositionValue;
	}

	public void setMarginCloseoutPositionValue(String marginCloseoutPositionValue) {
		this.marginCloseoutPositionValue = marginCloseoutPositionValue;
	}

	public String getUnrealizedPL() {
		return unrealizedPL;
	}

	public void setUnrealizedPL(String unrealizedPL) {
		this.unrealizedPL = unrealizedPL;
	}

	public Trades[] getTrades() {
		return trades;
	}

	public void setTrades(Trades[] trades) {
		this.trades = trades;
	}

	public String getNAV() {
		return NAV;
	}

	public void setNAV(String NAV) {
		this.NAV = NAV;
	}

	public String getMarginCloseoutMarginUsed() {
		return marginCloseoutMarginUsed;
	}

	public void setMarginCloseoutMarginUsed(String marginCloseoutMarginUsed) {
		this.marginCloseoutMarginUsed = marginCloseoutMarginUsed;
	}

	public String getMarginRate() {
		return marginRate;
	}

	public void setMarginRate(String marginRate) {
		this.marginRate = marginRate;
	}

	public String getPositionValue() {
		return positionValue;
	}

	public void setPositionValue(String positionValue) {
		this.positionValue = positionValue;
	}

	public String getWithdrawalLimit() {
		return withdrawalLimit;
	}

	public void setWithdrawalLimit(String withdrawalLimit) {
		this.withdrawalLimit = withdrawalLimit;
	}

	public String getHedgingEnabled() {
		return hedgingEnabled;
	}

	public void setHedgingEnabled(String hedgingEnabled) {
		this.hedgingEnabled = hedgingEnabled;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getMarginAvailable() {
		return marginAvailable;
	}

	public void setMarginAvailable(String marginAvailable) {
		this.marginAvailable = marginAvailable;
	}

	public String getOpenTradeCount() {
		return openTradeCount;
	}

	public void setOpenTradeCount(String openTradeCount) {
		this.openTradeCount = openTradeCount;
	}

	public Positions[] getPositions() {
		return positions;
	}

	public void setPositions(Positions[] positions) {
		this.positions = positions;
	}

	public String getPl() {
		return pl;
	}

	public void setPl(String pl) {
		this.pl = pl;
	}

	public String getResettablePL() {
		return resettablePL;
	}

	public void setResettablePL(String resettablePL) {
		this.resettablePL = resettablePL;
	}

	public String getCreatedByUserID() {
		return createdByUserID;
	}

	public void setCreatedByUserID(String createdByUserID) {
		this.createdByUserID = createdByUserID;
	}

	public String getOpenPositionCount() {
		return openPositionCount;
	}

	public void setOpenPositionCount(String openPositionCount) {
		this.openPositionCount = openPositionCount;
	}

	public String[] getOrders() {
		return orders;
	}

	public void setOrders(String[] orders) {
		this.orders = orders;
	}

	public String getMarginCloseoutUnrealizedPL() {
		return marginCloseoutUnrealizedPL;
	}

	public void setMarginCloseoutUnrealizedPL(String marginCloseoutUnrealizedPL) {
		this.marginCloseoutUnrealizedPL = marginCloseoutUnrealizedPL;
	}

	@Override
	public String toString() {
		return "ClassPojo [marginCloseoutNAV = " + marginCloseoutNAV + ", marginUsed = " + marginUsed
				+ ", pendingOrderCount = " + pendingOrderCount + ", marginCloseoutPercent = " + marginCloseoutPercent
				+ ", currency = " + currency + ", id = " + id + ", balance = " + balance
				+ ", marginCloseoutPositionValue = " + marginCloseoutPositionValue + ", unrealizedPL = " + unrealizedPL
				+ ", trades = " + trades + ", NAV = " + NAV + ", marginCloseoutMarginUsed = " + marginCloseoutMarginUsed
				+ ", marginRate = " + marginRate + ", positionValue = " + positionValue + ", withdrawalLimit = "
				+ withdrawalLimit + ", hedgingEnabled = " + hedgingEnabled + ", createdTime = " + createdTime
				+ ", alias = " + alias + ", marginAvailable = " + marginAvailable + ", openTradeCount = "
				+ openTradeCount + ", positions = " + positions + ", pl = " + pl + ", resettablePL = " + resettablePL + ", createdByUserID = " + createdByUserID
				+ ", openPositionCount = " + openPositionCount + ", orders = " + orders
				+ ", marginCloseoutUnrealizedPL = " + marginCloseoutUnrealizedPL + "]";
	}
}
