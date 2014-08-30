package io.github.quackmatic.gloop;

import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * A JPanel that renders a game screen.
 * @author Quackmatic
 * @see GameScreen
 */
public class GameScreenPanelRenderer extends JPanel {
	private static final long serialVersionUID = -6989759852874324842L;
	
	protected int panelWidth, panelHeight;
	protected GameScreen gameScreen;
	
	/**
	 * Create a new GameScreenPanelRenderer.
	 * @param gameScreen The {@link GameScreen} to render.
	 */
	public GameScreenPanelRenderer(GameScreen gameScreen) {
		this.setDoubleBuffered(true);
		setGameScreen(gameScreen);
	}
	
	/**
	 * Sets the {@link GameScreen} associated with this GameScreenPanelRenderer
	 * to the given game screen. 
	 * @param gameScreen The new game screen to render.
	 * @return Returns this, so you can chain these calls.
	 */
	public GameScreenPanelRenderer setGameScreen(GameScreen gameScreen) {
		if(this.gameScreen != gameScreen) {
			if(this.gameScreen != null) {
				this.gameScreen.getResizedEvent().remove(this);
			}
			this.gameScreen = gameScreen;
			this.gameScreen.getResizedEvent().add(this, new Runnable() {
				@Override
				public void run() {
					updateDimensions();
				}
			});
			updateDimensions();
		}
		return this;
	}
	
	@Override
	protected void finalize() throws Throwable {
		try {
			this.gameScreen.getResizedEvent().remove(this);
		} finally {
			// TODO free screen resources?
		}
	}
	
	/**
	 * Gets the {@link GameScreen} associated with this GameScreenPanelRenderer
	 * @return Returns the {@link GameScreen} associated with this GameScreenPanelRenderer.
	 */
	public GameScreen getGameScreen() {
		return this.gameScreen;
	}
	
	/**
	 * Updates the dimensions of this renderer to match the scale of the game screen.
	 * @return Returns this, so you can chain these calls.
	 */
	public GameScreenPanelRenderer updateDimensions() {
		if(this.gameScreen != null) {
			if(panelWidth != this.gameScreen.getWidth() * this.gameScreen.getScale() ||
			   panelHeight != this.gameScreen.getHeight() * this.gameScreen.getScale()) {
				panelWidth = this.gameScreen.getWidth() * this.gameScreen.getScale();
				panelHeight = this.gameScreen.getHeight() * this.gameScreen.getScale();
				this.setSize(panelWidth, panelHeight);
			}
		}
		return this;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(this.gameScreen != null) {
			g.drawImage(this.gameScreen.getRenderImage(), 0, 0, panelWidth, panelHeight, this);
		}
	}
}
