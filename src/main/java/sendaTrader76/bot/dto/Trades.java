package sendaTrader76.bot.dto;

public class Trades
{
    private String id;

    private String financing;

    private String price;

    private String initialUnits;

    private String state;

    private String unrealizedPL;

    private String openTime;

    private String realizedPL;

    private String instrument;

    private String currentUnits;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getFinancing ()
    {
        return financing;
    }

    public void setFinancing (String financing)
    {
        this.financing = financing;
    }

    public String getPrice ()
    {
        return price;
    }

    public void setPrice (String price)
    {
        this.price = price;
    }

    public String getInitialUnits ()
    {
        return initialUnits;
    }

    public void setInitialUnits (String initialUnits)
    {
        this.initialUnits = initialUnits;
    }

    public String getState ()
    {
        return state;
    }

    public void setState (String state)
    {
        this.state = state;
    }

    public String getUnrealizedPL ()
    {
        return unrealizedPL;
    }

    public void setUnrealizedPL (String unrealizedPL)
    {
        this.unrealizedPL = unrealizedPL;
    }

    public String getOpenTime ()
    {
        return openTime;
    }

    public void setOpenTime (String openTime)
    {
        this.openTime = openTime;
    }

    public String getRealizedPL ()
    {
        return realizedPL;
    }

    public void setRealizedPL (String realizedPL)
    {
        this.realizedPL = realizedPL;
    }

    public String getInstrument ()
    {
        return instrument;
    }

    public void setInstrument (String instrument)
    {
        this.instrument = instrument;
    }

    public String getCurrentUnits ()
    {
        return currentUnits;
    }

    public void setCurrentUnits (String currentUnits)
    {
        this.currentUnits = currentUnits;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", financing = "+financing+", price = "+price+", initialUnits = "+initialUnits+", state = "+state+", unrealizedPL = "+unrealizedPL+", openTime = "+openTime+", realizedPL = "+realizedPL+", instrument = "+instrument+", currentUnits = "+currentUnits+"]";
    }
}