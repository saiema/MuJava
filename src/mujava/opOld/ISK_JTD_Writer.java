////////////////////////////////////////////////////////////////////////////
// Module : ISK_JTD_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.ptree.*;
import mujava.op.util.MutantCodeWriter;

/**
 * <p>Output and log ISD, ISI, and JTD mutants to files</p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */ 

public class ISK_JTD_Writer extends MutantCodeWriter
{
   FieldAccess mutant_field = null;
   FieldAccess original_field = null;

   /**
    * Set original source code and mutated code
    * @param original
    * @param mutant
    */
   public void setMutant(FieldAccess original, FieldAccess mutant)
   {
      this.original_field = original;
      this.mutant_field = mutant;
   }

   MethodCall mutant_method = null;
   MethodCall original_method = null;

   /**
    * Set original source code and mutated code
    * @param original
    * @param mutant
    */
   public void setMutant(MethodCall original, MethodCall mutant)
   {
      this.original_method = original;
      this.mutant_method = mutant;
   }

   public ISK_JTD_Writer( String file_name, PrintWriter out ) 
   {
	  super(file_name,out);
   }

   /**
    * Log mutated line
    */
   public void visit( FieldAccess p ) throws ParseTreeException
   {
      if (!(isSameObject(p, original_field)))
      {
		 super.visit(p);
      }
      else
      {
		 // -----------------------------------------------------------
  		 mutated_line = line_num;
		 String log_str = original_field.toString()+ " => " + mutant_field.toString();
		 writeLog(removeNewline(log_str));

		 // -------------------------------------------------------------
		 visit(mutant_field);
      }
   }

   /**
    * Log mutated line
    */
   public void visit( MethodCall p ) throws ParseTreeException
   {
      if (!(isSameObject(p, original_method)))
      {
		 super.visit(p);
      }
      else
      {
		 // --------------------------------------------------------------
		 mutated_line = line_num;
		 String log_str = original_method.toString()+ " => " + mutant_method.toString();
		 writeLog(removeNewline(log_str));
		 // --------------------------------------------------------------
		 visit(mutant_method);
      }
   }
}

