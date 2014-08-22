package io.github.quackmatic.gloop;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * A JFrame implementation that displays a game.
 * @author Quackmatic
 */
public class GameFrame extends JFrame {
	private static final long serialVersionUID = -6105164417764443781L;
	private Game game;
	private GameTimer tickTimer, drawTimer;
	private GameScreenPanelRenderer panelRenderer;

	/**
	 * Create a new GameFrame.
	 * @param title The title of the game frame.
	 * @param game The game to render.
	 * @param initialWidth The initial width of the GameFrame.
	 * @param initialHeight The initial height of the GameFrame.
	 */
	public GameFrame(String title, final Game game, int initialWidth, int initialHeight) {
		super(title);
		this.game = game;
		this.game.load();
		
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				resizeGame();
			}
		});
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				stop();
			}
			
			@Override
			public void windowOpened(WindowEvent e) {
				super.windowOpened(e);
				game.init();
			}
		});
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				super.mouseMoved(e);
				game.mouse.x = e.getX();
				game.mouse.y = e.getY();
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				game.mouse.x = e.getX();
				game.mouse.y = e.getY();
				
				if(SwingUtilities.isLeftMouseButton(e)) {
					game.mouseButtons[Game.MOUSE_BUTTON_LEFT] = true;
					game.mouseDown(Game.MOUSE_BUTTON_LEFT);
				} else if(SwingUtilities.isMiddleMouseButton(e)) {
					game.mouseButtons[Game.MOUSE_BUTTON_MID] = true;
					game.mouseDown(Game.MOUSE_BUTTON_MID);
				} else if(SwingUtilities.isRightMouseButton(e)) {
					game.mouseButtons[game.MOUSE_BUTTON_RIGHT] = true;
					game.mouseDown(Game.MOUSE_BUTTON_RIGHT);
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				game.mouse.x = e.getX();
				game.mouse.y = e.getY();
				
				if(SwingUtilities.isLeftMouseButton(e)) {
					game.mouseButtons[Game.MOUSE_BUTTON_LEFT] = false;
					game.mouseUp(Game.MOUSE_BUTTON_LEFT);
				} else if(SwingUtilities.isMiddleMouseButton(e)) {
					game.mouseButtons[Game.MOUSE_BUTTON_MID] = false;
					game.mouseUp(Game.MOUSE_BUTTON_MID);
				} else if(SwingUtilities.isRightMouseButton(e)) {
					game.mouseButtons[game.MOUSE_BUTTON_RIGHT] = false;
					game.mouseUp(Game.MOUSE_BUTTON_RIGHT);
				}
			}
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				super.mouseWheelMoved(e);
				game.mouseScroll(e.getWheelRotation());
			}
		});
		
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				int key = e.getKeyCode();
				if(key >= 0 && key < Game.KEY_ARRAY_SIZE) {
					game.keyboard[key] = true;
				}
				game.keyDown(key, e.getKeyChar());
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
				int key = e.getKeyCode();
				if(key >= 0 && key < Game.KEY_ARRAY_SIZE) {
					game.keyboard[key] = true;
				}
				game.keyUp(key, e.getKeyChar());
			}
		});
		
		this.setSize(initialWidth, initialHeight);
		this.setVisible(true); // get actual w/h
		resizeGame(); // set buffer size
		
		panelRenderer = createPanelRenderer();
		this.add(panelRenderer);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/**
	 * Creates a {@link GameScreenPanelRenderer} for the current game and game screen.
	 * @return A new {@link GameScreenPanelRenderer}.
	 */
	private GameScreenPanelRenderer createPanelRenderer() {
		return new GameScreenPanelRenderer(game.gameScreen);
	}
	
	/**
	 * Calls the <i>resize()</i> method on the {@link Game} contained by this GameFrame.
	 */
	private void resizeGame() {
		game.resize(this.getContentPane().getWidth(), this.getContentPane().getHeight());
	}
	
	/**
	 * Gets the Game object representing the game being drawn and handled by this GameFrame.
	 * @return The Game object representing the game being drawn and handled by this GameFrame.
	 */
	public Game getGame() {
		return this.game;
	}
	
	/**
	 * Starts the game tick and draw loops.
	 * @returns Returns this, so you can chain these calls.
	 */
	public GameFrame start() {
		final GameFrame thisGameFrame = this;
		tickTimer =
			new GameTimer()
			.setTickHandler(new GameTimerTickHandler() {
				@Override
				public void tick(double delta, double total, boolean slow) {
					game.tick(delta, total, slow);
					game.draw(delta, total, slow, game.gameScreen.getImage(), game.gameScreen.getGraphics());
					game.gameScreen.nextBuffer();
				}
			})
			.setErrorHandler(new GameTimerErrorHandler() {
				@Override
				public void handle(Throwable e) {
					JOptionPane.showMessageDialog(
							thisGameFrame,
							"Tick: " + e.getMessage(),
							e.getClass().getName(),
							JOptionPane.OK_OPTION);
				}
			})
			.setCleanupHandler(new GameTimerCleanupHandler() {
				@Override
				public void cleanup() {
					game.destroy();
				}
			})
			.setInterval(game.getTickTime())
			.start(false);
		
		drawTimer = new GameTimer()
			.setTickHandler(new GameTimerTickHandler() {
				@Override
				public void tick(double delta, double total, boolean slow) {
					panelRenderer.repaint();
				}
			})
			.setErrorHandler(new GameTimerErrorHandler() {
				@Override
				public void handle(Throwable e) {
					JOptionPane.showMessageDialog(
							thisGameFrame,
							"Draw: " + e.getMessage(),
							e.getClass().getName(),
							JOptionPane.OK_OPTION);
				}
			}) // no cleanup needed as handled by tickTimer
			.setInterval(game.getFrameTime())
			.start(false);
		return this;
	}
	
	/**
	 * Stops the game tick and draw loops.
	 * @return Returns this, so you can chain these calls.
	 */
	public GameFrame stop() {
		if(tickTimer != null) tickTimer.stop(true);
		if(drawTimer != null) drawTimer.stop(true);
		return this;
	}
}
