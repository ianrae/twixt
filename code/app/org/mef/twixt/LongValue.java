package org.mef.twixt;

import org.mef.twixt.validate.Validator;

public class LongValue extends Value
{
	public LongValue()
	{
		this(0L);
	}
	public LongValue(Validator validator)
	{
		super(0L, validator);
	}
	public LongValue(Long n)
	{
		super(n);
	}

	@Override
	protected String render()
	{
		Long n = get();
		return n.toString();
	}

	@Override
	protected void parse(String input)
	{
		Long n = Long.parseLong(input);
		this.setUnderlyingValue(n);
	}

	//return in our type
	public long get()
	{
		Long nVal = (Long)obj;
		return nVal;
	}
	public void set(long nVal)
	{
		setUnderlyingValue(new Long(nVal));
	}
}
