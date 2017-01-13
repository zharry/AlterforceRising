import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Enemy extends GameObject {

	// Attack Variables
	double damage, knockback;
	double moveDist;

	// Health Variables
	double health = 100, maxHealth = 100;

	public Enemy(int x, int y, int type) {
		super(x, y, type);
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
		double totalDist = Math.sqrt(xChange*xChange+yChange*yChange);
		this.velX = -xChange/noNaN(totalDist)*this.moveDist;
		this.velY = -yChange/noNaN(totalDist)*this.moveDist;

		// Collision Detection
		ArrayList<GameObject> inCollisionWith = Game.gameController.isColliding(this);
		for (GameObject obj : inCollisionWith)
			if (obj.type == Game.TYPE_ENEMY) {
				Enemy enemy = (Enemy) obj;
				this.setKnockback(10, enemy.x, enemy.y);
			}
		inCollisionWith = Game.gameController.isCollidingSprite(this);
		double a = this.rotateDegs - Game.MAXBACKDEG;
		double b = this.rotateDegs + Game.MAXBACKDEG;
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
		g.drawImage(Game.sprAssassin1[(int) Math.round(this.rotateDegs)], (int) Math.round(this.x),
				(int) Math.round(this.y), null);

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
