Twixt is an alternative binding framework for Java Play 2.4 apps.

Play Framework documentation recommends that forms should be made from model objects.  By doing this, the validation 
annotations in the model are used during binding.  This works well in simple CRUD scenarios, but has limitations in larger applications:

   * the form may only need a subset of model fields.  Binding may fail due to missing @Required fields.
   * the same field may be used on multiple forms with different validation requirements.
   * a form may contain fields from multiple models.

A *twixt* is an object that sits between the model and the view.  
The fields in a twixt object have their own validation and formatting, separate  from the model. 

### TwixtForm

A twixt class derives from TwixtForm and consists of public "value" objects

	  public class User extends TwixtForm {
		public StringValue firstName;
		public StringValue lastName;
		public IntegerValue age;
	  }
  
  
Field names should match the corresponding model fields (although there are ways around this).
	
###Rendering a Form view
 
Let's looks at a controller's typical edit action:

      User user = User.find().getById(id); //get model
      UserTwixt twixt = new UserTwixt();   //create twixt
      twixt.copyFromModel(user);           //load twixt from the model
      Form<UserTwixt> form = Form.form(UserTwixt.class).fill(twixt);
      //..render the form

*copyFromModel* copies fields from the model (using its getters and setters). 

###Form Binding
On post-back, use a **TwixtBinder** to bind the data in the web request to your **TwixtForm**. 

      TwixtBinder<UserTwixt> binder = new TwixtBinder();
      if (! binder.bind()) {
        Form<UserTwixt> form = Form.form(UserTwixt.class).fill(binder.get());
        //render the form again with validation errors and user's input data
      } else {
        UserTwixt twixt = binder.get();
        User user = User.find().getById(id);
        twixt.copyToModel(user);  //copy data back to model
        user.update();
      }

*copyToModel* copies matching fields to the model.  Other fields in the model are not affected.     
This is useful when the model has been retrieved from the database.  *copyToModel* merges in the data from the form,
and the model can now be saved back to the database.

###Lists
If your model has fields that are lists, you need to define several functions in your twixt.  Let's say your model class contains
a list of email addresses

	   private List<String> emails;
	   public List<String> getEmails() { return emails; }
	   public void setEmails(List<String> emails) { this.emails = emails; }
   
The twixt class would have a list of *StringValue*

 	  public List<StringValue> emails;

Add the following three methods to the twixt class:

	@Override
	public List convertModelListToValueList(String fieldName, List modelL) 
	{
		if (fieldName.equals("emails"))
		{
			return this.copyStringList((List<String>) modelL); //returns a List<StringValue>
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
			return this.copyStringValueList((List<StringValue>) valueL); //returns a List<String>
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
   

## Custom Data Copying
Override *copyFrom* and *copyTo* in your twixt class.  Copy model fields directly or use any mechanism you like.

	@Override
	public void copyFrom(Object model) 
	{
		//add custom code here to copy from model to 'this'
	}

	@Override
	public void copyTo(Object model) 
	{
		//add custom code here to copy from 'this' to model
	}

The following two helpers can be used.  You can use them to copy most of the fields.  Fields that you want to handle yourself
should be specified in *fieldsNotToCopy*.

	protected void copyFieldsFromModel(Object model, String... fieldsToNotCopy); 
	protected void copyFieldsToModel(Object model, String... fieldsToNotCopy);
	  
## Value Objects
Each twixt class consists of public fields that are **Value objects**.  A value class contains its own validation and formatting.
Twixt provides BooleanValue, IntegerValue, StringValue, ListValue, and others.  

You can create value classes for your domain objects, such as: email, phone number, person name, and zip code.  
Here is a phone number class:

	  public class PhoneNumberValue extends StringValue
	  {
		 private class PhoneValidator {
			public void validate(ValContext vtx, Value val) {
			 Pattern p = Pattern.compile("^(?=.{7,32}$)(\\(?\\+?[0-9]*\\)?)?[0-9_\\- \\(\\)]*((\\s?x\\s?|ext\\s?|extension\\s?)\\d{1,5}){0,1}$");  
			 String phone = val.toString();
			 boolean matchFound = p.matcher(phone).matches();
			 if (! matchFound) {
				vtx.addError("Not a valid phone number.");
			 }
			}
		 }

		 public PhoneNumberValue()
		 {
			super(new PhoneValidator());
		 }
	  }
	  
Validation is code-based, not annotation-based.	  

##Converters
TBD converts data to/from string

## Views
Value objects render using their toString() method. 
For example, if the twixt form had a StringValue field named <b>name</b>, render
it in the form like this:

    @inputText(sampleForm("name"))

## Listbox

Use a SelectValue in the twixt

	public SelectValue lang = new SelectValue("lang");

Initialize the value to hold a map of values

		Map<String,String> opt = new HashMap<>();
		//first value is what the field value will be, 2nd value is display value
		opt.put("German", "German");
		opt.put("French", "French");
		lang.setOptions(opt);
   
Pass both the twixt and the form to the view.

    @(id:Long, frm: Form[twixt.UserTwixt], twixt:twixt.UserTwixt)
   
Render the listbox in the view:

	@helper.select(
        sampleForm("lang"),
        options = options(twixt.lang.options()))   
   
   
   
   
