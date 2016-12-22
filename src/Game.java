import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game {

	// Window Variables
	static final String VERSION = "a12.22r1";
	static final String TITLE = "Alterforce Rising" + " " + VERSION;
	static final int WIDTH = 640, HEIGHT = 480;

	// Game Variables
	static JPanel gamePanel;
	static Handler gameController;
	static boolean running;
	static Random random = new Random();
	static Player player;

	// Game Constants
	static final int TYPE_PLAYER = 386721;
	static final int TYPE_ENEMY = 396863;

	public static void main(String[] args) {
		
		// Make Game Objects
		gameController = new Handler();
		player = new Player(WIDTH / 2 - 32, HEIGHT / 2 - 32, TYPE_PLAYER);
		gameController.add(player);
		
		// Start Game
		running = true;
		createWindow();
		
		// Game Loop
		long lastTime = System.nanoTime(), timer = System.currentTimeMillis();
		double numTicks = 60.0, ns = 1000000000 / numTicks, delta = 0;
		int fps = 0;
		while (running) {
			long curTime = System.nanoTime();
			delta += (curTime - lastTime) / ns;
			lastTime = curTime;
			while (delta >= 1) {
				// Process Game Changes
				gameController.tick();
				delta--;
			}
			// Update the Graphics
			gamePanel.repaint();
			// Display FPS
			fps++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				//System.out.println("FPS: " + fps);
				fps = 0;
			}
		}
	}

	@SuppressWarnings("serial")
	static void createWindow() {
		JFrame frame = new JFrame(TITLE);
		frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		frame.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);

		gamePanel = new JPanel() {
			@Override
			public void paint(Graphics g) {
				// Reset Frame
				g.setColor(Color.GREEN);
				g.fillRect(0, 0, getWidth(), getHeight());
				// Render Game
				gameController.render(g);
			}
		};		
		
		// Player Controls
		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent key) {
				int k = key.getKeyCode();
				if (k == KeyEvent.VK_W) {
					player.setVelY(-1);
					System.out.println("UP");
				}
				if (k == KeyEvent.VK_A) {
					player.setVelX(-1);
				}
				if (k == KeyEvent.VK_S) {
					player.setVelY(1);
					System.out.println("DOWN");
				}
				if (k == KeyEvent.VK_D) {
					player.setVelX(1);
				}
			}
			@Override
			public void keyReleased(KeyEvent key) {
				int k = key.getKeyCode();
				if (k == KeyEvent.VK_W) {
					player.setVelY(0);
				}
				if (k == KeyEvent.VK_A) {
					player.setVelX(0);
				}
				if (k == KeyEvent.VK_S) {
					player.setVelY(0);
				}
				if (k == KeyEvent.VK_D) {
					player.setVelX(0);
				}
			}
		});

		frame.add(gamePanel);
		frame.setVisible(true);
	}

}
