////////////////////////////////////////////////////////////////////////////
// Module : JSD.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import java.util.Vector;
import openjava.mop.*;
import openjava.ptree.*;
import mujava.MutationSystem;

/**
 * <p>Generate JSD (Java-specific static modifier deletion) --
 *    remove each instance of the <i>static</i> modifier 
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class JSD extends mujava.op.util.Mutator
{
   Vector staticFields;
   boolean isField;

   public JSD(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit)
   {
	  super( file_env,comp_unit );
      staticFields = new Vector();
      isField = false;
   }

   public void visit( ClassDeclaration p ) throws ParseTreeException 
   {
      if (p.getName().equals(MutationSystem.CLASS_NAME))
      {
         super.visit(p);
         for (int i=0; i<staticFields.size(); i++)
         {
            outputToFile((FieldDeclaration)(staticFields.get(i)));
         }
      }
   }

   public void visit( FieldDeclaration p ) throws ParseTreeException 
   {
      if (p.getModifiers().contains(ModifierList.STATIC))
      {
         staticFields.add(p);
      }
   }

   public void visit( MethodDeclaration p ) throws ParseTreeException 
   {
      if (p.getModifiers().contains(ModifierList.STATIC))
      {
         super.visit(p);
      }
   }

   public void visit( Variable p ) throws ParseTreeException 
   {
      for (int i=0; i<staticFields.size(); i++)
      {
         FieldDeclaration fd = (FieldDeclaration)(staticFields.get(i));
         String name = fd.getName();
         if (name.equals(p.toString()))
         {
            staticFields.remove(fd);
            return;
         }    
      }
   }

   /**
    * Output JSD mutants to files
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
		 JSD_Writer writer = new JSD_Writer( mutant_dir,out );
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
