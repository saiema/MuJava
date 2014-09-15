////////////////////////////////////////////////////////////////////////////
// Module : JTI.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import java.util.Vector;
import openjava.mop.*;
import openjava.ptree.*;

/**
 * <p>Generate JTI (Java-specific this keyword insertion) --
 *    insert the keyword <i>this</i> to instance variables or method parameters
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class JTI extends mujava.op.util.Mutator
{
   Vector instanceVar = new Vector();
   Vector localVar = new Vector();
   boolean isJTITarget = false;

   public JTI(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit)
   {
	  super( file_env, comp_unit );
   }

   boolean isTarget( Parameter p ) throws ParseTreeException 
   {
      for (int i=0; i<instanceVar.size(); i++)
      {
         String field_name = instanceVar.get(i).toString();
         if (field_name.equals(p.getVariable()))
         {
            localVar.add(p.getVariable());
            return true;
         }
      }
      return false;
   }

   public void visit( FieldDeclaration p ) throws ParseTreeException 
   {
      instanceVar.add(p.getName());
   }

   public void visit( AssignmentExpression p ) throws ParseTreeException 
   {
  	  Expression newp = this.evaluateDown( p );
	  if (newp != p) 
	  {
		 p.replace( newp );
	     return;
	  }
	  p.childrenAccept( this );
	  newp = this.evaluateUp( p );
	  if (newp != p)  
		 p.replace( newp );
   }

   public void visit( ConstructorDeclaration p ) throws ParseTreeException 
   {
      ParameterList plist = p.getParameters();
      localVar.removeAllElements();
      for (int i=0; i<plist.size(); i++)
      {
         if (isTarget(plist.get(i)))
         {
            isJTITarget = true;
         }
      }
      if (isJTITarget)
      {
         super.visit(p);
      }
      isJTITarget = false;
   }

   public void visit( MethodDeclaration p ) throws ParseTreeException 
   {
      ParameterList plist = p.getParameters();
      localVar.removeAllElements();
      for (int i=0; i<plist.size(); i++)
      {
         if (isTarget(plist.get(i)))
         {
            isJTITarget = true;
         }
      }
      
      if (isJTITarget)
      {
         super.visit(p);
      }
      isJTITarget = false;
   }

   public void visit( FieldAccess p ) throws ParseTreeException 
   {
	  Expression ref_exp = p.getReferenceExpr();
	  if (ref_exp instanceof SelfAccess)
	  {
         return;
	  } 
	  else
	  {
         super.visit(p);
	  }
   }

   public void visit( Variable p ) throws ParseTreeException 
   {
      for (int i=0; i<localVar.size(); i++)
      {
         if (p.toString().equals(localVar.get(i).toString()))
         {
		    outputToFile(p);
		 }
      }
   }

   /**
    * Output JTI mutants to files
    * @param original
    */
   public void outputToFile(Variable original)
   {
      if (comp_unit == null) 
    	 return;

      String f_name;
      num++;
      f_name = getSourceName(this);
      String mutant_dir = getMuantID();

      try 
      {
		 PrintWriter out = getPrintWriter(f_name);
		 JTI_Writer writer = new JTI_Writer(mutant_dir, out);
		 writer.setMutant(original);
		 comp_unit.accept( writer );
		 out.flush();  
		 out.close();
      } catch ( IOException e ) 
      {
		 System.err.println( "fails to create " + f_name );
      } catch ( ParseTreeException e ) 
      {
		 System.err.println( "errors during printing " + f_name );
		 e.printStackTrace();
      }
   }
}

