////////////////////////////////////////////////////////////////////////////
// Module : AODU.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld.basic;

import openjava.mop.*;
import openjava.ptree.*;
import java.io.*;

/**
 * <p>Generate AODU (Arithmetic Operator Deletion (Unary)) mutants --
 *    delete a unary operator (arithmetic -) before each variable or 
 *    expression
 * </p> 
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class AODU extends Arithmetic_OP
{
   boolean aor_flag = false;

   public AODU(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit)
   {
      super( file_env, comp_unit );
   }

   /**
    * Set AOR flag 
    * @param b
    */
   public void setAORflag(boolean b)
   {
      aor_flag = b;
   }

   public void visit( BinaryExpression p) throws ParseTreeException
   {
      // Examine equivalent
      if (aor_flag && isArithmeticType(p)) 
      {
         if ( (p.getOperator() == BinaryExpression.MINUS) || 
              (p.getOperator() == BinaryExpression.PLUS)  ||
              (p.getOperator() == BinaryExpression.MOD))
         {
            Expression e1 = p.getLeft();
            super.visit(e1);
         }
      }
   }

   public void visit( AssignmentExpression p ) throws ParseTreeException
   {
      // [ Example ]
      // int a=0;int b=2;int c=4;
      // Right Expression : a = b = -c;
      // Wrong Expression : a = -b = c;
      // Ignore left expression
      Expression rexp = p.getRight();
      rexp.accept( this );
   }

   public void visit( UnaryExpression p ) throws ParseTreeException
   {
      if (isArithmeticType(p))
      {
         int op = p.getOperator();
         if ( (op == UnaryExpression.MINUS) || (op == UnaryExpression.PLUS) )
         {
            outputToFile(p);
         } 
      }
   }

   /**
    * Write AODU mutants to files
    * @param original
    */
   public void outputToFile(UnaryExpression original)
   {
      if (comp_unit == null) 
    	 return;
      
      String f_name;
      num++;
      f_name = getSourceName("AODU");
      String mutant_dir = getMuantID("AODU");

      try 
      {
		 PrintWriter out = getPrintWriter(f_name);
		 AODU_Writer writer = new AODU_Writer(mutant_dir, out);
		 writer.setMutant(original);
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
