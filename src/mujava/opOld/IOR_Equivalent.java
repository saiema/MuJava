////////////////////////////////////////////////////////////////////////////
// Module : IOR_Equivalent.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import openjava.mop.*;
import openjava.ptree.*;
/**
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class IOR_Equivalent  extends mujava.op.util.Mutator
{
   String declaredClassName = null;
   String target_method_name = null;
   TypeName target_return_type = null;
   ParameterList target_parList = null;
   boolean isEquivalent = true;

   public IOR_Equivalent(FileEnvironment file_env, CompilationUnit comp_unit)
   {
	  super( file_env, comp_unit );
      isEquivalent = true;
   }

   public void setInformation(String class_name, MethodDeclaration mutant)
   {
      this.declaredClassName = class_name;
      this.target_method_name = mutant.getName();
      this.target_return_type = mutant.getReturnType();
      this.target_parList = mutant.getParameters();
   }

   public void visit( ClassDeclaration p ) throws ParseTreeException 
   {
      if (p.getName().equals(declaredClassName))
      {
         super.visit(p);
      }
   }

   public void visit( MethodCall p ) throws ParseTreeException 
   {
      if ( !(p.getName().equals(target_method_name)) ) 
    	 return;
    
      ExpressionList argList = p.getArguments();
      if ((argList == null) || (argList.size() == 0))
      {
         if ( (target_parList == null) || (target_parList.size() == 0))
         { 
        	isEquivalent = false; 
         }
      }
      else
      {
         if (target_parList.size() == 0) 
        	return;
      }
      
      int argNum = argList.size();
      for (int i=0; i<argNum; i++)
      {
         Expression exp = argList.get(i);
         OJClass clazz = getType(exp);
         if (!(clazz.getName().equals(target_parList.get(i).getTypeSpecifier().getName()))) 
        	return;
      }
      
      isEquivalent = false;
   }

   public boolean isEquivalent()
   {
      return isEquivalent;
  }
}
