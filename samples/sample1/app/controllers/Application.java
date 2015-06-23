package controllers;

import java.util.List;

import models.UserModel;

import com.google.inject.Inject;

import play.mvc.*;
import services.DynaService;
import services.MyService;
//import services.TestService;
import views.html.*;

public class Application extends Controller {

	//myservice is a singleton so all controllers share same instance
	//created at startup
    @Inject
    private MyService testService;
    
    //this is not a singleton, so each controller gets its own instance
    //but controllers only created once per app restart
    @Inject
    private DynaService dynaService;
    
    static int count;

    public Result index() {
 //       testService.echo("saeed");
        return ok(index.render("Your new application is ready."));
    }

    public Result show() {
    	List<UserModel> L = UserModel.all();
    	if (L.size() == 0)
    	{
    		UserModel user = new UserModel();
    		user.setFirstName("bob");
    		user.setLastName("smith");
    		user.save();
    		
    		L.add(user);
    	}
    	else
    	{
    		UserModel user = new UserModel();
    		user.setFirstName(String.format("bill%d", count++));
    		user.setLastName("smith");
    		user.save();
    		
    		L.add(user);
    		
    	}
        return ok(show.render(L));
    }
}
