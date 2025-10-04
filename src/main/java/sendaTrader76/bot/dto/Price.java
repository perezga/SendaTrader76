package sendaTrader76.bot.dto;

import com.oanda.v20.pricing.ClientPrice;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Price {
    private ZonedDateTime time;
    private String status;
    private BigDecimal closeoutBid;
    private BigDecimal closeoutAsk;
    private String instrument;

    public Price(ClientPrice clientPrice) {
        this.time = ZonedDateTime.parse(clientPrice.getTime());
        this.status = clientPrice.getStatus().toString();
        this.closeoutBid = clientPrice.getCloseoutBid().bigDecimalValue();
        this.closeoutAsk = clientPrice.getCloseoutAsk().bigDecimalValue();
        this.instrument = clientPrice.getInstrument().toString();
    }

    public Price(ZonedDateTime time, BigDecimal bid, BigDecimal ask) {
        this.time = time;
        this.closeoutBid = bid;
        this.closeoutAsk = ask;
        this.status = "tradeable";
        this.instrument = "EUR_USD";
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getCloseoutBid() {
        return closeoutBid;
    }

    public void setCloseoutBid(BigDecimal closeoutBid) {
        this.closeoutBid = closeoutBid;
    }

    public BigDecimal getCloseoutAsk() {
        return closeoutAsk;
    }

    public void setCloseoutAsk(BigDecimal closeoutAsk) {
        this.closeoutAsk = closeoutAsk;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    @Override
    public String toString() {
        return "Price{" +
                "time=" + time +
                ", status='" + status + '\'' +
                ", closeoutBid=" + closeoutBid +
                ", closeoutAsk=" + closeoutAsk +
                ", instrument='" + instrument + '\'' +
                '}';
    }
}