package services;

import org.thingworld.Permanent;
import org.thingworld.persistence.PersistenceContext;

import tw.domain.task.TaskInitializer;

public class MyPerm extends Permanent
{
//	public UserTaskRM readModel1;
	public TaskInitializer taskInit;
//	public UserInitializer userInit;

	public MyPerm(PersistenceContext persistenceCtx) 
	{
		super(persistenceCtx);

//		readModel1 = new UserTaskRM();
//		registerReadModel(readModel1);
//
		taskInit = new TaskInitializer();
		taskInit.init(this);
//		userInit = new UserInitializer();	
		
	}
}