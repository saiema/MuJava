////////////////////////////////////////////////////////////////////////////
// Module : Arithmetic_OP.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld.basic;

import openjava.mop.*;
import openjava.ptree.*;

/**
 * <p> </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class Arithmetic_OP extends MethodLevelMutator
{
   public Arithmetic_OP(FileEnvironment file_env, CompilationUnit comp_unit)
   {
      super( file_env, comp_unit );
   }

   /**
    * Determine whether a given expression is of arithmetic type
    * @param p
    * @return
    * @throws ParseTreeException
    */
   public boolean isArithmeticType(Expression p) throws ParseTreeException
   {
      OJClass type = getType(p);
      if ( type == OJSystem.INT || type == OJSystem.DOUBLE || type == OJSystem.FLOAT
         || type == OJSystem.LONG || type == OJSystem.SHORT 
         || type == OJSystem.CHAR || type == OJSystem.BYTE )
      {
         return true;
      }
      return false;
   }

   /**
    * Determine whether a given expression has a binary arithmetic operator
    * @param p
    * @return
    * @throws ParseTreeException
    */
   public boolean hasBinaryArithmeticOp( BinaryExpression p ) throws ParseTreeException 
   {
      int op_type = p.getOperator();
      if (  (op_type == BinaryExpression.TIMES) || (op_type == BinaryExpression.DIVIDE)
          || (op_type == BinaryExpression.MOD) || (op_type == BinaryExpression.PLUS)
          || (op_type == BinaryExpression.MINUS)) 
    	 return true;
      else 
    	 return false;
   }
}
