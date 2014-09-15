////////////////////////////////////////////////////////////////////////////
// Module : PCD_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.ptree.*;
import mujava.op.util.MutantCodeWriter;

/**
 * <p>Output and log PCD mutants to files</p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */ 

public class PCD_Writer extends MutantCodeWriter
{
   CastExpression original = null;

   /**
    * Set original source code 
    * @param original
    */
   public void setMutant(CastExpression original)
   {
      this.original = original;
   }

   public PCD_Writer( String file_name, PrintWriter out ) 
   {
	  super(file_name, out);
   }

   /**
    * Log mutated line
    */
   public void visit( CastExpression p ) throws ParseTreeException
   {
      if (isSameObject(p, original))
      {
         Expression expr = p.getExpression();
         if (expr instanceof AssignmentExpression
           || expr instanceof ConditionalExpression
           || expr instanceof BinaryExpression
           || expr instanceof InstanceofExpression
           || expr instanceof UnaryExpression)
         {
            writeParenthesis( expr );
         } 
         else 
         {
	        expr.accept( this );
         }
         // -------------------------------------------------------------
         mutated_line = line_num;
         writeLog(removeNewline(original.toString() + " => " + expr.toString()));
         // -------------------------------------------------------------
      }
      else
      {
         super.visit(p);
      }
   }
}
