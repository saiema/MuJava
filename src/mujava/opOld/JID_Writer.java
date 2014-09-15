////////////////////////////////////////////////////////////////////////////
// Module : JID_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.ptree.*;
import mujava.op.util.MutantCodeWriter;

/**
 * <p>Output and log JID mutants to files</p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */ 

public class JID_Writer extends MutantCodeWriter
{
   FieldDeclaration mutant = null;

   /**
    * Set mutated code
    * @param mutant
    */
   public void setMutant(FieldDeclaration mutant)
   {
      this.mutant = mutant;
   }

   public JID_Writer( String file_name, PrintWriter out ) 
   {
	  super(file_name, out);
   }

   public void visit( FieldDeclaration p ) throws ParseTreeException
   {
      writeTab();
      
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

	  if (isSameObject(p, mutant))
	  {
         mutated_line = line_num;
         String temp = mutant.getModifiers().toString()
		               + " " + mutant.getTypeSpecifier().toString()
		               + " " + mutant.getVariable();
         String mutant_str = temp + p.getInitializer().toString();
	     String log_str = temp+";";
	     writeLog(removeNewline(mutant_str + " => " + log_str));

         // -------------------------------------------------------------
  	  }
	  else
	  {
         /*"=" VariableInitializer*/
         VariableInitializer initializer = p.getInitializer();
         if (initializer != null) 
         {
            out.print(" = ");
            initializer.accept(this);
         }
	  }
      /*";"*/
      out.print(";");

      out.println(); line_num++;
   }
}
