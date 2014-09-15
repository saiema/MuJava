////////////////////////////////////////////////////////////////////////////
// Module : SOR_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld.basic;

import mujava.op.util.TraditionalMutantCodeWriter;
import openjava.ptree.*;
import java.io.*;

/**
 * <p>Output and log SOR mutants to files </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class SOR_Writer extends TraditionalMutantCodeWriter
{
   BinaryExpression original;
   BinaryExpression mutant;

   public SOR_Writer( String file_name, PrintWriter out ) 
   {
      super(file_name, out);
   }

   /**
    * Set original source code and mutated code
    * @param exp1
    * @param exp2
    */
   public void setMutant(BinaryExpression exp1, BinaryExpression exp2)
   {
      original = exp1;
      mutant = exp2;
   }

   /**
    * Log mutated line
    */
   public void visit( BinaryExpression p ) throws ParseTreeException
   {
      if (isSameObject(p, original))
      {
         super.visit(mutant);
         // -----------------------------------------------------------
         mutated_line = line_num;
         String log_str = p.toFlattenString()+ "  =>  " +mutant.toFlattenString();
         writeLog(removeNewline(log_str));
         // -------------------------------------------------------------
      }
      else
      {
         super.visit(p);
      }
   }
}
