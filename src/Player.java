import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends GameObject {

	// Movement Variables
	boolean goUp = false, goDown = false, goLeft = false, goRight = false, goTp = false;
	int moveDist;

	// Ability Variables
	int tpXi, tpYi, tpStep, tpDist, tpMoveDist;
	int p1x, p1y, p2x, p2y, p3x, p3y;
	double tpDX, tpDY;
	ArrayList<GameObject> tpDamaged = new ArrayList<GameObject>();

	// Player Variables
	int health, maxHealth;

	public Player(int x, int y, int type, BufferedImage img) {
		super(x, y, type, img);
		this.health = 100;
		this.maxHealth = 100;
		this.moveDist = 3;
		this.tpMoveDist = 32;
	}

	@Override
	public void tick() {
		if (this.goTp) {
			// TP the player tpMoveDist amount forward for this tick
			for (int i = 0; i < this.tpMoveDist; i++)
				if (this.tpStep <= this.tpDist)
					this.tpStep++;
				else {
					this.goTp = false;
					this.tpDamaged.removeAll(this.tpDamaged);
					break;
				}
			setX((int) (this.tpXi + tpDX * this.tpStep));
			setY((int) (this.tpYi + tpDY * this.tpStep));
		}

		// Set the velocity based on player input
		this.setVelX(0);
		this.setVelY(0);
		if (this.goUp)
			this.setVelY(-1);
		if (this.goDown)
			this.setVelY(this.getVelY() + 1);
		if (this.goLeft)
			this.setVelX(-1);
		if (this.goRight)
			this.setVelX(this.getVelX() + 1);
		// Move the player moveDist pixels in that direction
		this.x += this.moveDist * this.velX;
		this.y += this.moveDist * this.velY;

		// Collision Detection
		ArrayList<GameObject> inCollisionWith = Game.gameController.isColliding(this);
		for (GameObject obj : inCollisionWith) {
			if (obj.getType() == Game.TYPE_ENEMY) {
				Enemy enemy = (Enemy) obj;
				if (this.goTp && !this.tpDamaged.contains(obj)) {
					enemy.health /= 2;
					this.tpDamaged.add(obj);
				} else {
					this.health--;
				}
			}
		}

		// Make sure none of the values exceed the max and min for the game
		this.x = clamp(this.x, 0, Game.panelWidth - this.sprite.getWidth());
		this.y = clamp(this.y, 0, Game.panelHeight - this.sprite.getHeight());
		this.health = clamp(this.health, 0, this.maxHealth);
	}

	@Override
	public void render(Graphics g) {
		// Draw Game Object
		p1x = (int) (this.getX() + this.rotateLocX);
		p1y = (int) (this.getY() + this.rotateLocY);
		p2x = (int) (Game.mouseX + this.rotateLocX);
		p2y = (int) (Game.mouseY + this.rotateLocY);
		p3x = (int) (this.getX() + this.rotateLocX);
		p3y = -1;
		double p12 = Math.sqrt((p1x - p2x) * (p1x - p2x) + (p1y - p2y) * (p1y - p2y));
		double p23 = Math.sqrt((p2x - p3x) * (p2x - p3x) + (p2y - p3y) * (p2y - p3y));
		double p31 = Math.sqrt((p3x - p1x) * (p3x - p1x) + (p3y - p1y) * (p3y - p1y));
		this.rotateDegs = Math.abs(((p2x < p1x) ? -360 : 0)
				+ Math.toDegrees(Math.acos((p12 * p12 + p31 * p31 - p23 * p23) / (2 * p12 * p31))));
		AffineTransformOp op = new AffineTransformOp(
				AffineTransform.getRotateInstance(Math.toRadians(this.rotateDegs), this.rotateLocX, this.rotateLocY),
				AffineTransformOp.TYPE_BILINEAR);
		g.drawImage(op.filter(this.sprite, null), this.x, this.y, null);

		// Draw HUD Elements
		g.setColor(Color.black);
		g.fillRect(20, Game.panelHeight - 40, 100, 20);
		g.setColor(Color.green);
		g.fillRect(20, Game.panelHeight - 40, (int) (this.health / (double) this.maxHealth * 100), 20);
		g.setColor(Color.DARK_GRAY);
		g.drawRect(20, Game.panelHeight - 40, 100, 20);
		g.setColor(Color.black);
		g.drawString("Health: " + this.health + "/" + this.maxHealth, 20, Game.panelHeight - 45);
	}

	public void setTp(int x, int y) {
		this.goTp = true;
		this.tpXi = this.x;
		this.tpYi = this.y;
		this.tpStep = 1;
		this.tpDist = (int) (Math.sqrt((x - this.tpXi) * (x - this.tpXi) + (y - this.tpYi) * (y - this.tpYi)));
		this.tpDX = (x - this.tpXi) / ((double) this.tpDist);
		this.tpDY = (y - this.tpYi) / ((double) this.tpDist);
	}

}
