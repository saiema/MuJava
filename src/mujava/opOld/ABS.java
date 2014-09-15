////////////////////////////////////////////////////////////////////////////
// Module : ABS.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import openjava.mop.*;
import openjava.ptree.*;

/**
 * <p>Generate ABS (Absolute Value Insertion) mutants --
 *    modify each arthmetic expression (and subexpression) by the function 
 *    <i>abs()</i>, <i>negAbs()</i>, and <i>faileOnZero()</i>. 
 * </p>
 * <p>-- <i>abs()</i> returns the absolute value of the expression<br/> 
 *    -- <i>negAbs()</i> returns the negative of the absolute value<br/> 
 *    -- <i>failOnZero()</i> tests whether the expression is zero 
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class ABS extends mujava.op.util.Mutator
{
   public ABS(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit)
   {
      super( file_env, comp_unit );
   }
}
