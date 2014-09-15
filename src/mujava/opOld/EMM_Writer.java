////////////////////////////////////////////////////////////////////////////
// Module : EMM_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.ptree.*;
import mujava.op.util.MutantCodeWriter;

/**
 * <p>Output and log EMM mutants to files</p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */ 

public class EMM_Writer extends MutantCodeWriter
{
   MethodCall original = null;
   MethodCall mutant = null;

   /***
    * Set original source code and mutated code
    * @param original
    * @param mutant
    */
   public void setMutant(MethodCall original, MethodCall mutant)
   {
      this.mutant = mutant;
      this.original = original;
   }

   public EMM_Writer( String file_name, PrintWriter out ) 
   {
	  super(file_name,out);
   }

   public void visit( MethodCall p ) throws ParseTreeException
   {
      if (isSameObject(p, original))
      {
		 // -------------------------------------------------------------
		 mutated_line = line_num;
		 visit(mutant);
		 writeLog(removeNewline(original.toString()+" => "+mutant.toString()));
		 // -------------------------------------------------------------
      } 
      else 
      {
         Expression expr = p.getReferenceExpr();
         TypeName reftype = p.getReferenceType();

         if (expr != null) 
         {
            if (expr instanceof Leaf
                || expr instanceof ArrayAccess
                || expr instanceof FieldAccess
                || expr instanceof MethodCall
                || expr instanceof Variable) {
               expr.accept( this );
            } 
            else 
            {
			   writeParenthesis( expr );
            }
            out.print( "." );
         } 
         else if (reftype != null) 
         {
   	        reftype.accept( this );
	        out.print( "." );
	     }

         String name = p.getName();
         out.print( name );

         ExpressionList args = p.getArguments();
	     writeArguments( args );
      }
   }
}
