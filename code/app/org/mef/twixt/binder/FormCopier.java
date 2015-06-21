package org.mef.twixt.binder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mef.twixt.StringValue;
import org.mef.twixt.Value;
import org.springframework.util.ReflectionUtils;

import play.Logger;

public class FormCopier implements ReflectionUtils.FieldCallback
{
	public interface FieldCopier
	{
		void copyFieldFromModel(FormCopier copier, Field field);
		void copyFieldToModel(FormCopier copier, Field field);
	}

	TwixtForm form;
	private Object modelToCopyFrom;
	private Object modelToCopyTo;
	private FieldCopier fieldCopier;
	private String[] fieldsToNotCopy;

	public FormCopier(FieldCopier fieldCopier)
	{
		this.fieldCopier = fieldCopier;
	}

	public void copyToModel(TwixtForm twixtForm, Object model, String[] fieldsToNotCopy)
	{
		form = twixtForm;
		modelToCopyTo = model;
		this.fieldsToNotCopy = fieldsToNotCopy;
		ReflectionUtils.doWithFields(form.getClass(), this, ReflectionUtils.COPYABLE_FIELDS);
	}

	public void copyFromModel(Object model, TwixtForm twixtForm, String[] fieldsToNotCopy)
	{
		form = twixtForm;
		modelToCopyFrom = model;
		this.fieldsToNotCopy = fieldsToNotCopy;
		ReflectionUtils.doWithFields(form.getClass(), this, ReflectionUtils.COPYABLE_FIELDS);
	}

	@Override
	public void doWith(Field field)
	{
		if (this.fieldsToNotCopy != null)
		{
			for(String fieldName : fieldsToNotCopy)
			{
				if (field.getName().equals(fieldName))
				{
					return;
				}
			}
		}

		if (modelToCopyFrom != null)
		{
			fieldCopier.copyFieldFromModel(this, field);
		}
		else if (modelToCopyTo != null)
		{
			fieldCopier.copyFieldToModel(this, field);
		}
	}

