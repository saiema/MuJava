////////////////////////////////////////////////////////////////////////////
// Module : AORS.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld.basic;

import openjava.mop.*;
import openjava.ptree.*;
import java.io.*;

/**
 * <p>Generate AORS (Arithmetic Operator Replacement (Short-cut)) mutants --
 *    replace each occurrence of the increment and decrement operators 
 *    by each of the other operators
 * </p>
 * <p><i>Example</i>:
 *       for (int i=0; i<length; i++) is mutated to for (int i=0; i<length; i--)
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class AORS extends MethodLevelMutator
{
   boolean isPrePostEQ = true;

   public AORS(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit)
   {
      super( file_env, comp_unit );
   }

   public void visit( UnaryExpression p ) throws ParseTreeException
   {
      int op = p.getOperator();
      if ( (op == UnaryExpression.POST_DECREMENT) || (op == UnaryExpression.POST_INCREMENT) ||
           (op == UnaryExpression.PRE_DECREMENT) || (op == UnaryExpression.PRE_INCREMENT) )
      {
         genShortCutUnaryMutants(p, op);
      }
   }

   public void visit( BinaryExpression p ) throws ParseTreeException 
   {
      isPrePostEQ = false;
      super.visit(p);
      isPrePostEQ = true;
   }

   public void visit(AssignmentExpression p) throws ParseTreeException
   {
      //Expression lexpr = p.getLeft();
      // lexpr.accept( this );

      isPrePostEQ =  false;
      Expression rexp = p.getRight();
      rexp.accept( this );
      isPrePostEQ =  true;
   }

   void genShortCutUnaryMutants(UnaryExpression p, int op)
   {
      UnaryExpression mutant;
      if (isPrePostEQ)
      {  // Inside FOR stmts
         if ( (!(op == UnaryExpression.PRE_INCREMENT)) && (!(op == UnaryExpression.POST_INCREMENT)))
         {
            mutant = (UnaryExpression)(p.makeRecursiveCopy());
            mutant.setOperator(UnaryExpression.POST_INCREMENT);
            outputToFile(p, mutant);
         }
        
         if ( (!(op == UnaryExpression.PRE_DECREMENT)) && (!(op == UnaryExpression.POST_DECREMENT)))
         {
            mutant = (UnaryExpression)(p.makeRecursiveCopy());
            mutant.setOperator(UnaryExpression.POST_DECREMENT);
            outputToFile(p,mutant);
         }
      }  
      else
      {
         if (!(op == UnaryExpression.POST_DECREMENT))
         {
            mutant = (UnaryExpression)(p.makeRecursiveCopy());
            mutant.setOperator(UnaryExpression.POST_DECREMENT);
            outputToFile(p, mutant);
         }
         
         if (!(op == UnaryExpression.POST_INCREMENT))
         {
            mutant = (UnaryExpression)(p.makeRecursiveCopy());
            mutant.setOperator(UnaryExpression.POST_INCREMENT);
            outputToFile(p, mutant);
         }
         
         if (!(op == UnaryExpression.PRE_DECREMENT))
         {
            mutant = (UnaryExpression)(p.makeRecursiveCopy());
            mutant.setOperator(UnaryExpression.PRE_DECREMENT);
            outputToFile(p, mutant);
         }
      
         if (!(op == UnaryExpression.PRE_INCREMENT))
         {
            mutant = (UnaryExpression)(p.makeRecursiveCopy());
            mutant.setOperator(UnaryExpression.PRE_INCREMENT);
            outputToFile(p, mutant);
         }
      }
   }

   /**
    * Output AORS mutants to files
    * @param original
    * @param mutant
    */
   public void outputToFile(UnaryExpression original, UnaryExpression mutant)
   {
      if (comp_unit == null) 
    	 return;
      
      String f_name;
      num++;
      f_name = getSourceName("AORS");
      String mutant_dir = getMuantID("AORS");

      try 
      {
		 PrintWriter out = getPrintWriter(f_name);
		 AORS_Writer writer = new AORS_Writer(mutant_dir, out);
		 writer.setMutant(original, mutant);
         writer.setMethodSignature(currentMethodSignature);
		 comp_unit.accept( writer );
		 out.flush();  out.close();
      } catch ( IOException e ) {
		 System.err.println( "fails to create " + f_name );
      } catch ( ParseTreeException e ) {
		 System.err.println( "errors during printing " + f_name );
		 e.printStackTrace();
      }
   }

}
