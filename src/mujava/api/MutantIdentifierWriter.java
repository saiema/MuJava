package mujava.api;

import java.io.PrintWriter;

import mujava.op.util.MutantCodeWriter;
import mujava.op.AMC_Writer;
import mujava.op.BEE_Writer;
import mujava.op.CRCR_Writer;
import mujava.op.EMM_Writer;
import mujava.op.EOA_Writer;
import mujava.op.EOC_Writer;
import mujava.op.IC_Writer;
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
import mujava.op.LVR_Writer;
import mujava.op.NPER_Writer;
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
 * 
 * This class will call the specific {@code MutantCodeWriter} for each {@code Mutation}
 */
public class MutantIdentifierWriter {

	private CompilationUnit source;
	private PrintWriter output;

	/**
	 * Constructor
	 * 
	 * @param source	:	the original compilation unit				:	{@code CompilationUnit}
	 * @param output	:	the printer to write the compilation unit	:	{@code PrintWriter}
	 */
	public MutantIdentifierWriter(CompilationUnit source, PrintWriter output) {
		this.source = source;
		this.output = output;
	}
	
	/**
	 * This method writes the compilation unit defined in this class constructor
	 * and when it reaches the original node in the mutation it will write the mutant node instead
	 * 
	 * @param mutation	:	the mutation which contains the original and the mutated node	:	{@code Mutation}
	 * @return	the line number where the mutation was written or {@code -1} if the mutation couldn't be written	:	{@code int}
	 * @throws ParseTreeException
	 */
	public int write(Mutation mutation) throws ParseTreeException {
		MutantCodeWriter writer_generic;
		int ret = -1;
		boolean ihiOp = false;
		if (mutation.getMutOp().equals(MutationOperator.AODS)) {
			
			writer_generic = new AODS_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.AODU)) {
			
			writer_generic = new AODU_Writer(null, output, mutation);

		} else if (mutation.getMutOp().equals(MutationOperator.AOIS)) {
			
			writer_generic = new AOIS_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.AOIU)) {
			
			writer_generic = new AOIU_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.AORB)) {
			
			writer_generic = new AORB_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.AORS)) {
			
			writer_generic = new AORS_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.AORU)) {
			
			writer_generic = new AORU_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.ROR)) {
					
			writer_generic = new ROR_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.LOD)){
			
			writer_generic = new LOD_Writer(null, output, mutation);

		} else if (mutation.getMutOp().equals(MutationOperator.LOI)){

			writer_generic = new LOI_Writer(null, output, mutation);	

		} else if (mutation.getMutOp().equals(MutationOperator.LOR)){
			
			writer_generic = new LOR_Writer(null, output, mutation);

		} else if (mutation.getMutOp().equals(MutationOperator.SOR)){
				
			writer_generic = new SOR_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.PRVOR) || mutation.getMutOp().equals(MutationOperator.PRVOR_SMART) || mutation.getMutOp().equals(MutationOperator.PRVOR_REFINED)){
			
			writer_generic = new PRVO_Writer(null, output, mutation);

		} else if (mutation.getMutOp().equals(MutationOperator.PRVOL) || mutation.getMutOp().equals(MutationOperator.PRVOL_SMART)) {
			
			writer_generic = new PRVO_Writer(null, output, mutation);

		} else if (mutation.getMutOp().equals(MutationOperator.PRVOU) || mutation.getMutOp().equals(MutationOperator.PRVOU_SMART) || mutation.getMutOp().equals(MutationOperator.PRVOU_REFINED)) {
			
			writer_generic = new PRVO_Writer(null, output, mutation);

		} else if (mutation.getMutOp().equals(MutationOperator.ASRS)) {
			
			writer_generic = new ASRS_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.COD)) {
			
			writer_generic = new COD_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.COI)) {
			
			writer_generic = new COI_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.COR)) {
			
			writer_generic = new COR_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.AMC)) {
			
			writer_generic = new AMC_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.IHI)) {
			
			writer_generic = new IHI_Writer(null, output, mutation);
			ihiOp = true;
			
		} else if(mutation.getMutOp().equals(MutationOperator.EMM)) { 
			
			writer_generic = new EMM_Writer(null, output, mutation);
			
		} else if(mutation.getMutOp().equals(MutationOperator.EAM)) { 
			
			writer_generic = new EMM_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.EOC)) {
			
			writer_generic = new EOC_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.IHD)) {
			
			writer_generic = new IHD_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.JDC)) {
			
			writer_generic = new JDC_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.EOA) || mutation.getMutOp().equals(MutationOperator.EOA_DUMB) || mutation.getMutOp().equals(MutationOperator.EOA_STRICT)) {
			
			writer_generic = new EOA_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.IPC)) {
			
			writer_generic = new IPC_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.ISI) || mutation.getMutOp().equals(MutationOperator.ISI_SMART) || mutation.getMutOp().equals(MutationOperator.OAN) || mutation.getMutOp().equals(MutationOperator.OAN_RELAXED)) {
			
			writer_generic = new ISI_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.ISD) || mutation.getMutOp().equals(MutationOperator.ISD_SMART) || mutation.getMutOp().equals(MutationOperator.JTD)) {
			
			writer_generic = new ISD_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.IOD)) {
			
			writer_generic = new IOD_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.IOP)) {
			
			writer_generic = new IOP_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.JID)) {
			
			writer_generic = new JID_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.JSD) || mutation.getMutOp().equals(MutationOperator.JSI)) {
			
			writer_generic = new JSD_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.JTI) || mutation.getMutOp().equals(MutationOperator.JTI_SMART)) {
			
			writer_generic = new JTI_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.PCC)) {
			
			writer_generic = new PCC_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.PCD)) {
			
			writer_generic = new PCD_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.PCI)) {
			
			writer_generic = new PCI_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.PMD) || mutation.getMutOp().equals(MutationOperator.PPD)) {
			
			writer_generic = new PMD_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.OMR)) {
			
			writer_generic = new OMR_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.PNC)) {
			
			writer_generic = new PNC_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.NPER)) {
			
			writer_generic = new NPER_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.BEE)) {
			
			writer_generic = new BEE_Writer(null, output, mutation);
			
		} else if (mutation.getMutOp().equals(MutationOperator.CRCR)) {
			
			writer_generic = new CRCR_Writer(null, output, mutation);
			
	    } else if (mutation.getMutOp().equals(MutationOperator.IC)) {
			
			writer_generic = new IC_Writer(null, output, mutation);
			
	    } else if (mutation.getMutOp().equals(MutationOperator.LVR)) {
			
			writer_generic = new LVR_Writer(null, output, mutation);
			
	    } else if (mutation.getMutOp().equals(MutationOperator.MULTI)) {
			
			writer_generic = new MutantCodeWriter(null, output, null);
			
		} else {
			throw new IllegalStateException("The mutant " + mutation.getMutOp() + " isn't writable");
		}
		if (mutation.getMutOp().equals(MutationOperator.MULTI)) {
			writeAST(source, writer_generic);
		} else {
			ret = writeMutant(source, writer_generic, mutation);
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
		if ((Api.useMutGenLimit() && parent != null && mutationsLeft > 0) || (!Api.useMutGenLimit())) {
			if (Api.useMutGenLimit()) ((NonLeaf) parent).setMutGenLimit(mutationsLeft - 1);
			source.accept(writer);
			ret = writer.getMutatedLine();
			if (Api.useMutGenLimit()) ((NonLeaf) parent).setMutGenLimit(mutationsLeft);
		}
		return ret;
	}
	
	private void writeAST(CompilationUnit source, MutantCodeWriter writer) throws ParseTreeException {
		source.accept(writer);
	}
	
}