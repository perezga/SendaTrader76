package sendaTrader76.bot.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Accounts {

	private List<AccountDetailsMini> accounts;

	public List<AccountDetailsMini> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<AccountDetailsMini> accounts) {
		this.accounts = accounts;
	}

}
