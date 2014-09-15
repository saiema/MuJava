////////////////////////////////////////////////////////////////////////////
// Module : PCD.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.mop.*;
import openjava.ptree.*;

/**
 * <p>Generate PCD (Type cast operator deletion) mutants --
 *    deletes type casting operator
 * </p>
 * <p><i>Example</i>: <br/>
 *   Child cRef; Parent pRef = cRef; ((Child)pRef).toString();  is mutated to <br> 
 *   Child cRef; Parent pRef = cRef; pRef.toString();   
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class PCD extends mujava.op.util.TypeCastMutator
{
   public PCD(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit)
   {
	  super( file_env, comp_unit );
   } 

   public void visit( CastExpression p )  throws ParseTreeException
   {
      String afterCastType = p.getTypeSpecifier().getName();
      String beforeCastType = getType(p.getExpression()).getName();
      
      if ((afterCastType == null) || (beforeCastType == null)) 
    	 return;
      
      if (afterCastType.equals(beforeCastType)) 
    	 return;
     
      if (currentMethodCall == null)
      {
         if (hasHidingVariableOrOverridingMethod(afterCastType, beforeCastType))
         {
            outputToFile(p);
         }
      } 
      else
      {
         try 
         {
            String method_name = currentMethodCall.getName();
            Class[] par_type = getParameterTypes(currentMethodCall);
            
            if ( isNonAbstractOverridingMethodCall(afterCastType, beforeCastType, method_name, par_type))
            {
               outputToFile(p);
            }
         } catch (Exception e)
         {
            // e.printStackTrace();
         }
      }
   }

   public void visit( MethodCall p ) throws ParseTreeException 
   {
      Expression newp = this.evaluateDown( p );
	  if (newp != p) 
	  {
	     p.replace( newp );
	     return;
	  }
    
	  Expression ref = p.getReferenceExpr();
      if (ref != null)
      {
         currentMethodCall = p;
         ref.accept(this);
         currentMethodCall = null;
      }
     
      ExpressionList list = p.getArguments();
      if (list != null) 
    	 list.accept(this);
   }

   /**
    * Write PCD mutants to files
    * @param original
    */
   public void outputToFile(CastExpression original)
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
		 PCD_Writer writer = new PCD_Writer( mutant_dir, out );
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
}
