////////////////////////////////////////////////////////////////////////////
// Module : JID.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.mop.*;
import openjava.ptree.*;

/**
 * <p>Generate JID (Java-specific member variable initialization deletion) --
 *    remove initialization of each member variable
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class JID extends mujava.op.util.Mutator
{
   public JID(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit)
   {
	  super( file_env,comp_unit );
   }

   /**
    * If an instance variable is not final, delete its initialization
    */
   public void visit( FieldDeclaration p ) throws ParseTreeException 
   {
      VariableInitializer initializer = p.getInitializer();
      if (p.getModifiers().contains(ModifierList.FINAL)) 
    	 return;
      
      if (initializer != null) 
    	 outputToFile(p);
   }

   /**
    * Output JID mutants to files
    * @param original
    */
   public void outputToFile(FieldDeclaration original)
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
	     JID_Writer writer = new JID_Writer( mutant_dir, out );
	     writer.setMutant(original);
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
