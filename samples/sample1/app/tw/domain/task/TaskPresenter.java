package tw.domain.task;

import org.thingworld.IFormBinder;
import org.thingworld.InterceptorContext;
import org.thingworld.MContext;
import org.thingworld.Presenter;
import org.thingworld.Reply;
import org.thingworld.Request;
import org.thingworld.log.Logger;
import org.thingworld.util.SfxTrail;

import tw.entities.Task;

public class TaskPresenter extends Presenter
{
	public static class InsertCmd extends Request
	{
		public InsertCmd(long userId) {
			this.userId = userId;
		}
		public int a;
		public String s;
		public long userId;
	}
	public static class UpdateCmd extends Request
	{
		public UpdateCmd(long id, IFormBinder binder)
		{
			this.entityId = id;
			this.binder = binder;
		}
	}

	private TaskReply reply = new TaskReply();
	public SfxTrail trail = new SfxTrail();

	public TaskPresenter(MContext mtx)
	{
		super(mtx);
	}
	protected Reply createReply()
	{
		return reply;
	}

	public void onInsertCmd(InsertCmd cmd)
	{
		Logger.log("insert");
		trail.add("index");

		Task scooter = new Task();
		scooter.setS(cmd.s);
		scooter.setUserId(cmd.userId);

		insertEntity(scooter);
		reply.setDestination(Reply.VIEW_INDEX);
	}

//	public void onUpdateCmd(UpdateCmd cmd) throws Exception 
//	{
//		Logger.log("update");
//		trail.add("update");
//		//binding fails handled in interceptor
//		TaskTwixt twixt = (TaskTwixt) cmd.getFormBinder().get();
//		Logger.log("twixt a=%s", twixt.s);
//		Task scooter = loadEntity(cmd);
//		twixt.copyTo(scooter);
//		updateEntity(scooter);
//		reply.setDestination(Reply.VIEW_INDEX);
//	}
//
//	private Task loadEntity(Request cmd) throws Exception
//	{
//		Task scooter = null;
//		scooter = (Task) mtx.loadEntity(Task.class, cmd.getEntityId());
//		return scooter;
//	}

	protected void beforeRequest(Request request, InterceptorContext itx)
	{
		trail.add("before");
	}
	protected void afterRequest(Request request)
	{
		trail.add("after");
	}
}
