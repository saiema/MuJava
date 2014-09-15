////////////////////////////////////////////////////////////////////////////
// Module : AODS.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld.basic;

import openjava.mop.*;
import openjava.ptree.*;
import java.io.*;

/**
 * <p>Generate AODS (Arithmetic Operator Deletion (Short-cut)) mutants --
 *    delete each occurrence of an increment operator (++) or a decrement 
 *    operator (--)  
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class AODS extends MethodLevelMutator
{
   public AODS(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit)
   {
      super( file_env, comp_unit );
   }

   public void visit( UnaryExpression p ) throws ParseTreeException
   {
      int op = p.getOperator();
      if ( (op == UnaryExpression.POST_DECREMENT) || (op == UnaryExpression.POST_INCREMENT) ||
           (op == UnaryExpression.PRE_DECREMENT) || (op == UnaryExpression.PRE_INCREMENT) )
      {
         outputToFile(p);
      } 
   }

   public void visit( ForStatement p ) throws ParseTreeException
   {
      // Do not consider conditions for "FOR STMT"
      StatementList stmts = p.getStatements();
      super.visit(stmts);
   }

   /**
    * Write AODS mutants to files
    * @param original
    */
   public void outputToFile(UnaryExpression original)
   {
      if (comp_unit == null) 
    	 return;
      
      String f_name;
      num++;
      f_name = getSourceName("AODS");
      String mutant_dir = getMuantID("AODS");

      try 
      {
		 PrintWriter out = getPrintWriter(f_name);
		 AODS_Writer writer = new AODS_Writer(mutant_dir, out);
		 writer.setMutant(original);
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
