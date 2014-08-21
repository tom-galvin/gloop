package io.github.quackmatic.gloop;

/**
 * Defines a method to clean up resources after a {@link GameTimer} is stopped.
 * @see GameTimer
 * @author Quackmatic
 */
public interface GameTimerCleanupHandler {
	/**
	 * Any operations to be performed once the {@link GameTimer} is stopped.
	 * @see GameTimer
	 */
	public void cleanup();
}
