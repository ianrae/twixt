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
