////////////////////////////////////////////////////////////////////////////
// Module : JSD_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.ptree.*;
import mujava.op.util.MutantCodeWriter;

/**
 * <p>Output and log JSD mutants to files</p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */ 

public class JSD_Writer extends MutantCodeWriter
{
   FieldDeclaration mutant = null;
   boolean isMutantTarget = false;

   /**
    * Set mutated code
    * @param f
    */
   public void setMutant(FieldDeclaration f)
   {
      mutant = f;
      isMutantTarget = false;
   }

   public JSD_Writer( String file_name, PrintWriter out ) 
   {
	  super(file_name,out);
   }

   public void visit( FieldDeclaration p ) throws ParseTreeException
   {
      if (isSameObject(p, mutant))
      {
         isMutantTarget = true;
         super.visit(p);
         isMutantTarget = false;
      }
      else
      {
         super.visit(p);
      }
   }

   public void visit( ModifierList p ) throws ParseTreeException
   {
      if (isMutantTarget)
      {
         ModifierList temp = (ModifierList)p.makeCopy();

         int mod = temp.getRegular();
         mod &= (~ModifierList.STATIC);
         temp.setRegular(mod);
         //temp.delete(ModifierList.STATIC);
         super.visit(temp);
         // -------------------------------------------------------------
         mutated_line = line_num;
         writeLog(removeNewline("static is deleted"));
         // -------------------------------------------------------------
      }
      else
      {
         super.visit(p);
      }
   }
}
