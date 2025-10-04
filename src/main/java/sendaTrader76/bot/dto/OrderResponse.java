package sendaTrader76.bot.dto;

public class OrderResponse
{
    private String lastTransactionID;

    private OrderFillTransaction orderFillTransaction;

    private OrderCreateTransaction orderCreateTransaction;

    private String[] relatedTransactionIDs;
    
    private Positions[] positions;


    public Positions[] getPositions() {
		return positions;
	}

	public void setPositions(Positions[] positions) {
		this.positions = positions;
	}

	public String getLastTransactionID ()
    {
        return lastTransactionID;
    }

    public void setLastTransactionID (String lastTransactionID)
    {
        this.lastTransactionID = lastTransactionID;
    }

    public OrderFillTransaction getOrderFillTransaction ()
    {
        return orderFillTransaction;
    }

    public void setOrderFillTransaction (OrderFillTransaction orderFillTransaction)
    {
        this.orderFillTransaction = orderFillTransaction;
    }

    public OrderCreateTransaction getOrderCreateTransaction ()
    {
        return orderCreateTransaction;
    }

    public void setOrderCreateTransaction (OrderCreateTransaction orderCreateTransaction)
    {
        this.orderCreateTransaction = orderCreateTransaction;
    }

    public String[] getRelatedTransactionIDs ()
    {
        return relatedTransactionIDs;
    }

    public void setRelatedTransactionIDs (String[] relatedTransactionIDs)
    {
        this.relatedTransactionIDs = relatedTransactionIDs;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [lastTransactionID = "+lastTransactionID+", orderFillTransaction = "+orderFillTransaction+", orderCreateTransaction = "+orderCreateTransaction+", relatedTransactionIDs = "+relatedTransactionIDs+"]";
    }
}
	