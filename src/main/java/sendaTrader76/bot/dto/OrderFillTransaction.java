package sendaTrader76.bot.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class OrderFillTransaction {
	private String accountID;

	private String accountBalance;

	private String orderID;

	private String reason;

	private String batchID;

	private String instrument;

	private String type;

	private BigDecimal units;

	private String id;

	private String userID;

	private String financing;

	private ZonedDateTime time;

	private BigDecimal price;

	private TradeOpened tradeOpened;

	private TradesClosed[] tradesClosed;

	private String pl;

	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public String getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
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

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getUnits() {
		return units;
	}

	public void setUnits(BigDecimal units) {
		this.units = units;
	}

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

	public String getFinancing() {
		return financing;
	}

	public void setFinancing(String financing) {
		this.financing = financing;
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

	public TradeOpened getTradeOpened() {
		return tradeOpened;
	}

	public void setTradeOpened(TradeOpened tradeOpened) {
		this.tradeOpened = tradeOpened;
	}

	public String getPl() {
		return pl;
	}

	public void setPl(String pl) {
		this.pl = pl;
	}

	public TradesClosed[] getTradesClosed() {
		return tradesClosed;
	}

	public void setTradesClosed(TradesClosed[] tradesClosed) {
		this.tradesClosed = tradesClosed;
	}

	@Override
	public String toString() {
		return "ClassPojo [accountID = " + accountID + ", accountBalance = " + accountBalance + ", orderID = " + orderID
				+ ", reason = " + reason + ", batchID = " + batchID + ", instrument = " + instrument + ", type = "
				+ type + ", units = " + units + ", id = " + id + ", userID = " + userID + ", financing = " + financing
				+ ", time = " + time + ", price = " + price + ", tradeOpened = " + tradeOpened + ", pl = " + pl + "]";
	}
}