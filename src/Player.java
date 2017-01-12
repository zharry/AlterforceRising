import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends GameObject {

	// Movement Variables
	boolean goUp = false, goDown = false, goLeft = false, goRight = false;
	int moveDist = 2;
	int p1x, p1y, p2x, p2y, p3x, p3y;

	// Primary Fire Variables
	int pfCooldownTimer = 0, pfCooldownAmount = (int) (0.5 * Game.tps);
	int pfProjSpeed = 6, pfTimeAlive = 2 * Game.tps;

	// Ability Variables
	boolean tpPrep, goTp = false;
	int tpXi, tpYi, tpLocX, tpLocY, tpStep, tpDist, tpMoveDist = 64;
	int p1xTP, p1yTP, p2xTP, p2yTP;
	double tpDX, tpDY;
	int tpCooldownTimer = 0, tpCooldownAmount = 3 * Game.tps; // All measured in
																// ticks
	// Knockback Variables
	boolean underKnockback;
	int kbVelX, kbVelY, kbStep, knockbackPerFrame = 8;

	// Health Variables
	double health = 100, maxHealth = 100;
	double healthRegen = 0.15;

	public Player(int x, int y, int type, BufferedImage img, Rectangle colBox) {
		super(x, y, type, img, colBox);
	}

	@Override
	public void tick() {
		this.tpCooldownTimer--;
		this.health += this.healthRegen / Game.tps;

		// Reset Player Velocity
		this.velX = 0;
		this.velY = 0;
		if (this.goTp) {
			// TP the player tpMoveDist amount forward for this tick
			for (int i = 0; i < this.tpMoveDist; i++)
				if (this.tpStep <= this.tpDist)
					this.tpStep++;
				else {
					this.goTp = false;
					// Once the TP is over, apply the damage to all intersecting
					// enemies
					ArrayList<GameObject> toDmg = Game.gameController
							.isCollidingRaySprite(new Line2D.Float(this.p2xTP, this.p2yTP, this.p1xTP, this.p1yTP));
					for (GameObject obj : toDmg) {
						if (obj.type == Game.TYPE_ENEMY) {
							Enemy temp = (Enemy) obj;
							temp.health /= 2;
						}
					}
					break;
				}
			this.x = (int) (this.tpXi + tpDX * this.tpStep);
			this.y = (int) (this.tpYi + tpDY * this.tpStep);
		} else if (this.underKnockback) {
			this.kbStep--;
			if (this.kbStep >= 0) {
				this.velX += this.kbVelX;
				this.velY += this.kbVelY;
			} else {
				this.underKnockback = false;
			}
		} else {
			// Set the velocity based on player input
			if (this.goUp)
				this.velY = -this.moveDist;
			if (this.goDown)
				this.velY = (this.velY + 1) * this.moveDist;
			if (this.goLeft)
				this.velX = -this.moveDist;
			if (this.goRight)
				this.velX = (this.velX + 1) * this.moveDist;
		}

		// Collision Detection
		ArrayList<GameObject> inCollisionWith = Game.gameController.isColliding(this);
		for (GameObject obj : inCollisionWith) {
			if (obj.type == Game.TYPE_ENEMY) {
				Enemy enemy = (Enemy) obj;
				if (!this.goTp) {
					this.health -= enemy.damage;
					this.setKnockback(enemy.knockback, enemy.x, enemy.y);
				}
			}
		}

		// Move the player moveDist pixels in that direction
		this.x += this.velX;
		this.y += this.velY;

		// Make sure none of the values exceed the max and min for the game
		this.x = clamp(this.x, 0, Game.panelWidth - this.sprite.getWidth());
		this.y = clamp(this.y, 0, Game.panelHeight - this.sprite.getHeight());
		this.health = clamp(this.health, 0, this.maxHealth);
		this.tpCooldownTimer = clamp(this.tpCooldownTimer, 0, this.tpCooldownAmount);

		// Move the collision box aswell
		this.colBox.x = this.x + this.colBoxOffsetX;
		this.colBox.y = this.y + this.colBoxOffsetY;
		if (this.goTp) {
			this.colBox.x = (int) (this.tpXi + tpDX * this.tpStep);
			this.colBox.y = (int) (this.tpYi + tpDY * this.tpStep);
		}
	}

	@Override
	public void render(Graphics g) {
		// Draw Game Object
		this.p1x = (int) (this.x + this.rotateLocX);
		this.p1y = (int) (this.y + this.rotateLocY);
		this.p2x = (int) (Game.mouseX + this.rotateLocX);
		this.p2y = (int) (Game.mouseY + this.rotateLocY);
		this.p3x = (int) (this.x + this.rotateLocX);
		this.p3y = -1;
		double p12 = Math.sqrt((p1x - p2x) * (p1x - p2x) + (p1y - p2y) * (p1y - p2y));
		double p23 = Math.sqrt((p2x - p3x) * (p2x - p3x) + (p2y - p3y) * (p2y - p3y));
		double p31 = Math.sqrt((p3x - p1x) * (p3x - p1x) + (p3y - p1y) * (p3y - p1y));
		this.rotateDegs = Math.abs(((p2x < p1x) ? -360 : 0)
				+ Math.toDegrees(Math.acos((p12 * p12 + p31 * p31 - p23 * p23) / (2 * p12 * p31))));
		AffineTransformOp op = new AffineTransformOp(
				AffineTransform.getRotateInstance(Math.toRadians(this.rotateDegs), this.rotateLocX, this.rotateLocY),
				AffineTransformOp.TYPE_BILINEAR);
		if (Game.mouseX == this.x && Game.mouseY == this.y) {
			g.drawImage(this.sprite, this.x, this.y, null);
		} else {
			g.drawImage(op.filter(this.sprite, null), this.x, this.y, null);
		}
		if (this.underKnockback) {
			g.setColor(new Color(255, 0, 0, 128));
			g.fillOval(this.x, this.y, 32, 32);
		}

		// Draw HUD Elements
		// Healthbar
		g.setColor(Color.red);
		g.fillRect(20, Game.panelHeight - 40, 100, 20);
		g.setColor(Color.green);
		g.fillRect(20, Game.panelHeight - 40, (int) (this.health / (double) this.maxHealth * 100), 20);
		g.setColor(Color.DARK_GRAY);
		g.drawRect(20, Game.panelHeight - 40, 100, 20);
		g.setColor(Color.black);
		g.drawString("Health: " + Math.round(this.health * 10) / 10.0 + "/" + this.maxHealth, 20,
				Game.panelHeight - 45);
		g.drawString("Health Regen: " + this.healthRegen, 20, Game.panelHeight - 58);

		// PF Cooldown Indicator
		g.setColor(Color.white);
		g.fillRect(150, Game.panelHeight - 55, 35, 35);
		g.drawImage(Game.sprPFIcon, 150, Game.panelHeight - 55, null);
		g.setColor(new Color(Color.gray.getRed(), Color.gray.getGreen(), Color.gray.getBlue(), 215));
		g.fillRect(150, Game.panelHeight - 55, 35, (int) (this.pfCooldownTimer / (double) this.pfCooldownAmount * 35));
		g.setColor(Color.black);
		g.drawRect(150, Game.panelHeight - 55, 35, 35);
		if (this.pfCooldownTimer > 0) {
			Font orig = g.getFont();
			g.setColor(Color.black);
			g.setFont(new Font("default", Font.BOLD, 14));
			g.drawString(Math.round((this.pfCooldownTimer / (double) Game.tps) * 10) / 10.0 + "", 159,
					Game.panelHeight - 33);
			g.setFont(orig);
		}

		// TP Cooldown Indicator
		g.setColor(Color.cyan);
		g.fillRect(200, Game.panelHeight - 55, 35, 35);
		g.drawImage(Game.sprTPIcon, 200, Game.panelHeight - 55, null);
		g.setColor(new Color(Color.gray.getRed(), Color.gray.getGreen(), Color.gray.getBlue(), 215));
		g.fillRect(200, Game.panelHeight - 55, 35, (int) (this.tpCooldownTimer / (double) this.tpCooldownAmount * 35));
		g.setColor(Color.black);
		g.drawRect(200, Game.panelHeight - 55, 35, 35);
		if (this.tpCooldownTimer > 0) {
			Font orig = g.getFont();
			g.setColor(Color.black);
			g.setFont(new Font("default", Font.BOLD, 14));
			g.drawString(Math.round((this.tpCooldownTimer / (double) Game.tps) * 10) / 10.0 + "", 200 + 9,
					Game.panelHeight - 33);
			g.setFont(orig);
		}
		// TP Location Indicator
		if (this.tpCooldownTimer == 0 && this.tpPrep) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			g.drawImage(op.filter(this.sprite, null), Game.mouseX, Game.mouseY, null);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		}
	}

	public void primaryFire() {
		Game.gameController.add(new Projectile(this.x + 8, this.y + 8, Game.TYPE_FRIENDLYPROJECTILE,
				Game.sprProjectile1, this.rotateDegs, Game.mouseX, Game.mouseY, pfProjSpeed, this.pfTimeAlive));
	}

	public void setTp(int x, int y) {
		if (this.tpCooldownTimer == 0) {
			this.p2xTP = p2x;
			this.p2yTP = p2y;
			this.p1xTP = p1x;
			this.p1yTP = p1y;
			this.tpPrep = false;
			this.underKnockback = false;
			this.tpCooldownTimer = this.tpCooldownAmount;
			this.goTp = true;
			this.tpXi = this.x;
			this.tpYi = this.y;
			this.tpLocX = x;
			this.tpLocY = y;
			this.tpStep = 1;
			this.tpDist = (int) (Math.sqrt((x - this.tpXi) * (x - this.tpXi) + (y - this.tpYi) * (y - this.tpYi)));
			this.tpDX = (x - this.tpXi) / ((double) this.tpDist);
			this.tpDY = (y - this.tpYi) / ((double) this.tpDist);
		}
	}

	public void setKnockback(int kb, int x2, int y2) {
		this.underKnockback = true;
		double dx = this.x - x2, dy = this.y - y2, dist = Math.sqrt(dx * dx + dy * dy);
		double velX = (dx / dist) * this.knockbackPerFrame, velY = (dy / dist) * this.knockbackPerFrame;
		this.kbVelX = (int) (Math.round(velX) == 0 ? (velX > 0 ? Math.ceil(velX) : Math.floor(velX))
				: Math.round(velX));
		this.kbVelY = (int) (Math.round(velY) == 0 ? (velY > 0 ? Math.ceil(velY) : Math.floor(velY))
				: Math.round(velY));
		this.kbStep = kb / this.knockbackPerFrame;
	}

	public void canelAbilities() {
		this.tpPrep = false;
	}

}
