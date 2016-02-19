package mujava2.parser;

import java.io.File;

import mujava.MutantsGenerator;
import mujava.OpenJavaException;
import openjava.ptree.CompilationUnit;

public class MJParser extends MutantsGenerator {

	public MJParser(File f) {
		super(f);
	}
	
	public CompilationUnit generateAST() throws OpenJavaException {
		makeMutants();
		return comp_unit;
	}

	@Override
	protected void genMutants() {}

}