package org.mef.twixt.validate;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import play.i18n.Messages;


public class ValidationErrors
{
	public static boolean inUnitTest = false; //only set true for unit tests where a FakeApplication is not being used
	
    public Map<String,List<ValidationErrorSpec>> map;
    String itemName;
    
    public String getItemName()
    {
    	return itemName;
    }
    public void setItemName(String itemName)
    {
    	this.itemName = itemName;
    }
    
    public void addError(String message, Object... arguments)
    {
    	ValidationErrorSpec spec = new ValidationErrorSpec();
    	spec.key = itemName;
    	spec.message = getMessageFromConf(message, arguments);
    	
    	List<ValidationErrorSpec> L = map.get(itemName);
    	if (L == null)
    	{
    		L = new ArrayList<ValidationErrorSpec>();
    	}
    	L.add(spec);
    	map.put(itemName, L);
    }
    
	private String getMessageFromConf(String message, Object... arguments) 
	{
		boolean flag = false;
		if (! inUnitTest)
		{
			flag = Messages.isDefined(message);
		}
		
		if (flag)
		{
			String s = Messages.get(message, arguments);
			return s;
		}
		else
		{
			String s = MessageFormat.format(message, arguments);
			return s;
		}
	}

}