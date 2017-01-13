import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Projectile extends GameObject {

	int timeAlive;

	/**
	 * @param x
	 *            X Location
	 * @param y
	 *            Y Location
	 * @param type
	 *            Projectile Type
	 * @param img
	 *            BufferedImage sprite
	 * @param degs
	 *            Rotation of the projectile
	 * @param x2
	 *            Destination X Location
	 * @param y2
	 *            Destination Y Location
	 * @param speed
	 *            Velocity of the projectile
	 * @param alive
	 *            Time before projectile despawns
	 */
	public Projectile(int x, int y, int type, BufferedImage[] sprite, double degs, double x2, double y2, double speed, int alive) {
		super(x, y, type, sprite, new Rectangle(6, 6, 4, 4));
		this.rotateDegs = degs;
		this.timeAlive = alive; // Measured in ticks
		double dist = Math.sqrt((x2 - x) * (x2 - x) + (y2 - y) * (y2 - y));
		double distX = Math.sin(Math.toRadians(degs)) * dist;
		double distY = Math.cos(Math.toRadians(degs)) * dist;
		this.velX = (distX / noNaN(dist)) * speed;
		this.velY = (-distY / noNaN(dist)) * speed;
	}

	@Override
	public void tick() {
		// Run the despawn timer
		this.timeAlive--;

		//move(false);
		tryDespawn();
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(Game.sprProjectile1[(int) Math.round(this.rotateDegs)], (int) Math.round(this.x), (int) Math.round(this.y), null);
	}

	@Override
	public void tryDespawn() {
		if (this.timeAlive < 0)
			Game.gameController.toRemove.add(this);
	}

}
