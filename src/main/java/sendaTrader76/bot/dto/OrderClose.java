package sendaTrader76.bot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OrderClose {
	private String longUnits;
	private String shortUnits;

	public String getShortUnits() {
		return shortUnits;
	}

	public void setShortUnits(String shortUnits) {
		this.shortUnits = shortUnits;
	}

	public String getLongUnits() {
		return longUnits;
	}

	public void setLongUnits(String longUnits) {
		this.longUnits = longUnits;
	}

}
