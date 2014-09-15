package mujava.op.basic;

import openjava.mop.*;
import openjava.ptree.*;
import java.io.*;
import mujava.MutationSystem;

public class MethodLevelMutator  extends mujava.op.util.Mutator
{
   String currentMethodSignature = null;

   public MethodLevelMutator(FileEnvironment file_env, CompilationUnit comp_unit)
   {
      super( file_env, comp_unit );
   }

   String getMethodSignature(MethodDeclaration p)
   {
      String str = p.getReturnType().getName() + "_" + p.getName() + "(";
      ParameterList pars = p.getParameters();
    
      for (int i=0; i<pars.size(); i++)
      {
         str += pars.get(i).getTypeSpecifier().getName();
         if (i != (pars.size()-1)) 
        	str += ",";
      }
      str += ")";
      return str;
   }

   String getConstructorSignature(ConstructorDeclaration p)
   {
      String str = p.getName() +"(";
      ParameterList pars = p.getParameters();
      for (int i=0; i<pars.size(); i++)
      {
         str += pars.get(i).getTypeSpecifier().getName();
         if (i != (pars.size()-1)) 
        	str+=",";
      }
      str += ")";
      return str;
   }

   /**
    * Retrieve the source's file name
    */
   public String getSourceName(mujava.op.util.Mutator clazz)
   {
	  // make directory for the mutant
	  String dir_name = MutationSystem.MUTANT_PATH + "/" + currentMethodSignature + "/"
                        + getClassName() + "_" + this.num;
	  File f = new File(dir_name);
	  f.mkdir();

	  // return file name
	  String name;
	  name = dir_name + "/" +  MutationSystem.CLASS_NAME + ".java";
      return name;
   }

   /**
    * Retrieve the source's file name
    */
   public String getSourceName(String op_name)
   {
 	  // make directory for the mutant
	  String dir_name = MutationSystem.MUTANT_PATH + "/" + currentMethodSignature + "/" + op_name + "_" + this.num;
	  File f = new File(dir_name);
	  f.mkdir();

	  // return file name
	  String name;
	  name = dir_name + "/" +  MutationSystem.CLASS_NAME + ".java";
      return name;
   }

   public void visit(MethodDeclaration p) throws ParseTreeException 
   {
      currentMethodSignature = getMethodSignature(p);
      super.visit(p);
   }

   public void visit(ConstructorDeclaration p) throws ParseTreeException 
   {
      currentMethodSignature = getConstructorSignature(p);
      super.visit(p);
   }
} 