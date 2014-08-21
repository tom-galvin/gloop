package io.github.quackmatic.gloop;

/**
 * Defines a method that is ran each tick of a {@link GameTimer} at a roughly-constant interval.
 * @see GameTimer
 * @author Quackmatic
 */
public interface GameTimerTickHandler {
	/**
	 * Any operations to be performed at each interval of a {@link GameTimer}.
	 * @param delta The time step in seconds since the last tick.
	 * @param total The total time elapsed since the game timer started.
	 * @param slow Whether or not the game timer is running slowly - ie. if delta is greater than the interval.
	 * @see GameTimer
	 */
	public void tick(double delta, double total, boolean slow);
}
