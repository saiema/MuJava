////////////////////////////////////////////////////////////////////////////
// Module : AODU_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op.basic;

import mujava.api.Mutation;
import mujava.op.util.MutantCodeWriter;
import openjava.ptree.*;

import java.io.*;

/**
 * <p>Output and log AODU mutants to files </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class AODU_Writer extends MutantCodeWriter {

   public AODU_Writer( String file_name, PrintWriter out, Mutation mi)  {
      super(file_name, out, mi);
   }

   public void visit( UnaryExpression p ) throws ParseTreeException {
      if (isSameObject(p, (UnaryExpression) this.mi.getOriginal())) {
         super.visit((Expression) this.mi.getMutant());
         // -----------------------------------------------------------
         mutated_line = line_num;
         String log_str = ((UnaryExpression) this.mi.getOriginal()).toString() + " => " + ((Expression) this.mi.getMutant()).toString();
         writeLog(removeNewline(log_str));
         // -------------------------------------------------------------
      } 
      else {
         super.visit(p);
      }
   }
}
