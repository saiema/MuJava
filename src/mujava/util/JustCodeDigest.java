package mujava.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

/**
 * Utility class to obtain a md5 digest of a java file prior to strip all coments and whitespace
 *
 * @author Simón Emmanuel Gutiérrez Brida
 * @version 0.4
 */
public class JustCodeDigest {
	private static final Pattern COMMENT_JAVADOC = Pattern.compile("/\\*\\*(.*)\\*/");
	private static final Pattern COMMENT_MULTILINE = Pattern.compile("/\\*((?<!\\*).*)\\*/");
	private static final Pattern COMMENT_SINGLE = Pattern.compile("//.*");
	private static final Pattern WHITESPACE = Pattern.compile("\\s");
	private static boolean printExceptions = false;
	private static Exception lastException = null;
	
	public static void printExceptions(boolean b) {
		JustCodeDigest.printExceptions = b;
	}
	
	public static Exception getLastException() {
		return JustCodeDigest.lastException;
	}

	private static String getJustCode(String original) {
		Matcher javadoc = COMMENT_JAVADOC.matcher(original);
		Matcher comment_multi = COMMENT_MULTILINE.matcher(javadoc.replaceAll(""));
		Matcher comment_single = COMMENT_SINGLE.matcher(comment_multi.replaceAll(""));
		Matcher whitespace = WHITESPACE.matcher(comment_single.replaceAll(""));
		return whitespace.replaceAll("");
	}
	
	public static byte[] digest(String original) {
		return digest(original, true);
	}
	
	public static byte[] digest(String original, boolean justCode) {
		String code = justCode?getJustCode(original):original;
		InputStream is = new ByteArrayInputStream(code.getBytes());
		DigestInputStream dis = null;
		JustCodeDigest.lastException = null;
		try {
			dis = new DigestInputStream(is, MessageDigest.getInstance("MD5"));
			while (dis.read() != -1) {}
			return dis.getMessageDigest().digest();
		} catch (NoSuchAlgorithmException e) {
			if (JustCodeDigest.printExceptions) e.printStackTrace();
			JustCodeDigest.lastException = e;
		} catch (IOException e) {
			if (JustCodeDigest.printExceptions) e.printStackTrace();
			JustCodeDigest.lastException = e;
		} finally {
			if (dis != null) {
				try {
					dis.close();
				} catch (IOException e) {
					if (JustCodeDigest.printExceptions) e.printStackTrace();
					if (JustCodeDigest.lastException != null) {
						Exception exc = new Exception(e);
						exc.initCause(JustCodeDigest.lastException);
						JustCodeDigest.lastException = exc;
					} else {
						JustCodeDigest.lastException = e;
					}
				}
			}
		}
		return null;
	}
	
	public static String toHex(byte[] bytes) {
		char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
	    char[] hexChars = new char[bytes.length * 2];
	    int v;
	    for ( int j = 0; j < bytes.length; j++ ) {
	        v = bytes[j] & 0xFF;
	        hexChars[j*2] = hexArray[v/16];
	        hexChars[j*2 + 1] = hexArray[v%16];
	    }
	    return new String(hexChars);
    }
	
	public static byte[] digest(File file) {
		return digest(file, true);
	}
	
	public static byte[] digest(File file, boolean justCode) {
		byte[] bytes;
		JustCodeDigest.lastException = null;
		try {
			bytes = Files.readAllBytes(file.toPath());
			String text = new String(bytes, StandardCharsets.UTF_8);
			return digest(text, justCode);
		} catch (IOException e) {
			if (JustCodeDigest.printExceptions) e.printStackTrace();
			JustCodeDigest.lastException = e;
		}
		return null;
	}
	
	public static byte[] digest(JarFile jar, String file, boolean justCode) {
		JustCodeDigest.lastException = null;
		ZipEntry entry = jar.getEntry(file);
		if (entry == null) return null;
		if (entry.isDirectory()) return null;
		int size = (int) entry.getSize();
		byte buff[] = new byte[size];
		DataInputStream dis;
		try {
			dis = new DataInputStream(jar.getInputStream(entry));
			dis.readFully(buff);
			dis.close();
			String text = new String(buff, StandardCharsets.UTF_8);
			return digest(text, justCode);
		} catch (IOException e) {
			if (JustCodeDigest.printExceptions) e.printStackTrace();
			JustCodeDigest.lastException = e;
		}
		return null;
	}

}
