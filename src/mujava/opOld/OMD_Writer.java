////////////////////////////////////////////////////////////////////////////
// Module : OMD_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import java.io.*;
import openjava.ptree.*;
import openjava.mop.*;
import mujava.op.util.MutantCodeWriter;

/**
 * <p>Output and log OMD mutants to files</p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */ 

public class OMD_Writer extends MutantCodeWriter
{
   MethodDeclaration original = null;
   OJMethod mutant = null;
   OJClass[] mutant_pars = null;
   boolean flag = false;

   /**
    * Set original source code and mutated code
    * @param original
    * @param mutant
    */
   public void setMutant(MethodDeclaration original, OJMethod mutant) 
   {
      this.original = original;
      this.mutant = mutant;
      this.mutant_pars = mutant.getParameterTypes();
   }

   public OMD_Writer( String file_name, PrintWriter out ) 
   {
	  super(file_name,out);
   }

   public void visit( MethodDeclaration p ) throws ParseTreeException
   {
      if (!(isSameObject(p, original)))
      {
         super.visit(p);
      }
      else
      {
        // 메소드를 바로 지우면 좋지만, 시험 데이터가 컴파일 될 때 original program의 시그너쳐에 맞춰 개발되어
        // 반드시 존재해야 하므로, 지우지 않고 redirection 한다.
         flag = true;
         super.visit(p);
         flag = false;
      }
   }

   public void visit( StatementList p ) throws ParseTreeException
   {
      if (!flag)
      {
         super.visit(p);
      }
      else
      {
         //-------------------------------------------------------
         mutated_line = line_num;
         String temp_str = original.getName() + "(" + original.getParameters().toString() +")";
         writeLog(removeNewline(temp_str+" => "+mutant.signature()));
         //----------------------------------------------------------
         temp_str = original.getName() + "(";
         ParameterList pl = original.getParameters();
         
         for (int i=0; i<pl.size()-1; i++)
         {
            Parameter par = pl.get(i);
            String mutated_type = mutant_pars[i].getName();
            
            if (par.getTypeSpecifier().getName().equals(mutated_type))
            {
               temp_str = temp_str + par.getVariable() + ",";
            }
            else
            {
               temp_str = temp_str + "(" + mutated_type + ")" + par.getVariable() + ",";
            }
	     }
         
         String mutated_type = mutant_pars[pl.size()-1].getName();
         
         if (pl.get(pl.size()-1).getTypeSpecifier().getName().equals(mutated_type))
         {
            temp_str = temp_str+pl.get(pl.size()-1).getVariable()+")";
         }
         else
         {
            temp_str = temp_str + "(" + mutated_type + ")" + pl.get(pl.size()-1).getVariable() + ")";
         }
         
	     writeLog(removeNewline("Redirect to  => " + temp_str));
	     writeTab();
	     
         if (original.getReturnType().toString().equals("void"))
         {
            out.println(temp_str+";");
         }
         else
         {
            out.println("return " + temp_str+";");
         }
         line_num++;
      }
   }
}
