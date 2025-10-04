package sendaTrader76.bot.dto;

public class TradesClosed
{
    private String financing;

    private String tradeID;

    private String realizedPL;

    private String units;

    public String getFinancing ()
    {
        return financing;
    }

    public void setFinancing (String financing)
    {
        this.financing = financing;
    }

    public String getTradeID ()
    {
        return tradeID;
    }

    public void setTradeID (String tradeID)
    {
        this.tradeID = tradeID;
    }

    public String getRealizedPL ()
    {
        return realizedPL;
    }

    public void setRealizedPL (String realizedPL)
    {
        this.realizedPL = realizedPL;
    }

    public String getUnits ()
    {
        return units;
    }

    public void setUnits (String units)
    {
        this.units = units;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [financing = "+financing+", tradeID = "+tradeID+", realizedPL = "+realizedPL+", units = "+units+"]";
    }
}
		