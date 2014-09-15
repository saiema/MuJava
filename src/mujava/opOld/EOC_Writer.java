////////////////////////////////////////////////////////////////////////////
// Module : EOC_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.ptree.*;
import mujava.op.util.MutantCodeWriter;

/**
 * <p>Output and log EOC mutants to files</p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */ 

public class EOC_Writer extends MutantCodeWriter
{
   MethodCall original_methodcall = null;
   BinaryExpression original_expression = null;
   String mutant = null;

   /**
    * Set original source code and mutated code
    * @param original
    * @param mutant
    */
   public void setMutant(BinaryExpression original, String mutant)
   {
      this.mutant = mutant;
      this.original_expression = original;
   }

   /**
    * Set original method call
    * @param original
    */
   public void setMutant(MethodCall original)
   {
      this.original_methodcall = original;
   }

   public EOC_Writer( String file_name, PrintWriter out ) 
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
            if (expr instanceof Leaf  ||
                expr instanceof ArrayAccess  ||
                expr instanceof FieldAccess  ||
                expr instanceof MethodCall   ||
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

         out.print( " == " );
         // -------------------------------------------------------------
         mutated_line = line_num;
         out.print(mutant);
         writeLog(removeNewline(" .equal() =>  == "));
         // -------------------------------------------------------------
         ExpressionList args = p.getArguments();
         args.accept( this );

      } 
      else
      {
         super.visit(p);
      } 
   }

   public void visit( BinaryExpression p ) throws ParseTreeException
   {
      if (isSameObject(p, original_expression))
      {
         // -------------------------------------------------------------
         mutated_line = line_num;
         out.print(mutant);
         writeLog(removeNewline(original_expression.toString()+" => "+mutant));
         // -------------------------------------------------------------
      }
      else
      {
         Expression lexpr = p.getLeft();
         if (isOperatorNeededLeftPar( p.getOperator(), lexpr )) 
         {
	        writeParenthesis( lexpr );
         } 
         else 
         {
            lexpr.accept( this );
         }

         String operator = p.operatorString();
         out.print( " " + operator + " " );

         Expression rexpr = p.getRight();
         if (isOperatorNeededRightPar( p.getOperator(), rexpr )) 
         {
	        writeParenthesis( rexpr );
         } 
         else 
         {
            rexpr.accept( this );
         }
      }
   }
}
