package sendaTrader76.bot.dto;

public class Bids
{
    private String liquidity;

    private double price;

    public String getLiquidity ()
    {
        return liquidity;
    }

    public void setLiquidity (String liquidity)
    {
        this.liquidity = liquidity;
    }

    public double getPrice ()
    {
        return price;
    }

    public void setPrice (double price)
    {
        this.price = price;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [liquidity = "+liquidity+", price = "+price+"]";
    }
}
