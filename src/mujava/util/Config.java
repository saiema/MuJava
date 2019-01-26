package mujava.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.Test;
import org.junit.runner.RunWith;

import main.api.DependencyScanner;
import mujava.api.MutationOperator;
import mujava.app.Core;
import mujava.app.MutatorsInfo;

public class Config {
	
	private static final int DEFAULT_JUNIT_PARALLEL_RUNNER_THREADS = 8;
	private static final int DEFAULT_RELOADER_INSTANCES_LIMIT = 150;
	
	//basic folders
	private final String originalSrcDir;
	private final String originalBinDir;
	private final String mutantsOutputFolder;
	private String junitPath; //can only be null if external junit runner is false
	private String hamcrestPath; //can only be null if external junit runner is false
	//basic folders
	
	//basic mutation values
	private String classToMutate;
	private String[] externalClassesToMutate; //PROTOTYPE
	private Set<String> methodsToMutate;
	private boolean useExternalMutants;
	private boolean useSockets;
	private boolean writePrologue;
	//basic mutation values
	
	//mutation operators
	private Set<MutationOperator> operators;
	//mutation operators
	
	//advanced mutation options
	private long testTimeout;
	private long discardTimeout;
	private boolean runTestsInSeparateProcesses;
	private boolean fullVerboseMode;
	private boolean allowClassMutations;
	private boolean allowFieldMutations;
	private Set<String> bannedFields;
	private Set<String> bannedMethods;
	private Set<String> allowedMembers;
	private boolean ignoreMutGenLimit;
	private Set<String> allowedPackagesToReload;
	private boolean allowNumericLiteralVariations;
	private boolean disablePrimitiveToObjectAssignments;
	private boolean wrapPrimitiveToObjectAssignments;
	private boolean applyRefinedPRVOInMethodCallStatements;
	private int generation;
	private int reloaderInstancesLimit;
	private boolean aoisSkipFinal;
	private boolean aodsSkipExpressionStatements;
	private boolean rorReplaceWithTrue;
	private boolean rorReplaceWithFalse;
	private boolean rorSmartLiteralReplace;
	private boolean corUseAndOp;
	private boolean corUseOrOp;
	private boolean corUseXorOp;
	private boolean corUseBitAndOp;
	private boolean corUseBitOrOp;
	private boolean prvoSameLenght;
	private boolean prvoIncreaseLength;
	private boolean prvoDecreaseLength;
	private boolean prvoOneByTwo;
	private boolean prvoTwoByOne;
	private boolean prvoAllByOneLeft;
	private boolean prvoAllByOneRight;
	private boolean prvoLiteralMutations;
	private boolean prvoAllocationMutations;
	private boolean prvoArrayAllocationMutations;
	private boolean prvoNonNavigationExpressionMutations;
	private boolean prvoUseSuper;
	private boolean prvoUseThis;
	private boolean prvoReplacementWithLiterals;
	private boolean prvoUseNullLiteral;
	private boolean prvoUseTrueLiteral; 
	private boolean prvoUseFalseLiteral;
	private boolean prvoUseEmptyStringLiteral;
	private boolean prvoUseZeroLiteral;
	private boolean prvoUseOneLiteral;
	private boolean prvoUseStringLiterals;
	private boolean prvoAllowFinalMembers;
	private boolean prvoEnableRelaxedTypes;
	private boolean prvoEnableAutoboxing;
	private boolean prvoEnableInheritedElements;
	private boolean prvoAllowStaticFromNonStaticExpression;
	private boolean prvoSmartMode_arithmeticOpShortcuts;
	private boolean prvoSmartMode_assignments;
	private boolean useSimpleClassNames;
	private boolean useExternalJUnitRunner;
	private boolean useParallelExternalJUnitRunner;
	private int parallelExternalJUnitRunnerThreads;
	private boolean beeScanExpressions;
	private boolean beeSkipEquivalentMutations;
	private boolean beeSkipConstantsWithTrueAndFalse;
	//advanced mutation options
	
	//mutation score
	private boolean runMutationScore;
	private String testsBinDir;
	private Set<String> testClasses;
	private boolean showSurvivingMutants;
	private boolean toughnessAnalysis;
	private boolean outputMutationsInfo;
	private boolean useOldJUnit = false; //TODO: complete getters, setters, and config in file
	//mutation score
	
	//mutation score advanced options
	private boolean quickDeath;
	private boolean dynamicSubsumption;
	private String dynamicSubsumptionOutput;
	private boolean reduceDynamicSubsumptionGraph;
	//mutation score advanced options
	
	//auxiliary values
	private Set<Method> methodsInClassToMutate;
	private List<String> classesInOriginalBinDir;
	private List<String> packagesInOriginalBinDir;
	private List<String> testClassesInTestsBinDir;
	private final MutationOperator[] validMutOps = MutationOperator.values();
	private DependencyScanner testsScanner = null;
	//auxiliary values
	

