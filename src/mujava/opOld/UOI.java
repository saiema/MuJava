////////////////////////////////////////////////////////////////////////////
// Module : UOI.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import openjava.mop.*;
import openjava.ptree.*;

/**
 * <p>Generate UOI (Unary operator insertion) mutants --
 *    inserts each unary operator (arithmetic +, arithmetic -, conditional !,
 *    logical ~) before each expression of the correct type    
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class UOI extends mujava.op.util.Mutator
{
   public UOI(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit)
   {
      super( file_env, comp_unit );
   }
}
