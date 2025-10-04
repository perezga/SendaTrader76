package sendaTrader76.bot.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class Price
{
	private ZonedDateTime time;

    private UnitsAvailable unitsAvailable;

    private String status;

    private BigDecimal closeoutBid;

    private Asks[] asks;

    private Bids[] bids;

    private String instrument;

    private QuoteHomeConversionFactory quoteHomeConversionFactory;

    private BigDecimal closeoutAsk;

    public ZonedDateTime getTime ()
    {
        return time;
    }

    public void setTime (ZonedDateTime time)
    {
        this.time = time;
    }

    public UnitsAvailable getUnitsAvailable ()
    {
        return unitsAvailable;
    }

    public void setUnitsAvailable (UnitsAvailable unitsAvailable)
    {
        this.unitsAvailable = unitsAvailable;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public BigDecimal getCloseoutBid ()
    {
        return closeoutBid;
    }

    public void setCloseoutBid (BigDecimal closeoutBid)
    {
        this.closeoutBid = closeoutBid;
    }

    public Asks[] getAsks ()
    {
        return asks;
    }

    public void setAsks (Asks[] asks)
    {
        this.asks = asks;
    }

    public Bids[] getBids ()
    {
        return bids;
    }

    public void setBids (Bids[] bids)
    {
        this.bids = bids;
    }

    public String getInstrument ()
    {
        return instrument;
    }

    public void setInstrument (String instrument)
    {
        this.instrument = instrument;
    }

    public QuoteHomeConversionFactory getQuoteHomeConversionFactory ()
    {
        return quoteHomeConversionFactory;
    }

    public void setQuoteHomeConversionFactory (QuoteHomeConversionFactory quoteHomeConversionFactory)
    {
        this.quoteHomeConversionFactory = quoteHomeConversionFactory;
    }

    public BigDecimal getCloseoutAsk ()
    {
        return closeoutAsk;
    }

    public void setCloseoutAsk (BigDecimal closeoutAsk)
    {
        this.closeoutAsk = closeoutAsk;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [time = "+time+", unitsAvailable = "+unitsAvailable+", status = "+status+", closeoutBid = "+closeoutBid+", asks = "+asks.toString()+", bids = "+bids.toString()+", instrument = "+instrument+", QuoteHomeConversionFactory = "+quoteHomeConversionFactory+", closeoutAsk = "+closeoutAsk+"]";
    }
}
