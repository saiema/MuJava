////////////////////////////////////////////////////////////////////////////
// Module : IPC_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.ptree.*;
import mujava.op.util.MutantCodeWriter;

/**
 * <p>Output and log IPC mutants to files</p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */ 

public class IPC_Writer extends MutantCodeWriter
{
   ConstructorInvocation mutant = null;

   /**
    * Set mutated code
    * @param mutant
    */
   public void setMutant(ConstructorInvocation mutant)
   {
      this.mutant = mutant;
   }

   public IPC_Writer( String file_name, PrintWriter out ) 
   {
	  super(file_name, out);
   }

   /**
    * Write and log mutants to files
    */
   public void visit( ConstructorInvocation p ) throws ParseTreeException
   {
      if (isSameObject(p, mutant))
      {
	     mutated_line = line_num;
         writeTab();
	     out.println("// " + p.toString());
	     line_num++;
         writeLog(removeNewline(p.toString()+" is deleted"));
      }
      else
      {
         writeTab();
         if (p.isSelfInvocation()) 
         {
            out.print( "this" );
         } 
         else 
         {
            Expression enclosing = p.getEnclosing();
            if (enclosing != null) 
            {
               enclosing.accept( this );
               out.print( " . " );
            }
            out.print( "super" );
         }

         ExpressionList exprs = p.getArguments();
         writeArguments( exprs );

	     out.print( ";" );
         out.println(); 
         line_num++;
      }
   }
}
