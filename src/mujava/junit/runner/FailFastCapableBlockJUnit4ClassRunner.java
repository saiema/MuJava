package mujava.junit.runner;

import java.util.concurrent.TimeUnit;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;


public class FailFastCapableBlockJUnit4ClassRunner extends BlockJUnit4ClassRunner {
	
	protected static boolean ignore = false;
	private static final boolean verbose = MuJavaJunitTestRunnerBuilder.verbose;
	private boolean failFast;
	protected long timeout;
	protected long discard;

	public FailFastCapableBlockJUnit4ClassRunner(Class<?> klass, boolean failFast, long timeout, long discard) throws InitializationError {
		super(klass);
		this.failFast = failFast;
		this.timeout = timeout;
		this.discard = discard;
	}
	
	
	
	@Override
	protected Statement withPotentialTimeout(FrameworkMethod method, Object test, Statement next) {
		boolean methodHasTimeout = false;
		Test ann = method.getAnnotation(Test.class);
		if (ann != null) methodHasTimeout = ann.timeout() > 0;
		if (timeout > 0 && !methodHasTimeout) {
			//System.out.println("RUNNING WITH TIMEOUT: " + timeout);
			return FailOnTimeout.builder()
		               .withTimeout(timeout, TimeUnit.MILLISECONDS)
		               .withLookingForStuckThread(true)
		               .build(next);
		}
		return super.withPotentialTimeout(method, test, next);
	}



	@Override
	protected void runChild(FrameworkMethod method, RunNotifier notifier) {
		EachTestNotifier eachNotifier= makeNotifier(method, notifier);
		if (method.getAnnotation(Ignore.class) != null) {
			eachNotifier.fireTestIgnored();
			return;
		}

		eachNotifier.fireTestStarted();
		long startTime = 0, endTime = 0;
		try {
			if (verbose) System.out.println("About to run test : " + method.getName());
			startTime = System.currentTimeMillis();
			if (!FailFastCapableBlockJUnit4ClassRunner.ignore) methodBlock(method).evaluate();
		} catch (AssumptionViolatedException e) {
			if (verbose) System.out.println("Test : " + method.getName() + " violated an assumption");
			eachNotifier.addFailedAssumption(e);
			if (failFast) FailFastCapableBlockJUnit4ClassRunner.ignore = true;
		} catch (Throwable e) {
			if (verbose) System.out.println("Test : " + method.getName() + " throwed an exception");
			eachNotifier.addFailure(e);
			if (failFast) FailFastCapableBlockJUnit4ClassRunner.ignore = true;
		} finally {
			eachNotifier.fireTestFinished();
			endTime = System.currentTimeMillis();
			long testTime = endTime - startTime;
			if (verbose) System.out.println("Test : " + method.getName() + " took " + testTime);
			if (verbose) {
				System.out.println("ignore : " + FailFastCapableBlockJUnit4ClassRunner.ignore);
				System.out.println("testTime : " + testTime);
			}
			if (!FailFastCapableBlockJUnit4ClassRunner.ignore && startTime > 0 && endTime > 0 && discard > 0) {
				if (testTime > discard) {
					if (verbose) System.out.println("Test : " + method.getName() + " discarded");
					FailFastCapableBlockJUnit4ClassRunner.ignore = true;
					eachNotifier.addFailure(new DiscardedException("[DISCARDED]Discard timeout was " + discard + " and test took " + testTime));
				}
			}
		}
	}
	
	private EachTestNotifier makeNotifier(FrameworkMethod method, RunNotifier notifier) {
		Description description= describeChild(method);
		return new EachTestNotifier(notifier, description);
	}

}
