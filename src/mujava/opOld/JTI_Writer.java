////////////////////////////////////////////////////////////////////////////
// Module : JTI_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.ptree.*;
import mujava.op.util.MutantCodeWriter;


/**
 * <p>Output and log JTI mutants to files</p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */ 

public class JTI_Writer  extends MutantCodeWriter
{
   Variable target = null;

   /**
    * Set mutated code
    * @param f
    */
   public void setMutant(Variable f)
   {
      target = f;
   }

   public JTI_Writer( String file_name, PrintWriter out ) 
   {
	  super(file_name, out);
   }

   public void visit( Variable p ) throws ParseTreeException
   {
      if (isSameObject(p, target))
      {
         out.print("this."+p.toString());
         // -------------------------------------------------------------
         mutated_line = line_num;
         writeLog(removeNewline(p.toString()+"  -->  this." + p.toString()));
         // -------------------------------------------------------------
      }
      else
      {
         super.visit(p);
      }
   }
}
