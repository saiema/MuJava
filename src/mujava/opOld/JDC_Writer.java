////////////////////////////////////////////////////////////////////////////
// Module : JDC_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.ptree.*;
import mujava.op.util.MutantCodeWriter;

/**
 * <p>Output and log JDC mutants to files</p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */ 

public class JDC_Writer extends MutantCodeWriter
{
   ConstructorDeclaration mutant = null;

   /**
    * Set mutated code
    * @param mutant
    */
   public void setMutant(ConstructorDeclaration mutant)
   {
      this.mutant = mutant;
   }

   public JDC_Writer( String file_name, PrintWriter out ) 
   {
	  super(file_name, out);
   }

   public void visit( ConstructorDeclaration p ) throws ParseTreeException
   {
      if (!(isSameObject(p, mutant)))
      {
	     super.visit(p);
      }
      else
      {
         mutated_line = line_num;
         //call JDC.class_name instread of super.class_name
	     String log_str = p.getModifiers().toString() + " "
	                 + JDC.class_name + "(" + p.getParameters().toString() + ")";
	     
	     writeLog(removeNewline(log_str) + " is deleted");

	     writeTab();
	     out.println("// " + log_str + " { ... } ");
	     line_num++;
      }
   }
}