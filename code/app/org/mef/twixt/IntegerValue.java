package org.mef.twixt;

import org.mef.twixt.validate.Validator;

public class IntegerValue extends Value
{
	public IntegerValue()
	{
		this(0);
	}
	public IntegerValue(Validator validator)
	{
		super(0, validator);
	}
	public IntegerValue(Integer n)
	{
		super(n);
	}

	@Override
	protected String render()
	{
		Integer n = get();
		return n.toString();
	}

	@Override
	protected void parse(String input)
	{
		Integer n = Integer.parseInt(input);
		this.setUnderlyingValue(n);
	}

	//return in our type
	public int get()
	{
		Integer nVal = (Integer)obj;
		return nVal;
	}
	public void set(int val)
	{
		setUnderlyingValue(new Integer(val));
	}
}
