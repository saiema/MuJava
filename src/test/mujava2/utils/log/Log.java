package test.mujava2.utils.log;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A very simple logging class that allows to modify the current log level, two simple publish methods
 * to define a message, an exception (optional) and the class that originated a specific log message.
 * 
 * <p>
 * 
 * This class is implemented as a Singleton, although it supports a clean method to restore the log to the initial state
 * 
 * @author Simon Emmanuel Gutierrez Brida
 * @version 0.1
 */
public class Log {
	
	private static Log instance;
	private int currentLvl = 0;
	private Queue<LogItem> log;
	
	/**
	 * @return an instance of this class
	 */
	public static Log getLog() {
		if (instance == null) {
			instance = new Log();
		}
		return instance;
	}
	
	/**
	 * Increment the current log level, any call to a publish method will use this new level
	 */
	public void incLevel() {
		this.currentLvl++;
	}
	
	/**
	 * Decrement the current log level, any call to a publish method will use this new level
	 */
	public void decLevel() {
		if (this.currentLvl > 0) this.currentLvl--;
	}

	/**
	 * Logs a new message
	 * 
	 * @param msg		:	the text of the log	:	{@code String}
	 * @param origin	:	the class that originated this new message	:	{@code Class<?>}
	 * @throws IllegalArgumentException if {@code origin} is {@code null}
	 */
	public void publish(String msg, Class<?> origin) throws IllegalArgumentException {
		publish(msg, null, origin);
	}
	
	/**
	 * Logs a new message
	 * 
	 * @param msg		:	the text of the log	:	{@code String}
	 * @param exc		:	the exception associated with this log message	:	{@code Exception}
	 * @param origin	:	the class that originated this new message	:	{@code Class<?>}
	 * @throws IllegalArgumentException if {@code origin} is {@code null}
	 */
	public void publish(String msg, Exception exc, Class<?> origin) throws IllegalArgumentException {
		if (origin == null) throw new IllegalArgumentException("log.Log#publish : can't use a null origin");
		this.log.add(new LogItem(msg, exc, origin, this.currentLvl));
	}
	
	/**
	 * Clear all log messages
	 */
	public void clear() {
		this.log.clear();
	}
	
	@Override
	public String toString() {
		String res = "";
		Iterator<LogItem> it = this.log.iterator();
		while (it.hasNext()) {
			LogItem curr = it.next();
			res += curr.toString();
			if (it.hasNext()) res += "\n"; 
		}
		return res;
	}
	
	private Log() {
		this.currentLvl = 0;
		this.log = new LinkedList<LogItem>();
	}
	
}
