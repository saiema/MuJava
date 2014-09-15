////////////////////////////////////////////////////////////////////////////
// Module : PCC_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.ptree.*;
import mujava.op.util.MutantCodeWriter;

/**
 * <p>Output and log PCC mutants to files</p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */ 

public class PCC_Writer extends MutantCodeWriter
{
   CastExpression original = null;
   String type = "";

   /**
    * Set original source code and mutated code
    * @param original
    * @param mutated_type
    */
   public void setMutant(CastExpression original, String mutated_type)
   {
      this.original = original;
      this.type = mutated_type;
   }

   public PCC_Writer( String file_name, PrintWriter out ) 
   {
	  super(file_name, out);
   }

   public void visit( CastExpression p ) throws ParseTreeException
   {
      if (isSameObject(p, original))
      {
         out.print( "(" + type + ") ");
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
         writeLog(removeNewline("(" + p.getTypeSpecifier().getName() + ")  =>  (" + type + ")"));
         // -------------------------------------------------------------
      }
      else
      {
         super.visit(p);
      }
   }
}
