package twixt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mef.twixt.BooleanValue;
import org.mef.twixt.DateValue;
import org.mef.twixt.IntegerValue;
import org.mef.twixt.SelectValue;
import org.mef.twixt.StringValue;
import org.mef.twixt.Value;
import org.mef.twixt.binder.TwixtForm;
import org.mef.twixt.validate.ValContext;
import org.mef.twixt.validate.Validator;

import tw.values.AccountType;

public class FlightTwixt extends TwixtForm
{
	public static class NoAValidator implements Validator
	{
		@Override
		public void validate(ValContext valctx, Value arg1) 
		{
			StringValue val = (StringValue) arg1;
			String s = val.get();
			if (s.contains("a"))
			{
				valctx.addError("val contains a");
			}
		}
	}

	public StringValue s = new StringValue(new NoAValidator());
	public IntegerValue size = new IntegerValue();
	public Integer size2 = 44;
	public SelectValue lang = new SelectValue("lang");
	public DateValue startDate = new DateValue();
	public BooleanValue isAdmin = new BooleanValue(); 
	public AccountType accountTypeId = new AccountType();
	public List<StringValue> emails = new ArrayList<>();

	public FlightTwixt()
	{
		Map<String,String> opt = new HashMap<>();
		//first value is what the field value will be, 2nd value is display value
		opt.put("German", "German");
		opt.put("French", "French");
		lang.setOptions(opt);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List convertModelListToValueList(String fieldName, List modelL) 
	{
		if (fieldName.equals("emails"))
		{
			List<String> srcL = (List<String>) modelL;
			return copyStringList(srcL);
		}
		else
		{
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List convertValueListToModelList(String fieldName, List valueL) 
	{
		if (fieldName.equals("emails"))
		{
			List<StringValue> aaL = (List<StringValue>) valueL;
			return copyStringValueList(aaL);
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
