package twixt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mef.twixt.BooleanValue;
import org.mef.twixt.DateValue;
import org.mef.twixt.IntegerValue;
import org.mef.twixt.ListValue;
import org.mef.twixt.SelectValue;
import org.mef.twixt.StringValue;
import org.mef.twixt.Value;
import org.mef.twixt.binder.TwixtForm;
import org.mef.twixt.validate.ValContext;
import org.mef.twixt.validate.Validator;

import play.Logger;
import play.data.Form;
import tw.entities.Flight;
import tw.values.AccountType;

public class FlightTwixt extends TwixtForm
{
	public static class EvenValidator implements Validator
	{
		@Override
		public void validate(ValContext valctx, Value arg1) 
		{
			IntegerValue val = (IntegerValue) arg1;
			int n = val.get();
			if (n % 2 != 0)
			{
				valctx.addError("val {0} not even!", n);
			}
		}
	}
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
	//	public Map<String,String> boolopt = new HashMap<>();
	public AccountType accountTypeId = new AccountType();
	//	public FileValue path;

	//	public ListValue emails = new ListValue();
	public List<StringValue> emails = new ArrayList<>();
	//	
	//	public String ball;

	public FlightTwixt()
	{
		Map<String,String> opt = new HashMap<>();
		//first value is what the field value will be, 2nd value is display value
		opt.put("German", "German");
		opt.put("French", "French");
		lang.setOptions(opt);

		//		@helper.inputCheckboxGroup(
		//		        userForm("languages"),
		//		        options = options(userForm.get().languages.options()))
		//				

		//		boolopt = new HashMap<>();
		//		//first value is what the field value will be, 2nd value is display value
		//		boolopt.put("true", "true");
		//		boolopt.put("false", "false");

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
			List<StringValue> aaL = (List<StringValue>) valueL;
			return this.copyStringValueList(aaL);
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
