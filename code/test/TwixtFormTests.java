import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;
import org.mef.twixt.StringValue;
import org.mef.twixt.Value;
import org.mef.twixt.binder.MockTwixtBinder;
import org.mef.twixt.binder.TwixtForm;
import org.mef.twixt.validate.ValContext;
import org.mef.twixt.validate.Validator;

import basetest.BaseTest;



public class TwixtFormTests extends BaseTest
{
	public static class CarTwixt extends TwixtForm
	{
		public StringValue a;
		public StringValue b;
		
		public CarTwixt()
		{
			a = new StringValue();
			b = new StringValue();
			a.setValidator(new MyValidator());
		}
		
		private class MyValidator implements Validator
		{

			@Override
			public void validate(ValContext valctx, Value obj) 
			{
				StringValue val = (StringValue) obj;
				String s = val.get();
				if (s.length() != 8) //258-1833
				{
					valctx.addError("sdfdfs");
				}
			}
			
		}
	}

	@Test
	public void test() 
	{
		CarTwixt twixt = new CarTwixt();
		assertEquals("", twixt.a.get());
		
		MockTwixtBinder<CarTwixt> binder = new MockTwixtBinder<CarTwixt>(CarTwixt.class, buildMap());
		
		boolean b = binder.bind();
		assertTrue(b);
		twixt = binder.get();
		
		assertEquals("244-5566", twixt.a.get());
		assertEquals("def", twixt.b.get());
	}

	
	private Map<String,String> buildMap()
	{
		Map<String,String> map = new TreeMap<String,String>();
		map.put("a", "244-5566");
		map.put("b", "def");
		
		return map;
	}
}
