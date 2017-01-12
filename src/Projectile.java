import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Projectile extends GameObject {
	
	// Alive Time
	int timeAlive;

	/**
	 * @param x X Location
	 * @param y Y Location
	 * @param type Projectile Type
	 * @param img BufferedImage sprite
	 * @param degs Rotation of the Projectile
	 * @param x2 Destination X Location
	 * @param y2 Destination Y Location
	 * @param speed Velocity of the Projectile
	 * @param alive Time before projectile despawns
	 */
	public Projectile(int x, int y, int type, BufferedImage img, double degs, int x2, int y2, int speed, int alive) {
		super(x, y, type, img, new Rectangle(6, 6, 4, 4));
		this.rotateDegs = degs;
		this.timeAlive = alive; // Measured in ticks
		double dist = Math.sqrt((x2 - x) * (x2 - x) + (y2 - y) * (y2 - y));
		double distX = Math.sin(Math.toRadians(degs)) * dist;
		double distY = Math.cos(Math.toRadians(degs)) * dist;
		this.velX = (int) ((distX / dist) * speed);
		this.velY = (int) ((-distY / dist) * speed);
	}

	@Override
	public void tick() {
		// Slowly run the despawn timer
		this.timeAlive--;
		
		// Move
		this.x += this.velX;
		this.y += this.velY;
		
		// Move the collision box aswell
		this.colBox.x = this.x + this.colBoxOffsetX;
		this.colBox.y = this.y + this.colBoxOffsetY;
		
		// Despawn Instance after it dies
		if (this.timeAlive < 0) {
			Game.gameController.toRemove.add(this);
		}
	}

	@Override
	public void render(Graphics g) {
		AffineTransformOp op = new AffineTransformOp(
				AffineTransform.getRotateInstance(Math.toRadians(this.rotateDegs), this.rotateLocX, this.rotateLocY),
				AffineTransformOp.TYPE_BILINEAR);
		g.drawImage(op.filter(this.sprite, null), this.x, this.y, null);
	}

}
