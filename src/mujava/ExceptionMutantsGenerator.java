////////////////////////////////////////////////////////////////////////////
// Module : ExceptionMutantsGenerator.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava;

import openjava.ptree.*;
import java.io.*;

import mujava.op.exception.*;
import mujava.op.util.*;
import mujava.util.Debug;

/**
 * <p>Generate exception-related mutants
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class ExceptionMutantsGenerator  extends MutantsGenerator
{
   String[] exceptionOp;

   public ExceptionMutantsGenerator(File f) 
   {
      super(f);
      exceptionOp = MutationSystem.em_operators;
   }

   public ExceptionMutantsGenerator(File f, boolean debug) 
   {
      super(f, debug);
      exceptionOp = MutationSystem.em_operators;
   }

   public ExceptionMutantsGenerator(File f, String[] eOP) 
   {
      super(f);
      exceptionOp = eOP;
   }

   void genMutants()
   {
      if (comp_unit == null)
      {
         System.err.println(original_file + " is skipped.");
      }
    
      ClassDeclarationList cdecls = comp_unit.getClassDeclarations();
      if (cdecls == null || cdecls.size() == 0) 
    	 return;

      if (exceptionOp != null && exceptionOp.length > 0)
      {
         MutationSystem.clearPreviousMutants();
         MutationSystem.MUTANT_PATH = MutationSystem.EXCEPTION_MUTANT_PATH;
         CodeChangeLog.openLogFile();
         genExceptionMutants(cdecls);
         CodeChangeLog.closeLogFile();
      }
   }

   /**
    * Compile exception-related mutants into bytecode
    */
   public void compileMutants()
   {
      if (exceptionOp != null && exceptionOp.length > 0)
      {
	     Debug.println("* Compiling exception-related mutants into bytecode");
         MutationSystem.MUTANT_PATH = MutationSystem.EXCEPTION_MUTANT_PATH;
         super.compileMutants();
      }
   }

   void genExceptionMutants(ClassDeclarationList cdecls)
   {
      for (int j=0; j<cdecls.size(); ++j)
      {
         ClassDeclaration cdecl = cdecls.get(j);

         if (cdecl.getName().equals(MutationSystem.CLASS_NAME))
         {
            try
            {
               mujava.op.util.Mutator mutant_op;
               if (hasOperator(exceptionOp, "EFD"))
               {
                  Debug.println("  Applying EFD ... ... ");
                  mutant_op = new EFD(file_env, cdecl, comp_unit);
                  comp_unit.accept(mutant_op);
               }

               if (hasOperator(exceptionOp, "EHC"))
               {
                  Debug.println("  Applying EHC ... ... ");
                  mutant_op = new EHC(file_env, cdecl, comp_unit);
                  comp_unit.accept(mutant_op);
               }

               if (hasOperator(exceptionOp, "EHD"))
               {
                  Debug.println("  Applying EHD ... ... ");
                  mutant_op = new EHD(file_env, cdecl, comp_unit);
                  comp_unit.accept(mutant_op);
               }

               if (hasOperator(exceptionOp, "EHI"))
               {
                  Debug.println("  Applying EHI ... ... ");
                  mutant_op = new EHI(file_env, cdecl, comp_unit);
                  comp_unit.accept(mutant_op);
               }

               if (hasOperator(exceptionOp, "ETC"))
               {
                  Debug.println("  Applying ETC ... ... ");
                  mutant_op = new ETC(file_env, cdecl, comp_unit);
                  comp_unit.accept(mutant_op);
               }

               if (hasOperator(exceptionOp, "ETD"))
               {
                  Debug.println("  Applying ETD ... ... ");
                  mutant_op = new ETD(file_env, cdecl, comp_unit);
                  comp_unit.accept(mutant_op);
               }
            } catch (ParseTreeException e)
            {
               System.err.println( "Exception, during generating traditional mutants for the class "
                               + MutationSystem.CLASS_NAME);
               e.printStackTrace();
            }
         }
      }
   }
}
