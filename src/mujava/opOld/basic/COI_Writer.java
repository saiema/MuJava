////////////////////////////////////////////////////////////////////////////
// Module : COI_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld.basic;

import mujava.op.util.TraditionalMutantCodeWriter;
import openjava.ptree.*;
import java.io.*;

/**
 * <p>Output and log COI mutants to files </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class COI_Writer extends TraditionalMutantCodeWriter
{
   BinaryExpression original_binary;
   Variable original_var;
   FieldAccess original_field;

   public COI_Writer( String file_name, PrintWriter out ) 
   {
      super(file_name, out);
   }

   /**
    * Set original source code
    * @param p
    */
   public void setMutant(BinaryExpression p)
   {
      original_binary = p;
   }

   /**
    * Set original source code
    * @param p
    */
   public void setMutant(Variable p)
   {
      original_var = p;
   }

   /**
    * Set original source code
    * @param p
    */
   public void setMutant(FieldAccess p)
   {
      original_field = p;
   }

   /**
    * Log mutated line
    */
   public void visit( BinaryExpression p ) throws ParseTreeException
   {
      if (isSameObject(p, original_binary))
      {
         out.print("!("+p.toString()+")");
         // -----------------------------------------------------------
         mutated_line = line_num;
         String log_str = p.toFlattenString()+ "  =>  " +"!("+p.toString()+")";
         writeLog(removeNewline(log_str));
         // -------------------------------------------------------------
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
         out.print("!"+p.toString());
         // -----------------------------------------------------------
         mutated_line = line_num;
         String log_str = p.toFlattenString()+ "  =>  " +"!"+p.toString();
         writeLog(removeNewline(log_str));
         // -------------------------------------------------------------
      }
      else
      {
         super.visit(p);
      }
   }
  
   /**
    * Log mutated line
    */
   public void visit( FieldAccess p ) throws ParseTreeException
   {
      if (isSameObject(p, original_field))
      {
         out.print("!"+p.toString());
         // -----------------------------------------------------------
         mutated_line = line_num;
         String log_str = p.toFlattenString()+ "  =>  " +"!"+p.toString();
         writeLog(removeNewline(log_str));
         // -------------------------------------------------------------
      }
      else
      {
         super.visit(p);
      }
   }
}
