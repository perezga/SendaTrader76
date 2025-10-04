package sendaTrader76.bot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Positions {

	@JsonProperty("short")
	private ShortPosition shortPosition;

	private String pl;

	private String unrealizedPL;

	private String resettablePL;

	private String instrument;

	@JsonProperty("long")
	private LongPosition longPosition;

	public ShortPosition getShortPosition() {
		return shortPosition;
	}

	public void setShortPosition(ShortPosition shortPosition) {
		this.shortPosition = shortPosition;
	}

	public String getPl() {
		return pl;
	}

	public void setPl(String pl) {
		this.pl = pl;
	}

	public String getUnrealizedPL() {
		return unrealizedPL;
	}

	public void setUnrealizedPL(String unrealizedPL) {
		this.unrealizedPL = unrealizedPL;
	}

	public String getResettablePL() {
		return resettablePL;
	}

	public void setResettablePL(String resettablePL) {
		this.resettablePL = resettablePL;
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public LongPosition getLongPosition() {
		return longPosition;
	}

	public void setLongPosition(LongPosition longPosition) {
		this.longPosition = longPosition;
	}

	@Override
	public String toString() {
		return "ClassPojo [shortPosition = " + shortPosition + ", pl = " + pl + ", unrealizedPL = " + unrealizedPL
				+ ", resettablePL = " + resettablePL + ", instrument = " + instrument + ", longPosition = "
				+ longPosition + "]";
	}
}
