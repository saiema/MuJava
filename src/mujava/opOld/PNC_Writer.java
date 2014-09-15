////////////////////////////////////////////////////////////////////////////
// Module : PNC_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.ptree.*;
import mujava.op.util.MutantCodeWriter;

/**
 * <p>Output and log PNC mutants to files</p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */ 

public class PNC_Writer extends MutantCodeWriter
{
   AllocationExpression original = null;
   AllocationExpression mutant = null;

   /**
    * Set original source code and mutated code
    * @param original
    * @param mutant
    */
   public void setMutant(AllocationExpression original, AllocationExpression mutant)
   {
      this.original = original;
      this.mutant = mutant;
   }

   public PNC_Writer( String file_name, PrintWriter out ) 
   {
	  super(file_name, out);
   }

   public void visit( AllocationExpression p ) throws ParseTreeException
   {
      if (isSameObject(p, original))
      {
         // -------------------------------------------------------------
	     mutated_line = line_num;
	     visit(mutant);
	     writeLog(removeNewline(original.toString() + " => " + mutant.toString()));
         // -------------------------------------------------------------
      }
      else
      {
         Expression encloser = p.getEncloser();
         if (encloser != null) 
         {
            encloser.accept( this );
            out.print( " . " );
         }

         out.print( "new " );

         TypeName tn = p.getClassType();
         tn.accept( this );

         ExpressionList args = p.getArguments();
         writeArguments( args );

         MemberDeclarationList mdlst = p.getClassBody();
         if (mdlst != null) 
         {
            out.println( "{" );
            pushNest();  
            mdlst.accept( this );  
            popNest();
            writeTab();  
            out.print( "}" );
         }
      }
   }
}
