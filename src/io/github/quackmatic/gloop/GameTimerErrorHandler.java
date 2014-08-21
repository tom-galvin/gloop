package io.github.quackmatic.gloop;

/**
 * Defines a method to handle thrown errors or exceptions in a game loop of a {@link GameTimer}.
 * @see GameTimer
 * @author Quackmatic
 */
public interface GameTimerErrorHandler {
	/**
	 * Any operations to be performed when an exception occurs in the tick operation of a {@link GameTimer}.
	 * @param e The exception, error or other throwable that occurred.
	 * @see GameTimer
	 */
	public void handle(Throwable e);
}
