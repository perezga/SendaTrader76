package sendaTrader76.bot.dto;

public class OrderCreateTransaction
{
    private String id;

    private String userID;

    private String accountID;

    private String time;

    private String positionFill;

    private String reason;

    private String batchID;

    private String timeInForce;

    private String instrument;

    private String type;

    private String units;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getUserID ()
    {
        return userID;
    }

    public void setUserID (String userID)
    {
        this.userID = userID;
    }

    public String getAccountID ()
    {
        return accountID;
    }

    public void setAccountID (String accountID)
    {
        this.accountID = accountID;
    }

    public String getTime ()
    {
        return time;
    }

    public void setTime (String time)
    {
        this.time = time;
    }

    public String getPositionFill ()
    {
        return positionFill;
    }

    public void setPositionFill (String positionFill)
    {
        this.positionFill = positionFill;
    }

    public String getReason ()
    {
        return reason;
    }

    public void setReason (String reason)
    {
        this.reason = reason;
    }

    public String getBatchID ()
    {
        return batchID;
    }

    public void setBatchID (String batchID)
    {
        this.batchID = batchID;
    }

    public String getTimeInForce ()
    {
        return timeInForce;
    }

    public void setTimeInForce (String timeInForce)
    {
        this.timeInForce = timeInForce;
    }

    public String getInstrument ()
    {
        return instrument;
    }

    public void setInstrument (String instrument)
    {
        this.instrument = instrument;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
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
        return "ClassPojo [id = "+id+", userID = "+userID+", accountID = "+accountID+", time = "+time+", positionFill = "+positionFill+", reason = "+reason+", batchID = "+batchID+", timeInForce = "+timeInForce+", instrument = "+instrument+", type = "+type+", units = "+units+"]";
    }
}
	