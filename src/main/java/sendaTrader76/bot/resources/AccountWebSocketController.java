package sendaTrader76.bot.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import sendaTrader76.bot.dto.Greeting;
import sendaTrader76.bot.dto.HelloMessage;

@Controller
public class AccountWebSocketController {

	private static final Logger log = LoggerFactory.getLogger(AccountWebSocketController.class);

	@MessageMapping("/position/example")
	@SendTo("/topic/positions")
	public Greeting greeting(String transaction) {
		log.info("LOG " + transaction);
		return new Greeting(transaction);
	}

}