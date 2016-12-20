import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game {

	// Window Variables
	static final String VERSION = "a12.20r3";
	static final String TITLE = "Alterforce Rising" + " " + VERSION;
	static final int WIDTH = 640, HEIGHT = 480;

	// Game Variables
	static JPanel gamePanel;
	static Handler gameController;
	static boolean running;

	// Game Constants
	public static final int TYPE_PLAYER = 386721;
	static final int TYPE_ENEMY = 396863;

	public static void main(String[] args) {

		// Start Game
		gameController = new Handler();
		running = true;
		createWindow();

		// Add Game Objects
		for (int i = 0; i < 15555; i++)
			gameController.add(new Player((int) (Math.random() * WIDTH), (int) (Math.random() * HEIGHT), TYPE_PLAYER,
					new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))));

		// Game Loop
		long lastTime = System.nanoTime(), timer = System.currentTimeMillis();
		double numTicks = 24.0, ns = 1000000000 / numTicks, delta = 0;
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
				System.out.println("FPS: " + fps);
				fps = 0;
			}
		}
	}

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

		frame.add(gamePanel);
		frame.setVisible(true);
	}

}
