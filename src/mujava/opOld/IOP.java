////////////////////////////////////////////////////////////////////////////
// Module : IOP.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.mop.*;
import openjava.ptree.*;

/**
 * <p>Generate IOP (Overriding method calling position change) mutants --
 *    move each call to an overridden method to the first and last 
 *    statements of the method and up and down one statement
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class IOP extends mujava.op.util.Mutator implements IOP_Helper
{
   MethodDeclaration containing_method = null;

   public IOP(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit)
   {
	  super( file_env,comp_unit );
   }

   public void visit(MethodDeclaration p) throws ParseTreeException 
   {
	  if (p.getName().equals("main")) 
		 return;
      containing_method = p;
      super.visit(p);
   }

   boolean compatibleParameters( ExpressionList args, ParameterList plist)
   {
      if (args.size() != plist.size()) 
    	 return false;
      
      OJClass type = null;

      for (int i=0; i<args.size(); i++)
      {
         try 
         {
            type = getType(args.get(i));
         } catch (ParseTreeException e)
         {
            type = null;
         }
         
         if (!(type.getName().equals(plist.get(i).getTypeSpecifier().getName())))
            return false;
      }
      return true;
   }

   boolean isOverridingMethodCallWithSameName(MethodCall p)
   {
      Expression lexp = p.getReferenceExpr();
      if (lexp == null) 
    	 return false;
      if ( !(lexp instanceof SelfAccess)) 
    	 return false;
      if ( !(((SelfAccess)lexp).getAccessType()==SelfAccess.SUPER) ) 
    	 return false;
      if ( !(p.getName().equals(containing_method.getName()))) 
    	 return false;
      if (!compatibleParameters(p.getArguments(),containing_method.getParameters()) ) 
    	 return false;
      
      return true;
   }

   /**
    * Write mutants to files and log mutated line
    */
   public void visit( StatementList p ) throws ParseTreeException 
   {
      this.evaluateDown( p );
      for (int i=0; i<p.size(); i++)
      {
         Statement stmt = p.get(i);
         if (stmt instanceof ExpressionStatement)
         {
            Expression exp = ((ExpressionStatement)stmt).getExpression();
            if (exp instanceof MethodCall)
            {
               if (isOverridingMethodCallWithSameName((MethodCall)exp))
               {
                  int[] change_mod = getChangeType(p.size(), i);
                  if (change_mod == null) 
                	 continue;
                  for (int h=0; h<change_mod.length; h++)
                  {
                     outputToFile(comp_unit, p, i, change_mod[h]);
                  }
               }
            }
         }
      }
      this.evaluateUp( p );
   }

   /**
    * Output IOP mutants to files
    * @param comp_unit
    * @param stmt_list
    * @param index
    * @param mod
    */
   public void outputToFile(CompilationUnit comp_unit, StatementList stmt_list, int index,int mod)
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
         IOP_Writer writer = new IOP_Writer( mutant_dir, out );
         writer.setMutant(stmt_list, index, mod);
         comp_unit.accept( writer );
         out.flush();  out.close();
      } catch ( IOException e ) 
      {
	     System.err.println( "fails to create " + f_name );
      } catch ( ParseTreeException e ) 
      {
	     System.err.println( "errors during printing " + f_name );
	     e.printStackTrace();
      }
   }

   static int[] getChangeType(int stmt_num, int index)
   {
      int[] result = null;

      switch (stmt_num)
      {
         case 0: break;
         case 1: break;
	     case 2: result = new int[1];
		         if (index == 0) 
		            result[0] = LAST;
			     else 
			        result[0] = FIRST;
			     break;
  	     case 3: result = new int[2];
	             if (index == 0)
	             {
				    result[0] = LAST;
				    result[1] = DOWN;
	             } 
	             else if (index == 1)
	             {
				    result[0] = FIRST;
				    result[1] = LAST;
	             } 
	             else if (index == 2)
	             {
				    result[0] = FIRST;
				    result[1] = UP;
	             }
			     break;
       	 default:
	             if (index == 0) 
	             {
				    result = new int[2];
				    result[0] = LAST;
				    result[1] = DOWN;
	             } 
	             else if (index == 1) 
	             {
				    result = new int[3];
				    result[0] = FIRST;
				    result[1] = LAST;
				    result[2] = DOWN;
	             }  
	             else if (index == stmt_num-2) 
	             {
				    result = new int[3];
				    result[0] = FIRST;
				    result[1] = LAST;
				    result[2] = UP;
	             } 
	             else if (index == stmt_num-1) 
	             {
				    result = new int[2];
				    result[0] = FIRST;
				    result[1] = UP;
	             } 
	             else 
	             {
				    result = new int[4];
				    result[0] = FIRST;
				    result[1] = LAST;
				    result[2] = UP;
				    result[3] = DOWN;
	             }

      }
      return result;
   }
}
