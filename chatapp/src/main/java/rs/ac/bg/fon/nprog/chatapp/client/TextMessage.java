package rs.ac.bg.fon.nprog.chatapp.client;

import java.util.GregorianCalendar;

public class TextMessage {
	private String sender;
	private String receiver;
	private String message;
	private GregorianCalendar time;
	
	public TextMessage(String sender, String receiver, String message, GregorianCalendar time) {
		this.sender = sender;
		this.receiver = receiver;
		this.message = message;
		this.time = time;
	}

	@Override
	public String toString() {
		return "TextMessage [sender=" + sender + ", receiver=" + receiver + ", message=" + message + ", time=" + 
				time.get(GregorianCalendar.HOUR_OF_DAY)+":" +time.get(GregorianCalendar.MINUTE)+"]";
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public GregorianCalendar getTime() {
		return time;
	}

	public void setTime(GregorianCalendar time) {
		this.time = time;
	}

	
	
	
}
