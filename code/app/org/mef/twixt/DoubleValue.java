package org.mef.twixt;

import org.mef.twixt.validate.Validator;

public class DoubleValue extends Value
{
	public DoubleValue()
	{
		this(0.0);
	}
	public DoubleValue(Validator validator)
	{
		super(0.0, validator);
	}
	public DoubleValue(Double n)
	{
		super(n);
	}

	@Override
	protected String render()
	{
		Double n = get();
		return n.toString();
	}

	@Override
	protected void parse(String input)
	{
		Double n = Double.parseDouble(input);
		this.setUnderlyingValue(n);
	}

	//return in our type
	public double get()
	{
		Double nVal = (Double)obj;
		return nVal;
	}
	public void set(double val)
	{
		setUnderlyingValue(new Double(val));
	}
}
