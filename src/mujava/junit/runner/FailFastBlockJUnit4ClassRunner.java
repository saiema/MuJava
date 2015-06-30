package mujava.junit.runner;

import org.junit.Ignore;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class FailFastBlockJUnit4ClassRunner extends BlockJUnit4ClassRunner {
	
	protected static boolean ignore = false;

	public FailFastBlockJUnit4ClassRunner(Class<?> klass) throws InitializationError {
		super(klass);
		//System.out.println(this.getClass().getCanonicalName() + ".<init>");
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
			if (!FailFastBlockJUnit4ClassRunner.ignore) methodBlock(method).evaluate();
			//else System.out.println("Ignoring " + method.toString());
		} catch (AssumptionViolatedException e) {
			eachNotifier.addFailedAssumption(e);
			FailFastBlockJUnit4ClassRunner.ignore = true;
		} catch (Throwable e) {
			eachNotifier.addFailure(e);
			FailFastBlockJUnit4ClassRunner.ignore = true;
		} finally {
			eachNotifier.fireTestFinished();
		}
	}
	
	private EachTestNotifier makeNotifier(FrameworkMethod method, RunNotifier notifier) {
		Description description= describeChild(method);
		return new EachTestNotifier(notifier, description);
	}

}
