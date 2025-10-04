package sendaTrader76.bot.dto;

public class UnitsAvailable
{
    private ReduceFirst reduceFirst;

    private OpenOnly openOnly;

    private Default defaultValue;

    private ReduceOnly reduceOnly;

    public ReduceFirst getReduceFirst ()
    {
        return reduceFirst;
    }

    public void setReduceFirst (ReduceFirst reduceFirst)
    {
        this.reduceFirst = reduceFirst;
    }

    public OpenOnly getOpenOnly ()
    {
        return openOnly;
    }

    public void setOpenOnly (OpenOnly openOnly)
    {
        this.openOnly = openOnly;
    }

    public Default getDefaultValue ()
    {
        return defaultValue;
    }

    public void setDefaultValue (Default defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    public ReduceOnly getReduceOnly ()
    {
        return reduceOnly;
    }

    public void setReduceOnly (ReduceOnly reduceOnly)
    {
        this.reduceOnly = reduceOnly;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [reduceFirst = "+reduceFirst+", openOnly = "+openOnly+", default = "+defaultValue+", reduceOnly = "+reduceOnly+"]";
    }
}
			
