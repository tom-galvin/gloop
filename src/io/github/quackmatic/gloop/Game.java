package io.github.quackmatic.gloop;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * A base class for a game.
 * @author Quackmatic
 */
public abstract class Game {
	/**
	 * Left mouse button.
	 */
	public static final int MOUSE_BUTTON_LEFT = 0;
	
	/**
	 * Middle mouse button or scroll wheel.
	 */
	public static final int MOUSE_BUTTON_MID = 1;
	
	/**
	 * Right mouse button.
	 */
	public static final int MOUSE_BUTTON_RIGHT = 2;
	
	/**
	 * Size of the <i>keyboard</i> {@link Boolean}[] array.
	 */
	public static final int KEY_ARRAY_SIZE = 1024;
	
	/**
	 * The screen that a game container will render to a render target.
	 */
	protected GameScreen gameScreen;
	
	/**
	 * The location of the mouse on the screen.
	 */
	protected Point mouse;
	
	/**
	 * The state of all the mouse buttons.
	 */
	protected boolean[] mouseButtons;
	
	/**
	 * The state of all the keyboard keys.
	 */
	protected boolean[] keyboard;
	
	/**
	 * Creates a new instance of this game.
	 */
	public Game() {
		gameScreen = null;
		mouse = new Point(-1, -1);
		mouseButtons = new boolean[3];
		keyboard = new boolean[1024];
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
			gameScreen.setWidth((int)Math.ceil((double)componentWidth / pixelScale));
			gameScreen.setHeight((int)Math.ceil((double)componentHeight / pixelScale));
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
	 * Called when a mouse button is pressed.
	 * @param button The mouse button.
	 */
	public abstract void mouseDown(int button);
	
	/**
	 * Called when a mouse button is released.
	 * @param button The mouse button.
	 */
	public abstract void mouseUp(int button);
	
	/**
	 * Called when the mouse wheel is scrolled.
	 * @param delta The scroll amount (negative is up, positive is down.)
	 */
	public abstract void mouseScroll(int delta);
	
	/**
	 * Called when a key is pressed.
	 * @param key The key code.
	 * @param keyChar The key character.
	 * @see {@link java.awt.event.KeyEvent}
	 */
	public abstract void keyDown(int key, char keyChar);
	
	/**
	 * Called when a key is released.
	 * @param key The key code.
	 * @param keyChar The key character.
	 * @see {@link java.awt.event.KeyEvent}
	 */
	public abstract void keyUp(int key, char keyChar);
	
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
