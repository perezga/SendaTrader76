package sendaTrader76.bot.dto;

public class TradeOpened
{
    private String tradeID;

    private String units;

    public String getTradeID ()
    {
        return tradeID;
    }

    public void setTradeID (String tradeID)
    {
        this.tradeID = tradeID;
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
        return "ClassPojo [tradeID = "+tradeID+", units = "+units+"]";
    }
}