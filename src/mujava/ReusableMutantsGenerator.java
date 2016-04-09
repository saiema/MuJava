package mujava;

import java.lang.reflect.Constructor;
import java.util.Set;

import mujava.api.MutationOperator;
import mujava2.api.program.JavaAST;
import openjava.mop.Environment;
import openjava.mop.FileEnvironment;
import openjava.mop.OJClass;
import openjava.mop.OJSystem;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.ClassDeclarationList;
import openjava.ptree.ParseTreeException;
import openjava.ptree.util.MemberAccessCorrector;
import openjava.ptree.util.TypeNameQualifier;

public class ReusableMutantsGenerator extends NotDirBasedMutantsGenerator {

	private JavaAST originalAST;
	
	public ReusableMutantsGenerator() {
		super(null, null);
	}

	public void generateMutants(JavaAST originalAST, Set<MutationOperator> mutOps) throws OpenJavaException {
		if (originalAST == null) throw new IllegalArgumentException("null originalAST");
		if (originalAST.getCompUnit() == null) throw new IllegalArgumentException("originalAST contains null compilation unit");
		if (mutOps == null) throw new IllegalArgumentException("null mutOps");
		if (mutOps.isEmpty()) throw new IllegalArgumentException("empty mutOps");
		this.originalAST = originalAST;
		this.comp_unit = originalAST.getCompUnit();
		this.mutOps = mutOps;
		initializeGenerator();
		super.genMutants();
	}
	
	private void initializeGenerator() throws OpenJavaException {
		
		//generate parse tree
		String pubcls_name = this.originalAST.getClassName();

		file_env = new FileEnvironment(OJSystem.env, comp_unit, pubcls_name);
		ClassDeclarationList typedecls = comp_unit.getClassDeclarations();

		for (int j = 0; j < typedecls.size(); ++j) {
			ClassDeclaration class_decl = typedecls.get(j);
			OJClass c = makeOJClass(file_env, class_decl);
			OJSystem.env.record(c.getName(), c);
			recordInnerClasses(c);
		}
		
		//init parse tree
		try {
			comp_unit.accept(new TypeNameQualifier(file_env));
			MemberAccessCorrector corrector = new MemberAccessCorrector(file_env);
			comp_unit.accept(corrector);
		} catch (ParseTreeException e) {
			throw new OpenJavaException("can't initialize parse tree");
		}
	}
	
	/**
	 * Record inner-classes
	 * 
	 * @param c
	 */
	private static void recordInnerClasses(OJClass c) {
		OJClass[] inners = c.getDeclaredClasses();
		for (int i = 0; i < inners.length; ++i) {
			OJSystem.env.record(inners[i].getName(), inners[i]);
			recordInnerClasses(inners[i]);
		}
	}

	/** -> to move to OJClass.forParseTree() **/
	private OJClass makeOJClass(Environment env, ClassDeclaration cdecl) {
		OJClass result;
		String qname = env.toQualifiedName(cdecl.getName());
		Class<?> meta = OJSystem.getMetabind(qname);
		try {
			Constructor<?> constr = meta.getConstructor(new Class[] {
					Environment.class, OJClass.class, ClassDeclaration.class });
			Object[] args = new Object[] { env, null, cdecl };
			result = (OJClass) constr.newInstance(args);
		} catch (Exception ex) {
			System.err.println("errors during gererating a metaobject for "
					+ qname);
			ex.printStackTrace();
			result = new OJClass(env, null, cdecl);
		}
		return result;
	}
	
	
	

}