	public Config(String originalSourceDir, String originalBinDir, String mutantsOutputFolder) {
		originalSrcDir = addTrailingSeparator(originalSourceDir);
		this.originalBinDir = addTrailingSeparator(originalBinDir);
		this.mutantsOutputFolder = addTrailingSeparator(mutantsOutputFolder);
		useExternalMutants = false;
		generation = 1;
		showSurvivingMutants = false;
		toughnessAnalysis = false;
		reloaderInstancesLimit = DEFAULT_RELOADER_INSTANCES_LIMIT;
		useExternalJUnitRunner = false;
		useParallelExternalJUnitRunner = false;
		parallelExternalJUnitRunnerThreads = DEFAULT_JUNIT_PARALLEL_RUNNER_THREADS;
		junitPath = null;
		hamcrestPath = null;
		externalClassesToMutate = null;
		testTimeout(0);
		discardTimeout(0);
		runTestsInSeparateProcesses(false);
		useExternalMutants(false);
		useSockets(false);
		writePrologue(false);
		outputMutationsInfo(false);
		useSimpleClassNames(false);
		initializeAIOSOptions();
		initializeAODSOptions();
		initializePRVOOptions();
		intitializeROROptions();
		initializeCOROptions();
		initializeBEEOptions();
		clearMethodsToMutate();
		clearOperators();
		fullVerboseMode(false);
		allowClassMutations(false);
		allowFieldMutations(false);
		clearMethodsInsideClassToMutate();
		clearClassesInOriginalBinDir();
		clearPackagesInOriginalBinDir();
		clearAllowedMembers();
		clearBannedMethods();
		clearBannedFields();
		clearPackagesToReload();
		clearTestClassesInTestsBinDir();
		clearTestClasses();
	}
	
	private void initializeAIOSOptions() {
		aoisSkipFinal(false);
	}
	
	private void initializeAODSOptions() {
		aodsSkipExpressionStatements(false);
	}
	
	private void initializeCOROptions() {
		corUseAndOp(true);
		corUseOrOp(true);
		corUseXorOp(true);
		corUseBitAndOp(true);
		corUseBitOrOp(true);
	}

	private void intitializeROROptions() {
		rorReplaceWithTrue(true);
		rorReplaceWithFalse(true);
		rorSmartLiteralReplace(false);
	}

	private void initializePRVOOptions() {
		prvoSameLenght(true);
		prvoIncreaseLength(true);
		prvoDecreaseLength(true);
		prvoOneByTwo(true);
		prvoTwoByOne(true);
		prvoAllByOneLeft(false);
		prvoAllByOneRight(false);
		prvoLiteralMutations(true);
		prvoAllocationMutations(true);
		prvoArrayAllocationMutations(true);
		prvoNonNavigationExpressionMutations(true);
		prvoUseSuper(true);
		prvoUseThis(true);
		prvoReplacementWithLiterals(true);
		prvoUseNullLiteral(true);
		prvoUseTrueLiteral(true); 
		prvoUseFalseLiteral(true);
		prvoUseEmptyStringLiteral(true);
		prvoUseZeroLiteral(true);
		prvoUseOneLiteral(true);
		prvoUseStringLiterals(true);
		prvoAllowFinalMembers(false);
		prvoEnableRelaxedTypes(true);
		prvoEnableAutoboxing(true);
		prvoEnableInheritedElements(true);
		prvoAllowStaticFromNonStaticExpression(true);
		prvoSmartMode_arithmeticOpShortcuts(false);
		prvoSmartMode_assignments(false);
	}
	
	private void initializeBEEOptions() {
		beeScanExpressions(false);
		beeSkipEquivalentMutations(false);
		beeSkipConstantsWithTrueAndFalse(false);
	}

	public void classToMutate(String classToMutate) {
		this.classToMutate = classToMutate;
		clearMethodsToMutate();
		clearMethodsInsideClassToMutate();
	}
	
	public String classToMutate() {
		return this.classToMutate;
	}
	
	public long testTimeout() {
		return testTimeout;
	}

	public void testTimeout(long testTimeout) {
		this.testTimeout = testTimeout;
	}
	
	public long discardTimeout() {
		return discardTimeout;
	}
	
	public void discardTimeout(long discardTimeout) {
		this.discardTimeout = discardTimeout;
	}
	
	public boolean runTestsInSeparateProcesses() {
		return runTestsInSeparateProcesses;
	}
	
	public void runTestsInSeparateProcesses(boolean runTestsInSeparateProcesses) {
		this.runTestsInSeparateProcesses = runTestsInSeparateProcesses;
	}

	public String originalSourceDir() {
		return this.originalSrcDir;
	}
	
	public String originalBinDir() {
		return this.originalBinDir;
	}
	
	public String mutantsOutputFolder() {
		return this.mutantsOutputFolder;
	}
	
	public boolean useExternalMutants() {
		return useExternalMutants;
	}

	public void useExternalMutants(boolean b) {
		this.useExternalMutants = b;
	}
	
	public boolean useSockets() {
		return this.useSockets;
	}
	
	public void useSockets(boolean b) {
		this.useSockets = b;
	}
	
	public boolean writePrologue() {
		return this.writePrologue;
	}
	
