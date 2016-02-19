package mujava2.api.program;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Random;

import mujava.OpenJavaException;
import mujava.api.Api;
import mujava.api.Mutation;
import mujava.api.MutationOperator;
import mujava.security.ApiCaller;
import mujava.util.JustCodeDigest;
import mujava2.parser.MJParser;
import openjava.ptree.CompilationUnit;
import openjava.ptree.ParseTreeException;

public class JavaAST extends ApiCaller {
	
	private String rootFolder;
	private File javaFile;
	private String className, simpleClassName, pkg;
	private CompilationUnit comp_unit;
	private boolean mutated = false;
	
	private void initialize(String rootFolder, File javaFile, String className, CompilationUnit comp_unit) {
		this.rootFolder = addTrailingSeparator(rootFolder);
		this.javaFile = javaFile;
		this.className = className;
		int lastDot = this.className.lastIndexOf('.');
		if (lastDot > 0) {
			this.simpleClassName = this.className.substring(lastDot + 1);
			this.pkg = this.className.substring(0, lastDot);
		} else {
			this.simpleClassName = this.className;
			this.pkg = "";
		}
		this.comp_unit = comp_unit;
	}
	
	private JavaAST(String rootFolder, String fullyQualifiedClassName) throws OpenJavaException {
		Api.cleanOJSystem(this);
		File sourceFile = new File(mergePaths(rootFolder, dotsAsFileSep(fullyQualifiedClassName)+".java"));
		MJParser parser = new MJParser(sourceFile);
		CompilationUnit comp_unit = parser.generateAST();
		initialize(rootFolder, sourceFile, fullyQualifiedClassName, comp_unit);
	}
	
	private JavaAST(String rootFolder, File sourceFile) throws OpenJavaException, ParseTreeException {
		Api.cleanOJSystem(this);
		MJParser parser = new MJParser(sourceFile);
		CompilationUnit comp_unit = parser.generateAST();
		String className = comp_unit.getPublicClass().getName();
		initialize(rootFolder, sourceFile, className, comp_unit);
	}
	
	private JavaAST(File javaFile) throws OpenJavaException, ParseTreeException {
		Api.cleanOJSystem(this);
		File sourceFile = javaFile;
		MJParser parser = new MJParser(sourceFile);
		CompilationUnit comp_unit = parser.generateAST();
		String className = comp_unit.getPublicClass().getName();
		int classNameIndex = sourceFile.getAbsolutePath().indexOf(dotsAsFileSep(className));
		String sourceFolder = "";
		if (classNameIndex > 0) {
			sourceFolder = sourceFile.getAbsolutePath().substring(0, classNameIndex);
		}
		initialize(sourceFolder, sourceFile, className, comp_unit);
	}
	
	public static JavaAST fromFile(String rootFolder, String fullyQualifiedClassName) throws OpenJavaException {
		return new JavaAST(rootFolder, fullyQualifiedClassName);
	}
	
	public static JavaAST fromFile(String rootFolder, File javaFile) throws OpenJavaException, ParseTreeException {
		return new JavaAST(rootFolder, javaFile);
	}
	
	public static JavaAST fromFile(File javaFile) throws OpenJavaException, ParseTreeException {
		return new JavaAST(javaFile);
	}
	
	public byte[] writeToFile(File file) throws IOException, ParseTreeException {
		file.getParentFile().mkdirs();
		file.createNewFile();
		OutputStream os = new FileOutputStream(file, true);
		PrintWriter pw = new PrintWriter(os);
		Mutation dummy = new Mutation(MutationOperator.MULTI, null, null);
		Api.writeMutant(this.comp_unit, dummy, pw);
		return JustCodeDigest.digest(file);
	}
	
	public byte[] writeInFolder(File rootFolder) throws IOException, ParseTreeException {
		return writeInFolder(rootFolder, false);
	}
	
	public byte[] writeInFolder(File rootFolder, boolean useRandomFolder) throws IOException, ParseTreeException {
		String filePath = "";
		String pkgAsPath = dotsAsFileSep(this.pkg);
		if (useRandomFolder) filePath = mergePaths(randomString(10),pkgAsPath);
		else filePath = pkgAsPath;
		filePath = mergePaths(filePath, this.simpleClassName + ".java");
		File outputFile = new File(rootFolder, filePath);
		return writeToFile(outputFile);
	}
	
	public boolean isMutated() {
		return this.mutated;
	}
	
	void flagAsMutated() {
		this.mutated = true;
	}
	
	public String getClassName() { 
		return className;
	}

	public String getSimpleClassName() {
		return simpleClassName;
	}

	public String getPkg() {
		return pkg;
	}

	public String getRootFolder() {
		return rootFolder;
	}

	public File getJavaFile() {
		return javaFile;
	}

	public CompilationUnit getCompUnit() {
		return comp_unit;
	}
	
	//AUXILIARY FUNCTIONS

	private String mergePaths(String p1, String p2) {
		if (p1.endsWith(File.separator)) {
			if (p2.startsWith(File.separator)) {
				return p1 + p2.substring(1);
			} else {
				return p1 + p2;
			}
		} else {
			if (p2.startsWith(File.separator)) {
				return p1 + p2;
			} else {
				return p1 + File.separator + p2;
			}
		}
	}
	
	private String addTrailingSeparator(String path) {
		return mergePaths(path, "");
	}
	
	private String dotsAsFileSep(String pathWithDots) {
		return pathWithDots.replaceAll("\\.", File.separator);
	}
	
	/**
	 * Construct a random String using upper case letters and digits
	 * 
	 * @param len	:	the lenght of the resulting string	:	{@code int}
	 * @return a random String of size {@code len} using upper case letters and digits
	 */
	private String randomString(int len) {
		String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rnd = new Random();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}

}
