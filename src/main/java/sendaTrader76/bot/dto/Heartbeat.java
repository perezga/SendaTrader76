package sendaTrader76.bot.dto;

import java.time.ZonedDateTime;

public class Heartbeat {
	private ZonedDateTime time;

	private String type;

	public ZonedDateTime getTime() {
		return time;
	}

	public void setTime(ZonedDateTime time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ClassPojo [time = " + time + ", type = " + type + "]";
	}
}
