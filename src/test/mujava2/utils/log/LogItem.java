package test.mujava2.utils.log;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * This class represents all log messages that can be stored
 * 
 * <p>
 * 
 * Any log message is comprised of:
 * <p>
 * <li>A text message	:	the log message, a text</li>
 * <li>An exception		:	an exception associated with this log message (optional)</li>
 * <li>An origin class	:	the class that logged this particular log message</li>
 * 
 * @author Simon Emmanuel Gutierrez Brida
 * @version 0.1
 */
public class LogItem {
	
	private String logMsg;
	private Exception exc;
	private Class<?> origin;
	private int lvl;
	private final String INDENT = "\t";
	
	/**
	 * Constructs a new instance of this class, note that the constructor is protected, this is so
	 * because this class should only be used by {@link log.Log}.
	 * 
	 * @param logMsg	:	the text associated with this log message	:	{@code String}
	 * @param exc		:	the exception (if any) associated with this log message	:	{@code Exception}
	 * @param origin	:	the class that originated this log message	:	{@code Class<?>}
	 * @param lvl		:	the level of this log message with 0 being the root level	:	{@code int}
	 */
	protected LogItem(String logMsg, Exception exc, Class<?> origin, int lvl) {
		this.logMsg = logMsg;
		this.exc = exc;
		this.origin = origin;
		this.lvl = lvl;
	}
	
	@Override
	public String toString() {
		return indent() + "Origin: " + this.origin.getName() + "\n" + indent() + "Message: " + indentText(this.logMsg) + "\n" + (this.exc!=null?(indent() + printException() + "\n" + indent() + printTrace()):"");
	}
	
	private String printException() {
		return "Exception : " + this.exc.toString();
	}
	
	private String printTrace() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps;
		try {
			ps = new PrintStream(baos,true,"utf-8");
			this.exc.printStackTrace(ps);
			return "Trace : " + baos.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Trace : ERROR WHILE PROCESSING TRACE";
	}
	
	private String indent() {
		String indent = "";
		for (int i = 0; i < this.lvl; i++) indent += this.INDENT;
		return indent;
	}
	
	private String indentText(String text) {
		String[] lines = text.split("\n");
		String res = "";
		for (String l : lines) {
			res += indent() + l + "\n";
		}
		return res;
	}

}
