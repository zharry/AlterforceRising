import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Player extends GameObject {

	// Movement Variables
	boolean goUp = false, goDown = false, goLeft = false, goRight = false, goTp = false;
	int moveDist;
	
	// Ability Variables
	int tpXi, tpYi, tpStep, tpDist, tpMoveDist;
	double tpDX, tpDY;
	
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
					break;
				}
			setX((int) (this.tpXi + tpDX * this.tpStep));
			setY((int) (this.tpYi + tpDY * this.tpStep));
		}
		
		// Set the velocity based on player input
		this.setVelX(0);
		this.setVelY(0);
		if (this.goUp)
			setVelY(-1);
		if (this.goDown)
			setVelY(getVelY() + 1);
		if (this.goLeft)
			setVelX(-1);
		if (this.goRight)
			setVelX(getVelX() + 1);
		// Move the player moveDist pixels in that direction
		this.x += this.moveDist * this.velX;
		this.y += this.moveDist * this.velY;

		// Make sure none of the values exceed the maximum and minimum for the game
		this.x = clamp(this.x, 0, Game.panelWidth - this.sprite.getWidth());
		this.y = clamp(this.y, 0, Game.panelHeight - this.sprite.getHeight());
		this.health = clamp(this.health, 0, maxHealth);
	}

	@Override
	public void render(Graphics g) {
		// Draw Game Object
		AffineTransformOp op = new AffineTransformOp(
				AffineTransform.getRotateInstance(Math.toRadians(this.rotateDegs), this.rotateLocX, this.rotateLocY),
				AffineTransformOp.TYPE_BILINEAR);
		g.drawImage(op.filter(this.sprite, null), this.x, this.y, null);

		// Draw HUD Elements
		g.setColor(Color.black);
		g.fillRect(20, Game.panelHeight - 40, 100, 20);
		g.setColor(Color.green);
		g.fillRect(20, Game.panelHeight - 40, (int) (health / (double) maxHealth * 100), 20);
		g.setColor(Color.DARK_GRAY);
		g.drawRect(20, Game.panelHeight - 40, 100, 20);
		g.setColor(Color.black);
		g.drawString("Health: " + health + "/" + maxHealth, 20, Game.panelHeight - 45);
	}

	public void setTp(int x, int y) {
		this.goTp = true;
		this.tpXi = this.x;
		this.tpYi = this.y;
		this.tpStep = 1;
		this.tpDist = (int) (Math.sqrt(
				(x - this.tpXi) * (x - this.tpXi) + (y - this.tpYi) * (y - this.tpYi)));
		this.tpDX = (x - this.tpXi) / ((double) this.tpDist);
		this.tpDY = (y - this.tpYi) / ((double) this.tpDist);
	}

}
