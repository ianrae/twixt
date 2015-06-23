
import play.*;
import play.db.DB;


public class Global extends GlobalSettings 
{

	public void onStart(Application app) 
	{
		Logger.info("----------- Application has started. version " + "999" + " -----------");
	}



	private String getAppPath()
	{
		String path = Play.application().path().getAbsolutePath();
		if (path.contains("target/universal"))
		{
			return "/app"; // /app/target/universal/stage/. heroku
		}
		else
		{
			return path;
		}
	}

	public void onStop(Application app) 
	{
		Logger.info("Application shutdown...");
	}

}
