package sendaTrader76.bot.dto;

import java.util.List;

public class CandleResponse
{
    private String granularity;

    private List<Candle> candles;

    private String instrument;

    public String getGranularity ()
    {
        return granularity;
    }

    public void setGranularity (String granularity)
    {
        this.granularity = granularity;
    }

    public List<Candle> getCandles ()
    {
        return candles;
    }

    public void setCandles (List<Candle> candles)
    {
        this.candles = candles;
    }

    public String getInstrument ()
    {
        return instrument;
    }

    public void setInstrument (String instrument)
    {
        this.instrument = instrument;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [granularity = "+granularity+", candles = "+candles+", instrument = "+instrument+"]";
    }
}