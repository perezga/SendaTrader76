package sendaTrader76.bot.dto;


public class PricesResponse
{
private Price[] prices;

public Price[] getPrices ()
{
return prices;
}

public void setPrices (Price[] prices)
{
this.prices = prices;
}

@Override
public String toString()
{
return "ClassPojo [prices = "+prices+"]";
}
}
