////////////////////////////////////////////////////////////////////////////
// Module : AMC_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import mujava.op.util.MutantCodeWriter;
import openjava.ptree.*;
import java.io.*;

/**
 * <p>Output and log AMC mutants to files</p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */ 

public class AMC_Writer extends MutantCodeWriter
{
   ModifierList original;
   ModifierList mutant;

   public AMC_Writer( String file_name, PrintWriter out ) 
   {
      super(file_name, out);
   }

   /**
    * Set original source code and mutated code
    * @param original
    * @param mutant
    */
   public void setMutant(ModifierList original, ModifierList mutant)
   {
      this.original = original;
      this.mutant = mutant;
   }

   /**
    * Log mutated line
    */
   public void visit( ModifierList p )throws ParseTreeException
   {
      if (isSameObject(p, original))
      {
         super.visit(mutant);
         // -----------------------------------------------------------
         mutated_line = line_num;
         String log_str = p.toFlattenString()+ " => " + mutant.toFlattenString();
         writeLog(removeNewline(log_str));
         // -------------------------------------------------------------
      }
      else
      {
         super.visit(p);
      }
   }
}
