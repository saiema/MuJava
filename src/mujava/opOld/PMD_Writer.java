////////////////////////////////////////////////////////////////////////////
// Module : PMD_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.ptree.*;
import mujava.op.util.MutantCodeWriter;

/**
 * <p>Output and log PMD mutants to files</p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */ 

public class PMD_Writer extends MutantCodeWriter
{
   FieldDeclaration original_field = null;
   FieldDeclaration mutant_field = null;

   VariableDeclaration original_var = null;
   VariableDeclaration mutant_var = null;

   /**
    * Set original source code and mutated code
    * @param original
    * @param mutant
    */
   public void setMutant(FieldDeclaration original, FieldDeclaration mutant)
   {
      this.original_field = original;
      this.mutant_field = mutant;
   }

   public void setMutant(VariableDeclaration original, VariableDeclaration mutant)
   {
      this.original_var = original;
      this.mutant_var = mutant;
   }

   public PMD_Writer( String file_name, PrintWriter out ) 
   {
	  super(file_name, out);
   }

   /**
    * Log mutated line
    */
   public void visit( FieldDeclaration p ) throws ParseTreeException
   {
      if (isSameObject(p, original_field))
      {
         // -------------------------------------------------------------
         mutated_line = line_num;
         visit(mutant_field);
         writeLog(removeNewline(original_field.toString() + " => " + mutant_field.toString()));
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
   public void visit( VariableDeclaration p ) throws ParseTreeException
   {
      if (isSameObject(p, original_var))
      {
		 // -------------------------------------------------------------
		 mutated_line = line_num;
		 visit(mutant_var);
		 writeLog(removeNewline(original_var.toString() + " => " + mutant_var.toString()));
         // -------------------------------------------------------------
      }
      else
      {
		 super.visit(p);
      }
   }
}
