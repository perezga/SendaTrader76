package sendaTrader76.bot.dto;

public class LongPosition {
	private String pl;

	private String unrealizedPL;

	private String resettablePL;

	private String units;

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

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	@Override
	public String toString() {
		return "ClassPojo [pl = " + pl + ", unrealizedPL = " + unrealizedPL + ", resettablePL = " + resettablePL
				+ ", units = " + units + "]";
	}
}
