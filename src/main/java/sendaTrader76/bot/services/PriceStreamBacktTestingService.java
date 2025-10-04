package sendaTrader76.bot.services;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import sendaTrader76.bot.dto.Price;
import sendaTrader76.bot.strategies.StrategyExecutor;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PriceStreamBacktTestingService {

    private static final Log logger = LogFactory.getLog(PriceStreamBacktTestingService.class);

    private List<CandleChart> candleChartList;
    private AccountBackTestingService accountService;
    private StrategyExecutor strategyExecutor;

    public PriceStreamBacktTestingService(AccountBackTestingService accountService, List<CandleChart> candleChartList, StrategyExecutor strategyExecutor) {
        this.candleChartList = candleChartList;
        this.accountService = accountService;
        this.strategyExecutor = strategyExecutor;
    }

    public void startPriceService() throws IOException, URISyntaxException, InterruptedException {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(getClass().getClassLoader().getResource("DAT_ASCII_EURUSD_T_201606.csv").toURI()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineSplit = StringUtils.split(line, ",");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmmssSSS").withZone(ZoneId.of("GMT-5"));
                ZonedDateTime priceDate = ZonedDateTime.parse(lineSplit[0], formatter);

                BigDecimal bid = new BigDecimal(lineSplit[1]);
                BigDecimal ask = new BigDecimal(lineSplit[2]);
                Price price = new Price(priceDate, bid, ask);

                candleChartList.forEach(candleChart -> candleChart.addCandle(price));
                accountService.setAskPrice(ask.doubleValue(), priceDate);
                accountService.setBidPrice(bid.doubleValue(), priceDate);
                strategyExecutor.PriceConsumer(price);
            }
        }
    }
}