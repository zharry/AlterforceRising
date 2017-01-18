import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
	static final int MAXBACKDEG = 90; // One Directional, double for full range
	static final String assetsDir = "Assets/";
	static final int TYPE_PLAYER = 386721;

	static final int TYPE_ENEMY = 396863;
	static final int ENEMY_DEFAULT = 38569263, ENEMY_TANK = 23643875, ENEMY_ASSASSIN = 82764203, ENEMY_SCOUT = 23643473;
	static final int BOSS_1 = 123456, BOSS_2 = 236654, BOSS_3 = 215648;

	static final int TYPE_POTION = 376652;
	static final int POTION_HEALTH = 3564891, POTION_SPEED = 3279465, POTION_DAMAGE = 3651698, POTION_EXP = 3216547;

	static final int TYPE_FRIENDLYPROJECTILE = 326134;

	// Game Sprites
	static BufferedImage[] sprPlayer = new BufferedImage[361], sprTimberman1 = new BufferedImage[361];
	static BufferedImage[] sprTank1 = new BufferedImage[361], sprScout1 = new BufferedImage[361];
	static BufferedImage[] sprAssassin1 = new BufferedImage[361];
	static BufferedImage[] sprBoss1 = new BufferedImage[361], sprBoss2 = new BufferedImage[361];
	static BufferedImage[] sprBoss3 = new BufferedImage[361];
	static BufferedImage[] sprProjectile1 = new BufferedImage[361];
	static BufferedImage[] sprHealthPot = new BufferedImage[361];
	static BufferedImage[] sprDamagePot = new BufferedImage[361];
	static BufferedImage[] sprSpeedPot = new BufferedImage[361];
	static BufferedImage[] sprExpPot = new BufferedImage[361];

	static BufferedImage sprTPIcon, sprPFIcon;
	static BufferedImage sprSPIcon, sprDPIcon;
	static BufferedImage sprExpIcon;

	static BufferedImage sprBackground1, sprBackground2, sprBackground3;

	public static void main(String[] args) throws Exception {

		// Determine resolution
		int[] resolution = openLauncher();
		width = resolution[0];
		height = resolution[1];

		// Initialize Sprites
		sprPlayer[0] = ImageIO.read(new File(assetsDir + "GameObjects/Player.png"));
		sprTimberman1[0] = ImageIO.read(new File(assetsDir + "GameObjects/Timberman1.png"));
		sprTank1[0] = ImageIO.read(new File(assetsDir + "GameObjects/Bandit1.png"));
		sprScout1[0] = ImageIO.read(new File(assetsDir + "GameObjects/Rat1.png"));
		sprAssassin1[0] = ImageIO.read(new File(assetsDir + "GameObjects/Hornet1.png"));

		sprBoss1[0] = ImageIO.read(new File(assetsDir + "GameObjects/Ogre1.png"));
		sprBoss2[0] = ImageIO.read(new File(assetsDir + "GameObjects/Dragon1.png"));
		sprBoss3[0] = ImageIO.read(new File(assetsDir + "GameObjects/Scorpion1.png"));

		sprProjectile1[0] = ImageIO.read(new File(assetsDir + "Projectiles (16x16)/Projectile1.png"));

		sprHealthPot[0] = ImageIO.read(new File(assetsDir + "GameObjects/HealthPotion.png"));
		sprDamagePot[0] = ImageIO.read(new File(assetsDir + "GameObjects/DamagePotion.png"));
		sprSpeedPot[0] = ImageIO.read(new File(assetsDir + "GameObjects/SpeedPotion.png"));
		sprExpPot[0] = ImageIO.read(new File(assetsDir + "GameObjects/ExpPotion.png"));

		sprTPIcon = ImageIO.read(new File(assetsDir + "Icons (35x35)/TPIcon.png"));
		sprPFIcon = ImageIO.read(new File(assetsDir + "Icons (35x35)/PFIcon.png"));
		sprDPIcon = ImageIO.read(new File(assetsDir + "Icons (35x35)/DPIcon.png"));
		sprSPIcon = ImageIO.read(new File(assetsDir + "Icons (35x35)/SPIcon.png"));
		sprExpIcon = ImageIO.read(new File(assetsDir + "Projectiles (16x16)/LevelUp.png"));

		sprBackground1 = ImageIO.read(new File(assetsDir + "Backgrounds/Background1.png"));
		sprBackground2 = ImageIO.read(new File(assetsDir + "Backgrounds/Background2.png"));
		sprBackground3 = ImageIO.read(new File(assetsDir + "Backgrounds/Background3.png"));

		// Create Sprite Rotations
		AffineTransformOp op;
		for (int i = 1; i < 361; i++) {
			op = new AffineTransformOp(AffineTransform.getRotateInstance(Math.toRadians(i), sprPlayer[0].getWidth() / 2,
					sprPlayer[0].getHeight() / 2), AffineTransformOp.TYPE_BILINEAR);
			sprPlayer[i] = op.filter(sprPlayer[0], null);

			op = new AffineTransformOp(AffineTransform.getRotateInstance(Math.toRadians(i),
					sprTimberman1[0].getWidth() / 2, sprTimberman1[0].getHeight() / 2),
					AffineTransformOp.TYPE_BILINEAR);
			sprTimberman1[i] = op.filter(sprTimberman1[0], null);

			op = new AffineTransformOp(AffineTransform.getRotateInstance(Math.toRadians(i),
					sprProjectile1[0].getWidth() / 2, sprProjectile1[0].getHeight() / 2),
					AffineTransformOp.TYPE_BILINEAR);
			sprProjectile1[i] = op.filter(sprProjectile1[0], null);

			op = new AffineTransformOp(AffineTransform.getRotateInstance(Math.toRadians(i), sprTank1[0].getWidth() / 2,
					sprTank1[0].getHeight() / 2), AffineTransformOp.TYPE_BILINEAR);
			sprTank1[i] = op.filter(sprTank1[0], null);

			op = new AffineTransformOp(AffineTransform.getRotateInstance(Math.toRadians(i), sprScout1[0].getWidth() / 2,
					sprScout1[0].getHeight() / 2), AffineTransformOp.TYPE_BILINEAR);
			sprScout1[i] = op.filter(sprScout1[0], null);

			op = new AffineTransformOp(AffineTransform.getRotateInstance(Math.toRadians(i),
					sprAssassin1[0].getWidth() / 2, sprAssassin1[0].getHeight() / 2), AffineTransformOp.TYPE_BILINEAR);
			sprAssassin1[i] = op.filter(sprAssassin1[0], null);

			op = new AffineTransformOp(AffineTransform.getRotateInstance(Math.toRadians(i), sprBoss1[0].getWidth() / 2,
					sprBoss1[0].getHeight() / 2), AffineTransformOp.TYPE_BILINEAR);
			sprBoss1[i] = op.filter(sprBoss1[0], null);

			op = new AffineTransformOp(AffineTransform.getRotateInstance(Math.toRadians(i), sprBoss2[0].getWidth() / 2,
					sprBoss2[0].getHeight() / 2), AffineTransformOp.TYPE_BILINEAR);
			sprBoss2[i] = op.filter(sprBoss2[0], null);

			op = new AffineTransformOp(AffineTransform.getRotateInstance(Math.toRadians(i), sprBoss3[0].getWidth() / 2,
					sprBoss3[0].getHeight() / 2), AffineTransformOp.TYPE_BILINEAR);
			sprBoss3[i] = op.filter(sprBoss3[0], null);

		}

		// Make Game Objects
		gameController = new Handler();
		player = new Player(64, 64, TYPE_PLAYER, sprPlayer, new Rectangle(8, 8, 16, 16));
		gameController.add(player);

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
		String[] options = { "858x480", "1066x600 (Optimal)", "1280x720" };
		int returnCode = JOptionPane.showOptionDialog(null,
				"Welcome to " + TITLE + "!\n\n" + "Choose your game resolution", TITLE, JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
		if (returnCode == -1)
			System.exit(0);
		temp.dispose();
		// String to Int for the resolution selection
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
				g.drawImage(getWidth() == 600 ? sprBackground2 : getWidth() == 480 ? sprBackground1 : sprBackground3, 0,
						0, null);
				if (player.menuON) {
					g.setColor(Color.gray);
					g.fillRect(0, 0, getWidth(), getHeight());
				}
				if (!player.alive) {
					g.setColor(Color.gray);
					g.fillRect(0, 0, getWidth(), getHeight());
					g.setColor(Color.RED);
					g.setFont(new Font("default", Font.BOLD, 30));
					g.drawString("GAME OVER", getWidth() / 2 - 100, getHeight() / 2);
				}
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
				if (k == KeyEvent.VK_F4) {
					// Toggle Control Menu
					player.menuON = !player.menuON;
				}
				if (k == KeyEvent.VK_1) {
					if (player.ExpPoints > 0) {
						player.health *= 1.2;
						player.maxHealth *= 1.2;
						player.healthRegen *= 1.1;
						player.ExpPoints--;
					}
				}
				if (k == KeyEvent.VK_2) {
					if (player.ExpPoints > 0) {
						player.moveDist += 0.15;
						player.ExpPoints--;
					}
				}
				if (k == KeyEvent.VK_3) {
					if (player.ExpPoints > 0) {
						player.pfDamage += 2.5;
						player.ExpPoints--;
					}
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
