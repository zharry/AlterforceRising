import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Game {

	// Window Variables
	static final String VERSION = "a12.03r2";
	static final String TITLE = "Alterforce Rising" + " " + VERSION;
	static int width, height, panelWidth, panelHeight;

	// Debug Variables
	static boolean debug = false;
	static int fps;
	static int mouseX, mouseY, tpLocX, tpLocY;

	// Game Variables
	static JPanel gamePanel;
	static JFrame frame;
	static Handler gameController;
	static boolean running;
	static Random random = new Random();
	static Player player;
	static int p1x, p1y, p2x, p2y, p3x, p3y;

	// Game Constants
	static final String assetsDir = "Assets/";
	static final int TYPE_PLAYER = 386721;
	static final int TYPE_ENEMY = 396863;

	// Game Sprites
	static BufferedImage sprPlayer, sprAssassin1;

	public static void main(String[] args) throws Exception {

		// Determine resolution
		int[] resolution = openLauncher();
		width = resolution[0];
		height = resolution[1];

		// Initialize Sprites
		sprPlayer = ImageIO.read(new File(assetsDir + "Player.png"));
		sprAssassin1 = ImageIO.read(new File(assetsDir + "Assassin1.png"));

		// Make Game Objects
		gameController = new Handler();
		player = new Player(64, 64, TYPE_PLAYER, sprPlayer);
		gameController.add(player);

		// Start Game
		running = true;
		createWindow();

		// Game Loop
		long lastTime = System.nanoTime(), timer = System.currentTimeMillis();
		double numTicks = 60.0, ns = 1000000000 / numTicks, delta = 0;
		int fpsProc = 0;
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
			fpsProc++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				fps = fpsProc;
				fpsProc = 0;
			}
		}
	}

	static int[] openLauncher() {
		// Resolution Options
		Object[] options = { "480x360", "858x480", "1066x600 (Optimal)", "1280x720" };
		int returnCode = JOptionPane.showOptionDialog(null,
				"Welcome to " + TITLE + "!\n\n" + "Choose your game resolution", TITLE, JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, options, options[2]);
		if (returnCode == -1)
			System.exit(0);
		// LOL, String to Int for the resolution selection
		return new int[] { Integer.parseInt(options[returnCode].toString().split("x")[0]),
				Integer.parseInt(options[returnCode].toString().split("x")[1].split(" ")[0]) };
	}

	@SuppressWarnings("serial")
	static void createWindow() {
		frame = new JFrame(TITLE);
		frame.setPreferredSize(new Dimension(width, height));
		frame.setMaximumSize(new Dimension(width, height));
		frame.setMinimumSize(new Dimension(width, height));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);

		gamePanel = new JPanel() {
			@Override
			public void paint(Graphics g) {
				// Reset Frame
				g.setColor(Color.lightGray);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(Color.black);
				// Render Game
				gameController.render(g);
				if (debug)
					drawDebug(g);
			}
		};

		// Player Controls
		gamePanel.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent mouse) {
				mouseMoved(mouse);
			}

			@Override
			public void mouseMoved(MouseEvent mouse) {
				mouseX = mouse.getX();
				mouseY = mouse.getY();

				p1x = (int) (player.getX() + player.rotateLocX);
				p1y = (int) (player.getY() + player.rotateLocY);
				p2x = (int) (mouse.getX() + player.rotateLocX);
				p2y = (int) (mouse.getY() + player.rotateLocY);
				p3x = (int) (player.getX() + player.rotateLocX);
				p3y = -1;

				double p12 = Math.sqrt((p1x - p2x) * (p1x - p2x) + (p1y - p2y) * (p1y - p2y));
				double p23 = Math.sqrt((p2x - p3x) * (p2x - p3x) + (p2y - p3y) * (p2y - p3y));
				double p31 = Math.sqrt((p3x - p1x) * (p3x - p1x) + (p3y - p1y) * (p3y - p1y));

				player.rotateDegs = Math.abs(((p2x < p1x) ? -360 : 0)
						+ Math.toDegrees(Math.acos((p12 * p12 + p31 * p31 - p23 * p23) / (2 * p12 * p31))));
			}
		});
		gamePanel.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent mouse) {
				int m = mouse.getButton();
				int xCoor = mouse.getX(), yCoor = mouse.getY();
				if (m == MouseEvent.BUTTON3) {
					player.setTp(xCoor, yCoor);
					tpLocX = xCoor;
					tpLocY = yCoor;
				}
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent mouse) {
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
				if (k == KeyEvent.VK_F3) {
					// Toggle Debug State
					debug = !debug;
				}
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

		panelWidth = gamePanel.getWidth();
		panelHeight = gamePanel.getHeight();
	}

	static void drawDebug(Graphics g) {
		g.setColor(Color.BLACK);
		int drawY = 0, incY = 15;																																																
		g.drawString("FPS: " + fps, 10, drawY += incY);
		g.drawString("X: " + player.getX() + ", Y: " + player.getY() + ", Rotate: " + (int) player.rotateDegs, 10,
				drawY += incY);
		g.drawString("Vel X: " + player.getVelX() + ", Vel Y: " + player.getVelY(), 10,
				drawY += incY);
		g.drawString("TP: " + player.goTp, 10, drawY += incY);
		g.drawString("TP X: " + tpLocX + ", TP Y: " + tpLocY, 10, drawY += incY);
		g.drawString("Mouse X: " + mouseX + ", Mouse Y: " + mouseY, 10, drawY += incY);
		g.drawLine(p2x, p2y, p1x, p1y);
	}

}
