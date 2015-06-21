package org.mef.twixt.binder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.mef.twixt.ListValue;
import org.mef.twixt.StringValue;
import org.mef.twixt.Value;
import org.mef.twixt.ValueContainer;
import org.mef.twixt.validate.ValContext;
import org.mef.twixt.validate.ValidationErrorSpec;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import static org.reflections.ReflectionUtils.*;

public class ReflectionBinder
{
	ValContext vtx;
	private Field fieldBeingParsed;

	public ReflectionBinder()
	{
		vtx = new ValContext();
	}

	public Map<String,List<ValidationErrorSpec>> getErrors()
	{
		return vtx.getErrors();
	}
	public ValContext getContext()
	{
		return vtx;
	}

	boolean bind(ValueContainer input, Map<String,String> map) 
	{
		boolean ok = false;

		try {
			ok = bindImpl(input, map);
		} catch (Exception e) {
			e.printStackTrace();
			if (fieldBeingParsed != null) //failed in fromString?
			{
				vtx.setCurrentItemName(fieldBeingParsed.getName());				
				vtx.addError(String.format("%s: invalid input", this.fieldBeingParsed.getName()));
			}
		}

		//and validate
		if (ok)
		{
			//TODO if validateContainer exists call it, else use reflection
			input.validate(vtx);

			ok = (vtx.getFailCount() == 0);
		}

		return ok;
	}

	boolean bindImpl(ValueContainer input, Map<String,String> map) throws Exception
	{
		boolean ok = true;

		Set<Field> list = ReflectionUtils.getAllFields(input.getClass(), ReflectionUtils.withModifier(Modifier.PUBLIC));
		for(Field fld : list)
		{
			if (ListValue.class.isAssignableFrom(fld.getType()))
			{
				ok = ok && oldBindListElement(fld, input, map);
			}
			else if (fld.getType().equals(List.class))
			{
				ok = ok && bindListElement(fld, input, map);
			}
			else
			{
				String fieldName = fld.getName();

				String s = map.get(fieldName);
				if (s != null)
				{
					Object obj = fld.get(input);
					if (obj instanceof Value)
					{
						Value val = (Value)obj;
						fieldBeingParsed = fld;
						val.fromString(s);
						fieldBeingParsed = null;
					}
				}
			}
		}

		return ok;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean bindListElement(Field fld, ValueContainer input, Map<String,String> map) throws Exception
	{
		boolean ok = true;
		String fieldName = fld.getName();
		System.out.println("yyy " + fieldName);
		
		Map<String,String> itemMap = findListItems(map, fieldName);
		Object xobj = fld.get(input);
		List newL = (List)xobj;
		
		//key: email[0]
		for(String key: itemMap.keySet())
		{
			String valueToBind = itemMap.get(key);
			int index = extractIndex(key);
			if (index == newL.size())
			{
				newL.add(new StringValue(valueToBind)); //!for now we only support lists of strings
								//later add ability for ListValue to know what its element's underlying type is
			}
			else
			{
				System.out.println(String.format("[Twixt] Error: index %d unexpected: %s", index, key));
				ok = false;
			}
		}
		
		return ok;
	}
	
	private boolean oldBindListElement(Field fld, ValueContainer input, Map<String,String> map) throws Exception
	{
		boolean ok = true;
		String fieldName = fld.getName();
		System.out.println("xxx " + fieldName);
		
		Map<String,String> itemMap = findListItems(map, fieldName);
		List<Value> newL = new ArrayList<>();
		
		//key: email[0]
		for(String key: itemMap.keySet())
		{
			String valueToBind = itemMap.get(key);
			int index = extractIndex(key);
			if (index == newL.size())
			{
				newL.add(new StringValue(valueToBind)); //!for now we only support lists of strings
								//later add ability for ListValue to know what its element's underlying type is
			}
			else
			{
				System.out.println(String.format("[Twixt] Error: index %d unexpected: %s", index, key));
				ok = false;
			}
		}
		
		Object obj = fld.get(input);
		ListValue listVal = (ListValue) obj;
		listVal.set(newL);
		return ok;
	}

	private int extractIndex(String key) 
	{
		int pos = key.indexOf('[');
		int endpos = key.indexOf(']');
		
		String s = key.substring(pos + 1, endpos);
		Integer n = Integer.parseInt(s);
		return n;
	}

	private Map<String,String> findListItems(Map<String, String> map, String fieldName) 
	{
		Map<String,String> itemMap = new TreeMap<>();
		for(String key : map.keySet())
		{
			String val = map.get(key);
			String target = fieldName + "[";
			if (key.startsWith(target))
			{
				itemMap.put(key, val);
			}
		}
		return itemMap;
	}
}