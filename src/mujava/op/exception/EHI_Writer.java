////////////////////////////////////////////////////////////////////////////
// Module : EHI_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op.exception;

import java.io.*;

import mujava.op.util.MutantCodeWriter;

/**
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */ 

public class EHI_Writer extends MutantCodeWriter
{
  public EHI_Writer( String file_name,PrintWriter out ) {
	super(file_name,out);
  }
}
