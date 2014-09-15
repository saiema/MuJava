////////////////////////////////////////////////////////////////////////////
// Module : PCI_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.ptree.*;
import mujava.op.util.MutantCodeWriter;

/**
 * <p>Output and log PCI mutants to files</p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */ 

public class PCI_Writer extends MutantCodeWriter
{
   Variable original = null;
   String type_name = null;

   /**
    * Set original source code and mutated code
    * @param original
    * @param name
    */
   public void setMutant(Variable original, String name)
   {
      this.original = original;
      this.type_name = name;
   }

   public PCI_Writer( String file_name, PrintWriter out ) 
   {
	  super(file_name,out);
   }

   public void visit( Variable p ) throws ParseTreeException
   {
      if (isSameObject(p, original))
      {
         String str = "((" + type_name + ")" + p.toString() + ")";
         out.print(str);
         // -------------------------------------------------------------
         mutated_line = line_num;
         writeLog(removeNewline(p.toString() + " => " + str));
         // -------------------------------------------------------------
      }
      else
      {
         super.visit(p);
      }
   }
}
