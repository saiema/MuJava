package mujava;

import java.io.File;
import java.util.Set;

import mujava.api.MutationOperator;
import mujava.api.MutantsInformationHolder;
import mujava.op.AMC;
import mujava.op.EAM;
import mujava.op.EMM;
import mujava.op.EOA;
import mujava.op.EOC;
import mujava.op.IHD;
import mujava.op.IHI;
import mujava.op.IOD;
import mujava.op.IOP;
import mujava.op.IPC;
import mujava.op.ISD;
import mujava.op.ISI;
import mujava.op.JDC;
import mujava.op.JID;
import mujava.op.JSD;
import mujava.op.JSI;
import mujava.op.JTD;
import mujava.op.JTI;
import mujava.op.NPER;
import mujava.op.OAN;
import mujava.op.OMR;
import mujava.op.PCC;
import mujava.op.PCD;
import mujava.op.PCI;
import mujava.op.PMD;
import mujava.op.PNC;
import mujava.op.PRVO;
import mujava.op.basic.AODS;
import mujava.op.basic.AODU;
import mujava.op.basic.AOIS;
import mujava.op.basic.AOIU;
import mujava.op.basic.AORB;
import mujava.op.basic.AORS;
import mujava.op.basic.AORU;
import mujava.op.basic.ASRS;
import mujava.op.basic.COD;
import mujava.op.basic.COI;
import mujava.op.basic.COR;
import mujava.op.basic.LOD;
import mujava.op.basic.LOI;
import mujava.op.basic.LOR;
import mujava.op.basic.ROR;
import mujava.op.basic.SOR;
import mujava.util.Debug;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.ClassDeclarationList;
import openjava.ptree.ParseTreeException;

/**
 * This class is used to generate mutations, call each mutation operator
 * on a compilation unit.
 * 
 * This class does not create any directory nor mutated file.
 * 
 * @version 1.0
 */
public class NotDirBasedMutantsGenerator extends MutantsGenerator {

	/**
	 * Mutation operators to use : {@code Set<MutationOperator>}
	 */
	protected Set<MutationOperator> mutOps;
		
	/**
	 * Constructor
	 * 
	 * @param f			:	the java source file to mutate	:	{@code File}
	 * @param mutOps	:	the mutation operators to use 	:	{@code Set<MutationOperator>}
	 */
	public NotDirBasedMutantsGenerator(File f, Set<MutationOperator> mutOps) {
		super(f);
		this.mutOps = mutOps;
	}

	@Override
	protected void genMutants() {
		// Code taken from genTraditionalMutants.
		if (comp_unit == null) {
			System.err.println (original_file + " is skipped.");
		}
		MutantsInformationHolder.mainHolder().setCompilationUnit(comp_unit);
		ClassDeclarationList cdecls = comp_unit.getClassDeclarations();
		if (cdecls == null || cdecls.size() == 0) {
			return;
		}
		if (mutOps != null && mutOps.size() > 0) {
			generateMutants(cdecls);
		}

	}

