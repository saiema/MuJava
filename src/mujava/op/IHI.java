package mujava.op;

import java.util.LinkedList;
import java.util.List;

import mujava.api.Api;
import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;
import mujava.app.MutationRequest;
import openjava.mop.FileEnvironment;
import openjava.mop.OJClass;
import openjava.mop.OJField;
import openjava.mop.OJModifier;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.CompilationUnit;
import openjava.ptree.FieldDeclaration;
import openjava.ptree.ModifierList;
import openjava.ptree.ParseTreeException;
import openjava.ptree.ParseTreeObject;
import openjava.ptree.TypeName;

/**
 * <p>Generate IHI (Hiding variable insertion) mutants -- 
 *    insert a declaration to hide the declaration of 
 *    each variable declared in an ancestor</p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.1
 * <hr>
 * class rewriten to function properly with MuJava++
  */
public class IHI extends mujava.op.util.Mutator{
	
	ParseTreeObject parent = null;

	public IHI(FileEnvironment file_env, ClassDeclaration cdecl,CompilationUnit comp_unit) {
		super(file_env, comp_unit);
	}
	
	@Override
	public ClassDeclaration evaluateUp(ClassDeclaration ptree) throws ParseTreeException {
		return ptree;
	}
		
	
	public void visit(ClassDeclaration cs) throws ParseTreeException {
		super.visit(cs);
		
		
		if (Api.usingApi() && (Api.getMethodUnderConsideration().compareTo(MutationRequest.MUTATE_CLASS)!=0)) {
			return;
		}
		if (!(getMutationsLeft(cs) > 0)) return;
		
		OJField[] localFields = getSelfType().getDeclaredFields();
		OJField[] inheritedFields = filterInheritable(getSelfType().getSuperclass().getAllFields());
		for (int i = 0; i < inheritedFields.length; i++) {
			boolean isHidden = false;
			for (int l = 0; l < localFields.length; l++) {
				if (compareFields(inheritedFields[i], localFields[l])) {
					isHidden = true;
					break;
				}
			}
			if (!isHidden)
	         {
	            if(inheritedFields[i].getModifiers().isPublic() || !isEquivalent(comp_unit,inheritedFields[i]))
	            {
	               try
	               {
	            	  String generics = parseGenerics(inheritedFields[i].getByteCode().toGenericString());
	            	  ModifierList modlist = new ModifierList();
	                  OJModifier modif = inheritedFields[i].getModifiers();
	                  TypeName tname = TypeName.forOJClass( inheritedFields[i].getType() );
	                  tname.setGenerics(generics);
	                  modlist.add( modif.toModifier() );
	                  String name = inheritedFields[i].getName();
	                  FieldDeclaration mutant = new FieldDeclaration( modlist, tname, name, null );
	                  outputToFile(cs, mutant);
	               } catch(Exception ex)
	               {
	                  System.err.println("[Exception]  " + ex);
	               }
	            }
	         }
		}
		
	}
	
	private String parseGenerics(String genericString) {
		String res = "";
		int genStart = genericString.indexOf("<");
		int genEnd = genericString.lastIndexOf(">");
		if (genStart != -1 && genEnd != -1) {
			String generics = genericString.substring(genStart+1, genEnd);
			res = "<"+generics+">";
		}
		return res;
	}

	private OJField[] filterInheritable(OJField[] allFields) {
		List<OJField> inheritableFields = new LinkedList<OJField>();
		for (OJField f : allFields) {
			if (f.getModifiers().isFinal()) {
				continue;
			}
			if (f.getModifiers().isPrivate()) {
				continue;
			}
			inheritableFields.add(f);
		}
		return inheritableFields.toArray(new OJField[inheritableFields.size()]);
	}

	private boolean compareFields(OJField f1, OJField f2) {
		if (f1.getName().compareTo(f2.getName()) == 0) {
			OJClass f1Type = f1.getType();
			OJClass f2Type = f2.getType();
			if (f1Type.getName().compareTo(f2Type.getName())==0) return true;
			return false;
		} else {
			return false;
		}
	}
	
	private boolean isEquivalent(CompilationUnit comp_unit,OJField f) {
		Api.disableApi();
		IHD_IHI_EqAnalyzer engine = new IHD_IHI_EqAnalyzer(file_env, comp_unit, f.getName());
	    try {
	    	comp_unit.accept(engine);
	    }	catch (ParseTreeException e) {
	    	// do nothing
	    }
	    Api.enableApi();
	    if (engine.isEquivalent()) 
	    	return true;
	    else 
	    	return false;
	}

	private void outputToFile(ClassDeclaration original, FieldDeclaration mutant) {
		MutantsInformationHolder.mainHolder().addMutantIdentifier(Mutant.IHI, original, mutant);
	}

}
