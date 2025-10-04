package sendaTrader76.bot.dto;

public class Default
{
    private String shortPosition;

    private String longPosition;

    public String getshortPosition ()
    {
        return shortPosition;
    }

    public void setshortPosition (String shortPosition)
    {
        this.shortPosition = shortPosition;
    }

    public String getlongPosition ()
    {
        return longPosition;
    }

    public void setlongPosition (String longPosition)
    {
        this.longPosition = longPosition;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [shortPosition = "+shortPosition+", longPosition = "+longPosition+"]";
    }
}
