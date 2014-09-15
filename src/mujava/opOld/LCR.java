////////////////////////////////////////////////////////////////////////////
// Module : LCR.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import openjava.mop.*;
import openjava.ptree.*;

/**
 * <p>Generate LCR (Logical Operator Replacement) mutants -- 
 *    replaces each occurrence of each bitwise logical operator 
 *    (bitwise and - &, bitwise or - |, exclusive or - ^) by each of 
 *    the other operators
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class LCR extends mujava.op.util.Mutator 
{
   public LCR(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit)
   {
      super( file_env, comp_unit );
   }
}
