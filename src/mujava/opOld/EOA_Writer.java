////////////////////////////////////////////////////////////////////////////
// Module : EOA_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.ptree.*;
import mujava.op.util.MutantCodeWriter;

/**
 * <p>Output and log EOA mutants to files</p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */ 

public class EOA_Writer extends MutantCodeWriter
{
   MethodCall original_methodcall = null;
   AssignmentExpression original = null;
   String mutant = null;

   ExpressionStatement original_stmt = null;

   /**
    * Set original source code and mutated code
    * @param original
    * @param mutant
    */
   public void setMutant(AssignmentExpression original, String mutant)
   {
      this.mutant = mutant;
      this.original = original;
   }

   /**
    * Set original source code and mutated code
    * @param original
    * @param mutant
    */
   public void setMutant(ExpressionStatement original, String mutant)
   {
      this.mutant = mutant;
      this.original_stmt = original;
   }

   /**
    * Set original method call
    * @param original
    */
   public void setMutant(MethodCall original)
   {
      this.original_methodcall = original;
   }

   public EOA_Writer( String file_name, PrintWriter out ) 
   {
	  super(file_name, out);
   }

   public void visit( MethodCall p ) throws ParseTreeException
   {
      if (isSameObject(p, original_methodcall))
      {
         Expression expr = p.getReferenceExpr();
         TypeName reftype = p.getReferenceType();

         if (expr != null) 
         {
            if (expr instanceof Leaf ||
                expr instanceof ArrayAccess ||
                expr instanceof FieldAccess ||
                expr instanceof MethodCall ||
                expr instanceof Variable) 
            {
               expr.accept( this );
            } 
            else 
            {
		       writeParenthesis( expr );
            }
         } 
         else if (reftype != null) 
         {
            reftype.accept( this );
	     }

         // -------------------------------------------------------------
         mutated_line = line_num;
         out.print(mutant);
         writeLog(removeNewline(p.toString()+" =>  " + p.toString().substring(0,p.toString().length()-".clone()".length())));
         // -------------------------------------------------------------

      }
      else
      {
         super.visit(p);
      }
   }
	 
   public void visit( ExpressionStatement p ) throws ParseTreeException
   {
      if (isSameObject(p, original_stmt))
      {
		 // -------------------------------------------------------------
		 writeTab(); out.println("try{");
		 line_num++;
		 mutated_line = line_num;
		 pushNest();
		 writeTab(); 
		 out.println(mutant+";");
		 popNest();
		 writeLog(removeNewline(original_stmt.toString() + " => " + mutant));
		 writeTab(); 
		 out.println("}catch(CloneNotSupportedException cnse){");
		 line_num++;
		 pushNest();
		 writeTab(); 
		 out.println("System.err.println(cnse);");
		 popNest();
		 line_num++;
		 writeTab(); 
		 out.println("}");
		 line_num++;
		 // -------------------------------------------------------------
      }
      else
      {
		 super.visit(p);
      }
   }

   public void visit( AssignmentExpression p ) throws ParseTreeException
   {
      if (isSameObject(p, original))
      {
		 // -------------------------------------------------------------
		 mutated_line = line_num;
		 out.print(mutant);
		 writeLog(removeNewline(original.toString() + " => " + mutant));
		 // -------------------------------------------------------------
      }
      else
      {
		 super.visit(p);
      }
   }

}
