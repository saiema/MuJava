////////////////////////////////////////////////////////////////////////////
// Module : PPD_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.ptree.*;
import mujava.op.util.MutantCodeWriter;

/**
 * <p>Output and log PPD mutants to files</p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */ 

public class PPD_Writer extends MutantCodeWriter
{
   Parameter original = null;
   Parameter mutant = null;
   MethodDeclaration targetMethod = null;
   StatementList targetStmts  = null;

   /**
    * Set original source code and mutated code
    * @param m -- method to be mutated
    * @param original 
    * @param mutant
    */
   public void setMutant(MethodDeclaration m, Parameter original, Parameter mutant)
   {
      this.targetMethod = m;
      this.original = original;
      this.mutant = mutant;
      this.targetStmts = m.getBody();
   }

   public PPD_Writer( String file_name, PrintWriter out ) 
   {
	  super(file_name,out);
   }

   /**
    * Log mutated line
    */
   public void visit( StatementList p ) throws ParseTreeException
   {
      if (isSameObject(p, targetStmts))
      {
         mutated_line = line_num;
         writeTab();
         out.println(mutant.toString() + " = (" + mutant.getTypeSpecifier().getName()
                        + ")_" + original.getVariable() + ";");
         line_num++;
         writeLog(removeNewline(original.toString() + " => " + mutant.toString()));
      }
      writeList( p );
   }

   public void visit( Parameter p ) throws ParseTreeException
   {
      if (isSameObject(p, original))
      {
	     // -------------------------------------------------------------
   	     // mutated_line = line_num;
         Parameter temp = (Parameter)p.makeRecursiveCopy();
         temp.setVariable("_" + p.getVariable());
	     visit(temp);
   	     //writeLog(removeNewline(original.toString()+" => "+mutant.toString()));
	     // -------------------------------------------------------------
      }
      else
      {
         super.visit(p);
      }
   }
}

