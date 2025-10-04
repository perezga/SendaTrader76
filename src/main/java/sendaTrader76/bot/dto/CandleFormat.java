package sendaTrader76.bot.dto;

public enum CandleFormat {
	M("midpoint"), A("ask"),B("bid");
	
	private String description;
	
	CandleFormat(String description){
		this.description = description;
	}
}
