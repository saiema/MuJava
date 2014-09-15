////////////////////////////////////////////////////////////////////////////
// Module : LCR_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.opOld;

import mujava.op.util.MutantCodeWriter;
import openjava.ptree.*;

import java.io.*;

/**
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */ 

public class LCR_Writer extends MutantCodeWriter{

  BinaryExpression binary_original;
  UnaryExpression unary_original;
  int mutant_op;

  public LCR_Writer( String file_name, PrintWriter out ) {
    super(file_name,out);
  }

  public void setMutant(BinaryExpression exp, int op){
    binary_original = exp;
    mutant_op = op;
  }

  public void setMutant(UnaryExpression exp){
    unary_original = exp;
  }

  public void visit( BinaryExpression p )
  throws ParseTreeException
  {
    if(isSameObject(p,binary_original)){
    	BinaryExpression mutant_exp;
    	mutant_exp = (BinaryExpression)p.makeRecursiveCopy(); 
    	mutant_exp.setOperator(mutant_op);
    	super.visit(mutant_exp);

    	String operator = mutant_exp.operatorString();
    	out.print( " " + operator + " " );
	    // -----------------------------------------------------------
	    mutated_line = line_num;
	    String log_str = p.operatorString()+ " => " + operator;
	    writeLog(removeNewline(log_str));
	    // -------------------------------------------------------------

    	mutant_exp = null;
    }else{
      super.visit(p);
    }
  }

  public void visit( UnaryExpression p )
  throws ParseTreeException
  {
    if(isSameObject(p,unary_original)){
      if (p.isPrefix()) {
        // -----------------------------------------------------------
        mutated_line = line_num;
        String log_str = p.toString()+ " => " + p.toString().substring(1);
        writeLog(removeNewline(log_str));
        // -------------------------------------------------------------

      }else{
        out.print("!");
        // -----------------------------------------------------------
        mutated_line = line_num;
        String log_str = p.toString()+ " => !" + p.toString();
        writeLog(removeNewline(log_str));
        // -------------------------------------------------------------

      }

      Expression expr = p.getExpression();
        if (expr instanceof AssignmentExpression
	    || expr instanceof ConditionalExpression
	    || expr instanceof BinaryExpression
	    || expr instanceof InstanceofExpression
	    || expr instanceof CastExpression
	    || expr instanceof UnaryExpression){
	    writeParenthesis( expr );
        } else {
	    expr.accept( this );
        }

        if (p.isPostfix()) {
	    String operator = p.operatorString();
	    out.print( operator );


        }
    }else{
      super.visit(p);
    }
  }
}
