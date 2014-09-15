////////////////////////////////////////////////////////////////////////////
// Module : EOA.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.mop.*;
import openjava.ptree.*;

/**
 * <p>Generate EOA (Java-specific reference assignment and content assignment replacement) mutants --
 *    replace an assignment of a pointer reference with a copy of the object, using the Java convention
 *    of a <i>clone()</i> method
 * </p>
 * <p><i>Example</i>:
 *    List list1, list2; list1 = new List(); list2 = list1; is mutated to <br />
 *    List list1, list2; list1 = new List(); list2 = list1.clone(); 
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class EOA extends mujava.op.util.Mutator
{
   public EOA(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit)
   {
	  super( file_env, comp_unit );
   }

   public void visit( ExpressionStatement p ) throws ParseTreeException 
   {
	  Expression exp = p.getExpression();
	  if (exp instanceof AssignmentExpression)
	  {
		 Expression left = ((AssignmentExpression)exp).getLeft();
		 Expression right = ((AssignmentExpression)exp).getRight();

		 if (right instanceof Variable)
		 {
			Environment env = getEnvironment();
			OJClass bindedtype = env.lookupBind(right.toString());
			if (bindedtype != null)
			{
			   if (existCloneMethod(bindedtype))
			   {
				  String mutant = left.toString() + " = (" + bindedtype.getName()+")"
										+ right.toString() + ".clone()";
				  outputToFile( p , mutant );
				  return;
			   }
			}
		 }
      }
   }

   public void visit( MethodCall p ) throws ParseTreeException
   {
      if ( (p.getName().equals("clone")) && (p.getArguments().isEmpty()))
      {
         // do nothing
      }
      else
      {
         super.visit(p);
      }
   }

   /**
    * Output EOA mutants to files
    * @param original
    */
   public void outputToFile(MethodCall original)
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
	     EOA_Writer writer = new EOA_Writer( mutant_dir, out );
	     writer.setMutant(original);
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

   public void visit( AssignmentExpression p ) throws ParseTreeException 
   {
      Expression left = p.getLeft();
      Expression right = p.getRight();

      if ( right instanceof Variable)
      {
         Environment env = getEnvironment();
         OJClass bindedtype = env.lookupBind(right.toString());
         
         if (bindedtype != null)
         {
	        if (existCloneMethod(bindedtype))
	        {
	           String mutant = left.toString() + " = (" + bindedtype.getName()+")"
			                     + right.toString() + ".clone()";
	           outputToFile(p, mutant);
	           return;
	        }
         }
      }

      Expression newp = this.evaluateDown( p );
      if (newp != p) 
      {
         p.replace( newp );
         return;
      } 
      
      p.childrenAccept( this );
      newp = this.evaluateUp( p );
      
      if (newp != p)  
    	 p.replace( newp );
   }

   static boolean existCloneMethod(OJClass clazz)
   {
      OJMethod[] ms = clazz.getAllMethods();
      for (int i=0; i<ms.length; i++)
      {
         if ( ms[i].getName().equals("clone") && ms[i].getParameterTypes().length == 0  &&
	         !ms[i].getDeclaringClass().getName().equals("java.lang.Object"))
         {
	        return true;
         }
      }
      return false;
   }

   /**
    * Output EOA mutants to files
    * @param original
    * @param mutant
    */
   public void outputToFile(ExpressionStatement original, String mutant)
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
		 EOA_Writer writer = new EOA_Writer( mutant_dir, out );
		 writer.setMutant(original, mutant);
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
    * Output EOA mutants to files
    * @param original
    * @param mutant
    */
   public void outputToFile(AssignmentExpression original, String mutant)
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
		 EOA_Writer writer = new EOA_Writer( mutant_dir, out );
		 writer.setMutant(original, mutant);
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