	public void writePrologue(boolean b) {
		this.writePrologue = b;
	}

	public String junitPath() {
		return this.junitPath;
	}
	
	protected void junitPath(String s) {
		this.junitPath = s;
	}
	
	public String hamcrestPath() {
		return this.hamcrestPath;
	}
	
	protected void hamcrestPath(String s) {
		this.hamcrestPath = s;
	}
	
	public void addMethodToMutate(String method) {
		if (!this.methodsToMutate.contains(method)) this.methodsToMutate.add(method);
	}
	
	public void removeMethodToMutate(String method) {
		if (this.methodsToMutate.contains(method)) this.methodsToMutate.remove(method);
	}
	
	public void clearMethodsToMutate() {
		if (this.methodsToMutate == null) {
			this.methodsToMutate = new TreeSet<String>();
		} else {
			this.methodsToMutate.clear();
		}
	}
	
	public Set<String> methodToMutate() {
		return this.methodsToMutate;
	}
	
	public boolean addOperator(String op) {
		MutationOperator mop = isValidOp(op);
		if (mop != null) {
			this.operators.add(mop);
			return true;
		}
		return false;
	}
	
	public boolean removeOperator(String op) {
		MutationOperator mop = isValidOp(op);
		if (mop != null) {
			this.operators.remove(mop);
			return true;
		} else {
			return false;
		}
	}
	
	private MutationOperator isValidOp(String op) {
		for (MutationOperator mop : this.validMutOps) {
			if (mop.toString().compareTo(op) == 0) {
				return mop;
			}
		}
		return null;
	}
	
	public void clearOperators() {
		if (this.operators == null) {
			this.operators = new TreeSet<MutationOperator>();
		} else {
			this.operators.clear();
		}
	}
	
	public Set<MutationOperator> operators() {
		return this.operators;
	}
	
	public void fullVerboseMode(boolean enable) {
		this.fullVerboseMode = enable;
	}
	
	public boolean fullVerboseMode() {
		return this.fullVerboseMode;
	}
	
	public void allowClassMutations(boolean allow) {
		allowClassMutations = allow;
	}
	
	public boolean allowClassMutations() {
		return allowClassMutations;
	}
	
	public void allowFieldMutations(boolean allow) {
		allowFieldMutations = allow;
	}
	
	public boolean allowFieldMutations() {
		return allowFieldMutations;
	}
	
	public Set<String> allowedMembers() {
		return allowedMembers;
	}
	
	public void addAllowedMember(String m) {
		allowedMembers.add(m);
	}
	
	public void delAllowedMember(String m) {
		allowedMembers.remove(m);
	}
	
	public void clearAllowedMembers() {
		if (allowedMembers == null) {
			allowedMembers = new TreeSet<String>();
		} else {
			allowedMembers.clear();
		}
	}
	
	public void addBannedMethod(String m) {
		bannedMethods.add(m);
	}
	
	public void delBannedMethod(String m) {
		bannedMethods.remove(m);
	}
	
	public void clearBannedMethods() {
		if (bannedMethods == null) {
			bannedMethods = new TreeSet<String>();
		} else {
			bannedMethods.clear();
		}
	}
	
	public Set<String> bannedMethods() {
		return bannedMethods;
	}
	
	public void addBannedField(String f) {
		bannedFields.add(f);
	}
	
	public void delBannedField(String f) {
		bannedFields.remove(f);
	}
	
	public void clearBannedFields() {
		if (this.bannedFields == null) {
			this.bannedFields = new TreeSet<String>();
		} else {
			this.bannedFields.clear();
		}
	}
	
	public Set<String> bannedFields() {
		return this.bannedFields;
	}
	
	public boolean ignoreMutGenLimit() {
		return this.ignoreMutGenLimit;
	}
	
	public void ignoreMutGenLimit(boolean ignore) {
		this.ignoreMutGenLimit = ignore;
	}
	
	public void addPackageToReload(String p) {
		this.allowedPackagesToReload.add(p);
	}
	
	public void delPackageToReload(String p) {
		this.allowedPackagesToReload.remove(p);
	}
	
	public void clearPackagesToReload() {
		if (this.allowedPackagesToReload == null) {
			this.allowedPackagesToReload = new TreeSet<String>();
		} else {
			this.allowedPackagesToReload.clear();
		}
	}
	
	public Set<String> allowedPackagesToReload() {
		return this.allowedPackagesToReload;
	}
	
	public void allowNumericLiteralVariations(boolean allow) {
		allowNumericLiteralVariations = allow;
	}
	
	public boolean allowNumericLiteralVariations() {
		return allowNumericLiteralVariations;
	}
	
	public void runMutationScore(boolean run) {
		runMutationScore = run;
	}
	
	public boolean runMutationScore() {
		return runMutationScore;
	}
	
	public void dynamicSubsumption(boolean ds) {
		dynamicSubsumption = ds;
	}
	
	public boolean dynamicSubsumption() {
		return dynamicSubsumption;
	}
	
	public boolean reduceDynamicSubsumptionGraph() {
		return reduceDynamicSubsumptionGraph;
	}

