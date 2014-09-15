////////////////////////////////////////////////////////////////////////////
// Module : UOI_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import mujava.op.util.MutantCodeWriter;
import openjava.ptree.*;
import java.io.*;

/**
 * <p>Output and log UOI mutants to files</p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */ 

public class UOI_Writer extends MutantCodeWriter
{
   Variable original;
   MethodCall original_call;
   int mutant_op;

   private static final String unary_op_string[] = 
                            { "++", "--", "++", "--", "~", "!", "+", "-" };

   public UOI_Writer( String file_name, PrintWriter out ) 
   {
      super(file_name, out);
   }

   /**
    * Set original source code and mutated code (variable)
    * @param exp
    * @param op
    */
   public void setMutant(Variable exp, int op)
   {
      original = exp;
      mutant_op = op;
   }

   /**
    * Set original source code and mutated code (method call)
    * @param exp
    * @param op
    */
   public void setMutant(MethodCall exp, int op)
   {
      original_call = exp;
      mutant_op = op;
   }
 
   /**
    * Log mutated line
    */
   public void visit( Variable p ) throws ParseTreeException
   {
      if (isSameObject(p, original))
      {
         String mutated_str = "";
         if (mutant_op == UnaryExpression.PRE_DECREMENT || mutant_op == UnaryExpression.PRE_INCREMENT)
         {
            mutated_str = "(" + unary_op_string[mutant_op] + p.toString() + ")";
         }
         else
         {
            mutated_str = "(" + p.toString() + unary_op_string[mutant_op] + ")";
         }
         out.print(mutated_str);
         // -----------------------------------------------------------
         mutated_line = line_num;
         String log_str = p.toString()+ " =>  " + mutated_str;
         writeLog(removeNewline(log_str));
         // -------------------------------------------------------------
      }
      else
      {
         super.visit(p);
      }
   }

   /**
    * Log mutated line
    */
   public void visit( MethodCall p ) throws ParseTreeException
   {
      if (isSameObject(p, original_call))
      {
         String mutated_str = "(" + unary_op_string[mutant_op] + p.toString() + ")";
         out.print(mutated_str);
         // -----------------------------------------------------------
         mutated_line = line_num;
         String log_str = p.toString() + " =>  " + mutated_str;
         writeLog(removeNewline(log_str));
         // -------------------------------------------------------------
      }
      else
      {
         super.visit(p);
      }
   }
}
