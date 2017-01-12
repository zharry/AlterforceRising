import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Enemy extends GameObject {

	// Attack Variables
	int damage, knockback;
	int moveDist;

	// Health Variables
	double health = 100, maxHealth = 100;

	public Enemy(int x, int y, int type, BufferedImage[] sprite, Rectangle colBox, int damage, int knockback, int speed) {
		super(x, y, type, sprite, colBox);
		this.damage = damage;
		this.knockback = knockback;
		this.moveDist = speed;
	}

	@Override
	public void tick() {
		/** Move this object to Game.player
		int xChange = this.x - Game.player.x == 0 ? 1 : this.x - Game.player.x,
				yChange = this.y - Game.player.y == 0 ? 1 : this.y - Game.player.y;
		this.velX = (xChange < 0 ? 1 : (xChange == 0 ? 0 : -1)) * moveDist;
		this.velY = (yChange < 0 ? 1 : (yChange == 0 ? 0 : -1)) * moveDist;
		this.x += this.velX;
		this.y += this.velY;
		**/

		// Collision Detection
		ArrayList<GameObject> inCollisionWith = Game.gameController.isColliding(this);
		for (GameObject obj : inCollisionWith) {
			if (obj.type == Game.TYPE_ENEMY) {
				Enemy enemy = (Enemy) obj;
				this.setKnockback(10, enemy.x, enemy.y);
			}
		}

		// Clamp Move and then try to despawn
		this.health = clamp(this.health, 0, this.maxHealth);
		move();
		tryDespawn();
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(Game.sprAssassin1[(int) this.rotateDegs], this.x, this.y, null);

		// Draw Healthbar Elements
		g.setColor(Color.black);
		g.fillRect(this.x, this.y - 10, 32, 5);
		g.setColor(Color.green);
		g.fillRect(this.x, this.y - 10, (int) (this.health / (double) this.maxHealth * 32), 5);
		g.setColor(Color.DARK_GRAY);
		g.drawRect(this.x, this.y - 10, 32, 5);
	}

	@Override
	public void tryDespawn() {
		if (this.health == 0)
			Game.gameController.toRemove.add(this);
	}

	public void setKnockback(int kb, int x2, int y2) {
		double dx = this.x - x2, dy = this.y - y2, dist = Math.sqrt(dx * dx + dy * dy);
		double velX = (dx / dist), velY = (dy / dist);
		this.velX = (int) (Math.round(velX) == 0 ? (velX > 0 ? Math.ceil(velX) : Math.floor(velX)) : Math.round(velX));
		this.velY = (int) (Math.round(velY) == 0 ? (velY > 0 ? Math.ceil(velY) : Math.floor(velY)) : Math.round(velY));
		this.x += this.velX;
		this.y += this.velY;
	}

}