	public void reduceDynamicSubsumptionGraph(boolean reduceDynamicSubsumptionGraph) {
		this.reduceDynamicSubsumptionGraph = reduceDynamicSubsumptionGraph;
	}

	public void dynamicSubsumptionOutput(String o) {
		dynamicSubsumptionOutput = o;
	}
	
	public String dynamicSubsumptionOutput() {
		return dynamicSubsumptionOutput;
	}
	
	public String testsBinDir() {
		return testsBinDir;
	}
	
	public void testsBinDir(String dir) {
		testsBinDir = addTrailingSeparator(dir);
	}
	
	public void addTestClass(String c) {
		testClasses.add(c);
	}
	
	public void delTestClass(String c) {
		this.testClasses.remove(c);
	}
	
	public void clearTestClasses() {
		if (this.testClasses == null) {
			this.testClasses = new TreeSet<String>();
		} else {
			this.testClasses.clear();
		}
	}
	
	public Set<String> testClasses() {
		return this.testClasses;
	}
	
	public String[] externalClassesToMutate() {
		return this.externalClassesToMutate;
	}
	
	public void externalClassesToMutate(String[] cls) {
		this.externalClassesToMutate = cls;
	}
	
	public void quickDeath(boolean enable) {
		this.quickDeath = enable;
	}
	
	public boolean quickDeath() {
		return this.quickDeath;
	}
	
	public boolean allowPrimitiveToObjectAssignments() {
		return this.disablePrimitiveToObjectAssignments;
	}
	
	public void allowPrimitiveToObjectAssignments(boolean b) {
		this.disablePrimitiveToObjectAssignments = b;
	}
	
	public boolean wrapPrimitiveToObjectAssignments() {
		return this.wrapPrimitiveToObjectAssignments;
	}
	
	public void wrapPrimitiveToObjectAssignments(boolean b) {
		this.wrapPrimitiveToObjectAssignments = b;
	}
	
	public boolean applyRefinedPRVOInMethodCallStatements() {
		return this.applyRefinedPRVOInMethodCallStatements;
	}
	
	public void applyRefinedPRVOInMethodCallStatements(boolean b) {
		this.applyRefinedPRVOInMethodCallStatements = b;
	}
	
	public boolean aoisSkipFinal() {
		return aoisSkipFinal;
	}
	
	public void aoisSkipFinal(boolean v) {
		aoisSkipFinal = v;
	}
	
	public boolean aodsSkipExpressionStatements() {
		return aodsSkipExpressionStatements;
	}
	
	public void aodsSkipExpressionStatements(boolean v) {
		aodsSkipExpressionStatements = v;
	}
	
	public void rorReplaceWithTrue(boolean b) {
		this.rorReplaceWithTrue = b;
	}
	
	public void rorReplaceWithFalse(boolean b) {
		this.rorReplaceWithFalse = b;
	}
	
	public boolean rorSmartLiteralReplace() {
		return rorSmartLiteralReplace;
	}
	
	public void rorSmartLiteralReplace(boolean v) {
		rorSmartLiteralReplace = v;
	}
	
	public void corUseAndOp(boolean b) {
		this.corUseAndOp = b;
	}
	
	public void corUseOrOp(boolean b) {
		this.corUseOrOp = b;
	}
	
	public void corUseXorOp(boolean b) {
		this.corUseXorOp = b;
	}
	
	public void corUseBitAndOp(boolean b) {
		this.corUseBitAndOp = b;
	}
	
	public void corUseBitOrOp(boolean b) {
		this.corUseBitOrOp = b;
	}
	
	public void prvoSameLenght(boolean b) {
		this.prvoSameLenght = b;
	}
	
	public void prvoIncreaseLength(boolean b) {
		this.prvoIncreaseLength = b;
	}
	
	public void prvoDecreaseLength(boolean b) {
		this.prvoDecreaseLength = b;
	}
	
	public void prvoOneByTwo(boolean b) {
		this.prvoOneByTwo = b;
	}
	
	public void prvoTwoByOne(boolean b) {
		prvoTwoByOne = b;
	}
	
	public void prvoAllByOneLeft(boolean b) {
		prvoAllByOneLeft = b;
	}
	
	public void prvoAllByOneRight(boolean b) {
		prvoAllByOneRight = b;
	}
	
	public void prvoLiteralMutations(boolean b) {
		prvoLiteralMutations = b;
	}
	
	public boolean prvoLiteralMutations() {
		return prvoLiteralMutations;
	}
	
	public boolean prvoAllocationMutations() {
		return prvoAllocationMutations;
	}
	
	public void prvoAllocationMutations(boolean b) {
		prvoAllocationMutations = b;
	}
	
	public boolean prvoArrayAllocationMutations() {
		return prvoArrayAllocationMutations;
	}
	
	public void prvoArrayAllocationMutations(boolean b) {
		prvoArrayAllocationMutations = b;
	}
	
	public boolean prvoNonNavigationExpressionMutations() {
		return prvoNonNavigationExpressionMutations;
	}
	
