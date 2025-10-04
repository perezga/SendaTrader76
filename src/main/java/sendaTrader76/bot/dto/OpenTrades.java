package sendaTrader76.bot.dto;

import java.util.List;

public class OpenTrades {
	    private String lastTransactionID;

	    private List<Trades> trades;

	    public String getLastTransactionID ()
	    {
	        return lastTransactionID;
	    }

	    public void setLastTransactionID (String lastTransactionID)
	    {
	        this.lastTransactionID = lastTransactionID;
	    }

	    public List<Trades> getTrades ()
	    {
	        return trades;
	    }

	    public void setTrades (List<Trades> trades)
	    {
	        this.trades = trades;
	    }

	    @Override
	    public String toString()
	    {
	        return "ClassPojo [lastTransactionID = "+lastTransactionID+", trades = "+trades+"]";
	    }
	}