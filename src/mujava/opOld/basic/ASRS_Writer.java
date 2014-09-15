////////////////////////////////////////////////////////////////////////////
// Module : ASRS_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld.basic;

import mujava.op.util.TraditionalMutantCodeWriter;
import openjava.ptree.*;
import java.io.*;

/**
 * <p>Output and log ASRS mutants to files </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class ASRS_Writer extends TraditionalMutantCodeWriter
{
   AssignmentExpression assign_original;
   AssignmentExpression assign_mutant;

   public ASRS_Writer( String file_name, PrintWriter out ) 
   {
      super(file_name,out);
   }

   /**
    * Set original source code and mutated code
    * @param exp1
    * @param exp2
    */
   public void setMutant(AssignmentExpression exp1, AssignmentExpression exp2)
   {
      assign_original = exp1;
      assign_mutant = exp2;
   }

   /**
    * Log mutated line
    */
   public void visit( AssignmentExpression p ) throws ParseTreeException
   {
      if (isSameObject(p, assign_original))
      {
         super.visit(assign_mutant);
         // -----------------------------------------------------------
         mutated_line = line_num;
         String log_str = p.toString() + " => " + assign_mutant.toString();
         writeLog(removeNewline(log_str));
         // -------------------------------------------------------------
      }
      else
      {
         super.visit(p);
      }
   }
}
