////////////////////////////////////////////////////////////////////////////
// Module : AORS_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld.basic;

import mujava.op.util.TraditionalMutantCodeWriter;
import openjava.ptree.*;
import java.io.*;

/**
 * <p>Output and log AORS mutants to files </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class AORS_Writer extends TraditionalMutantCodeWriter
{
   UnaryExpression unary_original;
   UnaryExpression unary_mutant;

   public AORS_Writer( String file_name, PrintWriter out ) 
   {
      super(file_name,out);
   }

   /**
    * Set original source code and mutated code
    * @param exp1
    * @param exp2
    */
   public void setMutant(UnaryExpression exp1, UnaryExpression exp2)
   {
      unary_original = exp1;
      unary_mutant = exp2;
   }

   /**
    * Log mutated line
    */
   public void visit( UnaryExpression p ) throws ParseTreeException
   {
      if (isSameObject(p, unary_original))
      {
         super.visit(unary_mutant);
         // -----------------------------------------------------------
         mutated_line = line_num;
         String log_str = p.toString() + " => " + unary_mutant.toString();
         writeLog(removeNewline(log_str));
         // -------------------------------------------------------------
      }
      else
      {
         super.visit(p);
      }
   }
}
