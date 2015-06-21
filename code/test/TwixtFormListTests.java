import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
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
	public static class Honda
	{
		private String a;
		private List<String> emails;
		public String getA() {
			return a;
		}
		public void setA(String a) {
			this.a = a;
		}
		public List<String> getEmails() {
			return emails;
		}
		public void setEmails(List<String> emails) {
			this.emails = emails;
		}
		
	}
	
	public static class HondaTwixt extends TwixtForm
	{
		public StringValue a;
		public List<StringValue> emails;
		
		public HondaTwixt()
		{
			a = new StringValue();
			emails = new ArrayList<>();
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

		@Override
		public List convertModelListToValueList(String fieldName, List modelL) 
		{
			if (fieldName.equals("emails"))
			{
				List<String> srcL = (List<String>) modelL;
				return this.copyStringList(srcL);
			}
			else
			{
				return null;
			}
		}

		@Override
		public List convertValueListToModelList(String fieldName, List valueL) 
		{
			if (fieldName.equals("emails"))
			{
				return this.copyStringValueList((List<StringValue>)valueL);
			}
			else
			{
				return null;
			}
		}

		@Override
		public Value createListElement(String fieldName, String value) 
		{
			if (fieldName.equals("emails"))
			{
				return new StringValue(value);
			}
			else
			{
				return null;
			}
		}
	}

	@Test
	public void test() 
	{
		HondaTwixt twixt = new HondaTwixt();
		assertEquals("", twixt.a.get());
		
		MockTwixtBinder<HondaTwixt> binder = new MockTwixtBinder<HondaTwixt>(HondaTwixt.class, buildMap());
		
		boolean b = binder.bind();
		assertTrue(b);
		twixt = binder.get();
		
		assertEquals("244-5566", twixt.a.get());
		assertEquals(2, twixt.emails.size());
		assertEquals("ABC", twixt.emails.get(0).toString());
		assertEquals("DEF", twixt.emails.get(1).toString());
		
		Honda honda = new Honda();
		twixt.copyTo(honda);
		assertEquals("244-5566", honda.getA());
		assertEquals(2, honda.getEmails().size());
		assertEquals("ABC", honda.getEmails().get(0));
		assertEquals("DEF", honda.getEmails().get(1));
		
		HondaTwixt twixt2 = new HondaTwixt();
		twixt2.copyFrom(honda);
		assertEquals("244-5566", twixt2.a.get());
		assertEquals(2, twixt2.emails.size());
		assertEquals("ABC", twixt2.emails.get(0).toString());
		assertEquals("DEF", twixt2.emails.get(1).toString());
		
	}

	@Test
	public void testBadIndex() 
	{
		HondaTwixt twixt = new HondaTwixt();
		MockTwixtBinder<HondaTwixt> binder = new MockTwixtBinder<HondaTwixt>(HondaTwixt.class, buildBadMap());
		
		boolean b = binder.bind();
		assertFalse(b);
		twixt = binder.get();
		
		assertEquals("244-5566", twixt.a.get());
		assertEquals(1, twixt.emails.size());
		assertEquals("ABC", twixt.emails.get(0).toString());
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
