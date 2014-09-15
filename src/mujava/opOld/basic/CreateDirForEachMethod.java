package mujava.opOld.basic;

import openjava.mop.*;
import openjava.ptree.*;
import java.io.*;
import mujava.MutationSystem;

public class CreateDirForEachMethod extends MethodLevelMutator
{
   PrintWriter out = null;
   public CreateDirForEachMethod(FileEnvironment file_env, ClassDeclaration cdecl,
                                 CompilationUnit comp_unit, PrintWriter out)
   {
      super( file_env, comp_unit );
      this.out = out;
   }

   void createDirectory(String dir_name)
   { 
      out.println(dir_name);
      String absolute_dir_path = MutationSystem.MUTANT_PATH + "/" + dir_name;
      File dirF = new File(absolute_dir_path);
      dirF.mkdir();
   }

   public void visit(ConstructorDeclaration p) throws ParseTreeException 
   {
      createDirectory(getConstructorSignature(p));
   }

   public void visit(MethodDeclaration p) throws ParseTreeException
   {
      createDirectory(getMethodSignature(p));
   }
}
