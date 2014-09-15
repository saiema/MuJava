////////////////////////////////////////////////////////////////////////////
// Module : COI.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld.basic;

import openjava.mop.*;
import openjava.ptree.*;
import java.io.*;

/**
 * <p>Generate COI (Conditional Operator Insertion) mutants --
 *    insert logical operators (and-&&, or-||, 
 *    and with no conditional evaluation-&, 
 *    or with no conditional evaluation-|, not equivalent-^)
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class COI extends MethodLevelMutator
{
   public COI(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit)
   {
      super( file_env, comp_unit );
   }

   public void visit(UnaryExpression p)
   {
      // NO OP
   }

   public void visit( Variable p ) throws ParseTreeException
   {
      if (getType(p) == OJSystem.BOOLEAN)
      {
         outputToFile(p);
      }
   }

   public void visit( FieldAccess p ) throws ParseTreeException
   {
      if (getType(p) == OJSystem.BOOLEAN)
      {
         outputToFile(p);
      }
   }

   public void visit( BinaryExpression p ) throws ParseTreeException
   {
      Expression left = p.getLeft();
      left.accept(this);
      Expression right = p.getRight();
      right.accept(this);

      if (getType(p) == OJSystem.BOOLEAN)
      {
         outputToFile(p);
      }
   }

   /**
    * Output COI mutants to files
    * @param original
    */
   public void outputToFile(BinaryExpression original)
   {
      if (comp_unit == null) 
    	 return;

      String f_name;
      num++;
      f_name = getSourceName("COI");
      String mutant_dir = getMuantID("COI");

      try 
      {
		 PrintWriter out = getPrintWriter(f_name);
		 COI_Writer writer = new COI_Writer(mutant_dir,out);
		 writer.setMutant(original);
         writer.setMethodSignature(currentMethodSignature);
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

   /**
    * Output COI mutants to files
    * @param original
    */
   public void outputToFile(Variable original)
   {
      if (comp_unit==null) return;

      String f_name;
      num++;
      f_name = getSourceName("COI");
      String mutant_dir = getMuantID("COI");

      try 
      {
		 PrintWriter out = getPrintWriter(f_name);
		 COI_Writer writer = new COI_Writer(mutant_dir,out);
		 writer.setMutant(original);
         writer.setMethodSignature(currentMethodSignature);
		 comp_unit.accept( writer );
		 out.flush();  
		 out.close();
      } catch ( IOException e ) {
		 System.err.println( "fails to create " + f_name );
      } catch ( ParseTreeException e ) {
		 System.err.println( "errors during printing " + f_name );
		 e.printStackTrace();
      }
   }

   /**
    * Output COI mutants to files
    * @param original
    */
   public void outputToFile(FieldAccess original)
   {
      if (comp_unit == null) 
    	 return;

      String f_name;
      num++;
      f_name = getSourceName("COI");
      String mutant_dir = getMuantID("COI");

      try 
      {
		 PrintWriter out = getPrintWriter(f_name);
		 COI_Writer writer = new COI_Writer(mutant_dir,out);
		 writer.setMutant(original);
         writer.setMethodSignature(currentMethodSignature);
		 comp_unit.accept( writer );
		 out.flush();  
		 out.close();
      } catch ( IOException e ) {
		 System.err.println( "fails to create " + f_name );
      } catch ( ParseTreeException e ) {
		 System.err.println( "errors during printing " + f_name );
		 e.printStackTrace();
      }
   }
}