	@SuppressWarnings("unchecked")
	public void copyFieldFromModel(Field field)
	{
		Class<?> clazz = field.getType();
		if (Value.class.isAssignableFrom(clazz))
		{
			try 
			{
				field.setAccessible(true);
				Object valueObj = field.get(form);

				String fnName = "get" + uppify(field.getName());
				Method meth = ReflectionUtils.findMethod(modelToCopyFrom.getClass(), fnName);
				if (meth != null)
				{
					Object src = meth.invoke(modelToCopyFrom);
					src = adjustSrcFrom(src);
					fnName = "setUnderlyingValue";
					meth = ReflectionUtils.findMethod(clazz, fnName, Object.class);

					meth.invoke(valueObj, src);
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		else if (List.class.isAssignableFrom(clazz))
		{
			try 
			{
				field.setAccessible(true);
				//copy from honda.L<str> to twixt.L<strvalue>
				String fnName = "get" + uppify(field.getName());
				Method meth = ReflectionUtils.findMethod(modelToCopyFrom.getClass(), fnName);
				if (meth != null)
				{
					Object src = meth.invoke(modelToCopyFrom);
					List srcL = (List) src;
					List valueL = form.convertModelListToValueList(field.getName(), srcL);
					
					field.set(form, valueL); //assign to form.emails
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}

	//messy. convert list of String to list of Value
	private Object adjustSrcFrom(Object src) 
	{
		if (src.getClass().equals(ArrayList.class))
		{
			ArrayList L = (ArrayList) src;
			ArrayList<StringValue> newL = new ArrayList<StringValue>();
			for(Object obj : L)
			{
				newL.add(new StringValue(obj.toString()));
			}
			return newL;
		}
		return src;
	}


	public void copyFieldToModel(Field field)
	{
		Class<?> clazz = field.getType();
		if (Value.class.isAssignableFrom(clazz))
		{
			try 
			{
				field.setAccessible(true);
				Object valueObj = field.get(form);

				if (valueObj == null)
				{
					Logger.info(String.format("field %s is null -- did you forget to initialize it?", field.getName()));
					return;
				}

				String fnName = "getUnderlyingValue";
				Method meth = ReflectionUtils.findMethod(valueObj.getClass(), fnName);
				if (meth != null)
				{
					Object src = meth.invoke(valueObj);
					src = adjustSrcTo(src);
					fnName = "set" + uppify(field.getName());
					meth = findMatchingMethod(field, src);
					if (meth != null)
					{
						Logger.info("do: " + fnName + "=" + src);
						meth.invoke(modelToCopyTo, src);
					}
				} 
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		else if (List.class.isAssignableFrom(clazz))
		{
			field.setAccessible(true);

			try {
				Object listObj;
				listObj = field.get(form);
				if (listObj == null)
				{
					Logger.info(String.format("field %s is null -- did you forget to initialize it?", field.getName()));
					return;
				}

				List valueL = (List) listObj;
				List modelL = form.convertValueListToModelList(field.getName(), valueL);

				String fnName = "set" + uppify(field.getName());
				Method meth = findMatchingMethod(field, modelL);
				if (meth != null)
				{
					Logger.info("do: " + fnName + "=" + modelL);
					meth.invoke(modelToCopyTo, modelL);
				}
			}				
			catch (Exception e) 
			{
				e.printStackTrace();
			}

		}
	}

	//messy. convert list of Value to list of String
	private Object adjustSrcTo(Object src) 
	{
		if (src.getClass().equals(ArrayList.class))
		{
			ArrayList L = (ArrayList) src;
			ArrayList<String> newL = new ArrayList<String>();
			for(Object obj : L)
			{
				if (obj instanceof Value)
				{
					newL.add(obj.toString());
				}
			}
			return newL;
		}
		return src;
	}

	private Method findMatchingMethod(Field field, Object src)
	{
		String fnName = "set" + uppify(field.getName());
		Method meth = ReflectionUtils.findMethod(modelToCopyTo.getClass(), fnName, src.getClass());
		if (meth != null)
		{
			return meth;
		}

		if (src.getClass().equals(Integer.class))
		{
			meth = ReflectionUtils.findMethod(modelToCopyTo.getClass(), fnName, int.class);
			if (meth != null)
			{
				return meth;
			}
		}
		else if (src.getClass().equals(Boolean.class))
		{
			meth = ReflectionUtils.findMethod(modelToCopyTo.getClass(), fnName, boolean.class);
			if (meth != null)
			{
				return meth;
			}
			//			meth = ReflectionUtils.findMethod(modelToCopyTo.getClass(), fnName, Boolean.class);
			//			if (meth != null)
			//			{
			//				return meth;
			//			}
		}
		else if (src.getClass().equals(Long.class))
		{
			meth = ReflectionUtils.findMethod(modelToCopyTo.getClass(), fnName, long.class);
			if (meth != null)
			{
				return meth;
			}
		}
		else if (src.getClass().equals(Double.class))
		{
			meth = ReflectionUtils.findMethod(modelToCopyTo.getClass(), fnName, double.class);
			if (meth != null)
			{
				return meth;
			}
		}
		else if (src.getClass().equals(Date.class))
		{
			meth = ReflectionUtils.findMethod(modelToCopyTo.getClass(), fnName, Date.class);
			if (meth != null)
			{
				return meth;
			}
		}
		else if (src.getClass().equals(String.class))
		{
			meth = ReflectionUtils.findMethod(modelToCopyTo.getClass(), fnName, String.class);
			if (meth != null)
			{
				return meth;
			}
		}
		else if (src.getClass().equals(ArrayList.class))
		{
			meth = ReflectionUtils.findMethod(modelToCopyTo.getClass(), fnName, List.class);
			if (meth != null)
			{
				return meth;
			}
		}

		return null;
	}

	private String uppify(String name) 
	{
		String upper = name.toUpperCase();
		String s = upper.substring(0, 1);
		s += name.substring(1);
		return s;
	}	

}
