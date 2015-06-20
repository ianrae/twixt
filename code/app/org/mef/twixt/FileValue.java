package org.mef.twixt;

import org.mef.twixt.validate.Validator;

public class FileValue extends StringValue
{
	public FileValue()
	{
		this("");
	}
	public FileValue(Validator validator)
	{
		super(validator);
	}
	public FileValue(String n)
	{
		super(n);
	}

}
