package mujava.op;

import java.io.PrintWriter;

import openjava.ptree.ClassDeclaration;
import openjava.ptree.FieldDeclaration;
import openjava.ptree.MemberDeclaration;
import openjava.ptree.ParseTreeException;
import mujava.api.Mutation;
import mujava.op.util.MutantCodeWriter;
import mujava.op.util.Mutator;

public class IHI_Writer extends MutantCodeWriter{
	private ClassDeclaration original;
	private FieldDeclaration mutant;
	int declarations;

	public IHI_Writer(String mutant_dir, PrintWriter out, Mutation mi) {
		super(mutant_dir, out, mi);
		setMutant(this.mi.getOriginal(), this.mi.getMutant());
	}
	
	private void setMutant(Object original, Object mutant) {
		this.original = (ClassDeclaration) original;
		this.mutant = (FieldDeclaration) mutant;
		declarations = this.original.getBody().size();
		this.original.getBody().insertElementAt(this.mutant, 0);
		declarations++;
	}
	
	@Override
	public void visit(FieldDeclaration fd) throws ParseTreeException {
		if (this.mutant != null & compare(fd, this.mutant)) {
			super.visit(mutant);
	        // -----------------------------------------------------------
	        mutated_line = line_num;
	        String log_str = "added  => " + mutant.toFlattenString();
	        writeLog(removeNewline(log_str));
	        // -----------------------------------------------------------
		} else {
			super.visit(fd);
		}
	}
	
	public void restore() {
		for (int d = 0; d < declarations; d++) {
			MemberDeclaration md = this.original.getBody().get(d);
			if (md instanceof FieldDeclaration) {
				if (compare(this.mutant, (FieldDeclaration)md)) {
					this.original.getBody().remove(d);
					break;
				}
			}
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
