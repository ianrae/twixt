package tw.domain.task;

import org.thingworld.readmodel.AllIdsRM;

import tw.entities.Task;

public class TaskRM extends AllIdsRM<Task>
{
	public TaskRM()
	{
		super("task", Task.class);
	}
}