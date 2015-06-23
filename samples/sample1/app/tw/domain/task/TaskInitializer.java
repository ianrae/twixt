package tw.domain.task;

import org.thingworld.BindingIntercept;
import org.thingworld.IDomainIntializer;
import org.thingworld.MContext;
import org.thingworld.Permanent;
import org.thingworld.entity.EntityManagerRegistry;
import org.thingworld.entity.EntityMgr;

import tw.entities.Task;

public class TaskInitializer implements IDomainIntializer
{
	private Permanent perm;
	
	public TaskRM taskRM;

	@Override
	public void init(Permanent perm)
	{
		//create long-running objects
		this.perm = perm;
		EntityManagerRegistry registry = perm.getEntityManagerRegistry();
		registry.register(Task.class, new EntityMgr<Task>(Task.class));
		
		taskRM = new TaskRM();
		perm.registerReadModel(taskRM);
	}

	public TaskPresenter createMyPres(MContext mtx)
	{
		TaskPresenter pres = new TaskPresenter(mtx);
		return pres;
	}
	public TaskPresenter createMyPres(MContext mtx, int failDestination)
	{
		TaskPresenter pres = new TaskPresenter(mtx);
		pres.addInterceptor(new BindingIntercept(failDestination));
		return pres;
	}
}