////////////////////////////////////////////////////////////////////////////
// Module : AODS_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op.basic;

import mujava.api.Mutation;
import mujava.op.util.MutantCodeWriter;
import openjava.ptree.*;

import java.io.*;

/**
 * <p>Output and log AODS mutants to files </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class AODS_Writer extends MutantCodeWriter {

   public AODS_Writer( String file_name, PrintWriter out, Mutation mi) {
      super(file_name, out, mi);
   }

   public void visit( UnaryExpression p ) throws ParseTreeException {
      if (isSameObject(p, (UnaryExpression) this.mi.getOriginal())) {
         super.visit((Expression) this.mi.getMutant());
         // -----------------------------------------------------------
         mutated_line = line_num;
         // -------------------------------------------------------------
      } 
      else {
         super.visit(p);
      }
   }
}
