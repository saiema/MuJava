package timeoutTest;

public class TimeoutTestTarget {
	
	
	public static void timeout(int to) throws InterruptedException {
		int i = 0; //mutGenLimit 1
		Thread.sleep(to);
	}

}
