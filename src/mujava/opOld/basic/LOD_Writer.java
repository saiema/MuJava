////////////////////////////////////////////////////////////////////////////
// Module : LOD_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld.basic;

import mujava.op.util.TraditionalMutantCodeWriter;
import openjava.ptree.*;
import java.io.*;

/**
 * <p>Output and log LOD mutants to files </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class LOD_Writer extends TraditionalMutantCodeWriter
{
   UnaryExpression original;

   public LOD_Writer( String file_name, PrintWriter out ) 
   {
      super(file_name, out);
   }

   /**
    * Set original source code
    * @param exp1
    */
   public void setMutant(UnaryExpression exp1)
   {
      original = exp1;
   }

   /**
    * Log mutated line
    */
   public void visit( UnaryExpression p ) throws ParseTreeException
   {
      if (isSameObject(p, original))
      {
         super.visit(p.getExpression());
         // -----------------------------------------------------------
         mutated_line = line_num;
         String log_str = p.toString() + " => " + p.getExpression().toString();
         writeLog(removeNewline(log_str));
         // -------------------------------------------------------------
      }
      else
      {
         super.visit(p);
      }
   }
}
