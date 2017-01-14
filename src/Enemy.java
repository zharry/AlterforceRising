import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Enemy extends GameObject {

	// Attack Variables
	double damage, knockback;
	double moveDist;
	
	// Movement Variables
	double p1x, p1y, p2x, p2y, p3x, p3y;
	double rotSpeed;
	

	// Health Variables
	double health = 100, maxHealth = 100;

	public Enemy(int x, int y, int type, int subtype) {
		super(x, y, type);
		// Create Custom Enemy Definitions Here
		/*
		 * Required Variables to set: sprite, colBox, damage, knockback,
		 * moveDist, rotation speed
		 */
		if (subtype == Game.ENEMY_DEFAULT) {
			this.sprite = Game.sprAssassin1;
			this.colBox = new Rectangle(8, 8, 16, 16);
			this.damage = 3;
			this.knockback = 48;
			this.moveDist = 2.5;
			this.rotSpeed = 0.3;
		} else if (subtype == Game.ENEMY_TANK) {
			this.sprite = Game.sprPlayer;
			this.colBox = new Rectangle(-8, -8, 64, 64);
			this.damage = 1;
			this.knockback = 60;
			this.moveDist = 1;
			this.health = 500;
			this.maxHealth = 500;
			this.rotSpeed = 0.2;
		} else if (subtype == Game.ENEMY_ASSASSIN) {
			this.sprite = Game.sprAssassin1;
			this.colBox = new Rectangle(8, 8, 16, 16);
			this.damage = 10;
			this.knockback = 40;
			this.moveDist = 3.5;
			this.health = 25;
			this.maxHealth = 25;
			this.rotSpeed = 0.7;
		} else if (subtype == Game.ENEMY_SCOUT) {
			this.sprite = Game.sprAssassin1;
			this.colBox = new Rectangle(8, 8, 16, 16);
			this.damage = 0.25;
			this.knockback = 32;
			this.moveDist = 4.5;
			this.health = 50;
			this.maxHealth = 50;
			this.rotSpeed = 0.5;
		}
		// Process the Collision box
		this.colBoxOffsetX = colBox.x;
		this.colBoxOffsetY = colBox.y;
		this.colBox.x += x;
		this.colBox.y += y;
	}

	public Enemy(int x, int y, int type, BufferedImage[] sprite, Rectangle colBox, double damage, double knockback,
			double speed) {
		super(x, y, type, sprite, colBox);
		this.damage = damage;
		this.knockback = knockback;
		this.moveDist = speed;
	}

	@Override
	public void tick() {
		// Move this object to Game.player
		double xChange = this.x - Game.player.x == 0 ? 1 : this.x - Game.player.x,
				yChange = this.y - Game.player.y == 0 ? 1 : this.y - Game.player.y;
		double totalDist = Math.sqrt(xChange * xChange + yChange * yChange);
		this.velX = -xChange / noNaN(totalDist) * this.moveDist;
		this.velY = -yChange / noNaN(totalDist) * this.moveDist;

		// Collision Detection
		ArrayList<GameObject> inCollisionWith = Game.gameController.isColliding(this);
		for (GameObject obj : inCollisionWith)
			if (obj.type == Game.TYPE_ENEMY) {
				Enemy enemy = (Enemy) obj;
				this.setKnockback(10, enemy.x, enemy.y);
			}
		inCollisionWith = Game.gameController.isCollidingSprite(this);
		//double a = this.rotateDegs - Game.MAXBACKDEG;
		//double b = this.rotateDegs + Game.MAXBACKDEG;
		for (GameObject obj : inCollisionWith)
			if (obj.type == Game.TYPE_FRIENDLYPROJECTILE) {
				Projectile proj = (Projectile) obj;
				double delta = proj.rotateDegs - this.rotateDegs;
				double z = ((Math.abs(delta) % 360) + 360) % 360;
				delta = Math.min(z, Math.abs(360 - z));
				if (delta < Game.MAXBACKDEG) {
					this.health -= proj.damage;
				}
				Game.gameController.toRemove.add(obj);
			}

		// Clamp Move and then try to despawn
		this.health = clamp(this.health, 0, this.maxHealth);
		move();
		tryDespawn();
	}

	@Override
	public void render(Graphics g) {
		// Rotating enemy towards player
		this.p1x = this.x + this.sprite[0].getWidth() / 2;
		this.p1y = this.y + this.sprite[0].getHeight() / 2;
		this.p2x = Game.player.x + this.sprite[0].getWidth() / 2;
		this.p2y = Game.player.y + this.sprite[0].getHeight() / 2;
		this.p3x = this.x + this.sprite[0].getWidth() / 2;
		this.p3y = -1;
		double p12 = Math.sqrt((p1x - p2x) * (p1x - p2x) + (p1y - p2y) * (p1y - p2y));
		double p23 = Math.sqrt((p2x - p3x) * (p2x - p3x) + (p2y - p3y) * (p2y - p3y));
		double p31 = Math.sqrt((p3x - p1x) * (p3x - p1x) + (p3y - p1y) * (p3y - p1y));
		// Making rotation move slowly, may be complicated cause I have no clue what I'm doing
		double oldRotate = this.rotateDegs;
		
		this.rotateDegs = Math.abs(((p2x < p1x) ? -360 : 0) // Possible NaN here
				+ Math.toDegrees(Math.acos((p12 * p12 + p31 * p31 - p23 * p23) / (2 * p12 * p31))));
		if (Math.abs(this.rotateDegs - oldRotate) <= 180){
			if (this.rotateDegs > oldRotate){
				this.rotateDegs = clamp(this.rotateDegs, oldRotate, oldRotate + rotSpeed);
			}
			else{
				this.rotateDegs = clamp(this.rotateDegs, oldRotate - rotSpeed, oldRotate);
			}
		}
		else{
			if (this.rotateDegs > oldRotate){
				this.rotateDegs = oldRotate - rotSpeed;
				this.rotateDegs = this.rotateDegs < 0 ? this.rotateDegs + 360 : this.rotateDegs;
			}
			else{
				this.rotateDegs = oldRotate + rotSpeed;
				this.rotateDegs = this.rotateDegs > 360 ? this.rotateDegs - 360 : this.rotateDegs;
			}
		}
		
		g.drawImage(this.sprite[(int) Math.round(this.rotateDegs)], (int) Math.round(this.x), (int) Math.round(this.y),
				null);
		

		// Draw Healthbar Elements
		g.setColor(Color.black);
		g.fillRect((int) Math.round(this.x), (int) Math.round(this.y) - 10, 32, 5);
		g.setColor(Color.green);
		g.fillRect((int) Math.round(this.x), (int) Math.round(this.y) - 10,
				(int) (this.health / (double) this.maxHealth * 32), 5);
		g.setColor(Color.DARK_GRAY);
		g.drawRect((int) Math.round(this.x), (int) Math.round(this.y) - 10, 32, 5);
	}

	@Override
	public void tryDespawn() {
		if (this.health == 0)
			Game.gameController.toRemove.add(this);
	}

	public void setKnockback(double kb, double x2, double y2) {
		double dx = this.x - x2, dy = this.y - y2, dist = Math.sqrt(dx * dx + dy * dy);
		double velX = (dx / noNaN(dist)), velY = (dy / noNaN(dist));
		this.velX = velX;
		this.velY = velY;
		this.x += this.velX;
		this.y += this.velY;
	}

}
