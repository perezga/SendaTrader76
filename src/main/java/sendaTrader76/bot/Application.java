package sendaTrader76.bot;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.google.common.eventbus.EventBus;

import sendaTrader76.bot.dto.InstrumentType;
import sendaTrader76.bot.indicators.BollingerIndicator;
import sendaTrader76.bot.services.AccountBackTestingService;
import sendaTrader76.bot.services.AccountServiceOanda;
import sendaTrader76.bot.services.Candle1MChart;
import sendaTrader76.bot.services.CandleS1Chart;
import sendaTrader76.bot.services.PriceStreamBacktTestingService;
import sendaTrader76.bot.services.PriceStreamService;
import sendaTrader76.bot.services.TransactionStreamService;
import sendaTrader76.bot.strategies.StrategyExecutor;

@SpringBootApplication
public class Application implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	@Autowired
	private PriceStreamService priceStreamService;

	private PriceStreamBacktTestingService priceStreamBackTestingService;

	@Autowired
	private TransactionStreamService transactionStreamService;

	@Autowired
	private BollingerIndicator bollingerIndicator;

	@Autowired
	private AccountBackTestingService accountBackTestingService;
	
	@Autowired
	private AccountServiceOanda accountService;

	@Value("${oanda.account.id}")
	private String accountId;

	@Value("${strader76.strategy.test}")
	private boolean strategyTest;

	@Value("${strader76.websocket.topic}")
	private String webSocketTopic;
	
	@Value("${strader76.websocket.topic.positions}")
	private String webSocketTopicPositions;

	@Value("${strader76.websocket.test.topic}")
	private String webSocketTestTopic;
	
	@Autowired
	private EventBus eventBus;

	@Autowired
	private SimpMessagingTemplate template;

	public static void main(String args[]) {
		SpringApplication.run(Application.class);
	}

	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		log.info("Here code to execute after app starts...%s", "Prueba");

		if (strategyTest) {
			CandleS1Chart candleS1Chart = new CandleS1Chart(template, webSocketTopic);
			StrategyExecutor strategyExecutor = new StrategyExecutor(accountBackTestingService, candleS1Chart, bollingerIndicator, accountId);

			priceStreamBackTestingService = new PriceStreamBacktTestingService(accountBackTestingService,
					Arrays.asList(candleS1Chart), strategyExecutor);
			priceStreamBackTestingService.startPriceService();
		} else {
			transactionStreamService.startTransactionStream(accountId, webSocketTopicPositions);

			CandleS1Chart candleS1Chart = new CandleS1Chart(template, webSocketTopic);
			Candle1MChart candle1MChart = new Candle1MChart(template, webSocketTopic);
			
			
			StrategyExecutor strategyExecutor = new StrategyExecutor(accountService, candleS1Chart, bollingerIndicator, accountId);
			eventBus.register(strategyExecutor);
			

			priceStreamService.startPriceService(InstrumentType.EUR_USD, accountId, Arrays.asList(candleS1Chart, candle1MChart));
		}

	}
}