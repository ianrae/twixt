package controllers;

import java.util.ArrayList;
import java.util.List;

import org.thingworld.MContext;
import org.thingworld.Reply;

import models.UserModel;

import com.google.inject.Inject;

import play.mvc.*;
import services.MyPerm;
import services.MyService;
import tw.domain.task.TaskPresenter;
import tw.entities.Task;
//import services.TestService;
import views.html.*;

public class TaskController extends Controller {

	//myservice is a singleton so all controllers share same instance
	//created at startup
    @Inject
    private MyService svc;
    
    
    static int count;

    public Result index() {
 //       testService.echo("saeed");
    	List<Task> L = new ArrayList<>();
        return ok(views.html.Task.index.render(L));
    }

    public Result create() {
 //       testService.echo("saeed");
    	
    	TaskPresenter pres = svc.perm.taskInit.createMyPres(svc.perm.createMContext());
    	
    	long userId = 2;
		TaskPresenter.InsertCmd cmd = new TaskPresenter.InsertCmd(userId);
		cmd.a = 101;
		count++;
		cmd.s = String.format("susan%d", count+1);
		Reply reply = pres.process(cmd);    	
    	
    	List<Task> L = new ArrayList<>();
        return ok(views.html.Task.index.render(L));
    }
    
    public Result show() throws Exception
    {
 //       testService.echo("saeed");

		MContext mtx = svc.perm.createMContext();
		mtx.acquire(svc.perm.taskInit.taskRM.getClass());
		
		List<Task> L = svc.perm.taskInit.taskRM.queryAll(mtx);    	
        return ok(views.html.Task.index.render(L));
    }
}
