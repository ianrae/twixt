
Twixt is an alternative binding framework for Java Play apps.

Play documentation recommends making forms from model objects.  The validation annotations on the model are used
during binding.  This works well in simple CRUD scenarios, but has limitations in larger applications.  Such as:

   * the form may only have a subset of model fields.  Binding may fail due to missing @Required fields.
   * the same field may be used on multiple forms with different validation requirements.
   * a form may contain fields from multiple models.

A twixt is an object that sits between the model and the view.  The twixt fields have their own validation and formatting, separate
 from the model.

      User user = User.find().getById(id)
      UserTwixt twixt = new UserTwixt();
      twixt.copyFromModel(user);
      Form<UserTwixt> form = Form.form(UserTwixt.class).fill(twixt);
      //..render the form

copyFromModel copies fields automatically, or can be overridden to do custom copying.

On post-back, use a TwixtBinder to bind the HTTP request into your TwixtForm:

      TwixtBinder<UsreTwixt> binder = new TwixtBinder();
      if (! binder.bind()) {
        Form<UserTwixt> form = Form.form(UserTwixt.class).fill(binder.get());
        //render the form again with validation errors and user's input data
      } else {
        UserTwixt twixt = binder.get();
        User user = User.find().getById(id);
        twixt.copyToModel(user);
        user.update();
      }

 Twixt fields are instances of Twixt value classes.  A value class contains its own validation and formatting.  
 Twixt provides BooleanValue, IntegerValue, StringValue, ListValue, and others.
 Create your own classes to represent the types of data you need:

	  public class PhoneNumberValue extends StringValue
	  {
		 class PhoneValidator {
			public void validate(ValContext vtx, Value val) {
			 Pattern p = Pattern.compile("^(?=.{7,32}$)(\\(?\\+?[0-9]*\\)?)?[0-9_\\- \\(\\)]*((\\s?x\\s?|ext\\s?|extension\\s?)\\d{1,5}){0,1}$");  
			 String phone = val.toString();
			 Matcher m = p.matcher(phone);
			 boolean matchFound = m.matches();
			 if (! matchFound) {
				vtx.addError("Not a valid phone number.");
			 }
			}
		 }

		 public PhoneNumberValue()
		 {
			super();
			setValidator(new PhoneValidator());
		 }
	  }

# Automatic CRUD Controllers
Twixt integrates with play-crud to provide automatic CRUD.  The simplest is to create a controller based
on DynamicTwixtController. It will index, newForm, create, edit, update, show, and index actions. It will also render
views for each automatically.

If you want to define your own views, create a controller class based on TwixtController. It defines the same actions but
you must define the view .scala.html files.


   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   

Twixt is a binding library for Play Framework Java 2.3.x applications.

A "twixt" is an object that sits between a form view and the model.  Instead of building a form from a model object, use a twixt.
It allows the form to present data differently than the model.  The form can:

 * have a subset of model fields
 * split model fields into multiple UI fields, such as a phone number into an Area Code textbox and a Phone Number text box.
 * have fields from multiple models on a single form
 * have different validation rules for the same field when it appears on different forms

A twixt form consists of value objects (based on org.mef.twixt.Value).  Twixt provides these basic value types

 * StringValue
 * IntegerValue, LongValue, and DoubleValue
 * BooleanValue
 * DateValue
 * ListValue, SelectValue, and LongSelectValue

Values have plug-in validators which are a code-based alternative to the annotation-based approach to validation provided by Play.
 
Twixt is an extension of play-crud, and provides a controller (DyanamicTwixtController) that dynamically creates form views based on their
data values.