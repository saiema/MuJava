package mujava.junit.runner;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.rules.Timeout;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

public class FailFastCapableParameterized extends Suite {

	private class FailFastTestClassRunnerForParameters extends FailFastCapableBlockJUnit4ClassRunner {
		private final int fParameterSetNumber;

		private final List<Object[]> fParameterList;

		FailFastTestClassRunnerForParameters(Class<?> type, List<Object[]> parameterList, int i, boolean failFast, long timeout, long discard) throws InitializationError {
			super(type, failFast, timeout, discard);
			fParameterList = parameterList;
			fParameterSetNumber = i;
		}

		@Override
		public Object createTest() throws Exception {
			return getTestClass().getOnlyConstructor().newInstance(
					computeParams());
		}

		private Object[] computeParams() throws Exception {
			try {
				return fParameterList.get(fParameterSetNumber);
			} catch (ClassCastException e) {
				throw new Exception(String.format(
						"%s.%s() must return a Collection of arrays.",
						getTestClass().getName(),
						getParametersMethod(getTestClass()).getName()));
			}
		}

		@Override
		protected String getName() {
			return String.format("[%s]", fParameterSetNumber);
		}

		@Override
		protected String testName(final FrameworkMethod method) {
			return String.format("%s[%s]", method.getName(),
					fParameterSetNumber);
		}

		@Override
		protected void validateConstructor(List<Throwable> errors) {
			validateOnlyOneConstructor(errors);
		}

		@Override
		protected Statement classBlock(RunNotifier notifier) {
			return childrenInvoker(notifier);
		}
	}

	private final ArrayList<Runner> runners = new ArrayList<Runner>();

	/**
	 * Only called reflectively. Do not use programmatically.
	 */
	public FailFastCapableParameterized(Class<?> klass, boolean failFast, long timeout, long discard) throws Throwable {
		super(klass, Collections.<Runner> emptyList());
		//System.out.println(this.getClass().getCanonicalName() + ".<init>");
		FailFastCapableBlockJUnit4ClassRunner.ignore = false;
		List<Object[]> parametersList = getParametersList(getTestClass());
		if (hasTimeoutRule(getTestClass())) timeout = 0L;
		for (int i = 0; i < parametersList.size(); i++)
			runners.add(new FailFastTestClassRunnerForParameters(getTestClass().getJavaClass(), parametersList, i, failFast, timeout, discard));
	}

	@Override
	protected List<Runner> getChildren() {
		return runners;
	}

	@SuppressWarnings("unchecked")
	private List<Object[]> getParametersList(TestClass klass) throws Throwable {
		return (List<Object[]>) getParametersMethod(klass).invokeExplosively(
				null);
	}

	private FrameworkMethod getParametersMethod(TestClass testClass)
			throws Exception {
		List<FrameworkMethod> methods = testClass
				.getAnnotatedMethods(Parameters.class);
		for (FrameworkMethod each : methods) {
			int modifiers = each.getMethod().getModifiers();
			if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers))
				return each;
		}

		throw new Exception("No public static parameters method on class " + testClass.getName());
	}
	
	private boolean hasTimeoutRule(TestClass klass) {
		List<FrameworkField> ruleFields = klass.getAnnotatedFields(Rule.class);
		if (ruleFields.isEmpty()) return false;
		for (FrameworkField field : ruleFields) {
			if (field.getField().getType().getName().compareTo(Timeout.class.getName()) == 0) return true;
		}
		return false;
	}

}
