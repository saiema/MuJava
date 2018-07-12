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
	private boolean failFast;
	protected long timeout;

	public FailFastCapableBlockJUnit4ClassRunner(Class<?> klass, boolean failFast, long timeout) throws InitializationError {
		super(klass);
		this.failFast = failFast;
		this.timeout = timeout;
	}
	
	
	
	@Override
	protected Statement withPotentialTimeout(FrameworkMethod method, Object test, Statement next) {
		boolean methodHasTimeout = false;
		Test ann = method.getAnnotation(Test.class);
		if (ann != null) methodHasTimeout = ann.timeout() > 0;
		if (timeout > 0 && methodHasTimeout) 
			return FailOnTimeout.builder()
		               .withTimeout(timeout, TimeUnit.MILLISECONDS)
		               .build(next);
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
		try {
			if (!failFast || !FailFastCapableBlockJUnit4ClassRunner.ignore) methodBlock(method).evaluate();
		} catch (AssumptionViolatedException e) {
			eachNotifier.addFailedAssumption(e);
			FailFastCapableBlockJUnit4ClassRunner.ignore = true;
		} catch (Throwable e) {
			eachNotifier.addFailure(e);
			FailFastCapableBlockJUnit4ClassRunner.ignore = true;
		} finally {
			eachNotifier.fireTestFinished();
		}
	}
	
	private EachTestNotifier makeNotifier(FrameworkMethod method, RunNotifier notifier) {
		Description description= describeChild(method);
		return new EachTestNotifier(notifier, description);
	}

}
