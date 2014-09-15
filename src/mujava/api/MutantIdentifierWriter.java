package mujava.api;

import java.io.PrintWriter;

import mujava.op.util.MutantCodeWriter;
import mujava.op.AMC_Writer;
import mujava.op.EMM_Writer;
import mujava.op.EOA_Writer;
import mujava.op.EOC_Writer;
import mujava.op.IHD_Writer;
import mujava.op.IHI_Writer;
import mujava.op.IOD_Writer;
import mujava.op.IOP_Writer;
import mujava.op.IPC_Writer;
import mujava.op.ISD_Writer;
import mujava.op.ISI_Writer;
import mujava.op.JDC_Writer;
import mujava.op.JID_Writer;
import mujava.op.JSD_Writer;
import mujava.op.JTI_Writer;
import mujava.op.OMR_Writer;
import mujava.op.PCC_Writer;
import mujava.op.PCD_Writer;
import mujava.op.PCI_Writer;
import mujava.op.PMD_Writer;
import mujava.op.PNC_Writer;
import mujava.op.PRVO_Writer;
import mujava.op.basic.AODS_Writer;
import mujava.op.basic.AODU_Writer;
import mujava.op.basic.AOIS_Writer;
import mujava.op.basic.AOIU_Writer;
import mujava.op.basic.AORB_Writer;
import mujava.op.basic.AORS_Writer;
import mujava.op.basic.AORU_Writer;
import mujava.op.basic.ASRS_Writer;
import mujava.op.basic.COD_Writer;
import mujava.op.basic.COI_Writer;
import mujava.op.basic.COR_Writer;
import mujava.op.basic.LOD_Writer;
import mujava.op.basic.LOI_Writer;
import mujava.op.basic.LOR_Writer;
import mujava.op.basic.ROR_Writer;
import mujava.op.basic.SOR_Writer;
import openjava.ptree.CompilationUnit;
import openjava.ptree.ParseTreeException;
import openjava.ptree.ParseTreeObject;
import openjava.ptree.NonLeaf;

/**
 * Used for converting mutant identifiers into java files
 */
public class MutantIdentifierWriter {

	private CompilationUnit source;
	private PrintWriter output;

	public MutantIdentifierWriter(CompilationUnit source, PrintWriter output) {
		this.source = source;
		this.output = output;
	}

