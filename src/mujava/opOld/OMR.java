////////////////////////////////////////////////////////////////////////////
// Module : OMR.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.mop.*;
import openjava.ptree.*;
import mujava.op.util.DeclAnalyzer;

/**
 * <p>Generate OMR (Overloading method contents replace) mutants --
 *    replaces the body of a method with the body of another method 
 *    that has the same name
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */ 

public class OMR extends DeclAnalyzer
{
   OverloadingHelper oM_helper = new OverloadingHelper();

   public void translateDefinition(CompilationUnit comp_unit) throws openjava.mop.MOPException
   {
      // 복잡한 알고리즘임 -_-;
      OJMethod[] m = getDeclaredMethods();
      int[] omNum = new int[m.length];   // number of overloading method

      int i, j, k;
      String name1, name2;
      boolean checkF = false;

      // Calculate overloading method number
      for (i=0; i<m.length; i++)
      {
         omNum[i]=1;
         checkF = false;
         for (j=0; j<m.length; j++)
         {
            name1 = m[i].getName();
            name2 = m[j].getName();
            if (i > j)
            {
               if (name1.equals(name2)) 
            	  checkF = true;
            }
            
            if (!checkF && i<j && name1.equals(name2))
            {
               omNum[i]++;
            }
         }
      }

      for (i=0; i<m.length; i++)
      {
         // If overloading methods exist
         if (omNum[i] > 1)
         {
            String omName = m[i].getName();

            // Collect overloading methods of m[i]
            OJMethod[] overloadingM = new OJMethod[omNum[i]];
            int index = 0;
            overloadingM[index] = m[i];
            index++;
            for (j=i+1; j<m.length; j++)
            {
               if ( omName.equals(m[j].getName()) )
               {
                  overloadingM[index] = m[j];
                  index++;
               }
            }

            // Change the contents between overloading methods
            // Idea : using permutation
            for (j = 0; j<omNum[i] ; j++)
            {
               for (k=0; k<omNum[i] ; k++)
               {
                  if ( j!=k && oM_helper.sameReturnType(overloadingM[j], overloadingM[k]) &&
                       oM_helper.compatibleParameter(overloadingM[j], overloadingM[k]))
                  {
                     generateMutant(overloadingM[j], overloadingM[k], comp_unit);
                  }
               }
            }
         }
      }
   }

   /**
    * Generate OMR mutants
    * @param m1
    * @param m2
    * @param comp_unit
    */
   public void generateMutant(OJMethod m1, OJMethod m2, CompilationUnit comp_unit)
   {
      int i,j;
      int num;
      int[][] compatibleIndex; 

      compatibleIndex = oM_helper.genCompatibleLocations(m1,m2);
      if (compatibleIndex != null)
      {
         num = compatibleIndex.length;
      } 
      else
      {
         num=0;
      }

      try
      {
         MethodDeclaration original = m1.getSourceCode();
         String mutant = null;
         String[] par_name;

         if (num == 0)
         {
		    mutant = m1.getName() + "();";
		    outputToFile(comp_unit, original, mutant);
         } 
         else
         {
			for( i=0; i<num ; i++ )
			{
			   mutant = m1.getName() + "(";
			   for(j=0; j< m2.getParameters().length; j++)
			   {
				  par_name = m1.getParameters();
				  mutant = mutant + par_name[(compatibleIndex[i][j])];
				  if (j != m2.getParameters().length-1)
				  {
					 mutant = mutant + ",";
				  }
			   }
			   mutant = mutant + ");";
			   outputToFile(comp_unit, original, mutant);
			}
         }
      } catch(Exception e)
      {
         System.err.println("Error " + e);
      }
   }

   /**
    * Output OMR mutants to files
    * @param comp_unit
    * @param original
    * @param mutant
    */
   public void outputToFile(CompilationUnit comp_unit,
		  MethodDeclaration original, String mutant)
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
		 OMR_Writer writer = new OMR_Writer( mutant_dir, out );
		 writer.setMutant(original,mutant);
		 comp_unit.accept( writer );
		 out.flush();  
		 out.close();
      } catch ( IOException e ) 
      {
		 System.err.println( "fails to create " + f_name );
      } catch ( ParseTreeException e ) {
		 System.err.println( "errors during printing " + f_name );
		 e.printStackTrace();
      }
   }

   public OMR( openjava.mop.Environment oj_param0, openjava.mop.OJClass oj_param1, openjava.ptree.ClassDeclaration oj_param2 )
   {
      super( oj_param0, oj_param1, oj_param2 );
   }

   public OMR( java.lang.Class oj_param0, openjava.mop.MetaInfo oj_param1 )
   {
      super( oj_param0, oj_param1 );
   }
}