	public void prvoNonNavigationExpressionMutations(boolean b) {
		prvoNonNavigationExpressionMutations = b;
		if (!b) {
			prvoAllocationMutations(false);
			prvoLiteralMutations(false);
			prvoArrayAllocationMutations(false);
		}
	}
	
	public void prvoUseSuper(boolean b) {
		prvoUseSuper = b;
	}
	
	public void prvoUseThis(boolean b) {
		prvoUseThis = b;
	}
	
	public void prvoReplacementWithLiterals(boolean b) {
		prvoReplacementWithLiterals = b;
	}
	
	public void prvoUseNullLiteral(boolean b) {
		prvoUseNullLiteral = b;
	}
	
	public void prvoUseTrueLiteral(boolean b) {
		this.prvoUseTrueLiteral = b;
	}
	
	public void prvoUseFalseLiteral(boolean b) {
		this.prvoUseFalseLiteral = b;
	}
	
	public void prvoUseEmptyStringLiteral(boolean b) {
		this.prvoUseEmptyStringLiteral = b;
	}
	
	public void prvoUseZeroLiteral(boolean b) {
		this.prvoUseZeroLiteral = b;
	}
	
	public void prvoUseOneLiteral(boolean b) {
		this.prvoUseOneLiteral = b;
	}
	
	public void prvoUseStringLiterals(boolean b) {
		this.prvoUseStringLiterals = b;
	}
	
	public boolean prvoAllowFinalMembers() {
		return prvoAllowFinalMembers;
	}

	public void prvoAllowFinalMembers(boolean prvoAllowFinalMembers) {
		this.prvoAllowFinalMembers = prvoAllowFinalMembers;
	}

	public boolean prvoEnableRelaxedTypes() {
		return prvoEnableRelaxedTypes;
	}

	public void prvoEnableRelaxedTypes(boolean prvoEnableRelaxedTypes) {
		this.prvoEnableRelaxedTypes = prvoEnableRelaxedTypes;
	}

	public boolean prvoEnableAutoboxing() {
		return prvoEnableAutoboxing;
	}

	public void prvoEnableAutoboxing(boolean prvoEnableAutoboxing) {
		this.prvoEnableAutoboxing = prvoEnableAutoboxing;
	}

	public boolean prvoEnableInheritedElements() {
		return prvoEnableInheritedElements;
	}

	public void prvoEnableInheritedElements(boolean prvoEnableInheritedElements) {
		this.prvoEnableInheritedElements = prvoEnableInheritedElements;
	}
	
	public boolean prvoAllowStaticFromNonStaticExpression() {
		return prvoAllowStaticFromNonStaticExpression;
	}
	
	public void prvoAllowStaticFromNonStaticExpression(boolean prvoAllowStaticFromNonStaticExpression) {
		this.prvoAllowStaticFromNonStaticExpression = prvoAllowStaticFromNonStaticExpression;
	}
	
	public boolean prvoSmartMode_arithmeticOpShortcuts() {
		return prvoSmartMode_arithmeticOpShortcuts;
	}
	
	public void prvoSmartMode_arithmeticOpShortcuts(boolean v) {
		prvoSmartMode_arithmeticOpShortcuts = v;
	}
	
	public boolean prvoSmartMode_assignments() {
		return prvoSmartMode_assignments;
	}
	
	public void prvoSmartMode_assignments(boolean v) {
		prvoSmartMode_assignments = v;
	}

	public boolean rorReplaceWithTrue() {
		return rorReplaceWithTrue;
	}
	
	public boolean rorReplaceWithFalse() {
		return rorReplaceWithFalse;
	}
	
	public boolean corUseAndOp() {
		return corUseAndOp;
	}
	
	public boolean corUseOrOp() {
		return corUseOrOp;
	}
	
	public boolean corUseXorOp() {
		return corUseXorOp;
	}
	
	public boolean corUseBitAndOp() {
		return corUseBitAndOp;
	}
	
	public boolean corUseBitOrOp() {
		return corUseBitOrOp;
	}
	
	public boolean prvoSameLenght() {
		return prvoSameLenght;
	}
	
	public boolean prvoIncreaseLength() {
		return prvoIncreaseLength;
	}
	
	public boolean prvoDecreaseLength() {
		return prvoDecreaseLength;
	}
	
	public boolean prvoOneByTwo() {
		return prvoOneByTwo;
	}
	
	public boolean prvoTwoByOne() {
		return prvoTwoByOne;
	}
	
	public boolean prvoAllByOneLeft() {
		return prvoAllByOneLeft;
	}
	
	public boolean prvoAllByOneRight() {
		return prvoAllByOneRight;
	}
	
	public boolean prvoUseSuper() {
		return prvoUseSuper;
	}
	
	public boolean prvoUseThis() {
		return prvoUseThis;
	}
	
	public boolean prvoReplacementWithLiterals() {
		return prvoReplacementWithLiterals;
	}
	
	public boolean prvoUseNullLiteral() {
		return prvoUseNullLiteral;
	}
	
	public boolean prvoUseTrueLiteral() {
		return prvoUseTrueLiteral;
	}
	
