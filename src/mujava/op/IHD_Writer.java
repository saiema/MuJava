package mujava.op;

import java.io.PrintWriter;

import openjava.ptree.FieldDeclaration;
import openjava.ptree.ParseTreeException;

import mujava.api.Mutation;
import mujava.op.util.MutantCodeWriter;
import mujava.op.util.Mutator;

public class IHD_Writer extends MutantCodeWriter {
	private FieldDeclaration mutant;

	public IHD_Writer(String mutant_dir, PrintWriter out, Mutation mi) {
		super(mutant_dir, out, mi);
		setMutant(this.mi.getMutant());
	}
	
	private void setMutant(Object mutant) {
		this.mutant = (FieldDeclaration) mutant;
	}
	
	@Override
	public void visit(FieldDeclaration fd) throws ParseTreeException {
		if (this.mutant != null && compare(fd, this.mutant)) {
			String mutantString = this.mutant.toFlattenString();
			this.mutant = null;
	        // -----------------------------------------------------------
	        mutated_line = line_num;
	        String log_str = "removed  => " + mutantString;
	        writeLog(removeNewline(log_str));
	        // -----------------------------------------------------------
		} else {
			super.visit(fd);
		}
	}
	
	private boolean compare(FieldDeclaration fd1, FieldDeclaration fd2) {
		String f1Name = fd1.getName();
		String f2Name = fd2.getName();
		String f1Type = fd1.getTypeSpecifier().getName();
		String f2Type = fd2.getTypeSpecifier().getName();
		if (f1Name.equals(f2Name) && Mutator.compareNamesWithoutGenerics(f1Type, f2Type, false)/*f1Type.equals(f2Type)*/) {
			return true;
		}
		return false;
	}

}
