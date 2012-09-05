package net.jfly.plugin.ActiveRecord;

@SuppressWarnings("serial")
public class ActiveRecordException extends RuntimeException {

	public ActiveRecordException(String message) {
		super(message);
	}

	public ActiveRecordException(Throwable cause) {
		super(cause);
	}

	public ActiveRecordException(String message, Throwable cause) {
		super(message, cause);
	}
}
