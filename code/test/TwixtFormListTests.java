import static org.junit.Assert.*;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;
import org.mef.twixt.ListValue;
import org.mef.twixt.StringValue;
import org.mef.twixt.Value;
import org.mef.twixt.binder.MockTwixtBinder;
import org.mef.twixt.binder.TwixtForm;
import org.mef.twixt.validate.ValContext;
import org.mef.twixt.validate.Validator;

import basetest.BaseTest;



public class TwixtFormListTests extends BaseTest
{
	public static class XCarTwixt extends TwixtForm
	{
		public StringValue a;
		public ListValue emails;
		
		public XCarTwixt()
		{
			a = new StringValue();
			emails = new ListValue();
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
		XCarTwixt twixt = new XCarTwixt();
		assertEquals("", twixt.a.get());
		
		MockTwixtBinder<XCarTwixt> binder = new MockTwixtBinder<XCarTwixt>(XCarTwixt.class, buildMap());
		
		boolean b = binder.bind();
		assertTrue(b);
		twixt = binder.get();
		
		assertEquals("244-5566", twixt.a.get());
		assertEquals(2, twixt.emails.size());
		assertEquals("ABC", twixt.emails.getIth(0).toString());
		assertEquals("DEF", twixt.emails.getIth(1).toString());
	}

	@Test
	public void testBadIndex() 
	{
		XCarTwixt twixt = new XCarTwixt();
		MockTwixtBinder<XCarTwixt> binder = new MockTwixtBinder<XCarTwixt>(XCarTwixt.class, buildBadMap());
		
		boolean b = binder.bind();
		assertFalse(b);
		twixt = binder.get();
		
		assertEquals("244-5566", twixt.a.get());
		assertEquals(1, twixt.emails.size());
		assertEquals("ABC", twixt.emails.getIth(0).toString());
	}
	
	private Map<String,String> buildMap()
	{
		Map<String,String> map = new TreeMap<String,String>();
		map.put("a", "244-5566");
		map.put("emails[0]", "ABC");
		map.put("emails[1]", "DEF");
		
		return map;
	}
	
	private Map<String,String> buildBadMap()
	{
		Map<String,String> map = new TreeMap<String,String>();
		map.put("a", "244-5566");
		map.put("emails[0]", "ABC");
		map.put("emails[2]", "DEF");
		
		return map;
	}
}
