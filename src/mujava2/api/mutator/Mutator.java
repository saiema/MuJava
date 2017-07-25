package mujava2.api.mutator;

import java.util.Collection;
import java.util.TreeMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import mujava.OpenJavaException;
import mujava.ReusableMutantsGenerator;
import mujava.api.Api;
import mujava.api.MutantType;
import mujava.api.MutantsInformationHolder;
import mujava.api.Mutation;
import mujava.api.MutationOperator;
import mujava.app.MutationInformation;
import mujava2.api.mutator.MutationRequest;
import mujava.app.MutatorsInfo;
import mujava.op.util.OLMO;
import mujava.security.ApiCaller;
import mujava2.api.program.JavaAST;
import openjava.ptree.ParseTreeException;

/**
 * A very simple mutation generator.
 * 
 * @author Simon Emmanuel Gutierrez Brida
 * @version 0.1
 */
public class Mutator extends ApiCaller {
	
	/**
	 * The AST to use when generating mutations
	 */
	private JavaAST originalAST;
	/**
	 * The mutation request
	 */
	private MutationRequest req;
	/**
	 * A reusable mutation generator, this is the class that really generates the mutations.
	 */
	private ReusableMutantsGenerator generator;
	
	/**
	 * Creates a new Mutator instance.
	 * 
	 * @param originalAST		:	the AST from which mutations will be generated
	 * @param req				:	the mutation request
	 */
	public Mutator(JavaAST originalAST, MutationRequest req) {
		super();
		this.originalAST = originalAST;
		this.req = req;
		this.generator = new ReusableMutantsGenerator();
	}

	
	/**
	 * Generates mutations from the AST and mutation request given in the constructor.
	 * 
	 * @return a collection of mutations (as {@code MutationInformation} objects) generated from the AST given in this class constructor
	 * @throws OpenJavaException
	 * @throws ParseTreeException
	 */
	public Collection<MutationInformation> generateMutations() throws OpenJavaException, ParseTreeException {
		Map<String, MutantsInformationHolder> mutants = new TreeMap<String, MutantsInformationHolder>();
		LinkedList<MutationInformation> mutations = new LinkedList<>();
		//|--------------------Initialization--------------------|
		Set<MutationOperator> mutOps = new HashSet<>(req.getOperators());
		//|--------------------Initialization--------------------|
		
		MutatorsInfo mutatorsInfo = MutatorsInfo.getInstance();
		
		//|--------------------Generation loop--------------------|
		for (String currentMethod : req.getMethods()) {
			//|--------------------Mutants Generation--------------------|
			//ParseTreeObject.resetObjectID();
			Api.hijackApi(this, req.getClassToMutateSimpleName(), currentMethod, mutOps);
			
			this.generator.generateMutants(originalAST, mutOps);
			MutantsInformationHolder mih = MutantsInformationHolder.mainHolder();
			MutantsInformationHolder.resetMainHolder(true);
			//|--------------------Mutants Generation--------------------|

			mutants.put(currentMethod, mih);
			
			for (Mutation mi : mih.getMutantsIdentifiers()) {
				OLMO olmo;
				boolean affectsOneLine = mutatorsInfo.affectsOneLine(mi.getMutOp());
				boolean isMethodLevelMutation = false;
				if (mutatorsInfo.getMutantType(mi.getMutOp()) == MutantType.MethodLevel) {
					isMethodLevelMutation = true;
				} else if (mutatorsInfo.getMutantType(mi.getMutOp()) == MutantType.ClassAndMethodLevel) {
					isMethodLevelMutation = currentMethod != MutationRequest.MUTATE_CLASS && currentMethod != MutationRequest.MUTATE_FIELDS;
				}
				if (affectsOneLine && isMethodLevelMutation/*mi.isOneLineInMethodOp()*/) {
					olmo = new OLMO(mi);
					olmo.visit(mih.getCompUnit());
					mi.setAffectedLine(olmo.getAffectedLine());
				} else if (affectsOneLine) {
					mi.setAffectedLine(Mutation.SINGLE_LINE_OUTSIDE_METHOD_DECLARATION);
				}
				MutationInformation mutationInformation = new MutationInformation(currentMethod, mi);
				mutations.add(mutationInformation);
			}

		}
		//|--------------------Generation loop--------------------|
		return mutations;
	}
	
}