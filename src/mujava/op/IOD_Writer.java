package mujava.op;

import java.io.PrintWriter;

import openjava.ptree.MethodDeclaration;
import openjava.ptree.Parameter;
import openjava.ptree.ParameterList;
import openjava.ptree.ParseTreeException;
import openjava.ptree.TypeName;

import mujava.api.Mutation;
import mujava.op.util.MutantCodeWriter;

public class IOD_Writer extends MutantCodeWriter {
	private MethodDeclaration mutant;

	public IOD_Writer(String mutant_dir, PrintWriter out, Mutation mi) {
		super(mutant_dir, out, mi);
		setMutant(this.mi.getMutant());
	}
	
	private void setMutant(Object mutant) {
		this.mutant = (MethodDeclaration) mutant;
	}
	
	@Override
	public void visit(MethodDeclaration fd) throws ParseTreeException {
		if (compare(fd, this.mutant)) {
	        // -----------------------------------------------------------
	        mutated_line = line_num;
	        String log_str = "removed  => " + mutant.toFlattenString();
	        writeLog(removeNewline(log_str));
	        // -----------------------------------------------------------
		} else {
			super.visit(fd);
		}
	}
	
	private boolean compare(MethodDeclaration md1, MethodDeclaration md2) {
		String f1Name = md1.getName();
		String f2Name = md2.getName();
		String f1RetType = md1.getReturnType().getName();
		String f2RetType = md2.getReturnType().getName();
		if (f1Name.equals(f2Name) && f1RetType.equals(f2RetType)) {
			TypeName[] fd1Exc = md1.getThrows();
			TypeName[] fd2Exc = md2.getThrows();
			if (fd1Exc.length == fd2Exc.length) {
				for (int e = 0; e < fd1Exc.length; e++) {
					if (fd1Exc[e].getName().compareTo(fd2Exc[e].getName())==0)
						return false;
				}
			}
			ParameterList fd1Params = md1.getParameters();
			ParameterList fd2Params = md2.getParameters();
			if (fd1Params.size() == fd2Params.size()) {
				for (int p = 0; p < fd1Params.size(); p++) {
					Parameter fd1Param = fd1Params.get(p);
					Parameter fd2Param = fd2Params.get(p);
					if (fd1Param.getModifiers().getRegular() != fd2Param.getModifiers().getRegular()) {
						return false;
					}
					if (fd1Param.getTypeSpecifier().getName().compareTo(fd2Param.getTypeSpecifier().getName())!=0) {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

}
