////////////////////////////////////////////////////////////////////////////
// Module : IOP_Helper.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

/**
 * <p>Interface for generating IOP mutants</p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public interface IOP_Helper
{
   // 4 mod exists.
   // -------------------------------------------------------------------
   // 1: first_line            // 2: last_line
   // 3: just one line up      // 4: just one line down
   // -------------------------------------------------------------------
   public static int FIRST = 1;
   public static int LAST = 2;
   public static int UP = 3;
   public static int DOWN = 4;
}
