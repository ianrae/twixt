package services;
import javax.inject.*;

import org.thingworld.persistence.PersistenceContext;

import tw.util.FactoryGirl;

@Singleton
public class MyService {

	public MyPerm perm;
	
	public MyService()
	{
		System.out.println("myservice!!!");
		
		PersistenceContext persistenceCtx = FactoryGirl.createPersistenceContext();
		perm = new MyPerm(persistenceCtx);
	}
}
