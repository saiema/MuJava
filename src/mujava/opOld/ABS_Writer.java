////////////////////////////////////////////////////////////////////////////
// Module : ABS_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import mujava.op.util.MutantCodeWriter;
import openjava.ptree.*;
import java.io.*;

/**
 * <p>Output and log ABS mutants to files </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */ 

public class ABS_Writer extends MutantCodeWriter
{
   final int ZERO = 0;
   final int NEGATIVE = 1;

   BinaryExpression original_exp;
   Variable original_var;
   MethodCall original_call;
   int flag;

   public ABS_Writer( String file_name, PrintWriter out ) 
   {
      super(file_name, out);
   }

   /**
    * Set a zero flag to a given method call
    * @param exp
    */
   public void setZeroFlag(MethodCall exp)
   {
      flag = ZERO;
      original_call = exp;
   }

   /**
    * Set a negative flag to a given method call
    * @param exp
    */
   public void setNegativeFlag(MethodCall exp)
   {
      flag = NEGATIVE;
      original_call = exp;
   }

   /**
    * Set a zero flag to a given expression
    * @param exp
    */
   public void setZeroFlag(BinaryExpression exp)
   {
      flag = ZERO;
      original_exp = exp;
   }

   /**
    * Set a negative flag to a given expression
    * @param exp
    */
   public void setNegativeFlag(BinaryExpression exp)
   {
      flag = NEGATIVE;
      original_exp = exp;
   }

   /**
    * Set a zero flag to a given instance variable
    * @param var
    */
   public void setZeroFlag(Variable var)
   {
      flag = ZERO;
      original_var = var;
   }

   /**
    * Set a negative flag to a given instance variable
    * @param var
    */
   public void setNegativeFlag(Variable var)
   {
      flag = NEGATIVE;
      original_var = var;
   }

   /**
    * Log mutated line
    */
   public void visit( BinaryExpression p ) throws ParseTreeException
   {
      if (isSameObject(p, original_exp))
      {
         if (flag == ZERO)
         {
            out.print(0);
            // -----------------------------------------------------------
            mutated_line = line_num;
            String log_str = p.toString()+ "  =>  0 ";
            writeLog(removeNewline(log_str));
            // -------------------------------------------------------------
         }
         else if (flag == NEGATIVE)
         {
            out.print("(-("+p.toString()+"))");
             // -----------------------------------------------------------
            mutated_line = line_num;
            String log_str = p.toString()+ "  =>  -("+p.toString()+")";
            writeLog(removeNewline(log_str));
            // -------------------------------------------------------------
         }
      }
      else
      {
         super.visit(p);
      }
   }

   /**
    * Log mutated line
    */
   public void visit( MethodCall p ) throws ParseTreeException
   {
      if (isSameObject(p, original_call))
      {
         if (flag == ZERO)
         {
            out.print(0);
            // -----------------------------------------------------------
            mutated_line = line_num;
            String log_str = p.toString()+ "  =>  0 ";
            writeLog(removeNewline(log_str));
            // -------------------------------------------------------------
         }
         else if (flag == NEGATIVE)
         {
            out.print("(-("+p.toString()+"))");
            // -----------------------------------------------------------
            mutated_line = line_num;
            String log_str = p.toString()+ "  =>  -("+p.toString()+")";
            writeLog(removeNewline(log_str));
            // -------------------------------------------------------------
         }
      }
      else
      {
         super.visit(p);
      }
   }

   /**
    * Log mutated line
    */
   public void visit( Variable p ) throws ParseTreeException
   {
      if (isSameObject(p, original_var))
      {
         if (flag == ZERO)
         {
            out.print(0);
            // -----------------------------------------------------------
            mutated_line = line_num;
            String log_str = p.toString()+ "  =>  0 ";
            writeLog(removeNewline(log_str));
            // -------------------------------------------------------------
         }
         else if (flag == NEGATIVE)
         {
            out.print("(-"+p.toString()+")");
            // -----------------------------------------------------------
            mutated_line = line_num;
            String log_str = p.toString()+ "  =>  -"+p.toString();
            writeLog(removeNewline(log_str));
            // -------------------------------------------------------------
         }
      }
      else
      {
         super.visit(p);
      }
   }
}
