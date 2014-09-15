////////////////////////////////////////////////////////////////////////////
// Module : ISI_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.ptree.*;
import mujava.op.util.MutantCodeWriter;


/**
 * <p>Output and log ISI mutants to files</p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */ 

public class ISI_Writer  extends MutantCodeWriter
{
   Variable target = null;
   MethodCall method_target = null;

   /**
    * Set mutated code for an instance variable
    * @param f
    */
   public void setMutant(Variable f)
   {
      target = f;
   }

   /**
    * Set mutated code for a method call
    * @param f
    */
   public void setMutant(MethodCall f)
   {
      method_target = f;
   }

   public ISI_Writer( String file_name, PrintWriter out ) 
   {
	  super(file_name,out);
   }

   /**
    * Log mutated line (variables)
    */
   public void visit( Variable p ) throws ParseTreeException
   {
      if (isSameObject(p, target))
      {
         out.print("super."+p.toString());
         // -------------------------------------------------------------
         mutated_line = line_num;
         writeLog(removeNewline(p.toString()+"  -->  super." + p.toString()));
         // -------------------------------------------------------------
      } 
      else
      {
         super.visit(p);
      }
   }

   /**
    * Log mutated line (method calls)
    */
   public void visit( MethodCall p ) throws ParseTreeException
   {
      if (isSameObject(p, method_target))
      {
        out.print("super."+p.toString());
         // -------------------------------------------------------------
        mutated_line = line_num;
        writeLog(removeNewline(p.toString()+"  -->  super." + p.toString()));
        // -------------------------------------------------------------
      }
      else
      {
         super.visit(p);
      }
   }
}
