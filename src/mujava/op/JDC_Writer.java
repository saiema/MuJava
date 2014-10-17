package mujava.op;

import java.io.PrintWriter;

import openjava.ptree.ConstructorDeclaration;
import openjava.ptree.ParseTreeException;
import mujava.api.Mutation;
import mujava.op.util.MutantCodeWriter;

public class JDC_Writer extends MutantCodeWriter {
	private ConstructorDeclaration mutant;

	public JDC_Writer(String mutant_dir, PrintWriter out, Mutation mi) {
		super(mutant_dir, out, mi);
		setMutant(this.mi.getMutant());
	}
	
	private void setMutant(Object mutant) {
		this.mutant = (ConstructorDeclaration) mutant;
	}
	
	@Override
	public void visit(ConstructorDeclaration fd) throws ParseTreeException {
		if (this.mutant != null && compare(fd, this.mutant)) {
			this.mutant = null;
	        // -----------------------------------------------------------
	        mutated_line = line_num;
	        // -----------------------------------------------------------
		} else {
			super.visit(fd);
		}
	}
	
	private boolean compare(ConstructorDeclaration cd1, ConstructorDeclaration cd2) {
		String c1Name = cd1.getName();
		String c2Name = cd2.getName();
		int cd1Params = cd1.getParameters().size();
		int cd2Params = cd2.getParameters().size();
		if (compareWithoutPackage(c1Name,c2Name) && cd1Params == 0 && cd2Params == 0) {
			return true;
		}
		return false;
	}
	
	private boolean compareWithoutPackage(String name1, String name2) {
		int lastDot1 = name1.lastIndexOf(".") + 1;
		int lastDot2 = name2.lastIndexOf(".") + 1;
		String name1NoPackage = name1.substring(lastDot1);
		String name2NoPackage = name2.substring(lastDot2);
		return name1NoPackage.equals(name2NoPackage);
	}
}
