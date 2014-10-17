////////////////////////////////////////////////////////////////////////////
// Module : AOIS_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op.basic;

import mujava.api.Mutation;
import mujava.op.util.MutantCodeWriter;
import openjava.ptree.*;
import java.io.*;

/**
 * <p>Output and log AOIS mutants to files </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class AOIS_Writer extends MutantCodeWriter {
   private Variable original_var = null;
   private FieldAccess original_field = null;
   private UnaryExpression original_uexp = null;
   private UnaryExpression mutant;

   public AOIS_Writer( String file_name, PrintWriter out, Mutation mi)  {
	   super(file_name,out, mi);
	   this.original_var = null;
	   this.original_field = null;
	   this.original_uexp = null;
	   
	   setMutant(this.mi.getOriginal(), this.mi.getMutant());
   }

   private void setMutant(Object v1, Object v2) {
	   UnaryExpression mutant = (UnaryExpression) v2;
	   if (v1 instanceof Variable) {
		   setMutant((Variable)v1, mutant);
	   } else if (v1 instanceof FieldAccess) {
		   setMutant((FieldAccess)v1, mutant);
	   } else if (v1 instanceof UnaryExpression) {
		   setMutant((UnaryExpression)v1, mutant);
	   }
   }
   
   private void setMutant(Variable original_var, UnaryExpression mutant) {
      this.original_var = original_var;
      this.mutant = mutant;
   }

   private void setMutant(FieldAccess original_field, UnaryExpression mutant) {
	   this.original_field = original_field;
	   this.mutant = mutant;
   }
   
   private void setMutant(UnaryExpression original_uexp, UnaryExpression mutant) {
	   this.original_uexp = original_uexp;
	   this.mutant = mutant;
   }

   public void visit( Variable p ) throws ParseTreeException {
	   if (this.original_var != null && isSameObject(p, this.original_var)) {
		   this.original_var = null;
		   super.visit(this.mutant);
		   // -----------------------------------------------------------
		   mutated_line = line_num;
		   // -------------------------------------------------------------
	   } else {
		   super.visit(p);
	   }
   }
   
   public void visit( FieldAccess p ) throws ParseTreeException {
	   if (this.original_field != null && isSameObject(p, this.original_field)) {
		   this.original_field = null;
		   super.visit(this.mutant);
		   // -----------------------------------------------------------
	       mutated_line = line_num;
	       // -------------------------------------------------------------
	   } else {
		   super.visit(p);
	   }
   }
   
   public void visit( UnaryExpression p ) throws ParseTreeException {
	   if (this.original_uexp != null && isSameObject(p, this.original_uexp)) {
		   this.original_uexp = null;
		   super.visit(this.mutant);
		   // -----------------------------------------------------------
	       mutated_line = line_num;
	       // -------------------------------------------------------------
	   } else {
		   super.visit(p);
	   }
   }

   
}
