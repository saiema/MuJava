////////////////////////////////////////////////////////////////////////////
// Module : PRV_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.ptree.*;
import mujava.op.util.MutantCodeWriter;

/**
 * <p>Output and log PRV mutants to files</p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class PRV_Writer extends MutantCodeWriter
{
   AssignmentExpression original = null;
   String mutant = null;

  /** 
   * Set original source code and mutated code
   * @param original
   * @param mutant
   */
   public void setMutant(AssignmentExpression original, String mutant)
   {
      this.original = original;
      this.mutant = mutant;
   }

   public PRV_Writer( String file_name, PrintWriter out ) 
   {
      super(file_name, out);
   }

   public void visit( AssignmentExpression p ) throws ParseTreeException
   {
      if (isSameObject(p, original))
      {
         Expression lexpr = p.getLeft();

         if (lexpr instanceof AssignmentExpression) 
         {
		 	writeParenthesis( lexpr );
         } 
         else 
         {
		    lexpr.accept( this );
         }

		 String operator = p.operatorString();
         out.print( " " + operator + " " );

         // -------------------------------------------------------------
	     mutated_line = line_num;
		 out.print(mutant);
		 writeLog(removeNewline(original.toString()+";  =>  "+ lexpr.toString() + " = " + mutant+";"));
		 // -------------------------------------------------------------

      }
      else
      {
         Expression lexpr = p.getLeft();

         if (lexpr instanceof AssignmentExpression) 
         {
	        writeParenthesis( lexpr );
         } 
         else 
         {
	        lexpr.accept( this );
         }

	     String operator = p.operatorString();
         out.print( " " + operator + " " );

         Expression rexp = p.getRight();
         rexp.accept( this );
      }
   }
}
