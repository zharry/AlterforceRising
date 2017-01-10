import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Enemy extends GameObject {

	// Attack Variables
	int damage, knockback; // Knockback Measured in pixels
	
	// Health Variables
	double health = 100, maxHealth = 100;
	
	public Enemy(int x, int y, int type, BufferedImage img, Rectangle colBox, int damage, int knockback) {
		super(x, y, type, img, colBox);
		this.damage = damage;
		this.knockback = knockback; 
	}

	@Override
	public void tick() {
		// Move this object to Game.player
		int x_change = this.x - Game.player.x;
		int y_change = this.y - Game.player.y; 
		x_change = (x_change == 0 ? 1 : x_change);
		y_change = (y_change == 0 ? 1 : y_change);
		this.velX = (x_change < 0 ? 1 : (x_change == 0 ? 0 : -1));
		this.velY = (y_change < 0 ? 1 : (y_change == 0 ? 0 : -1));
		
		this.x += this.velX;
		this.y += this.velY;
		
		// Make sure none of the values exceed the max and min for the game
		this.x = clamp(this.x, 0, Game.panelWidth - this.sprite.getWidth());
		this.y = clamp(this.y, 0, Game.panelHeight - this.sprite.getHeight());
		this.health = clamp(this.health, 0, this.maxHealth);

		// Move the collision box aswell
		this.colBox.x = this.x + this.colBoxOffsetX;
		this.colBox.y = this.y + this.colBoxOffsetY;
		
		if (this.health == 0) {
			Game.gameController.toRemove.add(this);
		}
	}

	@Override
	public void render(Graphics g) {
		AffineTransformOp op = new AffineTransformOp(
				AffineTransform.getRotateInstance(Math.toRadians(0), this.rotateLocX, this.rotateLocY),
				AffineTransformOp.TYPE_BILINEAR);
		g.drawImage(op.filter(this.sprite, null), this.x, this.y, null);
		
		// Draw Healthbar Elements
		g.setColor(Color.black);
		g.fillRect(this.x, this.y - 10, 32, 5);
		g.setColor(Color.green);
		g.fillRect(this.x, this.y - 10, (int) (this.health / (double) this.maxHealth * 32), 5);
		g.setColor(Color.DARK_GRAY);
		g.drawRect(this.x, this.y - 10, 32, 5);
	}
	
}
