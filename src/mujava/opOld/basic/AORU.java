////////////////////////////////////////////////////////////////////////////
// Module : AORU.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld.basic;

import openjava.mop.*;
import openjava.ptree.*;
import java.io.*;

// not use ??? (11/15/2009) 

/**
 * <p>Generate AORU (Arithmetic Operator Replacement (Unary)) mutants --
 *    replace each occurrence of one of the arithmetic operators + and - 
 *    by each of the other operators 
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class AORU extends MethodLevelMutator
{
   public AORU(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit)
   {
      super( file_env, comp_unit );
   }

   /**
    * If a given unary expression contains an arithmetic operator + or -,
    * generate an AORU mutant
    */
   public void visit( UnaryExpression p ) throws ParseTreeException
   {
      int op = p.getOperator();
      if ( (op == UnaryExpression.MINUS) || (op == UnaryExpression.PLUS) ) 
      {
         genBasicUnaryMutants(p,op);
      }
   }

   void genBasicUnaryMutants(UnaryExpression p, int op)
   {
      UnaryExpression mutant;
      if ( op == UnaryExpression.PLUS )
      {
         mutant = (UnaryExpression)(p.makeRecursiveCopy());
         mutant.setOperator(UnaryExpression.MINUS);
         outputToFile(p, mutant);
      }
      else if ( op == UnaryExpression.MINUS )
      {
         mutant = (UnaryExpression)(p.makeRecursiveCopy());
         mutant.setOperator(UnaryExpression.PLUS);
         outputToFile(p, mutant);
      }
   }

   /**
    * Output AORU mutants to files
    * @param original
    * @param mutant
    */
   public void outputToFile(UnaryExpression original, UnaryExpression mutant)
   {
      if (comp_unit == null) 
    	 return;
      
      String f_name;
      num++;
      f_name = getSourceName("AORU");
      String mutant_dir = getMuantID("AORU");

      try 
      {
		 PrintWriter out = getPrintWriter(f_name);
		 AORU_Writer writer = new AORU_Writer(mutant_dir, out);
		 writer.setMutant(original, mutant);
         writer.setMethodSignature(currentMethodSignature);
		 comp_unit.accept( writer );
		 out.flush();  
		 out.close();
      } catch ( IOException e ) {
		 System.err.println( "fails to create " + f_name );
      } catch ( ParseTreeException e ) {
		 System.err.println( "errors during printing " + f_name );
		 e.printStackTrace();
      }
   }
}
