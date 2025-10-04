package sendaTrader76.bot.dto;

public class AccountDetail {

	private Account account;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	private String lastTransactionID;

	public String getLastTransactionID() {
		return lastTransactionID;
	}

	public void setLastTransactionID(String lastTransactionID) {
		this.lastTransactionID = lastTransactionID;
	}

}
