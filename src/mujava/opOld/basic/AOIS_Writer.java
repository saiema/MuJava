////////////////////////////////////////////////////////////////////////////
// Module : AOIS_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld.basic;

import mujava.op.util.TraditionalMutantCodeWriter;
import openjava.ptree.*;
import java.io.*;

/**
 * <p>Output and log AOIS mutants to files </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class AOIS_Writer extends TraditionalMutantCodeWriter
{
   Variable original_var;
   FieldAccess original_field;
   String mutant;

   public AOIS_Writer( String file_name, PrintWriter out ) 
   {
      super(file_name,out);
   }

   /**
    * Set original source code and mutated code
    * @param exp1
    * @param mutant
    */
   public void setMutant(Variable exp1, String mutant)
   {
      original_var = exp1; 
      this.mutant = mutant;
   }

   /**
    * Set original source code and mutated code
    * @param exp1
    * @param mutant
    */
   public void setMutant(FieldAccess exp1, String mutant)
   {
      original_field = exp1;
      this.mutant = mutant;
   }

   /**
    * Log mutated line
    */
   public void visit( Variable p ) throws ParseTreeException
   {
      if (isSameObject(p, original_var))
      {
         out.print(mutant);
         // -----------------------------------------------------------
         mutated_line = line_num;
         String log_str = p.toString() + " => " + mutant;
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
   public void visit( FieldAccess p ) throws ParseTreeException
   {
      if (isSameObject(p, original_field))
      {
         out.print(mutant);
         // -----------------------------------------------------------
         mutated_line = line_num;
         String log_str = p.toString() + " => " + mutant;
         writeLog(removeNewline(log_str));
         // -------------------------------------------------------------
      }
      else
      {
         super.visit(p);
      }
   }
}
