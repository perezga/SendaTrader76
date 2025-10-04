package sendaTrader76.bot.services;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import sendaTrader76.bot.dto.Candle;
import sendaTrader76.bot.dto.CandleJS;
import sendaTrader76.bot.dto.GranuarityType;

public class Candle1MChart extends CandleChart {	

	public Candle1MChart(SimpMessagingTemplate template, String webSocketTopic) {
		super(template, webSocketTopic);
	}

	@Value("${strader76.strategy.test}")
	private boolean strategyTest;
	
	
	@Override
	public ZonedDateTime getTruncatedTime(ZonedDateTime candleTime) {
		return candleTime.truncatedTo(ChronoUnit.MINUTES);
	}

	@Override
	public int getCurrentTimeUnit(ZonedDateTime candleTime) {
		return candleTime.getMinute();
	}

	@Override
	GranuarityType getGranularityType() {
		return GranuarityType.M1;
	}
}