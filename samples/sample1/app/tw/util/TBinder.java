package tw.util;

import org.mef.twixt.ValueContainer;
import org.mef.twixt.binder.TwixtBinder;

public class TBinder<T extends ValueContainer> extends TwixtBinder<T>
{
	
	public TBinder(Class<T> clazz, T original)
	{
		super(clazz, original);
	}
	public TBinder(Class<T> clazz)
	{
		super(clazz);
	}

	

}
