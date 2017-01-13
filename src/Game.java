import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Game {

	// Window Variables
	static final String VERSION = "a12.013r1";
	static final String TITLE = "Alterforce Rising" + " " + VERSION;
	static int width, height, panelWidth, panelHeight;

	// Game Variables
	static boolean debug = false;
	static int fps, fpsProc = 0, tps = 60, curTps;
	static int mouseX, mouseY;

	static JPanel gamePanel;
	static JFrame frame;
	static Handler gameController;
	static boolean running;
	static Random random = new Random();
	static Player player;

	// Game Constants
	static final String assetsDir = "Assets/";
	static final int TYPE_PLAYER = 386721;
	static final int TYPE_ENEMY = 396863;
	static final int TYPE_FRIENDLYPROJECTILE = 326134;

	// Game Sprites
	static BufferedImage[] sprPlayer = new BufferedImage[361], sprAssassin1 = new BufferedImage[361];
	static BufferedImage[] sprProjectile1 = new BufferedImage[361];
	static BufferedImage sprTPIcon, sprPFIcon;

	public static void main(String[] args) throws Exception {

		
		// Determine resolution
		int[] resolution = openLauncher();
		width = resolution[0];
		height = resolution[1];

		// Initialize Sprites
		sprPlayer[0] = ImageIO.read(new File(assetsDir + "GameObjects (32x32)/Player.png"));
		sprAssassin1[0] = ImageIO.read(new File(assetsDir + "GameObjects (32x32)/Assassin1.png"));

		sprProjectile1[0] = ImageIO.read(new File(assetsDir + "Projectiles (16x16)/Projectile1.png"));

		sprTPIcon = ImageIO.read(new File(assetsDir + "Icons (35x35)/TPIcon.png"));
		sprPFIcon = ImageIO.read(new File(assetsDir + "Icons (35x35)/PFIcon.png"));

		// Create Sprite Rotations
		AffineTransformOp op;
		for (int i = 1; i < 361; i++) {
			op = new AffineTransformOp(AffineTransform.getRotateInstance(Math.toRadians(i), sprPlayer[0].getWidth() / 2,
					sprPlayer[0].getHeight() / 2), AffineTransformOp.TYPE_BILINEAR);
			sprPlayer[i] = op.filter(sprPlayer[0], null);

			op = new AffineTransformOp(AffineTransform.getRotateInstance(Math.toRadians(i),
					sprAssassin1[0].getWidth() / 2, sprAssassin1[0].getHeight() / 2), AffineTransformOp.TYPE_BILINEAR);
			sprAssassin1[i] = op.filter(sprAssassin1[0], null);

			op = new AffineTransformOp(AffineTransform.getRotateInstance(Math.toRadians(i),
					sprProjectile1[0].getWidth() / 2, sprProjectile1[0].getHeight() / 2),
					AffineTransformOp.TYPE_BILINEAR);
			sprProjectile1[i] = op.filter(sprProjectile1[0], null);
		}

		// Make Game Objects
		gameController = new Handler();
		player = new Player(64, 64, TYPE_PLAYER, sprPlayer, new Rectangle(8, 8, 16, 16));
		gameController.add(player);
		Enemy test = new Enemy(256, 128, TYPE_ENEMY, sprAssassin1, new Rectangle(8, 8, 16, 16), 3, 48, 1);
		gameController.add(test);
		
		// Start Game
		running = true;
		createWindow();
		
		// Game Loop
		long lastTime = System.nanoTime(), timer = System.currentTimeMillis();
		double ns = 1000000000 / (double) tps, delta = 0;
		int tpsProc = 0;
		while (running) {
			long curTime = System.nanoTime();
			delta += (curTime - lastTime) / ns;
			lastTime = curTime;
			while (delta >= 1) {
				// Process Game Changes
				gameController.tick();
				tpsProc++;
				delta--;
			}
			// Update the Graphics
			gamePanel.repaint();
			// Display FPS and TPS
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				fps = fpsProc;
				curTps = tpsProc;
				fpsProc = 0;
				tpsProc = 0;
				if (debug)
					Debug.debugConsole();
			}
			Thread.sleep(1);
		}
	}

	static int[] openLauncher() {
		JFrame temp = new JFrame(TITLE);
		temp.setUndecorated(true);
		temp.setVisible(true);
		temp.setLocationRelativeTo(null);
		// Resolution Options
		String[] options = { "480x360", "858x480", "1066x600 (Optimal)", "1280x720" };
		int returnCode = JOptionPane.showOptionDialog(null,
				"Welcome to " + TITLE + "!\n\n" + "Choose your game resolution", TITLE, JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, options, options[2]);
		if (returnCode == -1)
			System.exit(0);
		temp.dispose();
		// LOL, String to Int for the resolution selection
		return new int[] { Integer.parseInt(options[returnCode].split("x")[0]),
				Integer.parseInt(options[returnCode].split("x")[1].split(" ")[0]) };
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
				// Render Game
				gameController.render(g);
				if (debug)
					Debug.drawDebug(g);
				fpsProc++;
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
			}
		});
		gamePanel.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent mouse) {
				int m = mouse.getButton();
				if (m == MouseEvent.BUTTON3)
					player.tpPrep = true;
				else if (m == MouseEvent.BUTTON1)
					player.primaryFire();
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
			public void mouseReleased(MouseEvent mouse) {
				int m = mouse.getButton();
				int xCoor = mouse.getX(), yCoor = mouse.getY();
				if (m == MouseEvent.BUTTON3 && player.tpPrep)
					player.setTp(xCoor, yCoor);
				player.tpPrep = false;
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
				if (k == KeyEvent.VK_CONTROL)
					player.canelAbilities();
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

}
