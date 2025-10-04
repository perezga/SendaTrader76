package sendaTrader76.bot.dto;

import java.time.ZonedDateTime;

public class Candle {
	private ZonedDateTime time;

	private boolean complete;

	private String volume;

	private Mid mid;

	public ZonedDateTime getTime() {
		return time;
	}

	public void setTime(ZonedDateTime time) {
		this.time = time;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public Mid getMid() {
		return mid;
	}

	public void setMid(Mid mid) {
		this.mid = mid;
	}

	@Override
	public String toString() {
		return "time = " + time + ", complete = " + complete + ", volume = " + volume + ", mid = " + mid;
	}
}