	private void generateMutants(ClassDeclarationList cdecls) {
		for (int j = 0 ; j < cdecls.size() ; ++j ) {
			ClassDeclaration cdecl = cdecls.get(j);
			if (cdecl.getName().equals(MutationSystem.CLASS_NAME)) {
				try {
					mujava.op.util.Mutator mutant_op;
					if (mutOps.contains(MutationOperator.AORB)) {
						Debug.println("  Applying AOR-Binary ... ... ");
						mutant_op = new AORB(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					if (mutOps.contains(MutationOperator.AORS)) {
						Debug.println("  Applying AOR-Short-Cut ... ... ");
						mutant_op = new AORS(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					if (mutOps.contains(MutationOperator.AORU)) {
						Debug.println("  Applying AOR-Normal-Unary ... ... ");
						mutant_op = new AORU(file_env,cdecl,comp_unit);
						comp_unit.accept(mutant_op);
					}
					if (mutOps.contains(MutationOperator.AODU)) {
						Debug.println("  Applying AOD-Normal-Unary ... ... ");
						mutant_op = new AODU(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					if (mutOps.contains(MutationOperator.AODS)) {
						Debug.println("  Applying AOD-Short-Cut ... ... ");
						mutant_op = new AODS(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					if (mutOps.contains(MutationOperator.AOIU)) {
						Debug.println("  Applying AOI-Normal-Unary ... ... ");
						mutant_op = new AOIU(file_env,cdecl,comp_unit);
						comp_unit.accept(mutant_op);
					}
					if (mutOps.contains(MutationOperator.AOIS)) {
						Debug.println("  Applying AOI-Short-Cut ... ... ");
						mutant_op = new AOIS(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					if (mutOps.contains(MutationOperator.ROR)) {
						Debug.println("  Applying ROR ... ... ");
						mutant_op = new ROR(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					if (mutOps.contains(MutationOperator.COR)) {
						Debug.println("  Applying COR ... ... ");
						mutant_op = new COR(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					if (mutOps.contains(MutationOperator.COD)) {
						Debug.println("  Applying COD ... ... ");
						mutant_op = new COD(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					if (mutOps.contains(MutationOperator.COI)) {
						Debug.println("  Applying COI ... ... ");
						mutant_op = new COI(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					if (mutOps.contains(MutationOperator.SOR)) {
						Debug.println("  Applying SOR ... ... ");
						mutant_op = new SOR(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					if (mutOps.contains(MutationOperator.LOR)) {
						Debug.println("  Applying LOR ... ... ");
						mutant_op = new LOR(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					if (mutOps.contains(MutationOperator.LOI)) {
						Debug.println("  Applying LOI ... ... ");
						mutant_op = new LOI(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					if (mutOps.contains(MutationOperator.LOD)) { 
						Debug.println("  Applying LOD ... ... ");
						mutant_op = new LOD(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					if (mutOps.contains(MutationOperator.ASRS)) {
						Debug.println("  Applying ASR-Short-Cut ... ... ");
						mutant_op = new ASRS(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.PRVOL)) {
						Debug.println("  Applying PRVOL ... ... ");
						mutant_op = new PRVO(file_env, cdecl, comp_unit);
						((PRVO)mutant_op).setLeft();
						((PRVO)mutant_op).dumbMode();
						((PRVO)mutant_op).setOP(MutationOperator.PRVOL);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.PRVOL_SMART)) {
						Debug.println("  Applying PRVOL_SMART ... ... ");
						mutant_op = new PRVO(file_env, cdecl, comp_unit);
						((PRVO)mutant_op).setLeft();
						((PRVO)mutant_op).smartMode();
						((PRVO)mutant_op).setOP(MutationOperator.PRVOL_SMART);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.PRVOR)) {
						Debug.println("  Applying PRVOR ... ... ");
						mutant_op = new PRVO(file_env, cdecl, comp_unit);
						((PRVO)mutant_op).setRight();
						((PRVO)mutant_op).dumbMode();
						((PRVO)mutant_op).setOP(MutationOperator.PRVOR);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.PRVOR_SMART)) {
						Debug.println("  Applying PRVOR_SMART ... ... ");
						mutant_op = new PRVO(file_env, cdecl, comp_unit);
						((PRVO)mutant_op).setRight();
						((PRVO)mutant_op).smartMode();
						((PRVO)mutant_op).setOP(MutationOperator.PRVOR_SMART);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.PRVOR_REFINED)) {
						Debug.println("  Applying PRVOR_REFINED ... ... ");
						mutant_op = new PRVO(file_env, cdecl, comp_unit);
						((PRVO)mutant_op).setRight();
						((PRVO)mutant_op).setRefinedMode(true);
						((PRVO)mutant_op).smartMode();
						((PRVO)mutant_op).setOP(MutationOperator.PRVOR_REFINED);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.PRVOU)) {
						Debug.println("  Applying PRVOU ... ... ");
						mutant_op = new PRVO(file_env, cdecl, comp_unit);
						((PRVO)mutant_op).setUnary();
						((PRVO)mutant_op).dumbMode();
						((PRVO)mutant_op).setOP(MutationOperator.PRVOU);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.PRVOU_SMART)) {
						Debug.println("  Applying PRVOU_SMART ... ... ");
						mutant_op = new PRVO(file_env, cdecl, comp_unit);
						((PRVO)mutant_op).setUnary();
						((PRVO)mutant_op).smartMode();
						((PRVO)mutant_op).setOP(MutationOperator.PRVOU_SMART);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.PRVOU_REFINED)) {
						Debug.println("  Applying PRVOU_REFINED ... ... ");
						mutant_op = new PRVO(file_env, cdecl, comp_unit);
						((PRVO)mutant_op).setUnary();
						((PRVO)mutant_op).setRefinedMode(true);
						((PRVO)mutant_op).smartMode();
						((PRVO)mutant_op).setOP(MutationOperator.PRVOU_REFINED);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.AMC)) {
						Debug.println("  Applying AMC ... ... ");
						mutant_op = new AMC(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.IHI)) {
						Debug.println("  Applying IHI ... ... ");
						mutant_op = new IHI(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.EMM)) {
						Debug.println("  Applying EMM ... ... ");
						mutant_op = new EMM(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.EAM)) {
						Debug.println("  Applying EAM ... ... ");
						mutant_op = new EAM(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.EOC)) {
						Debug.println("  Applying EOC ... ... ");
						mutant_op = new EOC(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.IHD)) {
						Debug.println("  Applying IHD ... ... ");
						mutant_op = new IHD(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.JDC)) {
						Debug.println("  Applying JDC ... ... ");
						mutant_op = new JDC(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.EOA)) {
						Debug.println("  Applying EOA ... ... ");
						mutant_op = new EOA(file_env, cdecl, comp_unit);
						((EOA)mutant_op).normalMode();
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.EOA_DUMB)) {
						Debug.println("  Applying EOA_DUMB ... ... ");
						mutant_op = new EOA(file_env, cdecl, comp_unit);
						((EOA)mutant_op).dumbMode();
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.EOA_STRICT)) {
						Debug.println("  Applying EOA_STRICT ... ... ");
						mutant_op = new EOA(file_env, cdecl, comp_unit);
						((EOA)mutant_op).strictMode();
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.IPC)) {
						Debug.println("  Applying IPC ... ... ");
						mutant_op = new IPC(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.ISI)) {
						Debug.println("  Applying ISI ... ... ");
						mutant_op = new ISI(file_env, cdecl, comp_unit);
						((ISI)mutant_op).dumbMode();
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.ISI_SMART)) {
						Debug.println("  Applying ISI_SMART ... ... ");
						mutant_op = new ISI(file_env, cdecl, comp_unit);
						((ISI)mutant_op).smartMode();
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.ISD)) {
						Debug.println("  Applying ISD ... ... ");
						mutant_op = new ISD(file_env, cdecl, comp_unit);
						((ISD)mutant_op).dumbMode();
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.ISD_SMART)) {
						Debug.println("  Applying ISD_SMART ... ... ");
						mutant_op = new ISD(file_env, cdecl, comp_unit);
						((ISD)mutant_op).smartMode();
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.IOD)) {
						Debug.println("  Applying IOD ... ... ");
						mutant_op = new IOD(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.IOP)) {
						Debug.println("  Applying IOP ... ... ");
						mutant_op = new IOP(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.JID)) {
						Debug.println("  Applying JID ... ... ");
						mutant_op = new JID(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.JSD)) {
						Debug.println("  Applying JSD ... ... ");
						mutant_op = new JSD(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.JSI)) {
						Debug.println("  Applying JSI ... ... ");
						mutant_op = new JSI(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.JTD)) {
						Debug.println("  Applying JTD ... ... ");
						mutant_op = new JTD(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.JTI)) {
						Debug.println("  Applying JTI ... ... ");
						mutant_op = new JTI(file_env, cdecl, comp_unit);
						((JTI)mutant_op).dumbMode();
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.JTI_SMART)) {
						Debug.println("  Applying JTI SMART ... ... ");
						mutant_op = new JTI(file_env, cdecl, comp_unit);
						((JTI)mutant_op).smartMode();
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.OAN)) {
						Debug.println("  Applying OAN ... ... ");
						mutant_op = new OAN(file_env, cdecl, comp_unit);
						((OAN)mutant_op).strict();
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.OAN_RELAXED)) {
						Debug.println("  Applying OAN RELAXED ... ... ");
						mutant_op = new OAN(file_env, cdecl, comp_unit);
						((OAN)mutant_op).relaxed();
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.PCC)) {
						Debug.println("  Applying PCC ... ... ");
						mutant_op = new PCC(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.PCD)) {
						Debug.println("  Applying PCD ... ... ");
						mutant_op = new PCD(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.PMD)) {
						Debug.println("  Applying PMD ... ... ");
						mutant_op = new PMD(file_env, cdecl, comp_unit);
						((PMD)mutant_op).setPMD();
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.PPD)) {
						Debug.println("  Applying PPD ... ... ");
						mutant_op = new PMD(file_env, cdecl, comp_unit);
						((PMD)mutant_op).setPPD();
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.OMR)) {
						Debug.println("  Applying OMR ... ... ");
						mutant_op = new OMR(file_env, cdecl, comp_unit);
						((OMR)mutant_op).dumbMode();
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.PCI)) {
						Debug.println("  Applying PCI ... ... ");
						mutant_op = new PCI(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.PNC)) {
						Debug.println("  Applying PNC ... ... ");
						mutant_op = new PNC(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					
					if (mutOps.contains(MutationOperator.NPER)) {
						Debug.println("  Applying NPER ... ... ");
						mutant_op = new NPER(file_env, cdecl, comp_unit);
						comp_unit.accept(mutant_op);
					}
					
				} catch (ParseTreeException e) {
					System.err.println("Exception during generating  mutants for the class.");
					e.printStackTrace();
				}
			}
		}
	}
}