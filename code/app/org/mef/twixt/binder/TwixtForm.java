package org.mef.twixt.binder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.mef.twixt.*;
import org.mef.twixt.validate.ValContext;
import org.springframework.util.ReflectionUtils;

import play.data.Form;

public abstract class TwixtForm implements ValueContainer
{
	private class Facade implements  FormCopier.FieldCopier, ReflectionUtils.FieldCallback
	{
		@Override
		public void doWith(Field field)
		{
			Class<?> clazz = field.getType();
			if (Value.class.isAssignableFrom(clazz))
			{
				try 
				{
					field.setAccessible(true);
					if (field.get(TwixtForm.this) != null)
					{
						return; //skip ones that are already not null
					}

					Object obj = clazz.newInstance();
					field.set(TwixtForm.this, obj);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		}

		@Override
		public void copyFieldFromModel(FormCopier copier, Field field)
		{
			copier.copyFieldFromModel(field);
		}
		@Override
		public void copyFieldToModel(FormCopier copier, Field field)
		{
			copier.copyFieldToModel(field);
		}
	}

	private class ValidationFacade implements  ReflectionUtils.FieldCallback
	{
		private ValContext valctx;

		public ValidationFacade(ValContext valctx)
		{
			this.valctx = valctx;
		}

		@Override
		public void doWith(Field field)
		{
			Class<?> clazz = field.getType();
			if (Value.class.isAssignableFrom(clazz))
			{
				try 
				{
					field.setAccessible(true);
					Value value = (Value) field.get(TwixtForm.this);

					if (value != null)
					{
						valctx.setCurrentItemName(field.getName());
						value.validate(valctx);
					}
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		}
	}

	private Facade _facade = new Facade(); //avoid name clash, use _

	public TwixtForm()
	{}

	protected void initFields()
	{
		ReflectionUtils.doWithFields(this.getClass(), _facade, ReflectionUtils.COPYABLE_FIELDS);
	}


	@Override
	public void copyFrom(Object model) 
	{
		this.copyFieldsFromModel(model);
	}

	@Override
	public void copyTo(Object model) 
	{
		this.copyFieldsToModel(model);
	}

	protected void copyFieldsFromModel(Object model, String... fieldsToNotCopy) 
	{
		FormCopier copier = new FormCopier(_facade);
		copier.copyFromModel(model, this, fieldsToNotCopy);
	}
	protected void copyFieldsToModel(Object model, String... fieldsToNotCopy) 
	{
		FormCopier copier = new FormCopier(_facade);
		copier.copyToModel(this, model, fieldsToNotCopy);
	}


	@Override
	public void validate(ValContext valctx) 
	{
		//use reflection so we can set itemName for each Value
		ValidationFacade valfacade = new ValidationFacade(valctx);
		ReflectionUtils.doWithFields(this.getClass(), valfacade, ReflectionUtils.COPYABLE_FIELDS);
	}


	@SuppressWarnings("unchecked")
	public <T> Form<T> createFilledForm(Object entity)
	{
		this.copyFrom(entity);
		Form<T> frm = (Form<T>) Form.form(this.getClass());
		frm = frm.fill((T) this);
		return frm;
	}	

	//Methods that copy fields that are Lists.
	//Derived classes MUST override these if they have fields that are lists.
	public List convertModelListToValueList(String fieldName, List modelL)
	{
		return null;
	}
	public List convertValueListToModelList(String fieldName, List valueL)
	{
		return null;
	}

	//and provide default do-nothing implementation for this.
	//Derived classes MUST override these if they have fields that are lists.
	@Override
	public Value createListElement(String fieldName, String value)
	{
		return null;
	}

	//define some useful helpers
	public static List<StringValue> copyStringList(List<String> srcL) 
	{
		List<StringValue> valueL = new ArrayList<>();
		for(String tmp : srcL)
		{
			valueL.add(new StringValue(tmp));
		}	

		return valueL;
	}

	public static List<String> copyStringValueList(List<StringValue> valueL) 
	{
		List<String> modelL = new ArrayList<>();
		for(StringValue strval: valueL)
		{
			modelL.add(strval.toString());
		}

		return modelL;
	}

}
