package engine.events;

import engine.Actor;

public abstract class EventHandler
{
	public Actor parentActor;
	
	public void onAttach(Actor parentActor)
	{
		this.parentActor = parentActor;
		onAttach();
	}
	
	public abstract void onAttach();
	
	public abstract String getName();
	public abstract void handleEvent(EventArgumentContainer container);
	public abstract EventHandler clone();
}
