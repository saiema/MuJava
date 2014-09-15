////////////////////////////////////////////////////////////////////////////
// Module : JSI.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import java.util.Vector;
import openjava.mop.*;
import openjava.ptree.*;

/**
 * <p>Generate JSI (Java-specific static modifier insertion) --
 *    add the <i>static</i> modifier to instance variables
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class JSI extends mujava.op.util.Mutator
{
   Vector nonStaticFields;
   boolean isField;

   public JSI(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit)
   {
	  super( file_env,comp_unit );
      nonStaticFields = new Vector();
      isField = false;
   }

   public void visit( FieldDeclaration p ) throws ParseTreeException 
   {
      if (!(p.getModifiers().contains(ModifierList.STATIC)))
      {
         // nonStaticFields.add(p);
         outputToFile(p);
      }
   }

   /**
    * Output JSI mutants to files
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
	 	 JSI_Writer writer = new JSI_Writer( mutant_dir, out );
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
