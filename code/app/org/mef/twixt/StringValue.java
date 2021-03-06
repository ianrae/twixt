package org.mef.twixt;

import org.mef.twixt.validate.Validator;

public class StringValue extends Value
{
	public StringValue()
	{
		this("");
	}
	public StringValue(Validator validator)
	{
		super("", validator);
	}
	public StringValue(String n)
	{
		super(n);
	}

	@Override
	protected String render()
	{
		String n = get();
		return n.toString();
	}

	@Override
	protected void parse(String input)
	{
		this.setUnderlyingValue(input);
	}

	//return in our type
	public String get()
	{
		String nVal = (String)obj;
		return nVal;
	}
	public void set(String nVal)
	{
		setUnderlyingValue(nVal);
	}
}
