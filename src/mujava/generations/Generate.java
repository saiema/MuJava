package mujava.generations;

import mujava.api.Configuration;
import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;
import mujava.app.Core;
import mujava.app.MutationRequest;
import mujava.op.PRVO;

public class Generate {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String clazz = "roops" + Core.SEPARATOR + "core" + Core.SEPARATOR + "objects" + Core.SEPARATOR + "SinglyLinkedList3";
		//String clazz = "bugHunting" + Core.SEPARATOR + "PRVOMethodCall";
		//String clazz = "bugHunting" + Core.SEPARATOR + "SinglyLinkedList";
		//String clazz = "main" + Core.SEPARATOR + "Gcd";
		//String[] methods = {"insert"};
		//String[] methods = {"radiatedMethod"};
		String[] methods = {"getNode"};
		Mutant[] ops = {	
//							Mutant.AMC,
//							Mutant.AODS,
//							Mutant.AODU,
//							Mutant.AOIS,
//							Mutant.AOIU,
//							Mutant.AORB,
//							Mutant.AORS,
//							Mutant.AORU,
//							Mutant.ASRS,
//							Mutant.COD,
//							Mutant.COI,
//							Mutant.COR,
//							Mutant.EAM,
//							Mutant.EMM,
//							Mutant.EOA_STRICT,
//							Mutant.EOC_SMART,
//							Mutant.IHD,
//							Mutant.IHI,
//							Mutant.IOD,
//							Mutant.IOP,
//							Mutant.IPC,
//							Mutant.ISD_SMART,
//							Mutant.ISI_SMART,
//							Mutant.JDC,
//							Mutant.JID,
//							Mutant.JSD,
//							Mutant.JSI,
//							Mutant.JTD,
//							Mutant.JTI_SMART,
//							Mutant.LOD,
//							Mutant.LOI,
//							Mutant.LOR,
//							Mutant.OAN_RELAXED,
//							Mutant.OMR,
//							Mutant.PCC,
//							Mutant.PCD,
//							Mutant.PCI,
//							Mutant.PMD,
//							Mutant.PNC,
//							Mutant.PPD,
//							Mutant.PRVOL_SMART,
//							Mutant.PRVOR_REFINED,
//							Mutant.PRVOU_REFINED,
//							Mutant.ROR,
//							Mutant.SOR
							

//							Mutant.PRVOL_SMART,
							Mutant.PRVOR_REFINED,
							Mutant.PRVOU_REFINED,
							Mutant.PRVOL,
							Mutant.AODS,
							Mutant.AODU,
							Mutant.AOIS,
							Mutant.AOIU,
							Mutant.AORB,
							Mutant.AORS,
							Mutant.AORU,
							Mutant.ASRS,
							Mutant.COD,
							Mutant.COI,
							Mutant.COR,
							Mutant.LOD,
//							Mutant.LOI,
							Mutant.LOR,
							Mutant.ROR,
							Mutant.SOR
							
						};
		MutantsInformationHolder.setVerbose(false);
		Configuration.add(PRVO.ENABLE_SUPER, Boolean.TRUE);
		Configuration.add(PRVO.ENABLE_THIS, Boolean.TRUE);
		String basePathOriginals = args[0];
		String basePathMutants = args[1];
		MutationRequest originalRequest = new MutationRequest(clazz, methods, ops, basePathOriginals, basePathMutants);
		GoalTester goalTester = new GenerationsGoalTester(1);
		RequestGenerator requestGenerator = new SameRequestGenerator(originalRequest);
		Generator generator = new Generator(requestGenerator, goalTester, Generator.VERBOSE_LEVEL.FULL_VERBOSE);
		GenerationsInformation generationsInfo= generator.generate(false);
		System.out.println(generationsInfo.showBasicInformation());
	}

}
