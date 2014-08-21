package io.github.quackmatic.gloop;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Defines a game screen class that can have multiple buffers.
 * @author Quackmatic
 */
public class GameScreen {
	private int width;
	private int height;
	private int scale;
	private BufferedImage[] images;
	private Graphics2D[] graphics;
	private volatile int currentBuffer = 0;
	private int buffers;
	
	/**
	 * Create a new double buffered game screen.
	 * @param initialWidth The initial width of the screen.
	 * @param initialHeight The initial height of the screen.
	 * @param initialScale The initial scale of the screen. This is used by the render target.
	 */
	public GameScreen(int initialWidth, int initialHeight, int initialScale) {
		this(initialWidth, initialHeight, initialScale, 2);
	}
	
	/**
	 * Create a new buffered game screen.
	 * @param initialWidth The initial width of the screen.
	 * @param initialHeight The initial height of the screen.
	 * @param initialScale The initial scale of the screen. This is used by the render target.
	 * @param buffers The number of screen buffers. 
	 */
	public GameScreen(int initialWidth, int initialHeight, int initialScale, int buffers) {
		setWidth(initialWidth);
		setHeight(initialHeight);
		setScale(initialScale);
		if(buffers < 1) {
			throw new Error("Cannot have less than one screen buffer.");
		} else if(buffers > 3) {
			System.err.println("WARNING: More than 3 screen buffers is typically not needed.");
		}
		this.buffers = buffers;
		
		this.images = new BufferedImage[getBuffers()];
		this.graphics = new Graphics2D[getBuffers()];
		recreateBuffers();
	}
	
	/**
	 * Recreates the screen buffer images. Call this after resizing.
	 * @return Returns this, so you can chain these calls.
	 */
	public GameScreen recreateBuffers() {
		for(int i = 0; i < getBuffers(); i++) {
			this.images[i] = createImage();
			this.graphics[i] = this.images[i].createGraphics();
		}
		return this;
	}
	
	/**
	 * Create a {@link BufferedImage} with the appropriate size.
	 * @return Returns a new BufferedImage with the appropriate size.
	 */
	private BufferedImage createImage() {
		return new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
	}

	/**
	 * Gets the number of screen buffers.
	 * @return Returns the number of screen buffers.
	 */
	public int getBuffers() {
		return buffers;
	}

	/**
	 * Gets the width of the current screen buffer.
	 * @return Returns the width of the current screen buffer.
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Sets the width of the screen buffer.
	 * @param width The width of the screen buffer.
	 * @return Returns this, so you can chain these calls.
	 */
	public GameScreen setWidth(int width) {
		this.width = width;
		return this;
	}

	/**
	 * Gets the height of the screen buffer.
	 * @return The height of the screen buffer.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the height of the screen buffer.
	 * @param height The height of the screen buffer.
	 * @return Returns this, so you can chain these calls.
	 */
	public GameScreen setHeight(int height) {
		this.height = height;
		return this;
	}
	
	/**
	 * Gets the scale of the screen buffer.
	 * @return Returns the scale of the screen buffer.
	 */
	public int getScale() {
		return scale;
	}
	
	/**
	 * Sets the scale of the screen buffer.
	 * @param scale The scale of the screen buffer.
	 * @return Returns this, so you can chain these calls.
	 */
	public GameScreen setScale(int scale) {
		this.scale = scale;
		return this;
	}
	
	/**
	 * Gets the image to render to the render target.
	 * @return The previous buffer that is not currently being drawn to.
	 */
	public BufferedImage getRenderImage() {
		return images[(currentBuffer + buffers - 1) % buffers];
	}

	/**
	 * Gets the current image that is the draw target.
	 * @return The iamge that shall be drawn to in the render system.
	 */
	public BufferedImage getImage() {
		return images[currentBuffer];
	}

	/**
	 * Gets the {@link Graphics2D} associated with the current image that is the render target.
	 * @return The {@link Graphics2D} associated with the current image that is the render target.
	 */
	public Graphics2D getGraphics() {
		return graphics[currentBuffer];
	}
	
	/**
	 * Increment (and, if necessary, wrap around) the current buffer index.
	 * Call this after drawing.
	 */
	public void nextBuffer() {
		currentBuffer = (currentBuffer + 1) % buffers;
	}
}