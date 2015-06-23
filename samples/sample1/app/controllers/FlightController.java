package controllers;

import org.mef.twixt.binder.TwixtBinder;

import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import tw.dao.FakeDAO;
import tw.entities.Flight;
import twixt.FlightTwixt;

public class FlightController extends Controller 
{
	private FakeDAO dao = new FakeDAO();

    public Result index() 
    {
    	if (dao.size() == 0)
    	{
    		dao.initFlights();
    	}
        return ok(views.html.Flight.index.render(dao.data()));
    }
    
    public Result tedit(Long id) {
    	Flight flight = dao.findFlightById(id);
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
	    	Flight flight = dao.findFlightById(id);
	    	twixt.copyTo(flight);
			dao.insertDataDirect(id, flight);
			
			return redirect(controllers.routes.FlightController.index());
		}
		else
		{
			Logger.info("bind failed..");
	    	Form<FlightTwixt> frm = binder.getForm(); //with errors
	    	
	        return ok(views.html.Flight.tedit.render(id, frm, new FlightTwixt()));
		}
    }
}
