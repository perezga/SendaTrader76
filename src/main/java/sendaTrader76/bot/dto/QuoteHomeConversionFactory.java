package sendaTrader76.bot.dto;

public class QuoteHomeConversionFactory 
{
    private String positiveUnits;

    private String negativeUnits;

    public String getPositiveUnits ()
    {
        return positiveUnits;
    }

    public void setPositiveUnits (String positiveUnits)
    {
        this.positiveUnits = positiveUnits;
    }

    public String getNegativeUnits ()
    {
        return negativeUnits;
    }

    public void setNegativeUnits (String negativeUnits)
    {
        this.negativeUnits = negativeUnits;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [positiveUnits = "+positiveUnits+", negativeUnits = "+negativeUnits+"]";
    }
}