	public int write(Mutation mutant) throws ParseTreeException {
		MutantCodeWriter writer_generic;
		int ret = -1;
		boolean ihiOp = false;
		if (mutant.getMutOp().equals(Mutant.AODS)) {
			
			writer_generic = new AODS_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.AODU)) {
			
			writer_generic = new AODU_Writer(null, output, mutant);

		} else if (mutant.getMutOp().equals(Mutant.AOIS)) {
			
			writer_generic = new AOIS_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.AOIU)) {
			
			writer_generic = new AOIU_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.AORB)) {
			
			writer_generic = new AORB_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.AORS)) {
			
			writer_generic = new AORS_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.AORU)) {
			
			writer_generic = new AORU_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.ROR)) {
					
			writer_generic = new ROR_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.LOD)){
			
			writer_generic = new LOD_Writer(null, output, mutant);

		} else if (mutant.getMutOp().equals(Mutant.LOI)){

			writer_generic = new LOI_Writer(null, output, mutant);	

		} else if (mutant.getMutOp().equals(Mutant.LOR)){
			
			writer_generic = new LOR_Writer(null, output, mutant);

		} else if (mutant.getMutOp().equals(Mutant.SOR)){
				
			writer_generic = new SOR_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.PRVOR) || mutant.getMutOp().equals(Mutant.PRVOR_SMART) || mutant.getMutOp().equals(Mutant.PRVOR_REFINED)){
			
			writer_generic = new PRVO_Writer(null, output, mutant);

		} else if (mutant.getMutOp().equals(Mutant.PRVOL) || mutant.getMutOp().equals(Mutant.PRVOL_SMART)) {
			
			writer_generic = new PRVO_Writer(null, output, mutant);

		} else if (mutant.getMutOp().equals(Mutant.PRVOU) || mutant.getMutOp().equals(Mutant.PRVOU_SMART) || mutant.getMutOp().equals(Mutant.PRVOU_REFINED)) {
			
			writer_generic = new PRVO_Writer(null, output, mutant);

		} else if (mutant.getMutOp().equals(Mutant.ASRS)) {
			
			writer_generic = new ASRS_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.COD)) {
			
			writer_generic = new COD_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.COI)) {
			
			writer_generic = new COI_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.COR)) {
			
			writer_generic = new COR_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.AMC)) {
			
			writer_generic = new AMC_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.IHI)) {
			
			writer_generic = new IHI_Writer(null, output, mutant);
			ihiOp = true;
			
		} else if(mutant.getMutOp().equals(Mutant.EMM)) { 
			
			writer_generic = new EMM_Writer(null, output, mutant);
			
		} else if(mutant.getMutOp().equals(Mutant.EAM)) { 
			
			writer_generic = new EMM_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.EOC)) {
			
			writer_generic = new EOC_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.IHD)) {
			
			writer_generic = new IHD_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.JDC)) {
			
			writer_generic = new JDC_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.EOA) || mutant.getMutOp().equals(Mutant.EOA_DUMB) || mutant.getMutOp().equals(Mutant.EOA_STRICT)) {
			
			writer_generic = new EOA_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.IPC)) {
			
			writer_generic = new IPC_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.ISI) || mutant.getMutOp().equals(Mutant.ISI_SMART) || mutant.getMutOp().equals(Mutant.OAN) || mutant.getMutOp().equals(Mutant.OAN_RELAXED)) {
			
			writer_generic = new ISI_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.ISD) || mutant.getMutOp().equals(Mutant.ISD_SMART) || mutant.getMutOp().equals(Mutant.JTD)) {
			
			writer_generic = new ISD_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.IOD)) {
			
			writer_generic = new IOD_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.IOP)) {
			
			writer_generic = new IOP_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.JID)) {
			
			writer_generic = new JID_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.JSD) || mutant.getMutOp().equals(Mutant.JSI)) {
			
			writer_generic = new JSD_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.JTI) || mutant.getMutOp().equals(Mutant.JTI_SMART)) {
			
			writer_generic = new JTI_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.PCC)) {
			
			writer_generic = new PCC_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.PCD)) {
			
			writer_generic = new PCD_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.PCI)) {
			
			writer_generic = new PCI_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.PMD) || mutant.getMutOp().equals(Mutant.PPD)) {
			
			writer_generic = new PMD_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.OMR)) {
			
			writer_generic = new OMR_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.PNC)) {
			
			writer_generic = new PNC_Writer(null, output, mutant);
			
		} else if (mutant.getMutOp().equals(Mutant.MULTI)) {
			
			writer_generic = new MutantCodeWriter(null, output, null);
			
		} else {
			throw new IllegalStateException("The mutant " + mutant.getMutOp() + " isn't writable");
		}
		if (mutant.getMutOp().equals(Mutant.MULTI)) {
			writeAST(source, writer_generic);
		} else {
			ret = writeMutant(source, writer_generic, mutant);
			if (ihiOp) {
				((IHI_Writer)writer_generic).restore();
			}
		}
		output.flush();  
		output.close();
		return ret;
	}
	
	private int writeMutant(CompilationUnit source, MutantCodeWriter writer, Mutation mutant) throws ParseTreeException {
		int ret = -1;
		ParseTreeObject parent = null;
		parent = mujava.op.util.Mutator.getMutationsLimitParent(((ParseTreeObject) mutant.getOriginal()));
		int mutationsLeft = mujava.op.util.Mutator.getMutationsLeft(((ParseTreeObject) mutant.getOriginal()));
		if (parent != null && mutationsLeft > 0) {
			((NonLeaf) parent).setMutGenLimit(mutationsLeft - 1);
			source.accept(writer);
			ret = writer.getMutatedLine();
			((NonLeaf) parent).setMutGenLimit(mutationsLeft);
		}
		return ret;
	}
	
	private void writeAST(CompilationUnit source, MutantCodeWriter writer) throws ParseTreeException {
		source.accept(writer);
	}
	
}