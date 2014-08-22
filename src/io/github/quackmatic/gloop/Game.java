package io.github.quackmatic.gloop;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * A base class for a game.
 * @author Quackmatic
 */
public abstract class Game {
	/**
	 * The screen that a game container will render to a render target.
	 */
	protected GameScreen gameScreen;
	
	/**
	 * Creates a new instance of this game.
	 */
	public Game() {
		gameScreen = null;
	}

	/**
	 * Resizes the game screen.
	 * @param componentWidth The width of the parent's render target component.
	 * @param componentHeight The height of the parent's render target component.
	 */
	public void resize(int componentWidth, int componentHeight) {
		int pixelScale = getScale(getDiagonal(componentWidth, componentHeight));
		if(gameScreen == null) {
			gameScreen = new GameScreen(
				componentWidth / pixelScale,
				componentHeight / pixelScale,
				pixelScale);
		} else {
			// reinit framebuffers
			gameScreen.setWidth(componentWidth / pixelScale);
			gameScreen.setHeight(componentHeight / pixelScale);
			gameScreen.setScale(pixelScale);
			gameScreen.recreateBuffers();
		}
	}
	
	/**
	 * Gets the diagonal, corner-to-corner size of a render target component.
	 * @param width The width of the render target component.
	 * @param height The height of the render target component.
	 * @return
	 */
	private final int getDiagonal(int width, int height) {
		return (int)Math.sqrt(
				   width * width +
				   height * height);
	}
	
	/**
	 * Get the pixel scale from the diagonal size of the render target component.
	 * @param diagonal The diagonal size of the render target component.
	 * @return The pixel scale to render with.
	 */
	public abstract int getScale(int diagonal);
	
	/**
	 * Perform any resource loading for this game.
	 */
	public abstract void load();
	
	/**
	 * Perform any memory initialisation for this game.
	 */
	public abstract void init();
	
	/**
	 * Perform any memory destruction for this game.
	 */
	public abstract void destroy();
	
	/**
	 * Gets the time, in seconds, for every render frame.
	 * @return The time, in seconds, for every render frame.
	 */
	public abstract double getFrameTime();
	
	/**
	 * Gets the time, in seconds, for every game tick.
	 * @return The time, in seconds, for every game tick.
	 */
	public abstract double getTickTime();
	
	/**
	 * Perform any operations to be performed at each interval of the {@link GameTimer} controlling
	 * this game's main loop.
	 * @param delta The time step in seconds since the last tick.
	 * @param total The total time elapsed since the game tick timer started.
	 * @param slow Whether or not the game tick timer is running slowly - ie. if delta is greater than the interval.
	 * @see GameTimer
	 */
	public abstract void tick(double delta, double total, boolean slow);
	
	/**
	 * Perform any drawing operations.
	 * @param delta The time step in seconds since the last draw.
	 * @param total The total time elapsed since the game draw timer started.
	 * @param slow Whether or not the game draw timer is running slowly - ie. if delta is greater than the interval.
	 * @param buffer The buffer being drawn to.
	 * @param graphics The {@link Graphics2D} associated with the buffer being drawn to.
	 */
	public abstract void draw(double delta, double total, boolean slow, BufferedImage buffer, Graphics2D graphics);
}
