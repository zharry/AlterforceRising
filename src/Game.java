import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game {

	static final String VERSION = "a12.20r2";
	static final String TITLE = "Alterforce Rising" + " " + VERSION;
	static final int WIDTH = 640, HEIGHT = 480;
	static JPanel gamePanel;
	
	static int temp = 1;
	
	static boolean running;

	public static void main(String[] args) {

		createWindow();
		running = true;
		
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
				tick();
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
	
	static void tick() {
		temp++;
	}
	
	static void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(temp, 25, 125, 125);
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
				render(g);
			}
		};

		frame.add(gamePanel);
		frame.setVisible(true);
	}

}
