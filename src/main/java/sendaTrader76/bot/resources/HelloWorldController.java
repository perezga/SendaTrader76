package sendaTrader76.bot.resources;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import sendaTrader76.bot.dto.AccountDetail;
import sendaTrader76.bot.dto.Accounts;
import sendaTrader76.bot.dto.CandleFormat;
import sendaTrader76.bot.dto.CandleResponse;
import sendaTrader76.bot.dto.GranuarityType;
import sendaTrader76.bot.dto.InstrumentType;
import sendaTrader76.bot.dto.OrderRequest;
import sendaTrader76.bot.dto.OrderResponse;
import sendaTrader76.bot.dto.PricesResponse;
import sendaTrader76.bot.services.AccountService;
import sendaTrader76.bot.services.AccountServiceOanda;
import sendaTrader76.bot.services.CandleService;

@Controller
@RequestMapping("/accounts")
public class HelloWorldController {

	private AccountService accountService;

	private CandleService candleService;;

	private RestTemplate restTemplate;

	public HelloWorldController(RestTemplateBuilder restTemplateBuilder, CandleService candleService, AccountServiceOanda accountService) {
		this.restTemplate = restTemplateBuilder.build();
		this.candleService = candleService;
		this.accountService = accountService;
	}

	@RequestMapping(method = RequestMethod.GET)
	// public @ResponseBody Accounts getAccount(@RequestParam(value = "name",
	// required = false, defaultValue = "Stranger") String name) {
	public @ResponseBody Accounts getAccounts() {

		HttpEntity<Accounts> response = restTemplate.getForEntity("/v3/accounts/", Accounts.class);

		return response.getBody();
	}

	@RequestMapping(value = "/{accountId}", method = RequestMethod.GET)
	public @ResponseBody AccountDetail getAccount(@PathVariable String accountId) {

		HttpEntity<AccountDetail> response = restTemplate.getForEntity(
				"/v3/accounts/{accountId}", AccountDetail.class, accountId);

		return response.getBody();
	}

	@RequestMapping(value = "/{accountId}/orders", method = RequestMethod.POST)
	public @ResponseBody OrderResponse postOrder(@PathVariable String accountId,
			@RequestBody OrderRequest orderRequest) {

		return accountService.postOrder(accountId, orderRequest);
	}

	// @PathVariable @DateTimeFormat(iso=ISO.DATE)

	// ?instrument=EUR_USD&count=2&candleFormat=midpoint&granularity=D&dailyAlignment=0&alignmentTimezone=America%2FNew_York""+
	// "
	@GetMapping(value = "/candles")
	public @ResponseBody CandleResponse getCandles(@RequestParam InstrumentType instrument, @RequestParam Integer count,
			@RequestParam GranuarityType granularity, @RequestParam CandleFormat candleFormat) {

		CandleResponse candleResponse = new CandleResponse();
		candleResponse.setCandles(candleService.getCandles(instrument, count, granularity, candleFormat));

		return candleResponse;
	}

	@GetMapping(value = "/{accountId}/pricing")
	public @ResponseBody PricesResponse getPrice(@PathVariable String accountId,
			@RequestParam InstrumentType instrument) {

		return accountService.getPrice(accountId, instrument);
	}

}