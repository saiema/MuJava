////////////////////////////////////////////////////////////////////////////
// Module : IPC.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.mop.*;
import openjava.ptree.*;

/**
 * <p>Generate IPC (Explicit call to parent's constructor deletion) mutants --
 *    delete <i>super</i> constructor calls, causing the default constructor
 *    of the parent class to be called 
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class IPC extends mujava.op.util.Mutator
{
   // ClassDeclaration my_class;
   public IPC(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit)
   {
	  super( file_env, comp_unit );
	  //my_class = cdecl;
   }

   public void visit( ConstructorInvocation p ) throws ParseTreeException
   {
      if (!p.isSelfInvocation())
      {
         if (p.getArguments().size() > 0)
         {
            outputToFile(p);
         }
      }
   }

   /**
    * Output IPC mutants to files
    * @param mutant
    */
   public void outputToFile(ConstructorInvocation mutant)
   {
      if (comp_unit == null) 
    	 return;

      String f_name;
      num++;
      f_name = getSourceName(this);
      String mutant_dir = getMuantID();

      try 
      {
	     PrintWriter out = getPrintWriter(f_name);
	     IPC_Writer writer = new IPC_Writer( mutant_dir, out );
	     writer.setMutant(mutant);
	     comp_unit.accept( writer );
	     out.flush();  
	     out.close();
      } catch ( IOException e ) 
      {
	     System.err.println( "fails to create " + f_name );
      } catch ( ParseTreeException e ) 
      {
	     System.err.println( "errors during printing " + f_name );
	     e.printStackTrace();
      }
   }
}
