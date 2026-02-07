package sendaTrader76.bot.services;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import sendaTrader76.bot.dto.Candle;
import sendaTrader76.bot.dto.CandleJS;
import sendaTrader76.bot.dto.GranuarityType;

public class CandleS1Chart extends CandleChart {

	public CandleS1Chart(SimpMessageSendingOperations template, String webSocketTopic) {
		super(template, webSocketTopic);
	}

	@Override
	public ZonedDateTime getTruncatedTime(ZonedDateTime candleTime) {
		return candleTime.truncatedTo(ChronoUnit.SECONDS);
	}

	@Override
	public int getCurrentTimeUnit(ZonedDateTime candleTime) {
		return candleTime.getSecond();
	}

	@Override
	GranuarityType getGranularityType() {
		return GranuarityType.S1;
	}
}