	public boolean prvoUseFalseLiteral() {
		return prvoUseFalseLiteral;
	}
	
	public boolean prvoUseEmptyStringLiteral() {
		return prvoUseEmptyStringLiteral;
	}
	
	public boolean prvoUseZeroLiteral() {
		return prvoUseZeroLiteral;
	}
	
	public boolean prvoUseOneLiteral() {
		return prvoUseOneLiteral;
	}
	
	public boolean prvoUseStringLiterals() {
		return prvoUseStringLiterals;
	}
	
	public boolean beeScanExpressions() {
		return beeScanExpressions;
	}
	
	public void beeScanExpressions(boolean b) {
		this.beeScanExpressions = b;
	}
	
	public boolean beeSkipEquivalentMutations() {
		return beeSkipEquivalentMutations;
	}
	
	public void beeSkipEquivalentMutations(boolean b) {
		this.beeSkipEquivalentMutations = b;
	}
	
	public boolean beeSkipConstantsWithTrueAndFalse() {
		return beeSkipConstantsWithTrueAndFalse;
	}
	
	public void beeSkipConstantsWithTrueAndFalse(boolean b) {
		this.beeSkipConstantsWithTrueAndFalse = b;
	}
	
	public int generation() {
		return this.generation;
	}
	
	public void generation(int i) {
		this.generation = i;
	}
	
	public int reloaderInstancesLimit() {
		return this.reloaderInstancesLimit;
	}
	
	public void reloaderInstancesLimit(int i) {
		this.reloaderInstancesLimit = i;
	}
	
	public boolean showSurvivingMutants() {
		return this.showSurvivingMutants;
	}
	
	public void showSurvivingMutants(boolean b) {
		this.showSurvivingMutants = b;
	}
	
	public boolean toughnessAnalysis() {
		return this.toughnessAnalysis;
	}
	
	public void toughnessAnalysis(boolean b) {
		if (b) quickDeath(false);
		this.toughnessAnalysis = b;
	}
	
	/**
	 * @return the useSimpleClassNames
	 */
	public boolean useSimpleClassNames() {
		return useSimpleClassNames;
	}

	/**
	 * @param useSimpleClassNames the useSimpleClassNames to set
	 */
	public void useSimpleClassNames(boolean useSimpleClassNames) {
		this.useSimpleClassNames = useSimpleClassNames;
	}
	
	public  boolean useExternalJUnitRunner() {
		return this.useExternalJUnitRunner;
	}
	
	public void useExternalJUnitRunner(boolean b) {
		this.useExternalJUnitRunner = b;
	}
	
	public boolean useParallelExternalJUnitRunner() {
		return this.useParallelExternalJUnitRunner;
	}
	
	public void useParallelExternalJUnitRunner(boolean b) {
		this.useParallelExternalJUnitRunner = b;
	}
	
	public int parallelExternalJUnitRunnerThreads() {
		return this.parallelExternalJUnitRunnerThreads;
	}
	
	public void parallelExternalJUnitRunnerThreads(int v) {
		this.parallelExternalJUnitRunnerThreads = v;
	}

	/**
	 * @return the outputMutationsInfo
	 */
	public boolean outputMutationsInfo() {
		return outputMutationsInfo;
	}

	/**
	 * @param outputMutationsInfo the outputMutationsInfo to set
	 */
	public void outputMutationsInfo(boolean outputMutationsInfo) {
		this.outputMutationsInfo = outputMutationsInfo;
	}
	
	public Set<Method> getClassMethods() {
		if (this.methodsInClassToMutate != null && !this.methodsInClassToMutate.isEmpty()) {
			return this.methodsInClassToMutate;
		}
		Set<Method> classMethods = new TreeSet<>(methodComparator);//new TreeSet<Method>();
		if (verifyDirectory(this.originalBinDir) && verifyFile(sumPaths(this.originalBinDir, classNameAsPath(this.classToMutate))+".class")) {
			Class<?> classToMutate = loadClass(this.classToMutate, this.originalBinDir);
			if (classToMutate != null) {
				for (Method m : classToMutate.getDeclaredMethods()) {
					String methodName = m.getName();
					if (methodName.startsWith("access$")) continue;
					classMethods.add(m);
				}
			}
		}
		this.methodsInClassToMutate.addAll(classMethods);
		return classMethods;
	}
	
	private static class MethodComparator implements Comparator<Method> {
		@Override
		public int compare(Method o1, Method o2) {
			if (o1 == null || o2 == null) throw new IllegalArgumentException("null argument");
			return o1.getName().compareTo(o2.getName());
		}
	}
	
	private MethodComparator methodComparator = new MethodComparator();
	
