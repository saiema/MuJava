////////////////////////////////////////////////////////////////////////////
// Module : JSI_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.ptree.*;
import mujava.op.util.MutantCodeWriter;

/**
 * <p>Output and log JSI mutants to files</p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */ 

public class JSI_Writer extends MutantCodeWriter
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

   public JSI_Writer( String file_name, PrintWriter out ) 
   {
	  super(file_name, out);
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
    	 boolean empt = false;
         ModifierList temp = (ModifierList)p.makeRecursiveCopy();
         if (temp.isEmpty()) 
         {
        	empt = true;
         }
         temp.add(ModifierList.STATIC);
         super.visit(temp);
         
         if (empt) 
            out.print(" ");
         
         // -------------------------------------------------------------
         mutated_line = line_num;
         //out.print(mutated_modifier);
         writeLog(removeNewline("static is inserted"));
         // -------------------------------------------------------------
      }
      else
      {
         super.visit(p);
         //out.print( ModifierList.toString( p.getRegular() ) );
      }
   }
}
