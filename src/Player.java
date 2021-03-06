import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends GameObject {

	// Movement Variables
	boolean goUp = false, goDown = false, goLeft = false, goRight = false;
	double moveDist = 4;
	double p1x, p1y, p2x, p2y, p3x, p3y;

	// Primary Fire Variables
	int pfCooldownTimer = 0, pfCooldownAmount = (int) (0.2 * Game.tps), pfTimeAlive = 2 * Game.tps;;
	double pfProjSpeed = 10.0, pfDamage = 25;

	// Ability Variables
	int tpStep, tpCooldownTimer = 0, tpCooldownAmount = 3 * Game.tps;
	boolean tpPrep, goTp = false;
	double tpXi, tpYi, tpLocX, tpLocY, tpDist, tpMoveDist = 64.0;
	double p1xTP, p1yTP, p2xTP, p2yTP;
	double tpDX, tpDY;

	// Knockback Variables
	boolean underKnockback;
	double kbVelX, kbVelY, kbStep, knockbackPerFrame = 8.0;

	// Health Variables
	double health = 100.0, maxHealth = 100.0;
	double healthRegen = 0.15;

	// Experience Variables
	double Exp = 0;
	double maxExp = 20;
	double level = 1;
	double ExpPoints = 3;

	// Potion Variables
	double DPEffectTime = 6 * Game.tps;
	double SPEffectTime = 6 * Game.tps;
	double SPEffectAmount = 6 * Game.tps;
	double DPEffectAmount = 6 * Game.tps;
	int effectDraw;
	boolean sPotionON = false;
	boolean dPotionON = false;

	// Control Menu Variables
	boolean menuON = false;
	boolean alive = true;

	public Player(int x, int y, int type, BufferedImage[] sprite, Rectangle colBox) {
		super(x, y, type, sprite, colBox);
	}

	@Override
	public void tick() {
		this.sPotionON = true;
		this.dPotionON = true;
		reduceCooldowns();
		this.health += this.healthRegen / Game.tps;

		// Check for potion effects, and reduce cooldowns
		if (this.sPotionON) {
			this.SPEffectTime--;
			if (this.SPEffectTime == 0) {
				this.sPotionON = false;
				this.moveDist = 4;
				this.SPEffectTime = 6 * Game.tps;
			}
		}
		if (this.dPotionON) {
			this.DPEffectTime--;
			if (this.DPEffectTime == 0) {
				this.dPotionON = false;
				this.DPEffectTime = 6 * Game.tps;
				this.pfCooldownAmount = (int) (0.2 * Game.tps);
			}
		}

		// Reset Player Velocity
		this.velX = 0;
		this.velY = 0;
		// Determine Player Velocity
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
							.isCollidingRaySprite(new Line2D.Double(this.p2xTP, this.p2yTP, this.p1xTP, this.p1yTP));
					for (GameObject obj : toDmg) {
						if (obj.type == Game.TYPE_ENEMY) {
							Enemy temp = (Enemy) obj;
							temp.health /= 2;
						}
					}
					break;
				}
			this.x = this.tpXi + tpDX * this.tpStep;
			this.y = this.tpYi + tpDY * this.tpStep;
		} else if (this.underKnockback) {
			this.kbStep--;
			if (this.kbStep >= 0) {
				this.velX = this.kbVelX;
				this.velY = this.kbVelY;
			} else {
				this.underKnockback = false;
			}
		} else {
			// Set the velocity based on player input
			if (this.goUp)
				this.velY = -this.moveDist;
			if (this.goDown)
				this.velY = this.moveDist;
			if (this.goLeft)
				this.velX = -this.moveDist;
			if (this.goRight)
				this.velX = this.moveDist;
			if (this.goUp && this.goDown)
				this.velY = 0;
			if (this.goLeft && this.goRight) {
				this.velX = 0;
			}
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
			if (obj.type == Game.TYPE_POTION) {
				Potion potion = (Potion) obj;
				if (potion.subtype == Game.POTION_SPEED) {
					this.moveDist += potion.speedUp;
					this.sPotionON = true;
					this.SPEffectTime = 6 * Game.tps;
				} else if (potion.subtype == Game.POTION_DAMAGE) {
					this.pfCooldownAmount = (int) ((potion.aSpeedUp) * Game.tps);
					this.dPotionON = true;
					this.DPEffectTime = 6 * Game.tps;
				} else if (potion.subtype == Game.POTION_HEALTH) {
					this.health += (this.maxHealth - this.health) * potion.healUp;
				} else if (potion.subtype == Game.POTION_EXP) {
					this.Exp += (this.maxExp - this.Exp) / 2.0;
				}
				Game.gameController.toRemove.add(potion);
			}
		}

		// Experience
		if (this.Exp >= this.maxExp) {
			this.Exp = 0;
			if (this.level < 10) {
				this.maxExp += 5;
			} else if (this.level < 20) {
				this.maxExp += 10;
			} else {
				this.maxExp += 20;
			}
			if (this.level == 1000) {
				this.Exp = 0;
				this.maxExp = 10000;
			} else {
				this.level++;
				this.ExpPoints += 3;
			}
		}

		// Clamp Variables and Move
		this.health = clamp(this.health, 0, this.maxHealth);
		this.tpCooldownTimer = (int) clamp(this.tpCooldownTimer, 0, this.tpCooldownAmount);
		move();
		tryDespawn();

	}

	@Override
	public void render(Graphics g) {
		if (this.alive) {
			if (!this.menuON) {
				// Draw Game Object
				this.p1x = this.x + this.sprite[0].getWidth() / 2;
				this.p1y = this.y + this.sprite[0].getHeight() / 2;
				this.p2x = Game.mouseX + this.sprite[0].getWidth() / 2;
				this.p2y = Game.mouseY + this.sprite[0].getHeight() / 2;
				this.p3x = this.x + this.sprite[0].getWidth() / 2;
				this.p3y = -1;
				double p12 = Math.sqrt((p1x - p2x) * (p1x - p2x) + (p1y - p2y) * (p1y - p2y));
				double p23 = Math.sqrt((p2x - p3x) * (p2x - p3x) + (p2y - p3y) * (p2y - p3y));
				double p31 = Math.sqrt((p3x - p1x) * (p3x - p1x) + (p3y - p1y) * (p3y - p1y));
				this.rotateDegs = Math.abs(((p2x < p1x) ? -360 : 0) // Possible
																	// NaN here
						+ Math.toDegrees(Math.acos((p12 * p12 + p31 * p31 - p23 * p23) / (2 * p12 * p31))));
				if (Game.mouseX == this.x && Game.mouseY == this.y) {
					g.drawImage(Game.sprPlayer[0], (int) Math.round(this.x), (int) Math.round(this.y), null);
				} else {
					g.drawImage(Game.sprPlayer[(int) this.rotateDegs], (int) Math.round(this.x),
							(int) Math.round(this.y), null);
				}
				if (this.underKnockback) {
					g.setColor(new Color(255, 0, 0, 128));
					g.fillOval((int) Math.round(this.x), (int) Math.round(this.y), 32, 32);
				}

				// Draw HUD Elements
				g.drawImage(Game.sprHUD, 20, Game.panelHeight - 80, null);
				// Healthbar
				g.setColor(Color.red);
				int healthX = 103, healthY = Game.panelHeight - 75, healthHeight = 12, healthWidth = 84;
				g.fillRect(healthX, healthY, healthWidth, healthHeight);
				g.setColor(Color.green);
				g.fillRect(healthX, healthY, (int) (this.health / (double) this.maxHealth * healthWidth), healthHeight);
				g.setColor(Color.DARK_GRAY);
				g.drawRect(healthX, healthY, healthWidth, healthHeight);
				g.setColor(Color.black);
				//g.drawString("Health: " + Math.round(this.health * 100) / 100.0 + "/"
				//		+ Math.round(this.maxHealth * 100) / 100.0, 20, Game.panelHeight - 45);
				g.drawString("Health Regen: " + Math.round(this.healthRegen * 100) / 100.0, 20, Game.panelHeight - 58);

				// PF Cooldown Indicator
				g.setColor(Color.white);
				int pfLocXD = 236, pfLocYD = Game.panelHeight - 66;
				g.fillRect(pfLocXD, pfLocYD, 35, 35);
				g.drawImage(Game.sprPFIcon, pfLocXD, pfLocYD, null);
				g.setColor(new Color(Color.gray.getRed(), Color.gray.getGreen(), Color.gray.getBlue(), 215));
				g.fillRect(pfLocXD, pfLocYD, 35,
						(int) (this.pfCooldownTimer / (double) this.pfCooldownAmount * 35));
				g.setColor(Color.black);
				g.drawRect(pfLocXD, pfLocYD, 35, 35);
				if (this.pfCooldownTimer > 0) {
					Font orig = g.getFont();
					g.setColor(Color.black);
					g.setFont(new Font("default", Font.BOLD, 14));
					g.drawString(Math.round((this.pfCooldownTimer / (double) Game.tps) * 10) / 10.0 + "", 244,
							Game.panelHeight - 44);
					g.setFont(orig);
				}

				// Speed Potion Effect Indicator
				if (this.sPotionON) {
					this.effectDraw = this.dPotionON ? 300 : 250;
					g.setColor(Color.white);
					g.fillRect(this.effectDraw, Game.panelHeight - 55, 35, 35);
					g.drawImage(Game.sprSPIcon, this.effectDraw, Game.panelHeight - 55, null);
					g.setColor(new Color(Color.gray.getRed(), Color.gray.getGreen(), Color.gray.getBlue(), 215));
					g.fillRect(this.effectDraw, Game.panelHeight - 55, 35,
							(int) (this.SPEffectTime / (double) this.SPEffectAmount * 35));
					g.setColor(Color.black);
					g.drawRect(this.effectDraw, Game.panelHeight - 55, 35, 35);
					if (this.SPEffectTime > 0) {
						Font orig = g.getFont();
						g.setColor(Color.black);
						g.setFont(new Font("default", Font.BOLD, 14));
						g.drawString(Math.round((this.SPEffectTime / (double) Game.tps) * 10) / 10.0 + "",
								this.effectDraw + 9, Game.panelHeight - 33);
						g.setFont(orig);
					}
				}

				// Damage Potion Effect Indicator
				if (this.dPotionON) {
					this.effectDraw = 250;
					g.setColor(Color.white);
					g.fillRect(this.effectDraw, Game.panelHeight - 55, 35, 35);
					g.drawImage(Game.sprDPIcon, this.effectDraw, Game.panelHeight - 55, null);
					g.setColor(new Color(Color.gray.getRed(), Color.gray.getGreen(), Color.gray.getBlue(), 215));
					g.fillRect(this.effectDraw, Game.panelHeight - 55, 35,
							(int) (this.DPEffectTime / (double) this.DPEffectAmount * 35));
					g.setColor(Color.black);
					g.drawRect(this.effectDraw, Game.panelHeight - 55, 35, 35);
					if (this.DPEffectTime > 0) {
						Font orig = g.getFont();
						g.setColor(Color.black);
						g.setFont(new Font("default", Font.BOLD, 14));
						g.drawString(Math.round((this.DPEffectTime / (double) Game.tps) * 10) / 10.0 + "",
								this.effectDraw + 9, Game.panelHeight - 33);
						g.setFont(orig);
					}
				}

				// TP Cooldown Indicator
				g.setColor(Color.cyan);
				int tpLocXD = 280, tpLocYD = Game.panelHeight - 66;
				g.fillRect(tpLocXD, tpLocYD, 35, 35);
				g.drawImage(Game.sprTPIcon, tpLocXD, tpLocYD, null);
				g.setColor(new Color(Color.gray.getRed(), Color.gray.getGreen(), Color.gray.getBlue(), 215));
				g.fillRect(tpLocXD, tpLocYD, 35,
						(int) (this.tpCooldownTimer / (double) this.tpCooldownAmount * 35));
				g.setColor(Color.black);
				g.drawRect(tpLocXD, tpLocYD, 35, 35);
				if (this.tpCooldownTimer > 0) {
					Font orig = g.getFont();
					g.setColor(Color.black);
					g.setFont(new Font("default", Font.BOLD, 14));
					g.drawString(Math.round((this.tpCooldownTimer / (double) Game.tps) * 10) / 10.0 + "", 288,
							Game.panelHeight - 44);
					g.setFont(orig);
				}
				// TP Location Indicator
				if (this.tpCooldownTimer == 0 && this.tpPrep) {
					Graphics2D g2d = (Graphics2D) g;
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
					g.drawImage(Game.sprPlayer[(int) this.rotateDegs], Game.mouseX, Game.mouseY, null);
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
				}

				// Level Up Stats
				g.setColor(Color.DARK_GRAY);
				g.fillRect(20, Game.panelHeight - 80, 100, 10);
				g.setColor(Color.blue);
				g.fillRect(20, Game.panelHeight - 80, (int) (this.Exp / (double) this.maxExp * 100), 10);
				g.setColor(Color.black);
				g.drawString("Experience     Lvl: " + (int) (this.level), 20, Game.panelHeight - 85);
				if (this.ExpPoints > 0) {
					g.setColor(Color.white);
					g.fillRect(20, Game.panelHeight - 130, 80, 20);
					g.fillRect(20, Game.panelHeight - 160, 80, 20);
					g.fillRect(20, Game.panelHeight - 190, 80, 20);
					g.setColor(Color.black);
					g.drawString("ExpPoints: " + this.ExpPoints, 20, Game.panelHeight - 200);
					g.setFont(new Font("default", Font.BOLD, 14));
					g.drawString("Health (1)", 20, Game.panelHeight - 115);
					g.drawString("Speed (2)", 20, Game.panelHeight - 145);
					g.drawString("Damage (3)", 20, Game.panelHeight - 175);
					g.drawImage(Game.sprExpIcon, 105, Game.panelHeight - 128, null);
					g.drawImage(Game.sprExpIcon, 105, Game.panelHeight - 158, null);
					g.drawImage(Game.sprExpIcon, 105, Game.panelHeight - 188, null);
				}

				// Control Menu
				g.setColor(Color.black);
				g.setFont(new Font("default", Font.PLAIN, 14));
				g.drawString("Press F4 for controls", Game.panelWidth - 200, Game.panelHeight - 10);
			} else {
				// Control Menu
				g.setColor(Color.black);
				g.setFont(new Font("default", Font.BOLD, 18));
				g.drawString("Use WASD or up, down, right and left buttons to move.", 50, 50);
				g.drawString(
						"Use left-click for primary fire. Be careful, eneemies only take damage from primary fire if hit from behind.",
						50, 80);
				g.drawString("Use right-click to teleport. Teleport deals damage to enemies it passes through.", 50,
						110);
				g.drawString("Pick up potions for boosts in health, damage, speed, and experience.", 50, 140);
				g.drawString("Your player has levels. Defeat enemies to earn experience!", 50, Game.panelHeight - 250);
				g.drawString(
						"Every time you level up, you get experience points you can use to upgrade your health, speed and damage.",
						50, Game.panelHeight - 220);
				g.drawString("Use hotkeys 1, 2, and 3 respectively to level them up.", 50, Game.panelHeight - 190);
				g.setFont(new Font("default", Font.PLAIN, 14));
				g.drawString("F4 to exit", 50, Game.panelHeight - 160);
			}
		}
	}

	@Override
	public void tryDespawn() {
		if (this.health == 0) {
			Game.gameController.toRemove.add(this);
			this.alive = false;
		}
	}

	public void primaryFire() {
		if (this.pfCooldownTimer <= 0) {
			this.pfCooldownTimer = this.pfCooldownAmount;
			Game.gameController.add(new Projectile((int) Math.round(this.x) + 8, (int) Math.round(this.y) + 8,
					Game.TYPE_FRIENDLYPROJECTILE, Game.sprProjectile1, this.rotateDegs, Game.mouseX, Game.mouseY,
					pfProjSpeed, pfDamage, this.pfTimeAlive));
		}
	}

	public void setTp(double x, double y) {
		if (this.tpCooldownTimer <= 0) {
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
			this.tpDist = Math.sqrt((x - this.tpXi) * (x - this.tpXi) + (y - this.tpYi) * (y - this.tpYi));
			this.tpDX = (x - this.tpXi) / noNaN(this.tpDist);
			this.tpDY = (y - this.tpYi) / noNaN(this.tpDist);
		}
	}

	public void setKnockback(double kb, double x2, double y2) {
		this.underKnockback = true;
		double dx = this.x - x2, dy = this.y - y2, dist = Math.sqrt(dx * dx + dy * dy);
		double velX = (dx / noNaN(dist)) * this.knockbackPerFrame, velY = (dy / noNaN(dist)) * this.knockbackPerFrame;
		this.kbVelX = velX;
		this.kbVelY = velY;
		this.kbStep = kb / this.knockbackPerFrame;
	}

	public void reduceCooldowns() {
		this.tpCooldownTimer--;
		this.pfCooldownTimer--;
	}

	public void canelAbilities() {
		this.tpPrep = false;
	}

}