	public List<String> getClassesInOriginalBinDir() {
		if (this.classesInOriginalBinDir != null && !this.classesInOriginalBinDir.isEmpty()) return this.classesInOriginalBinDir;
		List<String> classesInOriginalBinDir = new LinkedList<String>();
		try {
			DependencyScanner depScanner = new DependencyScanner(toPath(addTrailingSeparator(this.originalBinDir)));
			classesInOriginalBinDir.addAll(depScanner.getDependencyMap().getClasses());
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.classesInOriginalBinDir = classesInOriginalBinDir;
		return classesInOriginalBinDir;
	}
	
	public List<String> getTestClassesInTestsBinDir() {
		if (this.testClassesInTestsBinDir != null && !this.testClassesInTestsBinDir.isEmpty()) return this.testClassesInTestsBinDir;
		List<String> testClassesInTestsBinDir = new LinkedList<String>();
		try {
			if (this.testsScanner == null || this.testsScanner.getScannedPath().compareTo(this.originalBinDir) != 0) {
				this.testsScanner = new DependencyScanner(toPath(addTrailingSeparator(this.testsBinDir)));
			}
			for (String c : this.testsScanner.getDependencyMap().getClasses()) {
				if (isTestClass(loadClass(c, this.testsBinDir))) {
					testClassesInTestsBinDir.add(c);
				}
			}
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.testClassesInTestsBinDir = testClassesInTestsBinDir;
		return testClassesInTestsBinDir;
	}
	
	private boolean existsAsTestClass(String c) {
		try {
			if (this.testsScanner == null || this.testsScanner.getScannedPath().compareTo(this.originalBinDir) != 0) {
				this.testsScanner = new DependencyScanner(toPath(addTrailingSeparator(this.testsBinDir)));
			}
			for (String tc : this.testsScanner.getDependencyMap().getClasses()) {
				if (tc.compareTo(c) == 0) {
					return isTestClass(loadClass(c, this.testsBinDir));
				}
			}
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public List<String> getpackagesInOriginalBinDir() {
		if (this.packagesInOriginalBinDir != null && !this.packagesInOriginalBinDir.isEmpty()) return this.packagesInOriginalBinDir;
		List<String> packagesInOriginalBinDir = new LinkedList<String>();
		try {
			DependencyScanner depScanner = new DependencyScanner(toPath(addTrailingSeparator(this.originalBinDir)));
			packagesInOriginalBinDir.addAll(depScanner.getScannedPackages());
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.packagesInOriginalBinDir = packagesInOriginalBinDir;
		return packagesInOriginalBinDir;
	}
	
	/**
	 * Validates current configuration
	 * @return an error message if the configuration is invalid or {@code null} otherwise : {@code String}
	 */
	public String validate() {
		boolean prototypeMode = externalClassesToMutate != null && externalClassesToMutate.length > 0;
		//verify folders
		if (!useExternalMutants() && !verifyDirectory(addTrailingSeparator(originalSrcDir))) return "Invalid directory (Original source folder) : " + originalSrcDir;
		if (!verifyDirectory(addTrailingSeparator(originalBinDir))) return "Invalid directory (Original binary folder) : " + originalBinDir;
		if (generation <= 0) return "Invalid generation (" + generation + ") , valid values are > 0"; 
		if (testsBinDir != null && !verifyDirectory(addTrailingSeparator(testsBinDir))) return "Invalid directory (Tests binary folder) : " + testsBinDir;
		if (testsBinDir == null && runMutationScore) return "Mutation score is enabled but no tests binary folder has been selected";
		
		if (!prototypeMode) if (!getClassesInOriginalBinDir().contains(this.classToMutate)) return "Class " + classToMutate + " can't be found inside " + originalBinDir;
		boolean usingTimeout = testTimeout() > 0;
		boolean usingDiscard = discardTimeout() > 0;
		if (runMutationScore && runTestsInSeparateProcesses && !useExternalJUnitRunner) return "Can't run tests in separate processes if not using external JUnit Runner";
		if (runMutationScore && testTimeout() < 0) return "Timeout can't be a negative value";
		if (runMutationScore && discardTimeout() < 0) return "Discard timeout can't be a negative value";
		if (runMutationScore && usingDiscard && discardTimeout() <= testTimeout()) return "Discard timeout must be greater than test timeout";
		if (runMutationScore && discardTimeout() > 0 && !useExternalJUnitRunner) return "Cannot use discard timeout without using the External JUnit Runner";
		if (runMutationScore && (testClasses == null || testClasses.isEmpty())) return "Mutation score is enabled but no test classes has been selected";
		if (runMutationScore && !prototypeMode) {
			for (String t : testClasses) {
				if (!existsAsTestClass(t)) {
					return "Class " + t + " can't be found inside " + testsBinDir + " or is not a valid test class";
				}
//				if (!isTestClass(loadClass(t, addTrailingSeparator(this.testsBinDir)))) {
//					return "Class " + t + " is not a valid test class";
//				}
			}
		}
		for (MutationOperator m : operators) {
			if (!MutatorsInfo.getInstance().isSupported(m)) return "Operator " + m.toString() + " is not supported";
		}
		if (!prototypeMode) {
			for (String method : methodsToMutate) {
				boolean found = false;
				for (Method m : methodsInClassToMutate) {
					if (m.getName().compareTo(method) == 0) {
						found = true;
					}
					if (!found) return "Method " + method + " doesn't belong to class " + classToMutate;
				}
			}
		}
		for (String apr : allowedPackagesToReload) {
			if (!packagesInOriginalBinDir.contains(apr)) return "Package " + apr + " is not present in " + originalBinDir;
		}
		if (quickDeath && !runMutationScore) return "Quick death option is enabled but mutation score is not";
		if (dynamicSubsumption && !runMutationScore) return "Dynamic Subsumption option is enabled but mutation score is not";
		if (!runMutationScore && showSurvivingMutants) return "Show surviving mutants is enabled but mutation score is not";
		if (!runMutationScore && toughnessAnalysis) return "Toughness analysis is enabled but mutation score is not";
		if (useExternalMutants && !useExternalJUnitRunner && !useParallelExternalJUnitRunner) {
			return "Can't use internal junit runner when using external mutants";
		}
		if (useExternalJUnitRunner || useParallelExternalJUnitRunner) {
			if (junitPath == null || junitPath.isEmpty()) return "Can't use external junit runner without defining the JUnit jar path";
			if (!verifyFile(this.junitPath)) return "The defined JUnit path doesn't not point to an existing file";
			if (!junitPath.endsWith(".jar")) return "The defined JUnit path doesn't point to a jar file";
			if (hamcrestPath == null || junitPath.isEmpty()) return "Can't use external junit runner without defining the hamcrest jar path";
			if (!verifyFile(this.hamcrestPath)) return "The defined hamcrest path doesn't not point to an existing file";
			if (!hamcrestPath.endsWith(".jar")) return "The defined hamcrest path doesn't point to a jar file";
			if (useParallelExternalJUnitRunner && parallelExternalJUnitRunnerThreads <= 0) {
				return "Using parallel external JUnit runner with a non-positive amount of threads";
			}
		} else if (prototypeMode) {
			return "Can't use protoype mode with internal runner";
		}
		return null;
	}
	
	private Path toPath(String p) {
		File path = new File(p);
		return path.toPath();
	}
	
	private String classNameAsPath(String cname) {
		return cname.replaceAll("\\.", Core.SEPARATOR);
	}
	
	private String addTrailingSeparator(String path) {
		if (path.endsWith(Core.SEPARATOR)) {
			return path;
		} else {
			return path + Core.SEPARATOR;
		}
	}
	
	private String sumPaths(String p1, String p2) {
		return addTrailingSeparator(p1) + p2;
	}
	
	private boolean verifyDirectory(String dir) {
		File directory = new File(dir);
		return directory.exists() && directory.isDirectory();
	}
	
	private boolean verifyFile(String f) {
		File file = new File(f);
		return file.exists() && file.isFile();
	}
	
	private Class<?> loadClass(String className, String fromPath) {
		File file = new File(fromPath);
		Class<?> clazz = null;
		String classToLoad = className;
		try {
		    URL url = file.toURI().toURL();
		    URL[] urls = new URL[]{url};
		    
		    URLClassLoader cl = new URLClassLoader(urls);

		    clazz = cl.loadClass(classToLoad);
		    cl.close();
		    
		} catch (MalformedURLException e) {
			
		} catch (ClassNotFoundException e) {
			
		} catch (IOException e) {
			
		}
		return clazz;
	}
	
	private void clearMethodsInsideClassToMutate() {
		if (this.methodsInClassToMutate == null) {
			this.methodsInClassToMutate = new TreeSet<Method>(methodComparator);
		} else {
			this.methodsInClassToMutate.clear();
		}
	}
	
	private void clearClassesInOriginalBinDir() {
		if (this.classesInOriginalBinDir == null) {
			this.classesInOriginalBinDir = new LinkedList<String>();
		} else {
			this.classesInOriginalBinDir.clear();
		}
	}
	
	private void clearPackagesInOriginalBinDir() {
		this.packagesInOriginalBinDir = null;
		this.packagesInOriginalBinDir = getpackagesInOriginalBinDir();
	}
	
	private void clearTestClassesInTestsBinDir() {
		if (this.testClassesInTestsBinDir == null) {
			this.testClassesInTestsBinDir = new LinkedList<String>();
		} else {
			this.testClassesInTestsBinDir.clear();
		}
	}
	
	private boolean isTestClass(Class<?> c) {
		if (c == null) return false;
		if (!this.useOldJUnit) {
			RunWith runWithAnnotation = c.getAnnotation(RunWith.class);
			if (runWithAnnotation != null) {
				return true;
			}
		}
		if (TestCase.class.isAssignableFrom(c)) {
			return true;
		} else if (TestSuite.class.isAssignableFrom(c)) {
			return true;
		} else if (c.getAnnotation(Test.class) != null) {
			return true;
		} else {
			return hasTestMethod(c);
		}
	}
	
	private boolean hasTestMethod(Class<?> testToRun) {
		Method[] methods = testToRun.getDeclaredMethods();
		for (Method m : methods) {
			if (Modifier.isPublic(m.getModifiers())) {
				if (m.getAnnotation(Test.class) != null) {
					return true;
				}
			}
		}
		return false;
	}

	
}
