////////////////////////////////////////////////////////////////////////////
// Module : JDC.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.mop.*;
import openjava.ptree.*;
import mujava.op.util.DeclAnalyzer;

/**
 * <p>Generate JDC (Java-supported default constructor creation) --
 *    delete each declaration of default constructor (with no 
 *    parameter)
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @update by Nan Li May 2012 fix a bug of JDC
 * @The bug is: when a constructor is deleted, the mutanted program shows // JDC_1(){...}; it supposes to show //class_name(){...}
 * @in other words, the mutanted progam shows the name of the mutant (JDC_1) instead of the name of the constructor (like VendingMachine)
 * @version 1.0
  */ 

public class JDC extends DeclAnalyzer
{
	// the class_name
   public static String class_name = null;
   public void translateDefinition(CompilationUnit comp_unit) throws openjava.mop.MOPException
   {
	  try
	  {
	     OJConstructor[] cons = getDeclaredConstructors();
	     OJConstructor base_const = getDeclaredConstructor(null);

         if (cons == null) 
        	return;

         if (cons.length == 1 && base_const != null)
         {
            StatementList stmts = base_const.getBody();
            if (!(stmts.isEmpty()))
            {
               ConstructorDeclaration original = base_const.getSourceCode();
               outputToFile(comp_unit, original);
            }
	     }
	  } catch(NoSuchMemberException e1)
	  {
   	    // default constructor does not exist
	    // No operation
	  } catch(Exception ex)
	  {
	     System.err.println("JDC : " + ex);
	  }
   }

   /**
    * Output JDC mutants to files
    * @param comp_unit
    * @param mutant
    */
   public void outputToFile(CompilationUnit comp_unit, ConstructorDeclaration mutant)
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
	     JDC_Writer writer = new JDC_Writer( mutant_dir, out );
	     
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

   public JDC( openjava.mop.Environment oj_param0, openjava.mop.OJClass oj_param1, openjava.ptree.ClassDeclaration oj_param2 )
   {
      super( oj_param0, oj_param1, oj_param2 );
      //initialize the class name
      class_name = oj_param2.getName();
   }

   public JDC( java.lang.Class oj_param0, openjava.mop.MetaInfo oj_param1 )
   {
      super( oj_param0, oj_param1 );
   }
}
