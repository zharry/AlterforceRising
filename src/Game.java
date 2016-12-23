import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game {

	// Window Variables
	static final String VERSION = "a12.23r1";
	static final String TITLE = "Alterforce Rising" + " " + VERSION;
	static final int WIDTH = 640, HEIGHT = 480;

	// Game Variables
	static JPanel gamePanel;
	static Handler gameController;
	static boolean running;
	static Random random = new Random();
	static Player player;

	// Game Constants
	static final String assetsDir = "Assets/";
	static final int TYPE_PLAYER = 386721;
	static final int TYPE_ENEMY = 396863;

	// Game Sprites
	static Image sprPlayer;

	public static void main(String[] args) throws Exception {

		// Initialize Sprites
		sprPlayer = ImageIO.read(new File(assetsDir +"Player.png"));

		// Make Game Objects
		gameController = new Handler();
		player = new Player(WIDTH / 2 - 32, HEIGHT / 2 - 32, TYPE_PLAYER, sprPlayer);
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
				System.out.println("FPS: " + fps);
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
		frame.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent mouse) {
				int m = mouse.getButton();
				int x_coor = mouse.getX(), y_coor = mouse.getY();
				if (m == MouseEvent.BUTTON3)
					player.setTp(x_coor, y_coor);
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		});
		frame.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent key) {
				int k = key.getKeyCode();
				if (k == KeyEvent.VK_W || k == KeyEvent.VK_UP)
					player.goUp = true;
				if (k == KeyEvent.VK_A || k == KeyEvent.VK_LEFT)
					player.goLeft = true;
				if (k == KeyEvent.VK_S || k == KeyEvent.VK_DOWN)
					player.goDown = true;
				if (k == KeyEvent.VK_D || k == KeyEvent.VK_RIGHT)
					player.goRight = true;
			}

			@Override
			public void keyReleased(KeyEvent key) {
				int k = key.getKeyCode();
				if (k == KeyEvent.VK_W || k == KeyEvent.VK_UP)
					player.goUp = false;
				if (k == KeyEvent.VK_A || k == KeyEvent.VK_LEFT)
					player.goLeft = false;
				if (k == KeyEvent.VK_S || k == KeyEvent.VK_DOWN)
					player.goDown = false;
				if (k == KeyEvent.VK_D || k == KeyEvent.VK_RIGHT)
					player.goRight = false;
			}
		});

		frame.add(gamePanel);
		frame.setVisible(true);
	}

}
