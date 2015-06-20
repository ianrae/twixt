package org.mef.twixt;

import org.mef.twixt.validate.Validator;

public class BooleanValue extends Value
{
	public BooleanValue()
	{
		this(false);
	}
	public BooleanValue(Validator validator)
	{
		super(false, validator);
	}
	public BooleanValue(Boolean n)
	{
		super(n);
	}

	@Override
	protected String render()
	{
		Boolean n = get();
		return n.toString();
	}

	@Override
	protected void parse(String input)
	{
		Boolean n = Boolean.parseBoolean(input);
		this.setUnderlyingValue(n);
	}

	//return in our type
	public boolean get()
	{
		Boolean bVal = (Boolean)obj;
		return bVal;
	}
	public void set(boolean b)
	{
		setUnderlyingValue(new Boolean(b));
	}
}
