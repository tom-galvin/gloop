package io.github.quackmatic.gloop;

/**
 * Defines a game timer solution that will tick every given interval,
 * and provides notification if the game loop is running slow.
 * @author Quackmatic
 */
public class GameTimer {
	/**
	 * The default {@link GameTimerTickHandler} for new timers.
	 * This is null and will raise an error if another tick handler is not set.
	 */
	public static final GameTimerTickHandler DEFAULT_TICK_HANDLER = null;

	/**
	 * The default {@link GameTimerErrorHandler} for new timers.
	 * This prints the throwable's stack trace.
	 */
	public static final GameTimerErrorHandler DEFAULT_ERROR_HANDLER = new GameTimerErrorHandler() {
		@Override
		public void handle(Throwable e) {
			System.err.println("Consider writing your own GameTimerErrorHandler.");
			e.printStackTrace();
		}
	};

	/**
	 * The default {@link GameTimerCleanupHandler} for new timers.
	 * This does nothing.
	 */
	public static final GameTimerCleanupHandler DEFAULT_CLEANUP_HANDLER = new GameTimerCleanupHandler() {
		@Override
		public void cleanup() {
			// nothing
		}
	};
	
	/**
	 * The operation to perform at each interval of the game timer's tick.
	 */
	public GameTimerTickHandler tickHandler;
	
	
	/**
	 * Any operations to be performed in the event of a {@link Throwable}
	 * being thrown.
	 */
	public GameTimerErrorHandler errorHandler;
	
	/**
	 * Any operations to be performed at cleanup. This is performed in a finally block.
	 * Finally block.
	 */
	public GameTimerCleanupHandler cleanupHandler;
	
	/**
	 * The interval, in seconds, for the {@link GameTimerTickHandler} associated
	 * with this GameTimer to be ticked.
	 */
	public double interval;
	
	/**
	 * Whether or not to cap the <b>delta</b> parameter passed to the {@link GameTimerTickHandler}
	 * associated with this GameTimer to this timer's interval.
	 */
	public boolean capDelta;
	
	private Thread tickThread;
	private boolean running;
	
	/**
	 * Create a new GameTimer with default values and handlers.
	 */
	public GameTimer() {
		this.running = false;
		
		this
			.setTickHandler(DEFAULT_TICK_HANDLER)
			.setErrorHandler(DEFAULT_ERROR_HANDLER)
			.setCleanupHandler(DEFAULT_CLEANUP_HANDLER)
			.setInterval(0)
			.setCapDelta(false);
	}
	
	/**
	 * Sets the timer interval, in seconds. If the game tick takes longer
	 * than this them the game loop may slow down. Delta will be adjusted
	 * accordingly unless <b>capDelta</b> is set to true.
	 * @param interval The timer interval in seconds.
	 * @return This, so you can chain these calls.
	 */
	public GameTimer setInterval(double interval) {
		this.interval = interval;
		return this;
	}
	
	/**
	 * Sets the tick handler.
	 * @param handler The tick to perform at each interval of the game timer's tick.
	 * @return Returns this, so you can chain these calls.
	 */
	public GameTimer setTickHandler(GameTimerTickHandler handler) {
		this.tickHandler = handler;
		return this;
	}
	
	/**
	 * Sets the error handler.
	 * @param handler The error handler called whenever an exception is thrown in a game tick handler.
	 * @return Returns this, so you can chain these calls.
	 */
	public GameTimer setErrorHandler(GameTimerErrorHandler handler) {
		this.errorHandler = handler;
		return this;
	}
	
	/**
	 * Sets the cleanup handler.
	 * @param handler The cleanup handler, called in a finally block, once this timer is stopped.
	 * @return Returns this, so you can chain these calls.
	 */
	public GameTimer setCleanupHandler(GameTimerCleanupHandler handler) {
		this.cleanupHandler = handler;
		return this;
	}
	
	/**
	 * Sets whether or not to cap the <b>delta</b> parameter passed to
	 * the {@link GameTimerTickHandler} associated with this GameTimer to this timer's interval.
	 * @param capDelta Whether or not to cap the <b>delta</b> parameter.
	 * @return Returns this, so you can chain these calls.
	 */
	public GameTimer setCapDelta(boolean capDelta) {
		this.capDelta = capDelta;
		return this;
	}

	/**
	 * Starts this game timer's thread.
	 * @return Returns this, so you can chain these calls.
	 */
	public GameTimer start() {
		return start(false);
	}
	
	/**
	 * Starts this game timer's thread.
	 * @param force Force a stop. If true, no exceptions thrown if not running.
	 * @return Returns this, so you can chain these calls.
	 */
	public GameTimer start(boolean force) {
		if(!running) {
			(tickThread = new Thread() {
				@Override
				public void run() {
					gameThread();
				}
			}).start();
		} else if(!force) {
			throw new Error("Game loop is already running.");
		}
		return this;
	}
	
	private void gameThread() {
		running = true;
		try {
			long beginTime = System.nanoTime();
			double previousDelta = 0.0;
			boolean runningSlowly = false;
			
			while(running) {
				if(interval <= 0) throw new Error("GameTimer interval must be >= 0.");
				
				long frameStartTime = System.nanoTime();
				double givenDelta = capDelta ?
						Math.min(previousDelta, interval) :
						previousDelta; // works out the (maybe) capped delta
						
				tickHandler.tick(
						givenDelta,
						(double)(frameStartTime - beginTime) / 1e+9,
						runningSlowly);
				
				long frameDeltaTime = System.nanoTime() - frameStartTime;
				double runningTime = (double)(frameDeltaTime) / 1e+9;
				
				long sleepTime = (long)(interval * 1e+9) - frameDeltaTime;
				runningSlowly = sleepTime < 0;
				if(runningTime < interval) {
					if(sleepTime >= 100000) {
						// only sleep if sleepTime's not too small
						Thread.sleep(
								sleepTime / 1000000l,
								(int)(sleepTime % 1000000l));
					}
				}
				previousDelta = (double)
						(System.nanoTime() - frameStartTime) / 1e+9;
			}
		} catch(InterruptedException e) {
			// ok
		} catch(Exception e) {
			errorHandler.handle(e);
		} finally {
			cleanupHandler.cleanup();
		}
	}
	
	/**
	 * Determine if this timer is currently running or not.
	 * @return Not rocket science, is it?
	 */
	public boolean isRunning() {
		return this.running;
	}
	
	/**
	 * Stops the game timer. If a tick is already taking place, it will finish first.
	 * @return Returns this, so you can chain these calls.
	 */
	public GameTimer stop() {
		return stop(false);
	}
	
	/**
	 * Stops the game timer. If a tick is already taking place, it will finish first.
	 * @param force Force a stop. If true, no exceptions thrown if not running.
	 * @return Returns this, so you can chain these calls.
	 */
	public GameTimer stop(boolean force) {
		if(running) {
			running = false;
		} else if(!force) {
			throw new Error("Game loop is not currently running.");
		}
		return this;
	}
}
