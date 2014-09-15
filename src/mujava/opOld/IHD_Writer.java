////////////////////////////////////////////////////////////////////////////
// Module : IHD_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.ptree.*;
import mujava.op.util.MutantCodeWriter;


/**
 * <p>Output and log IHD mutants to files </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */ 

public class IHD_Writer extends MutantCodeWriter
{
   FieldDeclaration original = null;
   FieldDeclaration mutant = null;

   /**
    * Set original source code and mutated code
    * @param original
    * @param mutant
    */
   public void setMutant(FieldDeclaration original, FieldDeclaration mutant)
   {
      this.original = original;
      this.mutant = mutant;
   }

   public IHD_Writer( String file_name, PrintWriter out ) 
   {
      super(file_name, out);
   }

   public void visit( FieldDeclaration p ) throws ParseTreeException
   {
      if (!(isSameObject(p, original)))
      {
         super.visit(p);
      } 
      else
      {
         writeTab();
		 out.print("// ");

         /*ModifierList*/
         ModifierList modifs = p.getModifiers();
         if (modifs != null) 
         {
            modifs.accept( this );
            if (! modifs.isEmptyAsRegular())  
               out.print( " " );
         }

         /*TypeName*/
         TypeName ts = p.getTypeSpecifier();
         ts.accept(this);

         out.print(" ");

         /*Variable*/
         String variable = p.getVariable();
         out.print(variable);
 
         /*"=" VariableInitializer*/
         VariableInitializer initializer = p.getInitializer();
         if (initializer != null) 
         {
            out.print(" = ");
            initializer.accept(this);
         }
         /*";"*/
         out.print(";");

         // -------------------------
	     mutated_line = line_num;
	     writeLog(removeNewline(mutant.toString())+" is deleted.");
	     // -------------------------

         out.println(); line_num++;
      }
   }
}
