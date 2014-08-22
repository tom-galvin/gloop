package io.github.quackmatic.gloop;

import java.util.HashMap;

/**
 * Provides a means of subscribing multiple handlers to an event that can be
 * raised, in a thread-safe manner.
 * @author Quackmatic
 */
public class GameEvent {
	private HashMap<Object, Runnable> subscribed;
	private Object lock;
	
	/**
	 * Create a new GameEvent with no handlers.
	 */
	public GameEvent() {
		this.subscribed = new HashMap<Object, Runnable>();
		this.lock = new Object();
	}
	
	/**
	 * Add an event handler with the given key.
	 * @param key The key for this event handler. This will be used in
	 * the event that the handler needs to be removed.
	 * @param subscriber The {@link Runnable} called whenever the event
	 * is raised.
	 * @return Returns this, so you can chain these calls.
	 */
	public GameEvent add(Object key, Runnable subscriber) {
		synchronized (lock) {
			subscribed.put(key, subscriber);
		}
		return this;
	}
	
	/**
	 * Remove an event handler with the given key.
	 * @param key The key of the event handler to remove.
	 * @return Returns this, so you can chain these calls.
	 */
	public GameEvent remove(Object key) {
		synchronized (lock) {
			subscribed.remove(key);
		}
		return this;
	}
	
	/**
	 * Removes all the event handlers subscribed to this event.
	 * @return Returns this, so you can chain these calls.
	 */
	public GameEvent removeAll() {
		synchronized (lock) {
			subscribed.clear();
		}
		return this;
	}
	
	/**
	 * Raises this event in a thread-safe manner.
	 * @return Returns this, so you can chain these calls.
	 */
	public GameEvent raise() {
		synchronized (lock) {
			for(Runnable subscriber : subscribed.values()) {
				subscriber.run();
			}
		}
		return this;
	}
}
