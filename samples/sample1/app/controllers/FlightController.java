package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mef.twixt.binder.TwixtBinder;
import org.thingworld.MContext;
import org.thingworld.Reply;

import models.UserModel;

import com.google.inject.Inject;

import play.Logger;
import play.data.Form;
import play.mvc.*;
import services.MyPerm;
import services.MyService;
import tw.domain.task.TaskPresenter;
import tw.entities.Flight;
import tw.util.TBinder;
import twixt.FlightTwixt;
//import services.TestService;
import views.html.*;

public class FlightController extends Controller {

	//myservice is a singleton so all controllers share same instance
	//created at startup
    @Inject
    private MyService svc;
    
    private static List<Flight> data = new ArrayList<>();
    
    static int count;

    public Result index() {
 //       testService.echo("saeed");
    	if (data.size() == 0)
    	{
    		initFlights(data);
    	}
        return ok(views.html.Flight.index.render(data));
    }
    
    public Result newEntity() {
    	Form<Flight> frm = Form.form(Flight.class);    	
        return ok(views.html.Flight.newflight.render(frm));
    }
    
    public Result create() {
    	Flight flight = Form.form(Flight.class).bindFromRequest().get();    	
    	Logger.info("--> " + flight.getS());
    	Long id = data.size() + 200L;
    	flight.setId(id);
    	data.add(flight);
        return redirect(controllers.routes.FlightController.index());
    }
    
    public Result edit(Long id) {
    	Flight flight = findFlightById(id);
    	Logger.info("ed: " + flight.getS());
    	Form<Flight> frm = Form.form(Flight.class);
    	frm.fill(flight);
        return ok(views.html.Flight.edit.render(flight.getId(), frm));
    }
    
    private Flight findFlightById(long id) 
    {
    	for(Flight flight : data)
    	{
    		if (flight.getId() == id)
    		{
    			return flight;
    		}
    	}
		return null;
	}

	public Result update(Long id) {
    	Flight flight = Form.form(Flight.class).bindFromRequest().get();    	
    	Logger.info("--> " + flight.getS());
    	insertData(id, flight);
        return redirect(controllers.routes.FlightController.index());
    }

    
    
	private void insertData(Long id, Flight target) 
	{
		int index = 0;
		for(Flight flight : data)
		{
			if (flight.getId() == id)
			{
				//data.set(index, target);
				//copy since id not passed
				flight.setS(target.getS());
				return;
			}
			index++;
		}
	}
	private void insertDataDirect(Long id, Flight target) 
	{
		int index = 0;
		for(Flight flight : data)
		{
			if (flight.getId() == id)
			{
				data.set(index, target);
				return;
			}
			index++;
		}
	}


	private void initFlights(List<Flight> L) 
	{
		Flight flight = new Flight();
		flight.setId(10L);
		flight.setS("abc");
		flight.setLang("German");
		flight.setUserId(100L);
		flight.setIsAdmin(true);
		flight.setStartDate(new Date());
		flight.setAccountTypeId(1L);
		flight.setEmails(buildEmails());
		L.add(flight);
		
		flight = new Flight();
		flight.setId(11L);
		flight.setS("def");
		flight.setLang("French");
		flight.setUserId(101L);
		flight.setStartDate(new Date());
		flight.setAccountTypeId(1L);
		flight.setEmails(buildEmails());
		L.add(flight);
	}

	private List<String> buildEmails() 
	{
		List<String> L = new ArrayList<>();
		L.add("emailA");
		L.add("emailB");
		return L;
	}

	//------twixt
    public Result tedit(Long id) {
    	Flight flight = findFlightById(id);
    	Logger.info("ed: " + flight.getS());
    	FlightTwixt twixt = new FlightTwixt();
    	Form<FlightTwixt> frm = twixt.createFilledForm(flight);
        return ok(views.html.Flight.tedit.render(flight.getId(), frm, twixt));
    }
    
	public Result tupdate(Long id)
	{
		TwixtBinder<FlightTwixt> binder = new TwixtBinder<FlightTwixt>(FlightTwixt.class);
		
		if (binder.bind())
		{
			FlightTwixt twixt = binder.get();
			Logger.info("--> " + twixt.s.get());
	    	Flight flight = findFlightById(id);
	    	twixt.copyTo(flight);
			insertDataDirect(id, flight);
			
			return redirect(controllers.routes.FlightController.index());
		}
		else
		{
			Logger.info("fff");
	    	Form<FlightTwixt> frm = binder.getForm(); //with errors
//	    	frm = frm.fill(binder.get());
	    	
//	    	frm.reject("my global error");
	    	
	        return ok(views.html.Flight.tedit.render(id, frm, new FlightTwixt()));
			
		}

    }
	
}
